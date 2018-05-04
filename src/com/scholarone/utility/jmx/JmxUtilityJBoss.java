/**
 * 
 */
package com.scholarone.utility.jmx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

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
public class JmxUtilityJBoss implements JmxUtility
{
  /**
   * the log4j logger object
   */
  protected static Logger log;

  /**
   * The MBeanServer of the server in which this class was created. Cached
   * here for speed.
   */
  protected MBeanServer rMBeanServer = null;

  /**
   * Initialization on demand holder idiom
   * 
   * @see http://en.wikipedia.org/wiki/Initialization_on_demand_holder_idiom
   */
  private static class SingletonHolder { 
    private final static JmxUtilityJBoss INSTANCE = new JmxUtilityJBoss();
  }

  /**
   * Gets the singleton JmxUtilityJBoss object
   * 
   * @return
   */
  public static JmxUtilityJBoss getInstance()
  {
    return SingletonHolder.INSTANCE;
  }

  /**
   * private default constructor
   */
  private JmxUtilityJBoss()
  {
    log = Logger.getLogger(JmxUtilityJBoss.class);
    getJmxMBeanServer();
  }

  /**
   * Gets the MBeanServer from the current server. 
   */
  public MBeanServer getJmxMBeanServer() {
    if (rMBeanServer == null) {
      try {
        ArrayList servers =  MBeanServerFactory.findMBeanServer(null);
        if( servers.size() > 0 )
        {
          rMBeanServer = (MBeanServer) servers.get(0);
        }
      } catch (Throwable t) {
        //t.printStackTrace();
        log.error("error getting RemoteMBeanServer", t);
        return null;
      }
      return rMBeanServer;
    } else {
      return rMBeanServer;
    }
  }

  /**
   * This method can be called in server to get its own server
   * name. This can then be used in other methods to get other data. 
   * 
   * @return String name of the server this method is called from
   */
  public String getMyServerName()
  {
    String fullServerName = null;
    try 
    {
      ObjectName name = new ObjectName("jboss.system:type=ServerConfig");
      String ServerName = (String)this.rMBeanServer.getAttribute(name, "ServerName");
      name = new ObjectName("jboss.system:type=ServerInfo");
      String HostName = (String)this.rMBeanServer.getAttribute(name, "HostName");
      fullServerName = HostName + "_" + ServerName;
    } catch (Throwable t)
    {
      log.error("error getting this local server name", t);
    }
    return fullServerName;
  }

  /**
   * This method can be called in a server to get its own cluster
   * name. This can then be used in other methods to get other data. 
   * 
   * @return String name of the cluster this method is called from
   */
  public String getMyClusterName()
  {
    String clusterName = null;
    try 
    {
      ObjectName name = new ObjectName("jboss.system:type=ServerConfig");
      clusterName = (String)this.rMBeanServer.getAttribute(name, "ServerName");
    } catch (Throwable t)
    {
      log.error("error getting this local cluster name", t);
    }
    return clusterName;
  }

  /**
   * Gets the InitialContext from the currently running server. Notice that 
   * this is context dependent.
   * 
   * @return InitialContext for the current server
   */
  public Context getContext()
  {
    Context context = null;
    try
    {
      context = new InitialContext();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      log.error("getting InitialContext",e);
    }
    return context;
  }  

  /**
   * Gets the server TCP listen address of the server, this may be 
   * either a name or an IP address
   * 
   * @param serverName - the server name whose listen address you want
   * @return a String containing the listen address of serverName
   */
  public String getListenAddress(String serverName)
  {
    Iterator mbeanIterator = null;
    String listenAddress = null;
    /*
      ServerMBean serverMBean = null;
      try
      {
        mbeanIterator = getMBeans("Server");
        while (mbeanIterator.hasNext())
        {
          Object obj = mbeanIterator.next();
          serverMBean = (ServerMBean) obj;
          if (serverName.trim().equalsIgnoreCase(serverMBean.getName().trim()))
          {
            listenAddress = serverMBean.getListenAddress(); 
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
        log.error("getting listen address for server",e);
      }*/
    return listenAddress;
  }

  /**
   * Gets the server listen port of the server name passed 
   * in as parameter.
   * 
   * @param serverName - the server name whose listen port is desired
   * @return the TCP listen port for the server in serverName
   */
  public int getListenPort(String serverName)
  {
    Iterator mbeanIterator = null;    
    int listenPort = 0;
    /*    
      ServerMBean serverMBean = null;
      try
      {
        mbeanIterator = getMBeans("Server");
        while (mbeanIterator.hasNext())
        {
          Object obj = mbeanIterator.next();
          serverMBean = (ServerMBean) obj;
          if (serverName.trim().equalsIgnoreCase(serverMBean.getName().trim()))
          {
            listenPort = serverMBean.getListenPort(); 
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
        log.error("getting the server listen port",e);
      }*/
    return listenPort;
  }

