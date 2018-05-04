package com.scholarone.persistence.valueobject;

// com imports
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.scholarone.configuration.CFactory;
import com.scholarone.file.S3File;
import com.scholarone.file.S3FileInputStream;
import com.scholarone.file.S3FileOutputStream;
import com.scholarone.file.S3FileReader;
import com.scholarone.persistence.PersistenceException;
import com.scholarone.persistence.helpers.Attribute;
import com.scholarone.persistence.helpers.ChildElement;
import com.scholarone.persistence.helpers.FinderMethod;
import com.scholarone.persistence.helpers.SaveMethod;
import com.scholarone.persistence.helpers.XMLContent;
import com.scholarone.xml.XMLConfigurationException;
import com.scholarone.xml.XMLUtils;

/**
 * Implements importing/exporting ValueObjects from XML files
 */
public class XMLPersistence extends GenericPersistenceStrategy
{
  protected static Logger log = null;
  
  private final String fileExtensionCompressed = ".gzip";


  private PrintWriter out = null;
  private StringBuffer indent = new StringBuffer();
  
  private boolean compressFile = false;

  
 
  
  // file to save object to
  private String outputFile;
  
  

  public XMLPersistence() 
  {
    this.setStoredInCacheFl(false);
    if (log == null)
    {
      log = Logger.getLogger(XMLPersistence.class);
    }
    compressFile = Boolean.valueOf(CFactory.instance().getProperty("xml.persistance.compress")).booleanValue();
  }
 
  public Integer copy(ValueObjectConfiguration config, Vector criteria) throws PersistenceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  public void save(ValueObject object) throws PersistenceException
  {
    // TODO Auto-generated method stub

  }

  protected void setObjectValue(ValueObject object, String attrName, Object value) throws PersistenceException
  {
//    log.debug("Entering XMLPersistence.setObjectValue");

    ValueObjectConfiguration config = object.getConfiguration();
    Attribute attr = config.getAttribute(attrName);

    // if I don't have an attribute that matches this column name
    // then don't try to set it
    if (attr == null)
    {
      String err = "XMLPersistence.setObjectValue- no value found for attribute name " + attrName;

      err += " Attempting to set for object of type " + object.getClass().getName();

      throw new PersistenceException(err);
    }

    // everything is a String in an XML file. because of this, many
    // attributes will have overloaded setters to take a string argument.
    // So first we'll just try to set it with its given type, if it fails
    // try setting it explicitly as a String
    try
    {
      attr.setNeedsConversion(true);
      setObjectValue(object, attr, value);
    }
    catch (PersistenceException pe)
    {
      // try again as String
      attr.setNeedsConversion(false);
      setObjectAsString(object, attr, value);
    }
  }

  /**
   * Convert String values from XML file into their proper types
   */
  protected Object convertReadObject(Object value, Attribute attr) throws PersistenceException
  {
//    log.debug("Entering XMLPersistence.convertReadObject");

    // sanity check...
    // empty strings cause number format exceptions.
    
    if (value == null || ((String)value).length() == 0)
    {
      return null;
    }

    String destTypeName = attr.getTypeName();

    // Strings are easy!
    if (destTypeName.equals("java.lang.String"))
    {
      return value;
    }

    // everything in the java.lang package is pretty easy too,
    // since they have constructors that take a String
    if (destTypeName.startsWith("java.lang"))
    {
      try
      {
        
        Class[] argTypes = { value.getClass() };
        Object[] args = { value };
        Constructor ctor = Class.forName(destTypeName).getConstructor(argTypes);

        return ctor.newInstance(args);
      }
      catch (Exception e)
      {
        throw new PersistenceException("Unable to convert value for attribute " + attr.getName() + " Reason = "
            + e.getMessage());
      }
    }

    // we need to convert the value to a long first, then
    // pass that in to the Timestamp ctor
    if (destTypeName.equals("java.sql.Timestamp"))
    {
      return new Timestamp(new Long((String) value).longValue());
    }

    return value;
  }

