package com.scholarone.file;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class S3FileInputStream extends FileInputStream
{
  public S3FileInputStream(String name) throws FileNotFoundException
  {
    // go get file
    super(name);
  }

  public S3FileInputStream(FileDescriptor fdObj)
  {
    super(fdObj);
  }

  public S3FileInputStream(S3File file) throws FileNotFoundException
  {
    // go get file
    super(file);
  }
}
