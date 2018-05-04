package com.scholarone.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class S3File extends File
{
  public S3File(String pathname)
  {
    super(pathname);
  }

  public S3File(String parent, String child)
  {
    super(parent, child);
  }

  public S3File(S3File parent, String child)
  {
    super(parent, child);
  }

  public S3File(URI uri)
  {
    super(uri);
  }

  public S3File(File file)
  {
    super(file.getPath());
  }

  public static S3File createTempFile(String prefix, String suffix) throws IOException
  {
    File f = File.createTempFile(prefix, suffix);

    return new S3File(f);
  }

  public static S3File createTempFile(String prefix, String suffix, S3File directory) throws IOException
  {
    File f = File.createTempFile(prefix, suffix, directory);

    return new S3File(f);
  }

  public S3File getAbsoluteFile()
  {
    File f = super.getAbsoluteFile();
    return new S3File(f);
  }

  public S3File getParentFile()
  {
    File f = super.getParentFile();
    return new S3File(f);
  }

  public boolean isFile()
  {
    return super.isFile();
  }
  
  public boolean isDirectory()
  {
    return super.isDirectory();
  }
  
  public String[] list()
  {
    return super.list();
  }

  public String[] list(S3FilenameFilter filter)
  {
    String names[] = list();
    if ((names == null) || (filter == null))
    {
      return names;
    }
    ArrayList<String> v = new ArrayList<String>();
    for (int i = 0; i < names.length; i++)
    {
      if (filter.accept(this, names[i]))
      {
        v.add(names[i]);
      }
    }
    return (String[]) (v.toArray(new String[v.size()]));
  }
  
  public S3File[] listFiles()
  {
    String[] ss = list();
    if (ss == null) return null;

    int n = ss.length;
    S3File[] fs = new S3File[n];
    for (int i = 0; i < n; i++)
    {
      fs[i] = new S3File(this, ss[i]);
    }
    return fs;
  }

  public S3File[] listFiles(S3FileFilter filter)
  {
    String ss[] = list();
    if (ss == null) return null;
    ArrayList<S3File> v = new ArrayList<S3File>();
    for (int i = 0; i < ss.length; i++)
    {
      S3File f = new S3File(this, ss[i]);
      if ((filter == null) || filter.accept(f))
      {
        v.add(f);
      }
    }
    return (S3File[]) (v.toArray(new S3File[v.size()]));
  }

  public S3File[] listFiles(S3FilenameFilter filter)
  {
    String ss[] = list();
    if (ss == null) return null;
    ArrayList<S3File> v = new ArrayList<S3File>();
    for (int i = 0; i < ss.length; i++)
    {
      if ((filter == null) || filter.accept(this, ss[i]))
      {
        v.add(new S3File(this, ss[i]));
      }
    }
    return (S3File[]) (v.toArray(new S3File[v.size()]));
  }
}