  /**
   * Timestamps need to be formatted via "XML format". Other objects need no conversion.
   */
  protected Object convertWriteObject(Object value, Attribute attr) throws PersistenceException
  {
    if (value instanceof Timestamp)
    {
      return ValueObject.getXmlDateFormatter(value); //SF_15052
    }

    return value;
  }

  /**
   * Call overloaded String setter method for the attribute
   */
  protected void setObjectAsString(ValueObject object, Attribute attr, Object value) throws PersistenceException
  {
//    log.debug("Entering XMLPersistence.setObjectAsString");

    try
    {
      PropertyDescriptor pd = new PropertyDescriptor(attr.getName(), object.getClass());
      Method origMethod = pd.getWriteMethod();

      Class[] paramTypes = { new String().getClass() };
      Method strSetter = object.getClass().getMethod(origMethod.getName(), paramTypes);

      Object[] args = { value };

      strSetter.invoke(object, args);
    }
    catch (NoSuchMethodException nsme)
    {
      // throw back an exception
      String err = "Cannot set value " + attr.getName() + " on object " + object.getClass().getName() + " Reason = ";

      throw new PersistenceException(err + nsme.toString());
    }
    catch (IntrospectionException ie)
    {
      // throw back an exception
      String err = "Cannot set value " + attr.getName() + " on object " + object.getClass().getName() + " Reason = ";

      throw new PersistenceException(err + ie.toString());
    }
    catch (InvocationTargetException ite)
    {
      String err = "Cannot set value " + attr.getName() + " on object " + object.getClass().getName() + " Reason = ";

      throw new PersistenceException(err + ite.toString());
    }
    catch (IllegalAccessException iae)
    {
      String err = "Cannot set value " + attr.getName() + " on object " + object.getClass().getName() + " Reason = ";

      throw new PersistenceException(err + iae.toString());
    }
    catch (IllegalArgumentException iarge)
    {
      String err = "Cannot set value " + attr.getName() + " on object " + object.getClass().getName();

      if (value != null)
      {
        err += " Value type = " + value.getClass().getName() + " Reason = ";
      }
      else
      {
        err += " Value is null. Reason = ";
      }

      throw new PersistenceException(err + iarge.toString());
    }
  }

  /**
   * This returns a single Object for when we know the criteria being passed in will identify the object uniquely
   */
  public ValueObject loadObject(ValueObjectConfiguration config, Vector keys, String methodName)
      throws PersistenceException
  {
//    log.debug("Entering XMLPersistence.loadObject");

    if ((keys == null) || (keys.size() < 1))
    {
      throw new PersistenceException("No XML file specified for data import");
    }

    String fileName = (String) keys.get(0);

    Boolean validateFl = Boolean.TRUE;

    if (keys.size() > 1)
    {
      validateFl = (Boolean) keys.get(1);
    }

   

    return readXMLFile(fileName, validateFl, config);
  }

  /**
   * loadList returns a multi-row ResultSet as a ValueObjectList. Right now it is a shallow load: will not read any
   * sub-objects of the objects in the list.
   */
  public ValueObjectList loadList(ValueObjectConfiguration config, Vector keys, String methodName)
      throws PersistenceException
  {
//    log.debug("Entering XMLPersistence.loadList");

    return null;
  }

