package com.scholarone.logging;


/**
 * Adapter from the generic Logger interface to the log4j underlying implementation.
 */

public class Log4jLogger extends com.scholarone.logging.Logger
{
  private static String FQCN = Log4jLogger.class.getName() + ".";

  private String loggerName = "";

  private transient org.apache.log4j.Logger logImpl = null;

  public Log4jLogger(String name)
  {
    loggerName = name;
  }

  private void createLogger(String name)
  {
    if ((name == null) || name.equals(""))
    {
      logImpl = org.apache.log4j.Logger.getRootLogger();
    }
    else
    {
      logImpl = org.apache.log4j.Logger.getLogger(name);
    }
  }

  public org.apache.log4j.Logger getImpl()
  {
    if (logImpl == null)
    {
      createLogger(loggerName);
    }

    return logImpl;
  }

  public void fatal(String msg)
  {
    getImpl().log(FQCN, ProfileLevel.FATAL, msg, null);
  }

  public void fatal(String msg, Throwable t)
  {
    getImpl().log(FQCN, ProfileLevel.FATAL, msg, t);
  }

  public void error(String msg)
  {
    getImpl().log(FQCN, ProfileLevel.ERROR, msg, null);
  }

  public void error(String msg, Throwable t)
  {
    getImpl().log(FQCN, ProfileLevel.ERROR, msg, t);
  }

  public void warn(String msg)
  {
    getImpl().log(FQCN, ProfileLevel.WARN, msg, null);
  }

  public void warn(String msg, Throwable t)
  {
    getImpl().log(FQCN, ProfileLevel.WARN, msg, t);
  }

  public void info(String msg)
  {
    getImpl().log(FQCN, ProfileLevel.INFO, msg, null);
  }

  public void info(String msg, Throwable t)
  {
    getImpl().log(FQCN, ProfileLevel.INFO, msg, t);
  }

  public void debug(String msg)
  {
    getImpl().log(FQCN, ProfileLevel.DEBUG, msg, null);
  }

  public void debug(String msg, Throwable t)
  {
    getImpl().log(FQCN, ProfileLevel.DEBUG, msg, t);
  }

  public void profile(String msg)
  {
    getImpl().log(FQCN, ProfileLevel.PROFILE, msg, null);
  }

  public boolean isEnabledFor(int level)
  {
    return getImpl().isEnabledFor(ProfileLevel.toLevel(level));
  }

}

/*---  ---*/
