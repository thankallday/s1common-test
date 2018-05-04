package com.scholarone.persistence.validator;

import com.scholarone.persistence.PersistenceException;

public class PersistenceValidationException extends PersistenceException
{
  private static final long serialVersionUID = 1L;

  public PersistenceValidationException()
  {
    super();
  }

  /**
   * Constructor which takes a message about the nature of the exception
   * 
   * @param inMsg
   *          A message describing the exception
   */
  public PersistenceValidationException(String inMsg)
  {
    super(inMsg);
  }
}
