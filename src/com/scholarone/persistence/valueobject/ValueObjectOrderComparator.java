/*
 * Created on Aug 15, 2003
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
public class ValueObjectOrderComparator implements java.util.Comparator, Serializable
{
  protected boolean reverse;
  public static int GREATER_THEN = 1;
  public static int LESS_THEN = -1;
  public static int EQUAL = 0;
  
  public ValueObjectOrderComparator()
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
    int retValue = EQUAL;
    if (o1 == null && o2 == null) retValue = EQUAL;
    if (o1 == null && o2 != null) retValue = GREATER_THEN;
    if (o2 == null && o1 != null) retValue = LESS_THEN;

    ValueObject v1 = (ValueObject) o1;
    ValueObject v2 = (ValueObject) o2;

    if (v1.getOrder() == null && v2.getOrder() == null) retValue = EQUAL;
    if (v1.getOrder() == null && v2.getOrder() != null) retValue = GREATER_THEN;
    if (v2.getOrder() == null && v1.getOrder() != null) retValue = LESS_THEN;

    if (v2.getOrder() != null && v1.getOrder() != null) retValue = v1.getOrder().intValue() - v2.getOrder().intValue();

    if (!reverse)
      return retValue;
    else
      return (0 - retValue);
  }

}
