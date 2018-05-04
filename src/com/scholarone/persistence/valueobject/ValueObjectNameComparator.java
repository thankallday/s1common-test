/*
 * Created on Sep 22, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments 
 */
package com.scholarone.persistence.valueobject;

import java.io.Serializable;


/**
 * @author greg
 * 
 * To change the template for this generated type comment go to Window>Preferences>Java>Code Generation>Code and
 * Comments
 */
public class ValueObjectNameComparator implements java.util.Comparator, Serializable
{
  protected boolean reverse;
  public static int GREATER_THEN = 1;
  public static int LESS_THEN = -1;
  public static int EQUAL = 0;
  
  public ValueObjectNameComparator()
  {
    super();
    // set "natural" ordering for comparator
    reverse = false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare(Object o1, Object o2)
  {
    if (o1 == null && o2 == null) return EQUAL;
    if (o1 == null)
    {
      if (!reverse)
        return LESS_THEN;
      else
        return (GREATER_THEN);
    }
    if (o2 == null)
    {
      if (!reverse)
        return GREATER_THEN;
      else
        return (LESS_THEN);
    }
    ValueObject vo1 = (ValueObject) o1;
    ValueObject vo2 = (ValueObject) o2;

    if (vo1.getName() == null && vo2.getName() == null)
    {
      return EQUAL;
    }
    if (vo1.getName() == null)
    {
      if (!reverse)
        return LESS_THEN;
      else
        return (GREATER_THEN);
    }
    if (vo2.getName() == null)
    {
      if (!reverse)
        return GREATER_THEN;
      else
        return (LESS_THEN);
    }
    int compare = vo1.getName().toUpperCase().compareTo(vo2.getName().toUpperCase());
    if (reverse)
      return 0 - compare;
    else
      return compare;
  }

}