  /**
   * somewhat equivalent to readResultSet from SQLPersistence
   */
  protected ValueObject readXMLFile(String xmlFile, Boolean validateFl, ValueObjectConfiguration config)
      throws PersistenceException
  {
//    log.debug("Entering XMLPersistence.readXMLFile");

    InputSource source = null;
    XMLReader reader = null;
    RootHandler handler = null;
    BufferedReader br = null;
    String value = null;

    try
    {
      
        // lets try and inflate it..
       S3File f =  inflateXML(xmlFile);
       
      // def.10243: trying to determine file encoding based on BOM presence
      BOMInputStream bomIn = new BOMInputStream(new S3FileInputStream(f), ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE,
          ByteOrderMark.UTF_16BE); // this detects BOM and skips it
      try
      {
        if (bomIn.hasBOM())
        {
          if (bomIn.hasBOM(ByteOrderMark.UTF_16LE))
          {
            source = new InputSource(new BufferedReader(new InputStreamReader(bomIn, "UTF-16LE")));
          }
          else if (bomIn.hasBOM(ByteOrderMark.UTF_16BE))
          {
            source = new InputSource(new BufferedReader(new InputStreamReader(bomIn, "UTF-16BE")));
          }
          else if (bomIn.hasBOM(ByteOrderMark.UTF_8))
          {
            source = new InputSource(new BufferedReader(new InputStreamReader(bomIn, "UTF-8")));
          }
        }
      }
      finally
      {
        if (source == null && bomIn != null)
        {
          bomIn.close();
        }
      }
     
      if (source == null)
      {
        br = new BufferedReader(new S3FileReader(xmlFile));
        value = br.readLine();
        br.close();

        String encodingStr = "";
        if (value != null && value.indexOf("encoding=\"") > 0)
        {
          int start = value.indexOf("encoding=\"") + 10;
          int end = value.indexOf("\"", start);
          if (start > 0 && end > start)
          {
            encodingStr = value.substring(start, end);
          }
        }

        if (encodingStr != null && encodingStr.length() > 0)
          source = new InputSource(new BufferedReader(new InputStreamReader(new S3FileInputStream(xmlFile), encodingStr)));
        else
          // def.10243: default encoding changed from platform-default to UTF-8
          source = new InputSource(new BufferedReader(new InputStreamReader(new S3FileInputStream(xmlFile), "UTF-8")));
      }
      
      if (validateFl.booleanValue())
      {
        reader = XMLUtils.getXMLReader(XMLUtils.VALIDATING_PARSER_DTD);
      }
      else
      {
        reader = XMLUtils.getXMLReader(XMLUtils.NONVALIDATING_PARSER);
      }

      handler = new RootHandler(reader, config, this);

      reader.parse(source);
      
      // clean up. If its compressed, we need to delete the inflated file
      
      if(compressFile  || wasFileCompressed(xmlFile))
        f.delete();
      

      return handler.getResultObject();
    }
    catch (SAXParseException spe)
    {
      String err = "File " + xmlFile + " Line " + spe.getLineNumber() + ", Column " + spe.getColumnNumber();

      err = "Caught XML parse error: " + err + ", " + spe.getMessage();

      throw new PersistenceException(err);
    }
    catch (SAXException se)
    {
      String err = "Caught XML parse error in: " + xmlFile + ", " + se.getMessage();

      throw new PersistenceException(err);
    }
    catch (IOException ioe)
    {
     String err = "Caught XML parse error in: " + xmlFile + ", " + ioe.getMessage();

      throw new PersistenceException(err);
    }
    catch (XMLConfigurationException xce)
    {
      String err = "Caught XML parse error in: " + xmlFile + ", " + xce.getMessage();

      throw new PersistenceException(err);
    }
    
  
    

  }

  /**
   * Saves the object to XML. Calls save(object, methodName, tagName), with a "tagName" based on the name of this class.
   *
   * @param object
   *          The object to save.
   * @param the
   *          SaveMethod to use to save the object.
   * @throws NullPointerException
   *           if the XMLPrintWriter has not been set.
   * @throws PersistenceException
   *           for almost any other problem.
   */
  public void save(ValueObject object, String methodName) throws PersistenceException
  {
    String tagName = object.getClass().getName();

    tagName = tagName.substring(tagName.lastIndexOf(".") + 1);
    tagName = tagName.toLowerCase();

    save(object, methodName, tagName);
  }

