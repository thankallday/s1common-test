package com.scholarone.persistence.helpers;

/**
 * This is the default String Formatter class that returns the same string back when the getASCIIValue is called
 * @author 
 *
 */
public class StringFormaterDefault extends FieldFormater
{
  /**
   * This method will return the ASCII value of the string containing special characters
   * @param value
   * @return
   */
  public String getASCIIValue(String value)
  {
    return value;
  }  
}
