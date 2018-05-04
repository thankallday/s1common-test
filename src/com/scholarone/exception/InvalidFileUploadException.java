/**
 * Created on July 4th, 2007
 * InvalidFileUploadException.java checks whether the
 * uploading file is present in the hard drive of that machine or not.
 * It is responsible in catching Such exceptions and Showing Alert to the
 * User.
 * @author Jyothirmayi
 */
package com.scholarone.exception;

import com.scholarone.ScholarOneException;

/**
 * Class defining a package level exception for the ScholarOne package.
 */
public class InvalidFileUploadException extends ScholarOneException
{
  public InvalidFileUploadException()
  {
    super();
  }

  public InvalidFileUploadException(String msg)
  {
    super(msg);
  }

  public InvalidFileUploadException(Throwable cause)
  {
    super(cause);
  }
}
