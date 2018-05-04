package com.scholarone.framework;

import java.util.Enumeration;

import com.scholarone.ScholarOneException;
import com.scholarone.exception.InvalidFileUploadException;
import com.scholarone.exception.ScholarOneVirusException;
import com.scholarone.file.S3File;

public interface IDataTransferObject
{
  public static final int SCOPE_READ_ONLY = 0;
  public static final int SCOPE_READ_WRITE = 1;
  public static final int SCOPE_SESSION = 2;

  public static final int AJAX_STATUS_SUCCESS = 1;
  public static final int AJAX_STATUS_SECURITY_FAILURE = -1;
  public static final int AJAX_STATUS_GENERAL_FAILURE = -2;
  public static final int AJAX_STATUS_VALIDATION_FAILURE = -3;

  public Object clone() throws CloneNotSupportedException;
  
  public boolean getCallSuccess();

  public void setCallSuccess(boolean success);

  public int getStatus();

  public void setStatus(int status);

  public Object getAttribute(String name);

  public Object getAttribute(String name, int scope);

  public String getAttributeStr(String name);

  public String getAttributeStr(String name, int scope);

  public int getAttributeInt(String name);

  public int getAttributeInt(String name, int scope);

  public long getAttributeLong(String name);

  public long getAttributeLong(String name, int scope);

  public boolean isAttributeNull(String name);

  public boolean isAttributeNull(String name, int scope);

  public boolean getAttributeBool(String name);

  public boolean getAttributeBool(String name, int scope);

  public int getAttributeCount(String prefix, int scope);

  public void setAttribute(String name, Object object);

  public void setAttribute(String name, Object object, int scope);

  public void removeAttribute(String name);

  public void removeAttribute(String name, int scope);

  public Enumeration<String> getAttributeNames(int scope);

  public S3File getFile(String key) throws ScholarOneVirusException, InvalidFileUploadException, ScholarOneException;

  public String getFileNameOnly(String name);

  public Object getContextItem(String key);

  public void setContextItem(String key, Object item);

  public DTOResponse getDTOResponse();

  public void registerAttributeObservable(String name, ScholarOneObservable observable);

  public void unregisterAttributeObservable(String name);

  public boolean isAttributeObserved(String name);
}
