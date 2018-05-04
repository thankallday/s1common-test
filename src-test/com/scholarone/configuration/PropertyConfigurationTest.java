package com.scholarone.configuration;

import java.io.BufferedWriter;
import junit.framework.TestCase;

import com.scholarone.file.S3File;
import com.scholarone.file.S3FileWriter;

/**
 * Reads in configuration options from a given properties file
 */
public class PropertyConfigurationTest extends TestCase
{

	S3File	testFile;

	public PropertyConfigurationTest()
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
			bf.write("test.item=Hello");
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
	public void testGetProperty()
	{
		try
		{
			Configuration conf = new PropertyConfiguration();
			String str = conf.getProperty("test.item");
			assertEquals(str, "Hello");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * test the factory gives us a PropertyConfiguration object back
	 */
	public void testCFactoryInstance()
	{
		Configuration conf = CFactory.instance();
		assertTrue(conf instanceof PropertyConfiguration);
	}
}