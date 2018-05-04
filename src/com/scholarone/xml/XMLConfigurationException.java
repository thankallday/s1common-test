package com.scholarone.xml;

/**
 * The fundamental exception thrown by config and its brethren.
 */

public class XMLConfigurationException extends Exception
{

  public XMLConfigurationException()
  {
    super();
  }

  /**
   * Constructor which takes a message about the nature of the exception
   * 
   * @param inMsg
   *          A message describing the exception
   */
  public XMLConfigurationException(String inMsg)
  {
    super(inMsg);
  }
}

/*---  ---*/