  /**
   * Saves the object to XML. The tagName is the name to use in the XML tag for this object. Generally the tagName is
   * specified by the parent object.
   *
   * @param object
   *          The object to save.
   * @param the
   *          SaveMethod to use to save the object.
   * @throws NullPointerException
   *           if the XMLPrintWriter has not been set.
   * @throws PersistenceException
   *           for almost any other problem.
   */
  public void save(ValueObject object, String methodName, String tagName) throws PersistenceException
  {
    try
    {
      
      
      ValueObjectConfiguration config = object.getConfiguration();
      SaveMethod saver = null;
     
      
      try
      {
        saver = config.getSaveMethod(methodName);
      }
      catch (PersistenceException e)
      {
        log.info("Persistence Exception (" + e + ").  Method probably not defined.");
      }

      boolean singleLine = ((saver == null) ? (false) : (saver.getSingleLine().booleanValue()));
      String spacing = ((singleLine) ? (" ") : ("\n" + indent + "\t"));

      out.print(indent + "<" + tagName);

      Object[] names = config.getAttributeNames();

      for (int ind = 0; ind < names.length; ++ind)
      {
        String name = (String) names[ind];

        if (!name.startsWith("dummy_"))
        {
          Attribute at = config.getAttribute(name);

          if (at.isWrapper()) continue; // Don't output XML element if its a wrapper (e.g. upper ascii).

          Object value = getObjectValue(object, at);

          if (at.isWritable(value))
          {
            out.print(spacing);

            if (value == null)
            {
              value = "";
            }

            out.print(at.getName() + "=\""
                + XMLUtils.removeNonSafeCharacters(XMLUtils.convertXMLCharacters(value.toString())) + "\"");
          }
        }
      }

      if ((saver == null) || (saver.getXMLContent() == null))
      {
        out.println("/>");
      }
      else
      {
        out.println(">");

        indent.append("\t");

        int numChildrenSaved = saveChildren(object, saver);

        if (indent.length() > 0)
        {
          indent.setLength(indent.length() - 1);
        }

        out.println(indent + "</" + tagName + ">");
      }
    }
    catch (PersistenceException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new PersistenceException("Error saving " + object, e);
    }
    
  }

  /**
   * Saves the children objects of an object. Uses the SaveMethod to determine what the children elements are and how to
   * fetch them. Then saves each in turn, calling the save method that matches the passed parameter.
   *
   * @param object
   *          The parent object whose children are to be saved.
   * @param saver
   *          The SaveMethod used to find the children and save them.
   * @return The number of children saved.
   * @throws NoSuchMethodException
   *           if one of the getMethods doesn't exist.
   * @throws IllegalAccessException
   *           if one of the getMethods doesn't have public access.
   * @throws InvocationTargetException
   *           if one of the getMethods isn't appropriate to this object.
   * @throws PersistenceException
   *           for most any other type of problem.
   */
  protected int saveChildren(ValueObject object, SaveMethod saver) throws NoSuchMethodException,
      IllegalAccessException, InvocationTargetException, PersistenceException
  {
    XMLContent content = saver.getXMLContent();

    if (content == null)
    {
      return 0;
    }

    List names = content.getChildrenNames();
    int numChildrenFound = 0;
    Iterator it = names.iterator();
    Class[] params = new Class[0];

    while (it.hasNext())
    {
      ChildElement ce = (ChildElement) content.getChildElement((String) it.next());
      Method m = ce.getGetMethod(object.getClass());
      Object child = m.invoke(object, (Object[])params);
      int num = ((child == null) ? (-1) : ((child instanceof ValueObjectList) ? (((ValueObjectList) child).size())
          : (1)));

      if (child instanceof List)
      {
        Iterator children = ((List) child).iterator();

        while (children.hasNext())
        {
          ValueObject realChild = (ValueObject) children.next();

          realChild = convertChild(realChild, ce.getObjectType());

          save(realChild, saver.getMethodName(), ce.getElementName());

          ++numChildrenFound;
        }
      }
      else if (child instanceof ValueObject)
      {
        ValueObject realChild = convertChild((ValueObject) child, ce.getObjectType());

        save(realChild, saver.getMethodName(), ce.getElementName());

        ++numChildrenFound;
      }
      // else don't save the child.
    }

    return numChildrenFound;
  }

