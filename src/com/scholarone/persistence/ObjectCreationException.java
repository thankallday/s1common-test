package com.scholarone.persistence;

// com imports
import com.scholarone.ScholarOneException;

/**
 * The fundamental exception thrown by ValueObjectFactory
 */

public class ObjectCreationException extends ScholarOneException
{

  public ObjectCreationException()
  {
    super();
  }

  /**
   * Constructor which takes a message about the nature of the exception
   * 
   * @param inMsg
   *          A message describing the exception
   */
  public ObjectCreationException(String inMsg)
  {
    super(inMsg);
  }
}