  /**
   * Gets an Iterator to a set of MBeans
   * 
   * @param type - type of MBeans to return a Set of 
   * @return an Iterator to the set of MBeans of the type type
   * @throws Exception
   */
  public Iterator getMBeans(String type) throws NamingException
  {
    Set mbeanSet = null;
    Iterator mbeanIterator = null;
    java.util.Hashtable env = new java.util.Hashtable(5);
    env.put(Context.SECURITY_PRINCIPAL,"jmxuser");
    env.put(Context.SECURITY_CREDENTIALS,"w3@kguy!");
    Context context = new javax.naming.InitialContext(env); 
//  Context context = getContext();
    /*    MBeanHome home = null;
      home = (MBeanHome) context.lookup(MBeanHome.ADMIN_JNDI_NAME);
      mbeanSet = home.getMBeansByType(type);
      mbeanIterator = mbeanSet.iterator();*/
    return mbeanIterator;
  }

  /**
   * Gets the MBeanHome object of the Admin server.  
   * Not used in JBoss
   * 
   * @return the MBeanHome of the Admin server
   */
  public Object getAdminMBeanHome()
  {
    Object home = null;
    //MBeanHome home = null;

    try
    {
      java.util.Hashtable env = new java.util.Hashtable(5);
      env.put(Context.SECURITY_PRINCIPAL,"jmxuser");
      env.put(Context.SECURITY_CREDENTIALS,"w3@kguy!");
      Context context = new javax.naming.InitialContext(env); 
      //home = (MBeanHome) context.lookup(MBeanHome.ADMIN_JNDI_NAME);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      log.error("getting the MBeanHome object for the Admin server",e);
    }
    return home;
  }

  /**
   * Get the RemoteMBeanServer of the current Admin server. This should work
   * even from a managed server.
   * 
   * @return RemoteMBeanServer - An instance of the MBeanServer of the Admin Server for a cluster
   */
  public Object getAdminRemoteMBeanServer()
  {
    Object rMbeanServer = null;
    //RemoteMBeanServer rMbeanServer = null;

    try
    {
      /*      
        MBeanHome home = getAdminMBeanHome();
        rMbeanServer = home.getMBeanServer();
       */      
    }
    catch (Exception e)
    {
      e.printStackTrace();
      log.error("getting the RemoteMBeanServer of the Admin server",e);
    }
    return rMbeanServer;
  }

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
  public Object getMBeanHome(String serverName)
  {
    Object home = null;
    //MBeanHome home = null;
    /*    
      try
      {
        Environment env = new Environment();
        env.setSecurityPrincipal("jmxuser");
        env.setSecurityCredentials("w3@kguy!");
        String listenAddress = getListenAddress(serverName);
        int listenPort = getListenPort(serverName);
        env.setProviderUrl("t3://"+listenAddress+":"+listenPort);
        Context context = env.getInitialContext();
        home = (MBeanHome) context.lookup(MBeanHome.LOCAL_JNDI_NAME);
      }
      catch (Exception e)
      {
        e.printStackTrace();
        log.error("getting the MBeanHome of the local server:"+serverName,e);
      }
     */    
    return home;
  }

  /**
   * Gets the RemoteMBeanServer of the local managed server. This MBeanserver
   * will have runtime MBeans related to the specific managed server. 
   * 
   * @param serverName - the name of the managed server we want
   * 
   * @return RemoteMBeanServer of the local managed server we are lookng at
   */
  public Object getRemoteMBeanServer(String serverName)
  {
    Object rMbeanServer = null;
    //RemoteMBeanServer rMbeanServer = null;

    try
    {
      /*      
        MBeanHome home = getMBeanHome(serverName);
        rMbeanServer = home.getMBeanServer();
       */      
    }
    catch (Exception e)
    {
      e.printStackTrace();
      log.error("getting the RemoteMBeanServer of the local server:"+serverName,e);
    }
    return rMbeanServer;
  }

  /**
   * Uses the Weblogic MBeanHome method getMBeansByType to 
   * get the Set of all configuration ClusterMBeans in the server.
   * <br/>
   * <b>Note: This is Weblogic specific</b>   
   * 
   * @return a Set of configuration ClusterMBeans 
   */
  public Set getClusterSet()
  {
    Set clusters = null;
    /*
      try
      {

        MBeanHome home = getAdminMBeanHome();
        clusters = home.getMBeansByType("Cluster");
      }
      catch (Exception e)
      {
        e.printStackTrace();
        log.error("getting the Set of ClusterMBeans",e);
      }
     */    
    return clusters;
  }