  /**
   * Checks whether the child object is an instance of the indicated type. If not, It tries to construct an object of
   * that type, giving the child as as a parameter. The constructor sought is specific to the actual type of the child.
   * If the child is of the appropriate type, then it is returned.
   * <P>
   * Some possible causes of exceptions: either parameter is null, Class "type" does not exist, No constructor accepting
   * the appropriate type exists.
   *
   * @param child
   *          The object to convert
   * @param type
   *          The name of the type to convert to.
   * @return The child if it is of appripriate type, or a new object constrcuted from the child.
   * @throws PersistenceException
   *           if anything goes wrong.
   */
  protected ValueObject convertChild(ValueObject child, String type) throws PersistenceException
  {
    ValueObject result = null;

    try
    {
      Class childClass = Class.forName(type);

      if (childClass.isInstance(child)) // Result is of proper type.
      {
        result = child;
      }
      else
      {
        Class[] params = new Class[] { child.getClass() };
        Constructor con = childClass.getConstructor(params);

        result = (ValueObject) con.newInstance(new Object[] { child });
      }
    }
    catch (Exception e)
    {
      throw new PersistenceException("Error converting child", e);
    }

    return result;
  }

  protected static String replace(String text, String oldValue, String newValue)
  {
    StringBuffer buffer = new StringBuffer();

    if ((oldValue == null) || (newValue == null))
    {
      return text;
    }

    int end = 0;

    while (end != -1)
    {
      end = text.indexOf(oldValue);

      if (end != -1)
      {
        buffer.append(text.substring(0, end));
        buffer.append(newValue);

        text = text.substring(end + oldValue.length());
      }
      else
      {
        buffer.append(text);
      }
    }

    return buffer.toString();
  }

  @Override
  public ValueObjectList loadList(Connection con, ValueObjectConfiguration config, Vector keys, String methodName) throws PersistenceException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  static class AbstractHandler extends DefaultHandler
  {

    /**
     * Previous handler for the document. When the next element is finished, control returns to this handler.
     */
    protected DefaultHandler parentHandler;

    protected XMLReader reader;

    protected ValueObjectConfiguration config;

    protected static Locator locator;

    protected static Logger log;

    /**
     * Creates a handler and sets the parser to use it for the current element.
     */
    public AbstractHandler(XMLReader reader, DefaultHandler parentHandler, ValueObjectConfiguration config)
    {
      this.parentHandler = parentHandler;
      this.reader = reader;
      this.config = config;
      log = Logger.getLogger(AbstractHandler.class);

      // Start handling SAX events
      reader.setContentHandler(this);
      reader.setDTDHandler(this);
      reader.setEntityResolver(new XMLUtils.DTDSchemaResolver());
      reader.setErrorHandler(this);
    }

    public void setDocumentLocator(Locator locator)
    {
//      log.debug("Entering AbstractHandler.setDocumentLocator");

      AbstractHandler.locator = locator;
    }

    /**
     * Handles the start of an element. This base implementation just throws an exception.
     */
    public void startElement(String namespace, String localName, String qName, Attributes attrs)
        throws SAXParseException
    {
//      log.debug("Entering AbstractHandler.startElement");

//      log.debug("Base class start element localname = " + localName);

      for (int i = 0; i < attrs.getLength(); i++)
      {
//        log.debug("Attribute name = " + attrs.getLocalName(i) + " value = " + attrs.getValue(i));
      }
    }

    /**
     * Handles text within an element. This base implementation just throws an exception.
     *
     */
    public void characters(char[] buf, int start, int count) throws SAXParseException
    {
//      log.debug("Entering AbstractHandler.characters");

      if (count > 0)
      {
        String s = new String(buf, start, count).trim();

        if (s.length() > 0)
        {
          throw new SAXParseException("Unexpected characters '" + s + " in XML file", locator);
        }
      }
    }

    /**
     * Called when this element and all elements nested into it have been handled.
     */
    protected void finished()
    {
    }

    /**
     * Handles the end of an element. Any required clean-up is performed by the finished() method and then the original
     * handler is restored to the parser.
     *
     */
    public void endElement(String namespace, String localName, String qName) throws SAXException
    {
      finished();

      // Let parent resume handling SAX events
      if (parentHandler != null)
      {
        reader.setContentHandler(parentHandler);
        reader.setDTDHandler(parentHandler);
        reader.setEntityResolver(parentHandler);
        reader.setErrorHandler(parentHandler);
      }
    }

    /**
     * implementation of ErrorHandler interface
     */
    public void warning(SAXParseException e) throws SAXException
    {
      log.debug("Entering AbstractHandler.warning");

      log.debug("Validation warning at line " + e.getLineNumber() + ", column" + e.getColumnNumber() + " "
          + e.getMessage());
    }

    public void error(SAXParseException e) throws SAXException
    {
      log.debug("Entering AbstractHandler.error");

      throw new SAXException("Validation error at " + e.getLineNumber() + ", " + e.getColumnNumber(), e);
    }

    public void fatalError(SAXParseException e) throws SAXException
    {
      log.debug("Entering AbstractHandler.fatalError");

      throw new SAXException("Validation error at " + e.getLineNumber() + ", " + e.getColumnNumber(), e);
    }

  }

