/**
 * 
 */
package com.scholarone.utility.jmx;

import com.scholarone.configuration.CFactory;

/**
 * @author U0097675
 *
 */
public class JmxUtilityFactory
{
  private static JmxUtility jmxUtility = null;
  
  public static JmxUtility getInstance()
  {
    if (jmxUtility == null)
    {
      String appServer = CFactory.instance().getProperty("app.server");
      if ("jboss".equalsIgnoreCase(appServer))
      {
        jmxUtility = JmxUtilityJBoss.getInstance();
      }
    }
    return jmxUtility;
  }
}