  /**
   * This method will return one ClusterMBean object when passed the  
   * name of a valid Cluster in a particular domain.
   * 
   * @param clusterName the String name of a cluster
   * 
   * @return either the ClusterMBean for that name, or null if there is 
   * no cluster with that name found. 
   */
  public Object getClusterMBean(String clusterName)
  {
    Object cluster = null;
    // ClusterMBean cluster = null;
    Set clusters = getClusterSet();
    /*    
      if (clusters != null)
      {
        for (Iterator itr = clusters.iterator(); itr.hasNext(); )
        {
          cluster = (ClusterMBean)itr.next();
          if (cluster.getName().equalsIgnoreCase(clusterName))
          {
            return cluster;
          }
        }
      } 
      else
      {
        log.error("unable to get cluster set");
      }
     */    
    return null;
  }

  /**
   * Gets the ServerMBean when passed a cluster and server name.
   * 
   * @param clusterName - the cluster name 
   * @param serverName - the server name whose MBean you want
   * @return
   */
  public Object getServerMBean(String clusterName, String serverName)
  {
    Object server = null;
    //ServerMBean server = null;
    /*    
      ClusterMBean cluster = getClusterMBean(clusterName);
      if (cluster != null)
      {
        ServerMBean[] Servers = cluster.getServers();
        for (int ix = 0; ix < Servers.length; ix++)
        {
          server = (ServerMBean)Servers[ix];
          if (serverName.equalsIgnoreCase(server.getName()))
          {
            break;
          }
        }
      } 
      else
      {
        log.error("unable to get the ClusterMBean");
      }
     */    
    return server; 
  }

  /**
   * Gets the ServerRuntimeMBean when passed a server name.
   * 
   * @param serverName - the server name whose ServerRuntimeMBean you need
   * @return
   */
  public Object getServerRuntimeMBean(String serverName)
  {
    Object serverRT = null;
    /*    
      MBeanHome home = getAdminMBeanHome();
      Set ServerRuntimes = home.getMBeansByType("ServerRuntime");
      ServerRuntimeMBean serverRT = null;
      for (Iterator srtItr = ServerRuntimes.iterator(); srtItr.hasNext(); )
      {
        serverRT = (ServerRuntimeMBean)srtItr.next();
        if (serverName.equalsIgnoreCase(serverRT.getName()))
        {
          break;  
        }
      }*/
    return serverRT;
  }

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
  public String getServerRuntimeStatus(String clusterName, String serverName)
  {
    /*
      MBeanHome home = getAdminMBeanHome();
      Set ServerRuntimes = home.getMBeansByType("ServerRuntime");
      String healthState = "UNKNOWN";
      ServerMBean server = getServerMBean(clusterName, serverName);
      ServerRuntimeMBean serverRT = null;
      for (Iterator srtItr = ServerRuntimes.iterator(); srtItr.hasNext(); )
      {
        serverRT = (ServerRuntimeMBean)srtItr.next();
        if (server.getName().equals(serverRT.getName()))
        {
          break;  
        }
        else
        {
          serverRT = null;
        }
      }
      if (serverRT != null) {
        healthState = serverRT.getState();
        if (healthState != null)
        {
          return healthState;
        }
      }
     */    
    return "UNKNOWN";
  }


  /**
   * Get a Map of all the ServerRuntimeMBeans of all the running 
   * servers in a cluster.  
   * 
   * @param clusterName
   * 
   * @return
   */  
  public Map getClustersServerRuntimeMBeanMap(String clusterName)
  {
    HashMap serverRTs = new HashMap();
    /*    
      MBeanHome home = getAdminMBeanHome();
      Set Clusters = home.getMBeansByType("Cluster");
      Set ServerRuntimes = home.getMBeansByType("ServerRuntime");
      for (Iterator itr = Clusters.iterator(); itr.hasNext(); )
      {
        ClusterMBean cluster = (ClusterMBean)itr.next();
        if (!cluster.getName().equalsIgnoreCase(clusterName))
        {
          continue;
        }
        ServerMBean[] Servers = cluster.getServers();
        for (int ix = 0; ix < Servers.length; ix++)
        {
          ServerMBean server = (ServerMBean)Servers[ix];
          ServerRuntimeMBean serverRT = null;
          for (Iterator srtItr = ServerRuntimes.iterator(); srtItr.hasNext(); )
          {
            serverRT = (ServerRuntimeMBean)srtItr.next();
//          System.out.println(server.getName() + " == " + serverRT.getName()+"?");
            if (server.getName().equals(serverRT.getName()))
            {
//            System.out.println("Yes! " + server.getName() + " == " + serverRT.getName());
              serverRTs.put(serverRT.getName(),serverRT);
              break;  
            }
            else
            {
              serverRT = null;
            }
          }
        }
      }
     */    
    return serverRTs;
  }  


}
