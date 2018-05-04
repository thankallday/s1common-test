package com.scholarone.persistence;

// com imports
import com.scholarone.ScholarOneException;

/**
 * The fundamental exception thrown by the Persistence package
 */

public class PersistenceException extends ScholarOneException
{

  private String sqlState;

  public final static String DUPE_USER_NAME = "75001";

  public final static String MORE_THAN_ONE_ROW = "75002";

  public final static String QUERY_TOO_EXPENSIVE = "75003";

  public final static String DATA_TRUNCATION = "75004";

  public PersistenceException()
  {
    super();
  }

  /**
   * Constructor which takes a message about the nature of the exception
   * 
   * @param inMsg
   *          A message describing the exception
   */
  public PersistenceException(String inMsg)
  {
    super(inMsg);
  }

  public PersistenceException(String message, Throwable cause)
  {
    super(message);

    initCause(cause);
  }

  public PersistenceException(String inMsg, String sqlState)
  {
    super(inMsg);

    this.sqlState = sqlState;
  }

  /**
   * @return String
   */
  public String getSqlState()
  {
    return sqlState;
  }
}

/*---  ---*/
