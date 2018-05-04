package com.scholarone.cache;

import junit.framework.TestCase;

public class CacheKeyTest extends TestCase
{

	public void testConstructor1()
	{
		CacheKey key = new CacheKey();

		assertNull("Class should be null", key.getObjectClass());
		assertNull("ID should be null", key.getCriteria());
		assertNull("Generator should be null", key.getGenerator());
	}

	public void XtestConstructor2()
	{
		CacheCriteria c = new CacheCriteria(Integer.valueOf(3));
		CacheKey key = new CacheKey(MockCacheableObject.class, c);
		assertSame("Wrong class", MockCacheableObject.class, key.getObjectClass());
		assertSame("Wrong criteria", c, key.getCriteria());
		assertNull("Generator should be null", key.getGenerator());
	}

	public void testConstructor3()
	{
		MockCacheableObject mco = new MockCacheableObject();
		CacheCriteria c = new CacheCriteria(mco.getId());
		CacheableObject.ObjectGenerator gen = new MockCacheableObject.Generator();

		CacheKey key = new CacheKey(mco.getClass(), c, gen);
		assertSame("Wrong class", MockCacheableObject.class, key.getObjectClass());
		assertSame("Wrong critiera", c, key.getCriteria());
		assertSame("Wrong generator", gen, key.getGenerator());
	}

	public void testConstructor4()
	{
		MockCacheableObject mco = new MockCacheableObject();
		CacheCriteria c = new CacheCriteria(mco.getId());
		CacheKey key = new CacheKey(mco.getClass(), mco.getId());

		assertSame("Wrong class", MockCacheableObject.class, key.getObjectClass());
		assertEquals("Wrong critiera", c, key.getCriteria());
		assertNull("Generator should be null", key.getGenerator());
	}

	public void testEquals()
	{
		MockCacheableObject mco = new MockCacheableObject(true);
		CacheKey one = new CacheKey(MockCacheableObject.class, new CacheCriteria(mco.getId()));
		CacheKey two = new CacheKey(MockCacheableObject.class, new CacheCriteria(mco.getId()));

		assertEquals("Keys should match", one, two);

		CacheKey three = new CacheKey(CacheKey.class, mco.getId());
		assertFalse("Different classes should not be equal", one.equals(three));

		MockCacheableObject mco2 = new MockCacheableObject(true);
		CacheKey four = new CacheKey(mco2.getClass(), mco2.getId());
		assertFalse("Different objects should not have same key", one.equals(four));
	}
}
