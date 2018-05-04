package com.scholarone.cache;

import java.io.*;
import java.util.ArrayList;

import junit.framework.*;

public class CacheListTest extends TestCase
{
	public void setUp()
	{
		CacheList.listCount = 0;
		CacheManager.getInstance().reset();
	}

	public void testConstructor1()
	{
		long start = System.currentTimeMillis();
		CacheList list = new CacheList();
		assertNotNull("Null object-array", list.objects);
		assertEquals("Wrong array length", 16, list.objects.length);
		assertEquals("Wrong size", 0, list.size());

		assertEquals("Wrong list count", 1, CacheList.listCount);
		assertEquals("Wrong list id", 1, list.whichList);
		for (int i = 2; i < 10; ++i)
		{
			list = new CacheList();
			long now = System.currentTimeMillis();
			assertEquals("Wrong list id", i, list.whichList);
			long time = list.creationTime;
			assertTrue("Wrong time, should have (" + start + " <= " + time + "<= " + now + ")",
					((start <= time) && (time <= now)));
		}
	}

	public void testConstructor2()
	{
		CacheList list = null;

		for (int i = -3; i <= 32; ++i)
		{
			list = new CacheList(i);
			int size = (i < 17) ? (16) : (32);
			assertEquals("Wrong size (" + i + ")", size, list.objects.length);
		}
	}

	public void testConstructor3()
	{
		CacheList list = new CacheList(12, 7, 12);
		assertEquals("Wrong id 1", 7, list.getId().intValue());
		assertEquals("Wrong time 1", 12, list.creationTime);

		list = new CacheList(12);
		assertEquals("Wrong id 2", 1, list.getId().intValue());

		list = new CacheList(12, 2, 47);
		assertEquals("Wrong id 3", 2, list.getId().intValue());
		assertEquals("Wrong time 3", 47, list.creationTime);

		list = new CacheList(12);
		assertEquals("Wrong id 4", 2, list.getId().intValue());

		list = new CacheList(12, 5, 99);
		CacheList list2 = new CacheList(12, 5, 206);
		CacheKey key1 = list.getPrimaryKey();
		CacheKey key2 = list2.getPrimaryKey();
		assertFalse("Keys should not match", key1.equals(key2));
	}

	public void testConstructor4()
	{
		ArrayList arr = new ArrayList();
		for (int i = 0; i < 35; ++i)
		{
			arr.add(Integer.valueOf(i + 12));
		}

		CacheList list = new CacheList(arr);
		assertEquals("Wrong size", 35, list.size());
		assertEquals("Wrong capacity", 48, list.objects.length);
		assertEquals("Lists should be equal", arr, list);
	}

	public void testEnsureCapacity()
	{
		CacheList list = new CacheList();
		CacheableObjectHolder[] temp = new CacheableObjectHolder[12];

		list.objects = temp;
		list.ensureCapacity(12);
		assertSame("Wrong array 0a", temp, list.objects);
		assertEquals("Wrong size 0b", 12, list.objects.length);

		list.ensureCapacity(13);
		assertNotSame("Wrong array 0c", temp, list.objects);
		assertEquals("Wrong size 0d", 16, list.objects.length);

		list.objects = new CacheableObjectHolder[0];
		list.ensureCapacity(1);
		assertEquals("Wrong size 2", 16, list.objects.length);

		list.objects = null;
		list.ensureCapacity(1);
		assertEquals("Wrong size 3", 16, list.objects.length);

		temp = new CacheableObjectHolder[16];
		list.objects = temp;
		list.ensureCapacity(1);
		assertSame("Wrong array 4", temp, list.objects);

		list.ensureCapacity(15);
		assertSame("Wrong array 5", temp, list.objects);

		list.ensureCapacity(16);
		assertSame("Wrong array 6", temp, list.objects);

		list.ensureCapacity(17);
		assertNotSame("Wrong array 7", temp, list.objects);
		assertEquals("Wrong size 7", 32, list.objects.length);
	}

	public void testGet()
	{
		CacheList list = new CacheList();
		MockCacheableObject mco = new MockCacheableObject(false);

		Object got = null;
		try
		{
			got = list.get(0);
			fail("Should have thrown exception");
		}
		catch (IndexOutOfBoundsException expected)
		{
		}

		list.add(mco);
		list.add(mco);

		got = list.get(0);
		assertNotNull("Null got 0", got);
		assertSame("Wrong got 0", mco, got);
		got = list.get(1);
		assertNotNull("Null got 1", got);
		assertSame("Wrong got 1", mco, got);
		try
		{
			got = list.get(2);
			fail("Should have thrown exception 2");
		}
		catch (IndexOutOfBoundsException expected)
		{
		}
	}

