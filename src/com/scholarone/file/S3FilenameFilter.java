package com.scholarone.file;


public interface S3FilenameFilter
{
  boolean accept(S3File dir, String name);
}
