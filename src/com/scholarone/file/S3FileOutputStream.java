package com.scholarone.file;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class S3FileOutputStream extends FileOutputStream
{

  public S3FileOutputStream(String name) throws FileNotFoundException
  {
    super(name);
  }

  public S3FileOutputStream(S3File file) throws FileNotFoundException
  {
    super(file);
  }

  public S3FileOutputStream(FileDescriptor fdObj)
  {
    super(fdObj);
  }

  public S3FileOutputStream(String name, boolean append) throws FileNotFoundException
  {
    super(name, append);
  }

  public S3FileOutputStream(S3File file, boolean append) throws FileNotFoundException
  {
    super(file, append);
  }

  public void close() throws IOException
  {
    // put file if necessary
    
    super.close();
  }
}