	public void testSet()
	{
		CacheList list = new CacheList();
		MockCacheableObject mco = new MockCacheableObject(false);

		Object got = null;
		try
		{
			got = list.set(0, this);
			fail("Should have thrown exception");
		}
		catch (IndexOutOfBoundsException expected)
		{
		}

		list.add(mco);
		list.add(mco);

		got = list.set(0, this);
		assertSame("Wrong got 0", mco, got);
		got = list.set(0, null);
		assertSame("Wrong got 0a", this, got);
		got = list.set(0, mco);
		assertNull("Wrong got 0b", got);

		got = list.set(1, mco);
		assertNotNull("Null got 1", got);
		assertSame("Wrong got 1", mco, got);
		try
		{
			got = list.set(2, mco);
			fail("Should have thrown exception 2");
		}
		catch (IndexOutOfBoundsException expected)
		{
		}
	}

	public void testAdd()
	{
		CacheManager cm = CacheManager.getInstance();

		CacheList list = new CacheList();
		MockCacheableObject mco1 = new MockCacheableObject(false);
		MockCacheableObject mco2 = new MockCacheableObject(false);
		MockCacheableObject mco3 = new MockCacheableObject(false);

		try
		{
			list.add(-1, mco1);
			fail("Should have thrown exception");
		}
		catch (IndexOutOfBoundsException expected)
		{
		}

		list.add(0, mco1);
		list.add(1, mco2);
		list.add(2, mco3);
		assertEquals("Wrong size 1", 0, cm.size());

		ArrayList temp = new ArrayList();
		temp.add(mco1);
		temp.add(mco2);
		temp.add(mco3);

		assertEquals("List mismatch", temp, list);
		try
		{
			list.add(4, mco1);
			fail("Should have thrown exception.");
		}
		catch (IndexOutOfBoundsException expected)
		{
		}

		list.clear();
		list.add(this);
		mco1.isCacheable = true;
		mco2.isCacheable = true;
		mco3.isCacheable = true;
		list.add(mco2);
		list.add(mco3);
		list.add(mco1);

		temp.clear();
		temp.add(this);
		temp.add(mco2);
		temp.add(mco3);
		temp.add(mco1);

		assertEquals("Wrong size 2", 3, cm.size());
		assertEquals("List mismatch (2)", temp, list);
	}

	public void testAdd2()
	{
		MockCacheableObject.lastId = 0;
		CacheList list = new CacheList();

		for (int i = 0; i < 100; ++i)
		{
			try
			{
				MockCacheableObject mco = new MockCacheableObject();
				list.add(0, mco);
			}
			catch (IndexOutOfBoundsException ioobe)
			{
				fail(ioobe.toString() + ", at index (" + i + ")");
			}
		}
		assertEquals("Wrong size", 100, list.size());
	}

	public void testRemove()
	{
		CacheList list = new CacheList();
		MockCacheableObject mco1 = new MockCacheableObject(false);
		MockCacheableObject mco2 = new MockCacheableObject(false);
		MockCacheableObject mco3 = new MockCacheableObject(false);
		MockCacheableObject mco4 = new MockCacheableObject(true);

		Object removed = null;
		try
		{
			removed = list.remove(0);
			fail("Should have thrown exception");
		}
		catch (IndexOutOfBoundsException expected)
		{
		}

		list.add(mco1);
		list.add(mco2);
		list.add(mco3);
		list.add(mco4);
		list.add(this);

		removed = list.remove(1);
		assertSame("Wrong removed", mco2, removed);
		assertEquals("Wrong new size", 4, list.size());
		assertSame("Wrong item 0", mco1, list.get(0));
		assertSame("Wrong item 1", mco3, list.get(1));
		assertSame("Wrong item 2", mco4, list.get(2));
		assertSame("Wrong item 3", this, list.get(3));
		assertNull("Last item should be null", list.objects[4]);
	}

