package com.scholarone.cache;

import junit.framework.TestCase;

public class CacheCriteriaTest extends TestCase
{
	public void testConstructor1()
	{
		CacheCriteria cc = new CacheCriteria();
		Object[] c = cc.getCriteria();

		assertNull("Should have null criteria", c);
	}

	public void testConstructor2()
	{
		Object it = null;
		CacheCriteria cc = new CacheCriteria(it);
		Object[] c = cc.getCriteria();
		assertNull("Should have null criteria", c);

		it = new Object[] { Integer.valueOf(43) };
		cc = new CacheCriteria(it);
		c = cc.getCriteria();
		assertNotNull("Null criteria 1", c);
		assertEquals("Wrong length 1", 1, c.length);
		assertEquals("Wrong value 1", Integer.valueOf(43), c[0]);

		it = Integer.valueOf(12);
		cc = new CacheCriteria(it);
		c = cc.getCriteria();
		assertNotNull("Null criteria 2", c);
		assertEquals("Wrong length 2", 1, c.length);
		assertEquals("Wrong value 2", it, c[0]);
	}

	public void testConstructor3()
	{
		Object[] it = null;
		CacheCriteria cc = new CacheCriteria(it);
		Object[] c = cc.getCriteria();
		assertNull("Should have null criteria", c);

		it = new Object[] { Integer.valueOf(43) };
		cc = new CacheCriteria(it);
		c = cc.getCriteria();
		assertNotNull("Null criteria 1", c);
		assertEquals("Wrong length 1", 1, c.length);
		assertEquals("Wrong value 1", Integer.valueOf(43), c[0]);
	}

	public void testHashCode()
	{
		Object it = null;
		CacheCriteria cc = new CacheCriteria();

		assertEquals("Wrong hashcode 1", System.identityHashCode(cc), cc.hashCode());

		it = Integer.valueOf(12);
		cc = new CacheCriteria(it);
		assertEquals("Wrong hashcode 2", 12, cc.hashCode());

		it = new Object[] { Integer.valueOf(12), Integer.valueOf(43) };
		cc = new CacheCriteria(it);
		int value = 12 * 31 + 43;
		assertEquals("Wrong hashcode 3", value, cc.hashCode());
	}

	public void testEquals()
	{
		Object it = null;
		CacheCriteria cc1 = new CacheCriteria();
		CacheCriteria cc2 = new CacheCriteria();

		assertFalse("Objects should be not equal", cc1.equals(cc2));
		assertTrue("Object should equal itself", cc1.equals(cc1));

		it = Integer.valueOf(12);
		cc1 = new CacheCriteria(it);
		it = Integer.valueOf(12);
		cc2 = new CacheCriteria(it);
		assertEquals("Should be equal 1", cc1, cc2);

		it = new Object[] { Integer.valueOf(12), Integer.valueOf(43), Integer.valueOf(97) };
		cc1 = new CacheCriteria(it);
		it = new Integer[] { Integer.valueOf(12), Integer.valueOf(43), Integer.valueOf(97) };
		cc2 = new CacheCriteria(it);
		assertEquals("Should be equal 2", cc1, cc2);

		it = new Object[] { "A", "B", "c" };
		cc1 = new CacheCriteria(it);
		it = new Object[] { Integer.valueOf("A".hashCode()), Integer.valueOf("B".hashCode()), Integer.valueOf("c".hashCode()) };
		cc2 = new CacheCriteria(it);
		assertEquals("HashCodes should be equal 4", cc1.hashCode(), cc2.hashCode());
		assertFalse("Objects should not be equal 4", cc1.equals(cc2));

		cc1 = new CacheCriteria(new Integer[0]);
		cc2 = new CacheCriteria(new String[0]);
		assertEquals("Should be equal 5", cc1, cc2);
	}
}
