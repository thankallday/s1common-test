package com.scholarone.security;

import com.scholarone.ScholarOneRuntineException;

public class SecurityException extends ScholarOneRuntineException
{
  private static final long serialVersionUID = 1L;

  public SecurityException(String msg)
  {
    super(msg);
  }
}