  static class RootHandler extends AbstractHandler
  {

    protected ValueObject resultObject = null;

    static protected XMLPersistence strategy;

    public RootHandler(XMLReader reader, ValueObjectConfiguration config, XMLPersistence strat)
    {
      super(reader, null, config);

      strategy = strat;
    }

    public RootHandler(XMLReader reader, DefaultHandler parent, ValueObjectConfiguration config)
    {
      super(reader, parent, config);
    }

    /**
     * Handles the start of a valueobject element.
     */
    public void startElement(String namespace, String localName, String qName, Attributes attrs)
        throws SAXParseException
    {
//      log.debug("Entering RootHandler.startElement");

      resultObject = new ValueObjectInputHandler(reader, this, config).init(namespace, localName, qName, attrs);
    }

    public ValueObject getResultObject()
    {
//      log.debug("Entering RootHandler.getResultObject");

      return resultObject;
    }

  }

  static class ValueObjectInputHandler extends RootHandler
  {

    protected ValueObject currentObj = null;

    // finder method contains meta-info about our import
    FinderMethod finder;

    private boolean validateOnly = false;

    public ValueObjectInputHandler(XMLReader reader, DefaultHandler parentHandler, ValueObjectConfiguration config)
    {
      super(reader, parentHandler, config);

      try
      {
        finder = config.getFinderMethod("readFromXML");
      }
      catch (Exception e)
      {
        // log.info("Caught exception locating readFromXML method, probably not declared");
      }
    }

    // initial values for the element that caused this class to be invoked
    public ValueObject init(String namespace, String localName, String qName, Attributes attrs)
        throws SAXParseException
    {
//      log.debug("Entering ValueObjectInputHandler.init");

      ValueObject obj = null;

      if (validateOnly)
      {
        return obj;
      }

      try
      {
        obj = ValueObjectFactory.getInstance().createObjectFromConfig(config);

        for (int i = 0; i < attrs.getLength(); i++)
        {
          // disregard any attributes outside our (local) namespace
          if (attrs.getLocalName(i).equals(attrs.getQName(i)))
          {
            String name = attrs.getLocalName(i);
            String value = attrs.getValue(i);

            if (value != null)
            {
              value = replace(value, "\\n", "\n");
              value = replace(value, "\\r", "\r");
              value = replace(value, "\\t", "\t");
            }

            strategy.setObjectValue(obj, name, value);
          }
        }

        currentObj = obj;

        return obj;
      }
      catch (Exception e)
      {
        String err = "Error initializating " + localName + ": " + e.getMessage();

        throw new SAXParseException(err, locator, e);
      }
    }

