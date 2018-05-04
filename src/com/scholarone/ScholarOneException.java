package com.scholarone;
//
//변경

// java imports
import java.sql.Timestamp;

/**
 * Class defining a package level exception for the ScholarOne package.
 */
public class ScholarOneException extends Exception
{
  private static final long serialVersionUID = 1L;

  private Timestamp time;

  private String userErrorMessage;

  /**
   * Constructs an ScholarOneException
   * 
   * @param time
   *          the Timestamp when the exception occurred
   */

  public ScholarOneException()
  {
    super();

    time = new Timestamp(System.currentTimeMillis());
  }

  public ScholarOneException(String msg)
  {
    super(msg);

    time = new Timestamp(System.currentTimeMillis());
  }

  public ScholarOneException(Throwable cause)
  {
    super(cause);

    time = new Timestamp(System.currentTimeMillis());
  }

  public ScholarOneException(String msg, Throwable cause)
  {
    super(msg, cause);

    time = new Timestamp(System.currentTimeMillis());
  }

  /**
   * Getter for property time.
   * 
   * @return Value of property time.
   */
  public Timestamp getTime()
  {
    return time;
  }

  /**
   * Setter for property time.
   * 
   * @param time
   *          New value of property time.
   */
  public void setTime(Timestamp time)
  {
    this.time = time;
  }

  public String getUserErrorMessage()
  {
    return userErrorMessage;
  }

  public void setUserErrorMessage(String userErrorMessage)
  {
    this.userErrorMessage = userErrorMessage;
  }
}
