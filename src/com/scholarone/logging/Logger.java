package com.scholarone.logging;

// com imports

// java imports
import java.io.Serializable;

/**
 * Abstract base class representing a logger in the system.
 * <p>
 * I wanted to provide this generic, stripped down interface to the system instead of the log4j package directly, in
 * case we want to replace it later.
 * <p>
 * Re-using the name Logger because it just fits so well. Be careful of collisions.
 */

public abstract class Logger implements Serializable
{

  public static final int LEVEL_FATAL = ProfileLevel.FATAL_INT;

  public static final int LEVEL_ERROR = ProfileLevel.ERROR_INT;

  public static final int LEVEL_WARN = ProfileLevel.WARN_INT;

  public static final int LEVEL_INFO = ProfileLevel.INFO_INT;

  public static final int LEVEL_DEBUG = ProfileLevel.DEBUG_INT;

  public static final int LEVEL_PROFILE = ProfileLevel.PROFILE_INT;

  public static final String THREAD_LOGGER_KEY = "THREAD_LOGGER_KEY";

  public static void configure()
  {
    LogFactory.getInstance().configureLogger();
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
  public static Logger getLogger(Object loggingObj)
  {
    return LogFactory.getInstance().getLogger(loggingObj);
  }

  /**
   * used to initialize additional diagnostic info that will be output for all the loggers associated with a particular
   * session
   */
  public static void initLoggingInfo(String message)
  {
    LogFactory.getInstance().initLoggingInfo(message);
  }

  /**
   * frees the memory associated with diagnostic info when finished. Should be called only once at exit of service()
   * method.
   */
  public static void freeLoggingInfo()
  {
    LogFactory.getInstance().freeLoggingInfo();
  }
  
  /**
   * Store the Object o under the key for later use in the current thread
   */
  public static void putLoggingObject(String key, Object o)
  {
    LogFactory.putLoggingObject(key, o);
  }
  
  /**
   * Retrieve the Object that was stored under the key in the current thread
   */
  public static Object getLoggingObject(String key)
  {
    return LogFactory.getLoggingObject(key);
  }

  /**
   * Remove the Object that was stored under the key in the current thread
   */
  public static void removeLoggingObject(String key)
  {
    LogFactory.removeLoggingObject(key);
  }
  
  /**
   * Store the given Logger so it can be retrieved for logging in the current thread
   */
  public static void registerLoggerForCurrentThread(Logger logger)
  {
    putLoggingObject(THREAD_LOGGER_KEY, logger);
  }

  /**
   * Get the Logger that has been registered for logging in the current thread, or the default
   * Logger if no Logger has been registered.
   */
  public static Logger getLoggerForCurrentThread()
  {
    Logger threadLogger = (Logger) getLoggingObject(THREAD_LOGGER_KEY);
    return (threadLogger == null) ? getLogger("") : threadLogger;
  }

  /**
   * Remove the Logger that has been registered for logging in the current thread
   */
  public static void removeLoggerForCurrentThread()
  {
    removeLoggingObject(THREAD_LOGGER_KEY);
  }

  public abstract void fatal(String msg);

  public abstract void fatal(String msg, Throwable t);

  public abstract void error(String msg);

  public abstract void error(String msg, Throwable t);

  public abstract void warn(String msg);

  public abstract void warn(String msg, Throwable t);

  public abstract void info(String msg);

  public abstract void info(String msg, Throwable t);

  public abstract void debug(String msg);

  public abstract void debug(String msg, Throwable t);

  /** added logging level to output profiling/timing info */
  public abstract void profile(String msg);

  public abstract boolean isEnabledFor(int level);

}

/*---  ---*/
