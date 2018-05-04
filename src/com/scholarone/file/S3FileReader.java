package com.scholarone.file;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class S3FileReader extends FileReader
{

  public S3FileReader(String fileName) throws FileNotFoundException
  {
    super(fileName);
    // TODO Auto-generated constructor stub
  }

  public S3FileReader(S3File file) throws FileNotFoundException
  {
    super(file);
    // TODO Auto-generated constructor stub
  }

  public S3FileReader(FileDescriptor fd)
  {
    super(fd);
    // TODO Auto-generated constructor stub
  }

}
