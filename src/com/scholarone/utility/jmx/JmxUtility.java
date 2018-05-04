package com.scholarone.utility.jmx;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;
import javax.naming.Context;
import javax.naming.NamingException;


/**
 * This class is a collection of convenience methods for using JMX. Currently
 * it contains methods to access the local MBeanServer of the server in
 * which the method is called. It also has methods for getting the cluster name
 * and Cluster MBean, the current server name, Server MBean, and Server Runtime
 * MBean.      
 * 
 * @author John Black
 *
 */
public interface JmxUtility
{
  /**
   * Gets the local MBeanServer from the current server using the 
   * default user name/password. 
   * BEA claims that you can get the standard J2SE MBeanServer from a managed server, but their example
   * fails everytime. I have also found an article written by an BEA engineer that says it can't be done.
   * So this at least must remain BEA specific. It should be in a separate file. 
   */
  public MBeanServer getJmxMBeanServer();
  
  /**
   * This method can be called in a managed server to get its own server
   * name. This can then be used in other methods to get other data. 
   * 
   * @return String name of the server this method is called from
   */
  public String getMyServerName();

  /**
   * This method can be called in a managed server to get its own cluster
   * name. This can then be used in other methods to get other data. 
   * 
   * @return String name of the cluster this method is called from
   */
  public String getMyClusterName();

  /**
   * Gets the InitialContext from the currently running server. Notice that 
   * this is context dependent. If called from an Admin server it will 
   * get the context of the Admin server, if from a managed server, then 
   * it will return the context of a managed server.
   * 
   * @return InitialContext for the current server
   */
  public Context getContext();

  /**
   * Gets the server TCP listen address of the server, this may be 
   * either a name or an IP address
   * 
   * @param serverName - the server name whose listen address you want
   * @return a String containing the listen address of serverName
   */
  public String getListenAddress(String serverName);

  /**
   * Gets the Weblogic server listen port of the server name passed 
   * in as parameter.
   * 
   * @param serverName - the server name whose listen port is desired
   * @return the TCP listen port for the server in serverName
   */
  public int getListenPort(String serverName);
  
  /**
   * Gets an Iterator to a set of MBeans returned by the the Weblogic 
   * MBeanHome getMBeansByType method  
   * 
   * @param type - type of MBeans to return a Set of 
   * @return an Iterator to the set of MBeans of the type type
   * @throws Exception
   */
  public Iterator getMBeans(String type) throws NamingException;
  
  /**
   * Gets the MBeanHome object of the Admin server.  
   * 
   * @return the MBeanHome of the Admin server
   */
  public Object getAdminMBeanHome();

  /**
   * Get the RemoteMBeanServer of the current Admin server. This should work
   * even from a managed server.
   * 
   * @return RemoteMBeanServer - An instance of the MBeanServer of the Admin Server for a cluster
   */
  public Object getAdminRemoteMBeanServer();

  /**
   * This gets the MBeanHome object from a (possibly managed) server that 
   * you have the server name for  
   * 
   * @param serverName - String containing the name of a server on which you 
   * want to retrieve the MBeanHome object
   * 
   * @return an instance of MBeanHome from the server whose name was passed 
   * in as a parameter 
   */
  public Object getMBeanHome(String serverName);
  
  /**
   * Gets the RemoteMBeanServer of the local managed server. This MBeanserver
   * will have runtime MBeans related to the specific managed server. 
   * 
   * @param serverName - the name of the managed server we want
   * 
   * @return RemoteMBeanServer of the local managed server we are lookng at
   */
  public Object getRemoteMBeanServer(String serverName);

  /**
   * Uses the Weblogic MBeanHome method getMBeansByType to 
   * get the Set of all configuration ClusterMBeans in the server.
   * <br/>
   * <b>Note: This is Weblogic specific</b>   
   * 
   * @return a Set of configuration ClusterMBeans 
   */
  public Set getClusterSet();
    
  /**
   * This method will return one ClusterMBean object when passed the  
   * name of a valid Cluster in a particular domain.
   * 
   * @param clusterName the String name of a cluster
   * 
   * @return either the ClusterMBean for that name, or null if there is 
   * no cluster with that name found. 
   */
  public Object getClusterMBean(String clusterName);
  
  /**
   * Gets the ServerMBean when passed a cluster and server name.
   * 
   * @param clusterName - the cluster name 
   * @param serverName - the server name whose MBean you want
   * @return
   */
  public Object getServerMBean(String clusterName, String serverName);

  /**
   * Gets the ServerRuntimeMBean when passed a server name.
   * 
   * @param serverName - the server name whose ServerRuntimeMBean you need
   * @return
   */
  public Object getServerRuntimeMBean(String serverName);
  
  /**
   * Gets the status value of a particular ServerRuntimeMBean within a 
   * particular cluster
   * 
   * @param clusterName - the cluster name  
   * @param serverName - the server name of the ServerRuntimeMBean you 
   * want to access
   * @return the String with the status of the ServerRuntimeMBean you 
   * are interested in. The value 
   */
  public String getServerRuntimeStatus(String clusterName, String serverName);
  
  /**
   * Get a Map of all the ServerRuntimeMBeans of all the running 
   * servers in a cluster.  
   * 
   * @param clusterName
   * 
   * @return
   */  
  public Map getClustersServerRuntimeMBeanMap(String clusterName);

}