    /**
     * Handles the start of a valueobject element.
     *
     * Possible sub-elements are "attributes", "finderMethod", and "saveMethod".
     */
    public void startElement(String namespace, String localName, String qName, Attributes attrs)
        throws SAXParseException
    {
//      log.debug("Entering ValueObjectInputHandler.startElement");

      if (validateOnly)
      {
        return;
      }

      // need to find the configuration for this element then recursively process it
      try
      {
        ChildElement ce = finder.getXMLContent().getChildElement(localName);

        if (ce == null)
        {
          throw new SAXParseException("No child element named " + localName + " defined", locator);
        }

        ValueObjectConfiguration conf = ValueObjectFactory.getInstance().getObjectConfiguration(ce.getObjectType());

        ValueObject newObj = new ValueObjectInputHandler(reader, this, conf).init(namespace, localName, qName, attrs);

        // call addObjectType on currentObj
        addChildObject(currentObj, newObj, ce.getAddMethodName());
      }
      catch (Exception e)
      {
        throw new SAXParseException("Error in startElement: " + e.getMessage(), locator, e);
      }
    }

    /**
     * Add the child object to its parent using the given method name Obviously, the parent must implement the method.
     */
    protected void addChildObject(ValueObject parentObj, ValueObject childObj, String methodName)
        throws SAXParseException
    {
//      log.debug("Entering ValueObjectInputHandler.addChildObject");

      // sanity checks
      if (parentObj == null)
      {
        throw new SAXParseException("Cannot add a child object to a null parent", locator);
      }
      else if (childObj == null)
      {
        throw new SAXParseException("Cannot add a null child object to " + parentObj, locator);
      }
      else if (methodName == null)
      {
        throw new SAXParseException("No add method defined to add " + childObj + " to " + parentObj, locator);
      }

      Object[] args = { childObj };
      Class[] argTypes = { childObj.getClass() };

      try
      {
        Method addMethod = parentObj.getClass().getMethod(methodName, argTypes);

        addMethod.invoke(parentObj, args);
      }
      catch (Exception e)
      {
        throw new SAXParseException("Error in addChildObject: " + e.getMessage(), locator, e);
      }
    }

  }

  /**
   * This version of the function is more generic as it takes an Attribute parameter as opposed to a column name
   */
  protected void setObjectValue(Object object, Attribute attr, Object value) throws PersistenceException
  {
    try
    {
      PropertyDescriptor pd = attr.getPropertyDescriptor();

      if (pd == null)
      {
        throw new PersistenceException("Unable to get PropertyDescriptor for attribute " + attr.getName()
            + " of object " + object.getClass().getName());
      }

      Method method = pd.getWriteMethod();

      if (attr.needsConversion())
      {
        value = convertReadObject(value, attr);
      }

      Object[] args = { value };

      method.invoke(object, args);
    }
    catch (InvocationTargetException ite)
    {
      String err = "Cannot set value " + attr.getName() + " on object " + object.getClass().getName() + " Reason = ";

      throw new PersistenceException(err + ite.getTargetException().toString(), ite);
    }
    catch (IllegalAccessException iae)
    {
      String err = "Cannot set value " + attr.getName() + " on object " + object.getClass().getName() + " Reason = ";

      throw new PersistenceException(err + iae.toString(), iae);
    }
    catch (IllegalArgumentException iarge)
    {
      String err = "Cannot set value " + attr.getName() + " on object " + object.getClass().getName();

      if (value != null)
      {
        err += " Value type = " + value.getClass().getName() + " Reason = ";
      }
      else
      {
        err += " Value is null. Reason = ";
      }

      throw new PersistenceException(err + iarge.toString(), iarge);
    }
  }

