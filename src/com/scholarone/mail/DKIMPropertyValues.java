package com.scholarone.mail;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import com.scholarone.file.S3File;
import com.scholarone.file.S3FileInputStream;

public class DKIMPropertyValues
{
  private static final int EXPIRATION_TIME = 1; // minutes

  private static Properties prop = new Properties();

  private static Date loadTime = null;

  private static String propFileName = "dkim.properties";
    
  public final static String PRIVATE_KEY = "private.key";
  
  public final static String SELECTOR = "selector";

  public final static String DOMAIN = "domain";
  
  public final static String DNS_SERVER = "dns.server";
  
  private static boolean isPropertiesExpired()
  {
    boolean expired = true;

    if (loadTime != null)
    {
      Date currentTime = new Date(System.currentTimeMillis());

      long diff = currentTime.getTime() - loadTime.getTime();
      long diffMinutes = diff / (60 * 1000) % 60;
      if (diffMinutes > EXPIRATION_TIME)
      {
        expired = true;
      }
      else
      {
        expired = false;
      }
    }

    return expired;
  }

  private static Properties getPropValues(String propertiesFileName) throws IOException
  {
    if (isPropertiesExpired())
    {
      S3File f = new S3File(propFileName);
      if ( f.exists() )
      {
        prop.load(new S3FileInputStream(propFileName));
      }
      else
      {
        prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propFileName));
      }
      
      loadTime = new Date(System.currentTimeMillis());
    }

    return prop;
  }

  public static void setPropertiesFileName(String fileName)
  {
    propFileName = fileName;
  }
  
  public static String getProperty(String name) throws IOException
  {
    return getPropValues(null).getProperty(name);
  }
}