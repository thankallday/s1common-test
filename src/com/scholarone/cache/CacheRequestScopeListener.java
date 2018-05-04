package com.scholarone.cache;

import java.util.ArrayList;

import javax.servlet.ServletRequest;

import com.scholarone.persistence.TransactionContext;

/**
 * @author ridings
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class CacheRequestScopeListener
{
  private static CacheRequestScopeListener instance;

  private ArrayList requests;

  private ArrayList threads;

  protected CacheRequestScopeListener()
  {
    requests = new ArrayList();
    threads = new ArrayList();
  }

  public static CacheRequestScopeListener getInstance()
  {
    if (instance == null)
    {
      instance = new CacheRequestScopeListener();
    }

    return instance;
  }

  public void requestInitialized(ServletRequest request)
  {
    if (request != null)
    {
      synchronized (requests)
      {
        if (!requests.contains(request))
        {
          requests.add(request);
          threads.add(Thread.currentThread());
        }
      }

      // make sure this thread has no open transactions before starting the
      // request
      TransactionContext tc = new TransactionContext();
      tc.clear();
    }
  }

  public void requestDestroyed(ServletRequest request)
  {
    if (request != null)
    {
      synchronized (requests)
      {
        int n = requests.indexOf(request);
        if (n > -1)
        {
          requests.remove(request);
          threads.remove(n);
        }
      }

      // make sure this thread has no open transactions before completing the
      // request
      TransactionContext tc = new TransactionContext();
      tc.clear();

      // CacheManager cacheManager = CacheManager.getInstance();
      // cacheManager.flush(request.hashCode());
    }
  }

  public ServletRequest getRequest(Thread currentThread)
  {
    ServletRequest r = null;

    synchronized (requests)
    {
      int n = threads.indexOf(currentThread);

      if (n > -1)
      {
        r = (ServletRequest) requests.get(n);
      }
    }

    return r;
  }
}
