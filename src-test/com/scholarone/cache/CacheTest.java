package com.scholarone.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import com.scholarone.persistence.ObjectCreationException;
import com.scholarone.persistence.PersistenceException;

public class CacheTest extends TestCase
{
	public void testConstructor()
	{
		Cache c = new Cache(1);
		assertNotNull("Null map", c.getCacheMap());
		assertEquals("Wrong map size", 0, c.getCacheMap().size());
		assertNotNull("Null time manager", c.getTimeManager());
	}

	public void testPut()
	{
		Cache c = new Cache(1);

		c.put(null, null);
		assertEquals("Wrong size A", 0, c.getCacheMap().size());

		MockCacheableObject mco1 = new MockCacheableObject(false);
		c.put(mco1.getPrimaryKey(), null);
		assertEquals("Wrong size B", 0, c.getCacheMap().size());

		c.put(mco1.getPrimaryKey(), mco1);
		assertEquals("Wrong size C", 0, c.getCacheMap().size());

		c.put(null, mco1);
		assertEquals("Wrong size D", 0, c.getCacheMap().size());

		mco1.isCacheable = true;
		c.put(mco1.getPrimaryKey(), mco1);
		assertEquals("Wrong size E", 1, c.getCacheMap().size());

		c.put(null, mco1);
		assertEquals("Wrong size F", 1, c.getCacheMap().size());

		c.put(mco1.getPrimaryKey(), mco1);
		assertEquals("Wrong size G", 1, c.getCacheMap().size());

		CacheKey key1 = new CacheKey(mco1.getClass(), new Object[0]);
		c.put(key1, mco1);
		assertEquals("Wrong size H", 2, c.getCacheMap().size());

		MockCacheableObject mco2 = new MockCacheableObject(true);
		CacheKey key2 = new CacheKey(mco2.getClass(), new Object[] { mco2 });
		c.put(key2, mco2);
		assertEquals("Wrong size I", 4, c.getCacheMap().size());

		HashMap map = (HashMap) c.getCacheMap();
		Set keys = map.keySet();
		assertEquals("Wrong keys size J", 4, keys.size());
		assertTrue("Missing key 1", keys.contains(mco1.getPrimaryKey()));
		assertTrue("Missing key 2", keys.contains(mco2.getPrimaryKey()));
		assertTrue("Missing key 3", keys.contains(key1));
		assertTrue("Missing key 4", keys.contains(key2));

		Set values = new HashSet(map.values());
		assertEquals("Wrong values size K", 2, values.size());

		assertTrue("Wrong type for key 1", map.get(key1) instanceof CachePointer);
		assertTrue("Wrong type for key 2", map.get(key2) instanceof CachePointer);

		assertEquals("Keys should hit same pointer 1", map.get(key1), map.get(mco1.getPrimaryKey()));
		assertEquals("Keys should hit same pointer 2", map.get(key2), map.get(mco2.getPrimaryKey()));
	}

	public void testPutFail()
	{
		Cache c = new Cache(1);
		MockCacheableObject mco = new MockCacheableObject();
		c.put(null, mco);
		assertEquals("Null-key put should succeed", 1, c.getCacheMap().size());

		c = new Cache(1);
		c.put(mco.getPrimaryKey(), null);
		assertEquals("Null-object put should do nothing and not fail.", 0, c.getCacheMap().size());

		SubMock sub = new SubMock();

		c = new Cache(1);
		c.put(sub.getPrimaryKey(), sub);
		assertEquals("Sub-mock put should succeed", 1, c.getCacheMap().size());

		c = new Cache(1);
		c.put(mco.getPrimaryKey(), sub);
		assertEquals("Sub-mock put with mock key should succeed.", 2, c.getCacheMap().size());

		c = new Cache(1);
		try
		{
			c.put(sub.getPrimaryKey(), mco);
			fail("Should not allow put(sub-mock key, mock-object)");
		}
		catch (AssertionFailedError afe)
		{
			throw afe;
		}
		catch (Throwable t)
		{
			assertTrue("Wrong type thrown: " + t.getClass().getName(), (t instanceof ClassCastException));
		}

		c = new Cache(1);
		try
		{
			CacheKey key = new CacheKey(String.class, Integer.valueOf(12));
			c.put(key, mco);
			fail("Should not allow put(String-class key, mock-object)");
		}
		catch (AssertionFailedError afe)
		{
			throw afe;
		}
		catch (Throwable t)
		{
			assertTrue("Wrong type thrown: " + t.getClass().getName(), (t instanceof ClassCastException));
		}

	}

