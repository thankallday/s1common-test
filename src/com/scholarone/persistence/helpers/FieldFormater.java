package com.scholarone.persistence.helpers;

/**
 * This FieldFormater class provides methods to get Upper case value of a string and the ASCII value of a string
 * 
 */
public abstract class FieldFormater
{
  /**
   * This method will return the ASCII value of the string containing special characters
   * 
   * @param value
   * @return
   */
  public abstract String getASCIIValue(String value);

  /**
   * This method will return the upper case ASCII value of the string containing special characters
   * 
   * @param value
   * @return
   */
  public String getUpperASCIIValue(String value)
  {
    if (value == null) return null;
    return getASCIIValue(value).toUpperCase();
  }

  /**
   * This method will return the upper case value of the string
   * 
   * @param value
   * @return
   */
  public String getUpperValue(String value)
  {
    return value.toUpperCase();
  }
}
