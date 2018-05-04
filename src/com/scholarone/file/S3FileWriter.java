package com.scholarone.file;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;

public class S3FileWriter extends FileWriter
{

  public S3FileWriter(String fileName) throws IOException
  {
    super(fileName);
    // TODO Auto-generated constructor stub
  }

  public S3FileWriter(S3File file) throws IOException
  {
    super(file);
    // TODO Auto-generated constructor stub
  }

  public S3FileWriter(FileDescriptor fd)
  {
    super(fd);
    // TODO Auto-generated constructor stub
  }

  public S3FileWriter(String fileName, boolean append) throws IOException
  {
    super(fileName, append);
    // TODO Auto-generated constructor stub
  }

  public S3FileWriter(File file, boolean append) throws IOException
  {
    super(file, append);
    // TODO Auto-generated constructor stub
  }

}