  /**
   * @return Object
   */
  public Object getObjectValue(Object object, Attribute attr) throws PersistenceException
  {
    Object value = null;

    try
    {
      PropertyDescriptor pd = attr.getPropertyDescriptor();

      if (pd == null)
      {
        throw new PersistenceException("Unable to get PropertyDescriptor for attribute " + attr.getName()
            + " of object " + object.getClass().getName());
      }

      Method method = pd.getReadMethod();

      if (!method.getDeclaringClass().isInstance(object))
      {
        log.error("Wrong class: " + object.getClass().getName() + " != " + method.getDeclaringClass().getName()
            + ", method = " + method);
      }

      Object[] args = {};

      value = method.invoke(object, args);

      value = convertWriteObject(value, attr);
    }
    catch (InvocationTargetException ite)
    {
      throw new PersistenceException(
          "Cannot get value: " + attr.getName() + ": " + ite.getTargetException().toString(), ite);
    }
    catch (IllegalAccessException iae)
    {
      throw new PersistenceException("Cannot get value: " + attr.getName() + ": " + iae.toString(), iae);
    }

    return value;
  }

  /**
   * Given a list of the expected attributes, read their corresponding values from the ValueObject and add them to the
   * Vector
   */
  protected Vector initializeArguments(Object obj, Vector params) throws PersistenceException
  {
    Vector args = new Vector();

    for (int i = 0; i < params.size(); i++)
    {
      Attribute attr = (Attribute) params.get(i);

      args.add(getObjectValue(obj, attr));
    }

    return args;
  }
  



  public void setOutputFile(String outputFile) throws PersistenceException
  {
    this.outputFile = outputFile;
    
  }
  
  public void setXMLPrintWriter(PrintWriter pw)
  {
    
    out = pw;
  }

  private void deflateXML() throws FileNotFoundException, IOException
  {
    
    GZIPOutputStream out = new GZIPOutputStream(new S3FileOutputStream(outputFile + fileExtensionCompressed )); 
    // Open the input file 
    S3FileInputStream in = new S3FileInputStream(outputFile); 
    // Transfer bytes from the input file to the GZIP output stream 
    byte[] buf = new byte[1024]; 
    int len;
    while ((len = in.read(buf)) > 0) 
    { 
      out.write(buf, 0, len); 
    } 
    in.close(); 
    // Complete the GZIP file 
    out.finish(); 
    out.close(); 
    
    // delete the original file
    S3File f = new S3File(outputFile);
    f.delete();
    
   }
  
  private S3File inflateXML(String xmlFile) throws FileNotFoundException, IOException
  {
    // lets see if the xml file exists, if so, no need to uncompress
    S3File f = new S3File(xmlFile);
    if(f.exists()) return f;
    
    
    GZIPInputStream gzipInputStream = null;
    S3FileInputStream fileInputStream = null;
    gzipInputStream = new GZIPInputStream(new S3FileInputStream(xmlFile + fileExtensionCompressed));
    OutputStream out = new S3FileOutputStream(xmlFile);
    byte[] buf = new byte[1024];
    int len;
    while ((len = gzipInputStream.read(buf)) > 0)
    {
      out.write(buf, 0, len);
    }
    gzipInputStream.close();
    out.close();
    
    return f;
  }
  public boolean tryCompression() throws PersistenceException
  {
    if (compressFile)
    {
      try
      {
        deflateXML();
      }
      catch (FileNotFoundException e)
      {
        throw new PersistenceException("Error compressing " + outputFile);
        
      }
      catch (IOException e)
      {
        throw new PersistenceException("Error compressing " + outputFile);
      }
      return true;
    }
    else
      return false;
  }

  private boolean wasFileCompressed(String uncompressedFile)
  {
    
   return new S3File(uncompressedFile+ fileExtensionCompressed).exists();
  }
  
  
}

