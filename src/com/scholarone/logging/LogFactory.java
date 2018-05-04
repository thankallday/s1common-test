package com.scholarone.logging;

// com imports
// java imports
import java.net.URL;
import java.util.StringTokenizer;

// org imports
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.xml.DOMConfigurator;

import com.scholarone.configuration.CFactory;

/**
 * Creates the appropriate Logger instance based on which object is requesting the logging services. Implements Factory
 * pattern, is a Singleton class. The object logging can be overridden in special circumstances using the call in
 * Logger.getLogger.
 * <p>
 * Requires the following properties to be defined in Configuration: <code>
 *    logger.knownpackages = pkg1, pkg2, pkgN <br>
 *    persistence.datasource.name = JNDI name of DataSource (for JDBC logger)<br>
 *  </code>
 */
public class LogFactory implements LoggerFactory
{
  private static LogFactory factory = null;

  /**
   * the names of all the "known" loggers we want to create special instances of. Application code will just pass in a
   * ref. to the current object, and the LogFactory will decide which Logger that object gets to use.
   */
  private static String[] knownLoggerNames = new String[0];

  /*
   * { "com.scholarone.tags", "com.scholarone.utility", "com.scholarone.persistence",
   * "com.scholarone.common.BaseServlet", "com.scholarone.common.BaseController", "com.scholarone.common.BaseData",
   * "com.scholarone.common.BaseHelper" };
   */
  LogFactory()
  {
    try
    {
      String names = CFactory.instance().getProperty("logger.knownpackages");

      if (!names.equals(""))
      {
        StringTokenizer st = new StringTokenizer(names, ", ");

        knownLoggerNames = new String[st.countTokens()];

        int i = 0;

        while (st.hasMoreTokens())
        {
          knownLoggerNames[i++] = st.nextToken();
        }
      }
    }
    catch (IllegalStateException ise)
    {
      System.err.println(ise);
    }
  }

  /**
   * implementation of LoggerFactory interface to make this play nice with log4j.
   */
  public org.apache.log4j.Logger makeNewLoggerInstance(String name)
  {
    if ((name == null) || (name.length() == 0))
    {
      return org.apache.log4j.Logger.getRootLogger();
    }
    else
    {
      return org.apache.log4j.Logger.getLogger(name);
    }
  }

  Logger getLogger(Object loggingObj)
  {
    return LogFactory.getInstance().findLogger(loggingObj);
  }

  /**
   * This method only needs to be called if re-initing. Log4j will find log4j.xml by default on startup.
   */
  public void configureLogger()
  {
    URL confFileURL = LogFactory.class.getClassLoader().getResource("log4j.xml");

    // if it's trying to load it from an http URL, it most likely means it's
    // being called from within the Applet. In which case, switch to a
    // stripped-down properties file contained within the JAR file instead
    if ((confFileURL != null) && confFileURL.toString().toLowerCase().startsWith("http"))
    {
      // for right now just return, logging will get the BasicConfiguration
      return;
    }
    else
    {
      DOMConfigurator.configure(confFileURL);
    }
  }

  public synchronized static LogFactory getInstance()
  {
    if (factory == null)
    {
      factory = new LogFactory();
    }

    return factory;
  }

  void initLoggingInfo(String message)
  {
    // clear out the last one to keep the NDC stack at 1
    NDC.pop();
    NDC.push(message);
  }

  void freeLoggingInfo()
  {
    NDC.pop();
    NDC.remove();
  }

  /**
   * Store the Object o under the key for later use in the current thread
   */
  public static void putLoggingObject(String key, Object o)
  {
    MDC.put(key, o);
  }

  /**
   * Retrieve the Object that was stored under the key in the current thread
   */
  public static Object getLoggingObject(String key)
  {
    return MDC.get(key);
  }

  /**
   * Remove the Object that was stored under the key in the current thread
   */
  public static void removeLoggingObject(String key)
  {
    MDC.remove(key);
  }
  
  /**
   * This finds the logger by class name OR using the specific name indicated in a String by the caller.
   * 
   * @param loggingObj
   *          EITHER the class being logged OR a string indicateing the log4j logger configured to handle this log
   *          entry. If the object is a string, this method assumes that string is the name of a logger defined in the
   *          log4j configuration and just passes the string through. If the argument is an instance of any other class,
   *          this method uses <b>knownLoggerNames</b> from the system properties to work out what log4j logger to use.
   * @return
   */
  private Logger findLogger(Object loggingObj)
  {
    String loggerName = ""; // = default logger

    Class origClazz;
    Class clazz;

    if (loggingObj instanceof String)
    {
      loggerName = (String) loggingObj;
    }
    else
    {
      if (loggingObj instanceof Class)
      {
        origClazz = (Class) loggingObj;
      }
      else
      {
        origClazz = loggingObj.getClass();
      }

      String pkgName = loggingObj.getClass().getPackage().getName();

      boolean found = false;

      for (int i = 0; i < knownLoggerNames.length; i++)
      {
        // find an exact match based on class
        clazz = origClazz;

        while (clazz != null)
        {
          String className = clazz.getName();

          if (className.equals(knownLoggerNames[i]))
          {
            loggerName = knownLoggerNames[i];
            found = true;

            break;
          }
          else
          {
            // look if a base class matches
            clazz = clazz.getSuperclass();
          }
        }

        // if pkgName matches, remember it, but keep looking for more specific
        if (!found && pkgName.equals(knownLoggerNames[i]))
        {
          loggerName = pkgName;
        }
      }
    }

    return createLogger(loggerName); // instantiate based on loggerName;
  }

  /**
   * the actual "Factory" method
   * 
   * @todo: implement construct of log4j logger obj.
   */
  private Logger createLogger(String loggerName)
  {
    return new Log4jLogger(loggerName);
  }
}

/*---  ---*/
