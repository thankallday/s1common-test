package com.scholarone.file;


public interface S3FileFilter
{
  public boolean accept(S3File pathname);
}
