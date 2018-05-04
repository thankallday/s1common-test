package com.scholarone.cache;

import junit.framework.TestCase;

public class TimeListTest extends TestCase
{

	public static void main(String[] args)
	{
		TimeListTest t = new TimeListTest();
		try
		{
			t.setUp();
			t.testTimeList();
			t.tearDown();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void testTimeList()
	{
		MockCacheableObject obj1 = new MockCacheableObject();
		MockCacheableObject obj2 = new MockCacheableObject();

		TimeList list = new TimeList();
		CachePointer p1 = new CachePointer(obj1, list);
		CachePointer p2 = new CachePointer(obj2, list);

		try
		{
			Thread.sleep(61 * 1000);
		}
		catch (InterruptedException e)
		{
		}

		try
		{
			list.clearBefore(System.currentTimeMillis());
		}
		catch (java.util.NoSuchElementException nsee)
		{
			fail("TimeList.clearBefore threw NoSuchElementException");
		}

	}
}
