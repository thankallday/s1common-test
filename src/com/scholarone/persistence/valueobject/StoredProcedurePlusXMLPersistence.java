package com.scholarone.persistence.valueobject;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Checksum;

import org.apache.log4j.Logger;

import com.scholarone.configuration.CFactory;
import com.scholarone.constants.Constants;
import com.scholarone.file.S3File;
import com.scholarone.file.S3FileInputStream;
import com.scholarone.file.S3FileOutputStream;
import com.scholarone.persistence.ObjectCreationException;
import com.scholarone.persistence.PersistenceException;
import com.scholarone.persistence.helpers.Attribute;

public class StoredProcedurePlusXMLPersistence extends GenericPersistenceStrategy
{
  // static final long serialVersionUID = 0;
  public static final String STORED_PROC_STRATEGY_CLASS_NAME = "com.scholarone.persistence.valueobject.StoredProcedurePersistence";

  public static final String XML_STRATEGY_CLASS_NAME = "com.scholarone.persistence.valueobject.XMLPersistence";

  private StoredProcedurePersistence spp;
  
  

  private XMLPersistence xp;

  protected static Logger log = null;

  private static long traceThreshold = 1;

  /**
   * 
   */
  public StoredProcedurePlusXMLPersistence()
  {
    this.setStoredInCacheFl(false);
    if (log == null)
    {
      log = Logger.getLogger(StoredProcedurePlusXMLPersistence.class);
    }

    try
    {
      spp = (StoredProcedurePersistence) ValueObjectFactory.getInstance().createStrategy(
          STORED_PROC_STRATEGY_CLASS_NAME);
      
     
      xp = (XMLPersistence) ValueObjectFactory.getInstance().createStrategy(XML_STRATEGY_CLASS_NAME);
  
      
    }
    catch (ObjectCreationException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
   

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.scholarone.persistence.valueobject.GenericPersistenceStrategy#convertReadObject(java.lang.Object,
   * com.scholarone.persistence.helpers.Attribute)
   */
  protected Object convertReadObject(Object value, Attribute attr) throws PersistenceException
  {
    return spp.convertReadObject(value, attr);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.scholarone.persistence.valueobject.GenericPersistenceStrategy#convertWriteObject(java.lang.Object,
   * com.scholarone.persistence.helpers.Attribute)
   */
  protected Object convertWriteObject(Object value, Attribute attr) throws PersistenceException
  {
    return spp.convertWriteObject(value, attr);
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.scholarone.persistence.valueobject.GenericPersistenceStrategy#copy(com.scholarone.persistence.valueobject.
   * ValueObjectConfiguration, java.util.Vector)
   */
  public Integer copy(ValueObjectConfiguration config, Vector criteria) throws PersistenceException
  {
    return spp.copy(config, criteria);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.scholarone.persistence.valueobject.GenericPersistenceStrategy#loadList(com.scholarone.persistence.valueobject
   * .ValueObjectConfiguration, java.util.Vector, java.lang.String)
   */
  public ValueObjectList loadList(ValueObjectConfiguration config, Vector keys, String methodName)
      throws PersistenceException
  {

    ValueObjectList results = spp.loadList(config, keys, methodName);
    for (Iterator itr = results.iterator(); itr.hasNext();)
    {
      ValueObject vo = (ValueObject) itr.next();
      if(vo == null)// it didn't load
        throw new PersistenceException("Unable to load object: " + config.getType().getCanonicalName() +" id: "+ keys.get(0).toString());
      
      
       loadObjectInner(config, keys, methodName, vo);

    }

    return results;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.scholarone.persistence.valueobject.GenericPersistenceStrategy#loadObject(com.scholarone.persistence.valueobject
   * .ValueObjectConfiguration, java.util.Vector, java.lang.String)
   */
  public ValueObject loadObject(ValueObjectConfiguration config, Vector keys, String methodName)
      throws PersistenceException
  {

    ValueObject vo = spp.loadObject(config, keys, methodName);
    if(vo == null)// it didn't load
      throw new PersistenceException("Unable to load object: " + config.getType().getCanonicalName() +" id: "+ keys.get(0).toString());
      
      
      
    return loadObjectInner(config, keys, methodName, vo);
    
  }

  // so we can utilize from load list.
  private ValueObject loadObjectInner(ValueObjectConfiguration config, Vector keys, String methodName, ValueObject vo)
      throws PersistenceException
  {
    boolean loaded = true;
    long startTime = System.currentTimeMillis();
    String fileDir = getXMLStorePath();
    String fileLocation = "";
    
    ArrayList distList = config.getDistributionList();
    if (distList.size() > 0)
    {
      FileStorageMap fsMap = (FileStorageMap) distList.get(0);
      String voFileStoreLocation = fsMap.fileLocation;
      Vector criteria = new Vector();
      Class voClass = vo.getClass();
      try
      {
        Class partypes[] = null;
        Method getDateAdded = voClass.getMethod(fsMap.getDatetimeAddedMethodName, (Class[])null);
        Timestamp timeStamp = (Timestamp) getDateAdded.invoke(vo, (Object[])null);
        String datePart = getDatePath(timeStamp);
        fileLocation = fileDir  + S3File.separator+ voFileStoreLocation + datePart + S3File.separator + vo.getId().toString() + ".xml";
        criteria.add(fileLocation);
        criteria.add(Boolean.FALSE);
        Class fsClass = Class.forName(voClass.getCanonicalName());
        ValueObjectConfiguration fsConfig = ((ValueObject) fsClass.newInstance()).getConfiguration();
        ValueObject voFs = this.xp.loadObject(fsConfig, criteria, "readFromXML");
        for (Iterator itr2 = fsMap.fieldMappings.iterator(); itr2.hasNext();)
        {
          FieldMappingMap fieldMap = (FieldMappingMap) itr2.next();
          String readFrom = fieldMap.read_from;
          PropertyDescriptor pd = new PropertyDescriptor(fieldMap.elementName,fsClass);
          
          if (readFrom == null || readFrom.length() == 0)
          {
            throw new PersistenceException("no read_from configuration value available for " + fieldMap.elementName);
          }
          
          if (readFrom.equalsIgnoreCase("XML"))
          {
            Method getOnChild;
            getOnChild = pd.getReadMethod();
            Object returnVal = getOnChild.invoke(voFs, (Object[])null);
            partypes = new Class[1];
            partypes[0] = Class.forName(fieldMap.type);
            Method setOnParent = pd.getWriteMethod();
            Object[] oPars = { returnVal };
            setOnParent.invoke(vo, oPars);

          }

        }
      }
      catch (SecurityException e)
      {
        log.error(e.getMessage());
       throw new PersistenceException(this.getClass().getCanonicalName(),e);
      }
      catch (NoSuchMethodException e)
      {
        log.error(e.getMessage());
        throw new PersistenceException(this.getClass().getCanonicalName(),e);
      }
      catch (ClassNotFoundException e)
      {
        log.error(e.getMessage());
        throw new PersistenceException(this.getClass().getCanonicalName(),e);
      }
      catch (IllegalArgumentException e)
      {
        log.error(e.getMessage());
        throw new PersistenceException(this.getClass().getCanonicalName(),e);
      }
      catch (IllegalAccessException e)
      {
        log.error(e.getMessage());
        throw new PersistenceException(this.getClass().getCanonicalName(),e);
      }
      catch (InvocationTargetException e)
      {
        log.error(e.getMessage());
        throw new PersistenceException(this.getClass().getCanonicalName(),e);
      }
      catch (InstantiationException e)
      {
        log.error(e.getMessage());
        throw new PersistenceException(this.getClass().getCanonicalName(),e);
      }
      catch (IntrospectionException e)
      {
        log.error(e.getMessage());
        throw new PersistenceException(this.getClass().getCanonicalName(),e);
      }
      catch (PersistenceException e)
      {
        // the only way here is if the XML load failed.
        // reload what we can from the DB and move on
        vo = spp.loadObject(config, keys, methodName);
        loaded = false;
        
      }
     
    }
    
    long duration = System.currentTimeMillis() - startTime;
    log.debug("loading " + vo.getClass().getCanonicalName() + ": " + duration + " ms [" + fileLocation + "] " + (loaded?"":"failed"));
    
    return vo;
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.scholarone.persistence.valueobject.GenericPersistenceStrategy#save(com.scholarone.persistence.valueobject.
   * ValueObject)
   */
  public void save(ValueObject object) throws PersistenceException
  {
    this.save(object, "save");
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.scholarone.persistence.valueobject.GenericPersistenceStrategy#save(com.scholarone.persistence.valueobject.
   * ValueObject, java.lang.String)
   */
  public void save(ValueObject object, String methodName) throws PersistenceException
  {

    long startTime = System.currentTimeMillis();
    String fileLocation = "";

    ValueObjectConfiguration config = object.getConfiguration();
    ArrayList restore = new ArrayList();

    ArrayList distList = config.getDistributionList();
    if (distList.size() > 0)
    {
      for (Iterator itr = distList.iterator(); itr.hasNext();)
      {
        FileStorageMap fsMap = (FileStorageMap) itr.next();
        /** @todo a new try block should be inside the inner for loop **/
        try
        {
          Class objectCls = object.getClass();
          Class fileStoreCls = object.getClass();
          ValueObject fileStoreVObj = (ValueObject) fileStoreCls.newInstance();
          Class partypes[] = null;
          String voFileStoreLocation = fsMap.fileLocation;
          for (Iterator itr2 = fsMap.fieldMappings.iterator(); itr2.hasNext();)
          {
            FieldMappingMap fieldMap = (FieldMappingMap) itr2.next();
            PropertyDescriptor pd = new PropertyDescriptor(fieldMap.elementName,fileStoreCls);
            
            String storeInXML = fieldMap.store_in_xml;
            if (storeInXML == null || storeInXML.length() == 0)
            {
              throw new PersistenceException("no store_in_xml configuration value available for "
                  + fieldMap.elementName);
            }
            if (storeInXML.equalsIgnoreCase("TRUE"))
            {
              Method getOnParent = pd.getReadMethod();
              Object returnVal = getOnParent.invoke(object, (Object[])null);
              partypes = new Class[1];
              partypes[0] = Class.forName(fieldMap.type);
              Method setOnFile =pd.getWriteMethod();
              Object[] oPars = { returnVal };
              setOnFile.invoke(fileStoreVObj, oPars);

            }
            String storeInDb = fieldMap.store_in_db;
            if (storeInDb == null || storeInDb.length() == 0)
            {
              throw new PersistenceException("no store_in_db configuration value available for " + fieldMap.elementName);
            }

            RestoreFields rfs = new RestoreFields();
            if (storeInDb.equalsIgnoreCase("FALSE"))
            {
              // set up the restore mappings so we can put the data we null out back.
              rfs.method = pd.getWriteMethod();
              rfs.objectType = fieldMap.type;
              Method getOnParent = pd.getReadMethod();
              rfs.objectValue = getOnParent.invoke(object, (Object[])null);
              restore.add(rfs);
              partypes = new Class[1];
              partypes[0] = Class.forName(fieldMap.type);
              Method setOnParent = pd.getWriteMethod();
              Object[] nulls = { null };
              setOnParent.invoke(object, nulls);
            }
          }

          this.spp.save(object, methodName);

          // ok, put the data we nulled out back
          for (Iterator itr1 = restore.iterator(); itr1.hasNext();)
          {

            RestoreFields rf = (RestoreFields) itr1.next();
            partypes = new Class[1];
            partypes[0] = Class.forName(rf.objectType);
            Method setOnObject = rf.method;
            Object[] oPars = { rf.objectValue };
            setOnObject.invoke(object, oPars);
          }

          Method getPrimaryKeyMethod = objectCls.getMethod(fsMap.getPrimaryKeyMethodName, (Class[])null);
          Object primaryKey = getPrimaryKeyMethod.invoke(object, (Object[])null);
          Method getDateAddedMethod = objectCls.getMethod(fsMap.getDatetimeAddedMethodName, (Class[])null);
          Timestamp timeStamp = (Timestamp) getDateAddedMethod.invoke(object, (Object[])null);
          String datePart = getDatePath(timeStamp);

          partypes = new Class[1];
          partypes[0] = Class.forName(fsMap.primaryKeyType);
          Method setPrimaryKeyMethod = fileStoreCls.getMethod(fsMap.setPrimaryKeyMethodName, partypes);
          Object[] oPars = { primaryKey };
          setPrimaryKeyMethod.invoke(fileStoreVObj, oPars);

          fileLocation = getXMLStorePath() + S3File.separator + voFileStoreLocation + datePart + S3File.separator;
          S3File newDirectory = new S3File(fileLocation);
          newDirectory.mkdirs();

          fileStoreVObj.setIsModified(true);
          fileLocation = fileLocation+ S3File.separator + primaryKey + ".xml";
          this.xp.setOutputFile(fileLocation );
          CheckedOutputStream checkedOutputStream = new CheckedOutputStream(new S3FileOutputStream(fileLocation), new CRC32());
          PrintWriter pw = new PrintWriter(new OutputStreamWriter(checkedOutputStream, "UTF-8"));
          pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
          this.xp.setXMLPrintWriter(pw);
          fileStoreVObj.save(Constants.INTEGER_0, "writeToXML", this.xp);
          pw.flush();
          pw.close();
          Checksum checkWrite = checkedOutputStream.getChecksum();
          
          if ( !validateFile(fileLocation, checkWrite) ) throw new PersistenceException("Checksum failed for " + fileLocation);
          
          this.xp.tryCompression();
          
        }
        catch (ClassNotFoundException e)
        {
          log.error(e.getMessage());
          throw new PersistenceException(this.getClass().getCanonicalName(),e);

        }
        catch (InstantiationException e)
        {
          log.error(e.getMessage());
          throw new PersistenceException(this.getClass().getCanonicalName(),e);
        }
        catch (IllegalAccessException e)
        {
          log.error(e.getMessage());
          throw new PersistenceException(this.getClass().getCanonicalName(),e);
        }
        catch (SecurityException e)
        {
          log.error(e.getMessage());
          throw new PersistenceException(this.getClass().getCanonicalName(),e);
        }
        catch (NoSuchMethodException e)
        {
          log.error(e.getMessage());
          throw new PersistenceException(this.getClass().getCanonicalName(),e);
        }
        catch (IllegalArgumentException e)
        {
          log.error(e.getMessage());
          throw new PersistenceException(this.getClass().getCanonicalName(),e);
        }
        catch (InvocationTargetException e)
        {
          log.error(e.getMessage());
          throw new PersistenceException(this.getClass().getCanonicalName(),e);
        }
        catch (UnsupportedEncodingException e)
        {
          log.error(e.getMessage());
          throw new PersistenceException(this.getClass().getCanonicalName(),e);
        }
        catch (FileNotFoundException e)
        {
          log.error(e.getMessage());
          throw new PersistenceException(this.getClass().getCanonicalName(),e);
        }
        catch (IntrospectionException e)
        {
          log.error(e.getMessage());
          throw new PersistenceException(this.getClass().getCanonicalName(),e);
        }
        catch(IOException ioe)
        {
          log.error(ioe.getMessage());
          throw new PersistenceException(this.getClass().getCanonicalName(),ioe);
        }    
      }
    }

    long duration = System.currentTimeMillis() - startTime;
    log.debug(object.getClass().getCanonicalName() + ": " + duration + " ms [" + fileLocation + "]");
  }

  private boolean validateFile(String fileLocation, Checksum checkWrite) throws IOException
  {
    CheckedInputStream checkedInputStream = new CheckedInputStream(new S3FileInputStream(fileLocation), new CRC32());
    BufferedInputStream in =  new BufferedInputStream(checkedInputStream);
    while (in.read() != -1) {
        // Read file in completely
    }
    Checksum checkRead = checkedInputStream.getChecksum();
    if ( checkWrite.getValue() == checkRead.getValue() )
    {
      return true;
    }  
    return false;
  }
  
  public String getDatePath(Timestamp timeStamp)
  {
    String dateAdded = timeStamp.toString();
    String year = dateAdded.substring(0, 4);
    String month = dateAdded.substring(5, 7);
    String day = dateAdded.substring(8, 10);
    String hour = dateAdded.substring(11, 13).trim();
    String datePart = S3File.separator + year + S3File.separator + month + S3File.separator + day + S3File.separator + hour;
    return datePart;
  }


  private String getXMLStorePath()
  {
    return CFactory.instance().getProperty("xml.persistance.directory");

  }

  @Override
  public ValueObjectList loadList(Connection con, ValueObjectConfiguration config, Vector keys, String methodName) throws PersistenceException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  class RestoreFields
  {
    public Method method;

    public String objectType;

    public Object objectValue;

  }

}
