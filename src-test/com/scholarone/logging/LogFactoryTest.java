package com.scholarone.logging;


import java.io.BufferedWriter;

import junit.framework.TestCase;

import com.scholarone.file.S3File;
import com.scholarone.file.S3FileWriter;

/**
 * Reads in configuration options from a given properties file
 */
public class LogFactoryTest extends TestCase
{

	S3File	testFile;

	public LogFactoryTest()
	{
	}

	/**
	 * create a dummy properties file that the configuration object will read
	 */
	protected void setUp()
	{
		try
		{
			testFile = new S3File("classes\\scholarone.properties");
			BufferedWriter bf = new BufferedWriter(new S3FileWriter(testFile));
			bf.write("logger.knownpackages = com.scholarone.tags, com.scholarone.utility, \\ ");
			bf.newLine();
			bf
					.write("  com.scholarone.persistence, com.scholarone.common.BaseServlet, com.scholarone.common.BaseController, \\ ");
			bf.newLine();
			bf.write("  com.scholarone.common.BaseData, com.scholarone.common.BaseHelper");
			bf.newLine();
			bf.write("persistence.datasource.name = ac_pooled_ds");
			bf.newLine();
			bf.close();
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * removes the temporary file
	 */
	protected void tearDown()
	{
		testFile.delete();
	}

	/**
	 * test we read in the value of the property as expected, don't throw exception
	 */
	public void testGetInstance()
	{
		LogFactory lf = LogFactory.getInstance();

		assertTrue(lf instanceof LogFactory);
	}

	public void testGetDBAppender()
	{
		Log4JDBAppender dba = new Log4JDBAppender();

		assertTrue(dba instanceof Log4JDBAppender);
	}

	public static void main(String[] args)
	{
		LogFactoryTest t = new LogFactoryTest();
		t.setUp();
		t.testGetInstance();
		t.testGetDBAppender();
		t.tearDown();
	}

}