	public void testGet()
	{
		MockCacheableObject mco1 = new MockCacheableObject(true);
		CacheKey key1 = new CacheKey(mco1.getClass(), new Object[] { mco1 });
		MockCacheableObject mco2 = new MockCacheableObject(true);
		CacheKey key2 = new CacheKey(mco2.getClass(), new Object[] { mco2 });
    try
    {

		Cache cache = new Cache(1);
		CacheableObject result = cache.get(null);
		assertNull("Result should be null (A)", result);

		result = cache.get(key1);
		assertNull("Result should be null (B)", result);

		cache.put(key1, mco1);
		cache.put(key2, mco2);

		assertEquals("Wrong get (C)", mco1, cache.get(key1));
		assertEquals("Wrong get (D)", mco1, cache.get(mco1.getPrimaryKey()));
		assertEquals("Wrong get (E)", mco2, cache.get(key2));
		assertEquals("Wrong get (F)", mco2, cache.get(mco2.getPrimaryKey()));
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

	public void testContainsKey()
	{
		MockCacheableObject mco1 = new MockCacheableObject(true);
		CacheKey key1 = new CacheKey(mco1.getClass(), new Object[] { mco1 });
		MockCacheableObject mco2 = new MockCacheableObject(true);
		CacheKey key2 = new CacheKey(mco2.getClass(), new Object[] { mco2 });

		Cache cache = new Cache(1);
		assertFalse("Empty 1", cache.containsKey(null));
		assertFalse("Empty 2", cache.containsKey(key1));

		cache.put(key1, mco1);
		cache.put(key2, mco2);

		assertTrue("Missing key 1", cache.containsKey(key1));
		assertTrue("Missing key 2", cache.containsKey(mco1.getPrimaryKey()));
		assertTrue("Missing key 3", cache.containsKey(key2));
		assertTrue("Missing key 4", cache.containsKey(mco2.getPrimaryKey()));
	}

	public void testFlush()
	{
		MockCacheableObject mco1 = new MockCacheableObject(true);
		CacheKey key1 = new CacheKey(mco1.getClass(), new Object[] { mco1 });
		MockCacheableObject mco2 = new MockCacheableObject(true);
		CacheKey key2 = new CacheKey(mco2.getClass(), new Object[] { mco2 });
		MockCacheableObject mco3 = new MockCacheableObject(true);
		CacheKey key3 = new CacheKey(mco3.getClass(), new Object[] { mco3 });

		Cache cache = new Cache(1);
		// Testing for exceptions
		cache.flush(key1, false);
		cache.flush((CacheKey) null, false);
		cache.flush(key1, true);
		cache.flush((CacheKey) null, true);

		cache.put(key1, mco1);
		cache.put(key2, mco2);
		cache.put(key3, mco3);

		cache.flush(key1, false);
		assertEquals("Wrong size A", 4, cache.getCacheMap().size());
		assertEquals("Wrong flush count 1", 0, mco1.flushCount);
		assertFalse("Should not contain key 1", cache.containsKey(key1));
		assertFalse("Should not contain key 2", cache.containsKey(mco1.getPrimaryKey()));
		assertTrue("Should contain key 3", cache.containsKey(key2));
		assertTrue("Should contain key 4", cache.containsKey(mco2.getPrimaryKey()));
		assertTrue("Should contain key 5", cache.containsKey(key3));
		assertTrue("Should contain key 6", cache.containsKey(mco3.getPrimaryKey()));

		HashMap temp = new HashMap(cache.getCacheMap());
		cache.flush(mco1.getPrimaryKey(), true);
		assertEquals("Map should not be changed 1a", temp, cache.getCacheMap());
		assertEquals("Wrong flush count 1a", 0, mco1.flushCount);
		cache.flush(key1, true);
		assertEquals("Map should not be changed 1b", temp, cache.getCacheMap());
		assertEquals("Wrong flush count 1b", 0, mco1.flushCount);

		cache.flush(mco2.getPrimaryKey(), false);
		assertEquals("Wrong size B", 2, cache.getCacheMap().size());
		assertEquals("Wrong flush count 2", 0, mco2.flushCount);
		assertFalse("Should not contain key 11", cache.containsKey(key1));
		assertFalse("Should not contain key 12", cache.containsKey(mco1.getPrimaryKey()));
		assertFalse("Should contain key 13", cache.containsKey(key2));
		assertFalse("Should contain key 14", cache.containsKey(mco2.getPrimaryKey()));
		assertTrue("Should contain key 15", cache.containsKey(key3));
		assertTrue("Should contain key 16", cache.containsKey(mco3.getPrimaryKey()));

		temp = new HashMap(cache.getCacheMap());
		cache.flush(mco2.getPrimaryKey(), true);
		assertEquals("Map should not be changed 2a", temp, cache.getCacheMap());
		assertEquals("Wrong flush count 2a", 0, mco2.flushCount);
		cache.flush(key2, true);
		assertEquals("Map should not be changed 2b", temp, cache.getCacheMap());
		assertEquals("Wrong flush count 2b", 0, mco2.flushCount);

		cache.flush(key3, true);

		assertEquals("Wrong size C", 0, cache.getCacheMap().size());
		assertEquals("Wrong flush count 3", 1, mco3.flushCount);

		temp = new HashMap(cache.getCacheMap());
		cache.flush(mco3.getPrimaryKey(), true);
		assertEquals("Map should not be changed 3a", temp, cache.getCacheMap());
		assertEquals("Wrong flush count 3a", 1, mco3.flushCount);
		cache.flush(key3, true);
		assertEquals("Map should not be changed 3b", temp, cache.getCacheMap());
		assertEquals("Wrong flush count 3b", 1, mco3.flushCount);
	}

	public void testFlushAll()
	{
		MockCacheableObject mco1 = new MockCacheableObject(true);
		CacheKey key1 = new CacheKey(mco1.getClass(), new Object[] { mco1 });
		MockCacheableObject mco2 = new MockCacheableObject(true);
		CacheKey key2 = new CacheKey(mco2.getClass(), new Object[] { mco2 });
		MockCacheableObject mco3 = new MockCacheableObject(true);
		CacheKey key3 = new CacheKey(mco3.getClass(), new Object[] { mco3 });

		Cache cache = new Cache(1);
		// Testing for exceptions
		cache.clear();

		cache.put(key1, mco1);
		cache.put(key2, mco2);
		cache.put(key3, mco3);

		cache.clear();
		assertEquals("Wrong size", 0, cache.getCacheMap().size());
		assertEquals("Wrong flush count 1", 0, mco1.flushCount);
		assertEquals("Wrong flush count 2", 0, mco2.flushCount);
		assertEquals("Wrong flush count 3", 0, mco3.flushCount);
	}

	public class SubMock extends MockCacheableObject
	{
	}

	public class SubMock2 extends MockCacheableObject
	{
		public CacheKey getPrimaryKey()
		{
			return new CacheKey(SubMock.class, getId());
		}
	}
}
