package com.scholarone.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PackageTest extends TestCase
{

	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTestSuite(CacheTest.class);
		suite.addTestSuite(CachePointerTest.class);
		//suite.addTestSuite(CacheKeyTest.class);
		//suite.addTestSuite(CacheListTest.class);
		suite.addTestSuite(CacheExpirationTimerTaskTest.class);
		suite.addTestSuite(CacheCriteriaTest.class);
		suite.addTestSuite(CacheableObjectHolderTest.class);
		return suite;
	}

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(suite());
	}
}
