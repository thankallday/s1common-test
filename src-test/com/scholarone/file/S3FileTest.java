package com.scholarone.file;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

public class S3FileTest
{

  @Test
  public void testConstructor1()
  {
    S3File testFile = new S3File("test");
    assertNotNull(testFile);
  }

  @Test
  public void testConstructor2()
  {
    S3File testFile = new S3File("parent", "child");
    assertNotNull(testFile);
  }

  @Test
  public void testConstructor3()
  {
    S3File testFile = new S3File(new S3File("parent"), "child");
    assertNotNull(testFile);
  }

  @Test
  public void testConstructor4()
  {
    URI uri;
    try
    {
      S3FileTest test = new S3FileTest();
      Package package1 = test.getClass().getPackage();
      String string = "/" + package1.getName().replace('.', '/');
      URL url = test.getClass().getResource(string);
      S3File testFile = new S3File(url.toURI());

      assertNotNull(testFile);
    }
    catch (URISyntaxException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testCreateTempFile1()
  {
    try
    {
      S3File testFile = S3File.createTempFile("pre", "post");
      assertNotNull(testFile);
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testCreateTempFile2()
  {
    try
    {
      S3File testFile = S3File.createTempFile("pre", "post", new S3File("."));
      assertNotNull(testFile);
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void testGetAbsoluteFile()
  {
    S3File testFile = new S3File("test");
    S3File absoluteFile = testFile.getAbsoluteFile();
    assertNotNull(absoluteFile);
  }

  @Test
  public void testGetParentFile()
  {
    S3File testFile = new S3File(new S3File("parent"), "child");
    S3File parentFile = testFile.getParentFile();
    assertNotNull(parentFile);
  }

  @Test
  public void testList1()
  {
    S3File testFile = new S3File(".");
    String[] files = (String[]) testFile.list();
    assertTrue(files.length > 0);
  }

  @Test
  public void testList2()
  {
    S3File testFile = new S3File(".");
    String[] files = (String[]) testFile.list(new S3FilenameFilter()
    {
      public boolean accept(S3File dir, String filename)
      {
        return (true);
      }
    });
    assertTrue(files.length > 0);
  }

  @Test
  public void testListFiles1()
  {
    S3File testFile = new S3File(".");
    S3File[] files = (S3File[]) testFile.listFiles();
    assertTrue(files.length > 0);
  }

  @Test
  public void testListFiles2()
  {
    S3File testFile = new S3File(".");
    S3File[] files = (S3File[]) testFile.listFiles(new S3FilenameFilter()
    {
      public boolean accept(S3File dir, String filename)
      {
        return (true);
      }
    });
    assertTrue(files.length > 0);
  }

  @Test
  public void testListFiles3()
  {
    S3File testFile = new S3File(".");
    S3File[] files = (S3File[]) testFile.listFiles(new S3FilenameFilter()
    {
      public boolean accept(S3File dir, String filename)
      {
        return (true);
      }
    });
    assertTrue(files.length > 0);
  }
  
  @Test
  public void testIsFile()
  {
    S3File test1 = new S3File("src");
    S3File test2 = new S3File("pom.xml");
    
    assertTrue(test2.isFile());
    assertFalse(test1.isFile());
  }
  
  @Test
  public void testIsDirectory()
  {
    S3File test1 = new S3File("src");
    S3File test2 = new S3File("pom.xml");
    
    assertTrue(test1.isDirectory());
    assertFalse(test2.isDirectory());
  }
}