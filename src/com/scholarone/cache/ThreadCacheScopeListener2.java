package com.scholarone.cache;

import java.util.HashMap;

/**
 * @author ridings
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class ThreadCacheScopeListener2
{
  private static ThreadCacheScopeListener2 instance;

  public static final ThreadLocal<HashMap<Object, Object>> threadCacheLocal = new ThreadLocal<HashMap<Object, Object>>();

  protected ThreadCacheScopeListener2()
  {
  }

  private static ThreadCacheScopeListener2 getInstance()
  {
    if (instance == null)
    {
      instance = new ThreadCacheScopeListener2();
      
    }

    return instance;
  }

  private void unset()
  {
    threadCacheLocal.remove();
  }

  private Object getAttribute(Object key)
  {
    HashMap<Object, Object> map = threadCacheLocal.get();
    if ( map == null )
    {
      map = new HashMap<Object, Object>();
      threadCacheLocal.set(map);
    }
    
    return map.get(key);
  }
  
  private void setAttribute(Object key, Object value)
  {
    HashMap<Object, Object> map = threadCacheLocal.get();
    if ( map == null )
    {
      map = new HashMap<Object, Object>();
      threadCacheLocal.set(map);
    }
    
    map.put(key, value);
  }
}
