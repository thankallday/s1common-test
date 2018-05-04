package com.scholarone.cache;

import junit.framework.TestCase;

public class CacheExpirationTimerTaskTest extends TestCase
{

	public void testConstructor()
	{
		CacheExpirationTimerTask task = new CacheExpirationTimerTask();
		assertNull("Timer should be null", task.timer);
	}

	public void testSetTimeManager()
	{
		CacheExpirationTimerTask task = new CacheExpirationTimerTask();
		MockTimeManager mock = new MockTimeManager();
		task.setTimeManager(mock);

		assertNotNull("Null manager", task.getTimeManager());
		assertSame("Wrong manager", mock, task.getTimeManager());
	}

	public void testStart()
	{
		CacheExpirationTimerTask task = new CacheExpirationTimerTask();
		MockTimeManager mock = new MockTimeManager();
		task.setTimeManager(mock);

		task.start(100);
		assertNotNull("Timer should not be null", task.timer);
		try
		{
			Thread.sleep(370);
		}
		catch (InterruptedException e)
		{
		}

		assertEquals("Wrong list-process count (could be random, non-error)", 3, mock.count);

		task.cancel();
		try
		{
			Thread.sleep(150);
		}
		catch (InterruptedException e)
		{
		}

		assertEquals("Wrong list-process count after cancel (this should not happen)", 3, mock.count);
	}

	public class MockTimeManager extends CacheTimeManager
	{
		public int	count	= 0;

		public void checkLists(long time)
		{
			++count;
			assertTrue("Should be in a Daemon", Thread.currentThread().isDaemon());
		}
	}
}
