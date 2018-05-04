package com.scholarone.cache;

import junit.framework.TestCase;

import com.scholarone.persistence.ObjectCreationException;
import com.scholarone.persistence.PersistenceException;

/**
 * @author kenk
 */
public class CacheableObjectHolderTest extends TestCase
{

  public void testConstructor()
  {
    try
    {
      CacheableObjectHolder co = new CacheableObjectHolder();
      assertNull("Wrong key 1", co.getKey());
      assertNull("Wrong object 1", co.getObject());

      MockCacheableObject mco = null;
      co = new CacheableObjectHolder(mco);
      assertNull("Wrong key 2", co.getKey());
      assertNull("Wrong object 2", co.getObject());

      mco = new MockCacheableObject(false);
      co = new CacheableObjectHolder(mco);
      assertNull("Wrong key 3", co.getKey());
      assertEquals("Wrong object 3", mco, co.getObject());

      mco = new MockCacheableObject(true);
      co = new CacheableObjectHolder(mco);
      assertEquals("Wrong key 4", mco.getPrimaryKey(), co.getKey());
      assertNull("Wrong object 4", co.getObject());

      Object foo = new Object();
      co = new CacheableObjectHolder(foo);
      assertNull("Wrong key 5", co.getKey());
      assertEquals("Wrong object 5", foo, co.getObject());
    }
    catch (ObjectCreationException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (PersistenceException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void testGet()
  {
    try
    {
      CacheableObjectHolder co = new CacheableObjectHolder();
      assertNull("Wrong get 1", co.getObject());

      MockCacheableObject mco = null;
      co = new CacheableObjectHolder(mco);
      assertNull("Wrong get 2", co.getObject());

      mco = new MockCacheableObject(false);
      co = new CacheableObjectHolder(mco);
      assertEquals("Wrong get 3", mco, co.getObject());

      mco = new MockCacheableObject(true);
      co = new CacheableObjectHolder(mco);
      assertEquals("Wrong get 4", mco, co.getObject());

      Object foo = new Object();
      co = new CacheableObjectHolder(foo);
      assertEquals("Wrong get 5", foo, co.getObject());
    }
    catch (ObjectCreationException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (PersistenceException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void testGetKey()
  {
    CacheableObjectHolder co = new CacheableObjectHolder();
    assertNull("Wrong key 1", co.getKey());

    MockCacheableObject mco = null;
    co = new CacheableObjectHolder(mco);
    assertNull("Wrong key 2", co.getKey());

    mco = new MockCacheableObject(false);
    co = new CacheableObjectHolder(mco);
    assertNull("Wrong key 3", co.getKey());

    mco = new MockCacheableObject(true);
    co = new CacheableObjectHolder(mco);
    assertEquals("Wrong key 4", mco.getPrimaryKey(), co.getKey());

    Object foo = new Object();
    co = new CacheableObjectHolder(foo);
    assertNull("Wrong key 5", co.getKey());
  }
}
