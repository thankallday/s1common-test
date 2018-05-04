package com.scholarone.framework;

import java.util.zip.ZipEntry;

import com.scholarone.file.S3File;
import com.scholarone.file.S3FileInputStream;

public class DTOResponse
{
  private boolean inUse = false;
  
  private String contentType;

  private String contentTypeHeader;

  private String contentDisposition;
  
  private String characterEncoding;

  private String responseData;

  private S3File fileToStream;
  
  private S3FileInputStream fileStream;
  
  private boolean writeBOM = false;
  
  private int contentLength;
  
  private boolean deleteFilePostStream = false; 
  
  //Current use only has a single zip entry.
  //May need to revisit for lists
  private ZipEntry zipEntry;
  
  private String zipData;
  
  private Object objectToStream;
  
  private String pragmaHeader;
  
  private String cacheControl;
  
  
  public String getContentType()
  {
    return contentType;
  }

  public void setContentType(String contentType)
  {
    this.contentType = contentType;
    inUse = true;
  }

  public String getResponseData()
  {
    return responseData;
  }

  public void setResponseData(String responseData)
  {
    this.responseData = responseData;
    inUse = true;
  }

  public String getContentDisposition()
  {
    return contentDisposition;
  }

  public void setContentDisposition(String contentDisposition)
  {
    this.contentDisposition = contentDisposition;
    inUse = true;
  }

  public String getContentTypeHeader()
  {
    return contentTypeHeader;
  }

  public void setContentTypeHeader(String contentTypeHeader)
  {
    this.contentTypeHeader = contentTypeHeader;
    inUse = true;
  }

  public boolean writeBOMNeeded()
  {
    return writeBOM;
  }

  public void setWriteBOM(boolean writeBOM)
  {
    this.writeBOM = writeBOM;
    inUse = true;
  }

  public String getCharacterEncoding()
  {
    return characterEncoding;
  }

  public void setCharacterEncoding(String characterEncoding)
  {
    this.characterEncoding = characterEncoding;
    inUse = true;
  }

  public boolean isInUse()
  {
    return inUse;
  }

  public ZipEntry getZipEntry()
  {
    return zipEntry;
  }

  public void setZipEntry(ZipEntry zipEntry)
  {
    this.zipEntry = zipEntry;
    inUse = true;
  }

  public String getZipData()
  {
    return zipData;
  }

  public void setZipData(String zipData)
  {
    this.zipData = zipData;
    inUse = true;
  }

  public int getContentLength()
  {
    return contentLength;
  }

  public void setContentLength(int contentLength)
  {
    this.contentLength = contentLength;
    inUse = true;
  }

  public String getPragmaHeader()
  {
    return pragmaHeader;
  }

  public void setPragmaHeader(String pragmaHeader)
  {
    this.pragmaHeader = pragmaHeader;
    inUse = true;
  }

  public String getCacheControl()
  {
    return cacheControl;
  }

  public void setCacheControl(String cacheControl)
  {
    this.cacheControl = cacheControl;
    inUse = true;
  }

  public S3File getFileToStream()
  {
    return fileToStream;
  }

  public void setFileToStream(S3File fileToStream)
  {
    this.fileToStream = fileToStream;
    inUse = true;
  }

  public void setFileToStream(S3File fileToStream, boolean deletePostStream)
  {
    this.fileToStream = fileToStream;
    inUse = true;
    deleteFilePostStream = deletePostStream;
  }

  public boolean getDeleteFilePostStream()
  {
    return deleteFilePostStream;
  }
  
  public S3FileInputStream getFileStream()
  {
    return fileStream;
  }

  public void setFileStream(S3FileInputStream fileStream)
  {
    this.fileStream = fileStream;
    inUse = true;
  }

  public Object getObjectToStream()
  {
    return objectToStream;
  }

  public void setObjectToStream(Object objectToStream)
  {
    this.objectToStream = objectToStream;
    inUse = true;
  }


}