	public void testRemoveRange()
	{
		CacheList list = new CacheList();
		MockCacheableObject.lastId = 0;
		for (int i = 0; i < 20; ++i)
		{
			list.add(new MockCacheableObject(false));
		}

		assertEquals("Wrong size 0", 20, list.size());
		assertEquals("Wrong capacity 0", 32, list.objects.length);

		list.removeRange(4, 7);
		assertEquals("Wrong size 1", 17, list.size());

		for (int i = 0; i < 17; ++i)
		{
			MockCacheableObject co = (MockCacheableObject) list.get(i);
			assertNotNull("Null object at index (" + i + ")", co);
			assertNotNull("Null id at index (" + i + ")", co.getId());
			int id = (i < 4) ? (i + 1) : (i + 4);
			assertEquals("Wrong id A (" + i + ")", id, co.getId().intValue());
		}
		for (int i = 17; i < 20; ++i)
		{
			assertNull("Should be null at index (" + i + ") (A)", list.objects[i]);
		}

		list.removeRange(5, 5);
		for (int i = 0; i < 17; ++i)
		{
			MockCacheableObject co = (MockCacheableObject) list.get(i);
			assertNotNull("Null object at index (" + i + ")", co);
			assertNotNull("Null id at index (" + i + ")", co.getId());
			int id = (i < 4) ? (i + 1) : (i + 4);
			assertEquals("Wrong id B (" + i + ")", id, co.getId().intValue());
		}
		for (int i = 17; i < 20; ++i)
		{
			assertNull("Should be null at index (" + i + ") (B)", list.objects[i]);
		}

		list.removeRange(7, 3);
		for (int i = 0; i < 17; ++i)
		{
			MockCacheableObject co = (MockCacheableObject) list.get(i);
			assertNotNull("Null object at index (" + i + ")", co);
			assertNotNull("Null id at index (" + i + ")", co.getId());
			int id = (i < 4) ? (i + 1) : (i + 4);
			assertEquals("Wrong id C (" + i + ")", id, co.getId().intValue());
		}
		for (int i = 17; i < 20; ++i)
		{
			assertNull("Should be null at index (" + i + ") (C)", list.objects[i]);
		}

		list.removeRange(14, 97);
		for (int i = 0; i < 14; ++i)
		{
			MockCacheableObject co = (MockCacheableObject) list.get(i);
			assertNotNull("Null object at index (" + i + ")", co);
			assertNotNull("Null id at index (" + i + ")", co.getId());
			int id = (i < 4) ? (i + 1) : (i + 4);
			assertEquals("Wrong id D (" + i + ")", id, co.getId().intValue());
		}
		for (int i = 17; i < 20; ++i)
		{
			assertNull("Should be null at index (" + i + ") (D)", list.objects[i]);
		}

		list.removeRange(0, 2);
		for (int i = 0; i < 12; ++i)
		{
			MockCacheableObject co = (MockCacheableObject) list.get(i);
			assertNotNull("Null object at index (" + i + ")", co);
			assertNotNull("Null id at index (" + i + ")", co.getId());
			int id = (i < 2) ? (i + 3) : (i + 6);
			assertEquals("Wrong id E (" + i + ")", id, co.getId().intValue());
		}
		for (int i = 17; i < 20; ++i)
		{
			assertNull("Should be null at index (" + i + ") (E)", list.objects[i]);
		}
	}

	public void testNonCacheable()
	{
		CacheList cl = new CacheList();
		MockCacheableObject mco1 = new MockCacheableObject(false);
		MockCacheableObject mco2 = new MockCacheableObject(true);
		MockCacheableObject mco3 = new MockCacheableObject(false);
		MockCacheableObject mco4 = new MockCacheableObject(true);

		cl.add(mco1);
		cl.add(mco2);
		cl.add(mco3);
		cl.add(mco4);

		assertEquals("Wrong item 1", mco1, cl.get(0));
		assertEquals("Wrong item 2", mco2, cl.get(1));
		assertEquals("Wrong item 3", mco3, cl.get(2));
		assertEquals("Wrong item 4", mco4, cl.get(3));
	}

	public void testSerialization()
			throws Exception
	{
		CacheList cl1 = new CacheList();
		CacheList result = (CacheList) pipeObject(cl1);
		assertEquals("Ids should be same (1)", cl1.getId(), result.getId());
		assertEquals("Empty list should be same", cl1, result);

		cl1 = new CacheList();
		for (int i = 0; i < 24; ++i)
		{
			cl1.add(String.valueOf(i));
		}
		result = (CacheList) pipeObject(cl1);
		assertEquals("Ids should be same (2)", cl1.getId(), result.getId());
		assertEquals("Wrong capacity", 32, result.objects.length);
		assertEquals("Should match list 2", cl1, result);

		MockCacheableObject mco1 = new MockCacheableObject();
		MockCacheableObject mco2 = new MockCacheableObject();
		MockCacheableObject mco3 = new MockCacheableObject();

		cl1 = new CacheList();
		cl1.add(mco1);
		cl1.add(mco2);
		cl1.add(mco3);

		result = (CacheList) pipeObject(cl1);
		assertEquals("Ids should be same (3)", cl1.getId(), result.getId());
		assertEquals("Should match list 3", cl1, result);

		mco1.isCacheable = false;
		mco2.isCacheable = false;
		mco3.isCacheable = false;

		cl1 = new CacheList();
		cl1.add(mco1);
		cl1.add(mco2);
		cl1.add(mco3);

		result = (CacheList) pipeObject(cl1);
		assertEquals("Ids should be same (4)", cl1.getId(), result.getId());
		assertEquals("Should match list 4", cl1, result);
	}

	protected Object pipeObject(final Object in)
			throws IOException, ClassNotFoundException
	{
		final PipedInputStream pi = new PipedInputStream();
		final PipedOutputStream po = new PipedOutputStream(pi);
		final ObjectOutputStream oo = new ObjectOutputStream(po);
		final ObjectInputStream oi = new ObjectInputStream(pi);
		Thread t = new Thread()
		{
			public void run()
			{
				try
				{
					oo.writeObject(in);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		};
		t.start();

		return oi.readObject();
	}
}
