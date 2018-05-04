package com.scholarone.cache;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CachePointerTest extends TestCase
{
	private long	startTime;

	public void setUp()
	{
		startTime = System.currentTimeMillis();
	}

	public void testEmpty()
	{
		CachePointer cp = new CachePointer();
		long now = System.currentTimeMillis();
		long last = cp.getLastAccessTime();
		assertNull("object should be null", cp.fetchObject());
		assertNull("time-list should be null", cp.fetchTimeList());
		assertTrue("Time is too early: (" + last + " < " + startTime + ")", (last >= startTime));
		assertTrue("Time is too late: (" + last + " > " + now + ")", (last <= now));
		int minute = TimeList.shrinkTime(last);
		assertEquals("Wrong minute", minute, cp.fetchLastAccessMinute());
		assertEquals("Wrong touched value", false, cp.fetchUntouched());
		Set keys = cp.getCacheKeys();
		assertEquals("Wrong number of keys", 0, keys.size());
	}

	public void testNormalConstructor()
	{
		MockCacheableObject mco = new MockCacheableObject();
		TimeList tl = new TimeList();
		CachePointer cp = new CachePointer(mco, tl);
		long now = System.currentTimeMillis();
		long last = cp.getLastAccessTime();
		assertSame("wrong object", mco, cp.fetchObject());
		assertSame("wrong time-list", tl, cp.fetchTimeList());
		assertTrue("Time is too early: (" + last + " < " + startTime + ")", (last >= startTime));
		assertTrue("Time is too late: (" + last + " > " + now + ")", (last <= now));
		int minute = TimeList.shrinkTime(last);
		assertEquals("Wrong minute", minute, cp.fetchLastAccessMinute());
		Set keys = cp.getCacheKeys();
		assertEquals("Wrong number of keys", 0, keys.size());
	}

	public void testTouch()
	{
		long wait = 50;
		CachePointer cp = new CachePointer();
		long middle = System.currentTimeMillis() + wait;
		sleep(wait);
		cp.touch();
		long last = cp.getLastAccessTime();
		long end = System.currentTimeMillis();

		int minute = TimeList.shrinkTime(last);
		assertTrue("Time 1 is too early: (" + last + " < " + middle + ")", (last >= middle));
		assertTrue("Time 1 is too late: (" + last + " > " + end + ")", (last <= end));
		assertEquals("Wrong minute 1 ", minute, cp.fetchLastAccessMinute());
		assertEquals("Wrong untouched 1", false, cp.fetchUntouched());

		MockCacheableObject mco = new MockCacheableObject();
		TimeList tl = new TimeList();
		cp = new CachePointer(mco, tl);

		middle = System.currentTimeMillis() + wait;
		sleep(wait);
		cp.touch();
		end = System.currentTimeMillis();
		last = cp.getLastAccessTime();

		assertTrue("Time 2 is too early: (" + last + " < " + middle + ")", (last >= middle));
		assertTrue("Time 2 is too late: (" + last + " > " + end + ")", (last <= end));
		minute = TimeList.shrinkTime(last);
		assertEquals("Wrong minute 2", minute, cp.fetchLastAccessMinute());
		assertEquals("Wrong untouched 2", false, cp.fetchUntouched());
	}

	public void testGetObject()
	{
		long wait = 50;
		CachePointer cp = new CachePointer();
		long middle = System.currentTimeMillis() + wait;
		sleep(wait);
		Object object = cp.getObject();
		assertNull("Object should be null", object);
		long last = cp.getLastAccessTime();
		long end = System.currentTimeMillis();

		int minute = TimeList.shrinkTime(last);
		assertTrue("Time 1 is too early: (" + last + " < " + middle + ")", (last >= middle));
		assertTrue("Time 1 is too late: (" + last + " > " + end + ")", (last <= end));
		assertEquals("Wrong minute 1", minute, cp.fetchLastAccessMinute());
		assertEquals("Wrong untouched 1", false, cp.fetchUntouched());

		MockCacheableObject mco = new MockCacheableObject();
		TimeList tl = new TimeList();
		cp = new CachePointer(mco, tl);

		middle = System.currentTimeMillis() + wait;
		sleep(wait);
		object = cp.getObject();
		assertSame("Wrong object returned", mco, object);
		end = System.currentTimeMillis();
		last = cp.getLastAccessTime();

		assertTrue("Time 2 is too early: (" + last + " < " + middle + ")", (last >= middle));
		assertTrue("Time 2 is too late: (" + last + " > " + end + ")", (last <= end));
		minute = TimeList.shrinkTime(last);
		assertEquals("Wrong minute 2", minute, cp.fetchLastAccessMinute());
		assertEquals("Wrong untouched 2", false, cp.fetchUntouched());
	}

	public void testExpireEmpty()
	{
		CachePointer cp = new CachePointer();
		cp.expire();
		Set keys = cp.getCacheKeys();
		assertEquals("Wrong number of keys", 0, keys.size());
		assertNull("object should be null", cp.fetchObject());
		cp.expire();
		keys = cp.getCacheKeys();
		assertEquals("Wrong number of keys", 0, keys.size());
		assertNull("object should be null", cp.fetchObject());
	}

	public void testAddKey()
	{
		MockCacheableObject mco = new MockCacheableObject();
		TimeList tl = new TimeList();
		CachePointer cp = new CachePointer(mco, tl);

		Set keys = cp.getCacheKeys();
		assertEquals("Wrong number of keys (A)", 0, keys.size());

		CacheKey key = mco.getPrimaryKey();
		cp.addKey(key);
		assertSame("Wrong key-set", keys, cp.getCacheKeys());
		assertEquals("Wrong number of keys (B)", 1, keys.size());

		key = mco.getPrimaryKey();
		cp.addKey(key);
		assertSame("Wrong key-set", keys, cp.getCacheKeys());
		assertEquals("Wrong number of keys (C)", 1, keys.size());

		//    key = new CacheKey(mco);
		//    cp.addKey(key);
		//    assertSame("Wrong key-set", keys, cp.getCacheKeys());
		//    assertEquals("Wrong number of keys (D)", 1, keys.size());

		key = new CacheKey();
		cp.addKey(key);
		assertSame("Wrong key-set", keys, cp.getCacheKeys());
		assertEquals("Wrong number of keys (E)", 2, keys.size());

		key = new CacheKey();
		cp.addKey(key);
		assertSame("Wrong key-set", keys, cp.getCacheKeys());
		assertEquals("Wrong number of keys (F)", 3, keys.size());

	}

	public void testExpireNormal()
	{
		MockCacheableObject mco = new MockCacheableObject();
		TimeList tl = new TimeList();
		CachePointer cp = new CachePointer(mco, tl);
		cp.addKey(mco.getPrimaryKey());
		Set keys = cp.getCacheKeys();
		assertEquals("Wrong number of keys", 1, keys.size());
		assertNotNull("object should not be null", cp.fetchObject());

		cp.expire();
		keys = cp.getCacheKeys();
		assertEquals("Wrong number of keys", 0, keys.size());
		assertNull("object should be null", cp.fetchObject());
	}

	protected void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException ie)
		{
		}
	}

	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTestSuite(CachePointerTest.class);
		return suite;
	}

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(suite());
	}
}
