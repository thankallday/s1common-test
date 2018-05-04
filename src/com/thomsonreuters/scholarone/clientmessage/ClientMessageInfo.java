package com.thomsonreuters.scholarone.clientmessage;

public class ClientMessageInfo
{
  private String codeValue;
  private String resourceName;

  // -----------------------------------------------------------------------

  public ClientMessageInfo(String codeValue, String resourceName)
  {
    this.codeValue = codeValue;
    this.resourceName = resourceName;
  }

  // -----------------------------------------------------------------------

  public String toString()
  {
    StringBuffer stringBuffer = new StringBuffer(this.getClass().getName());
    stringBuffer.append("{");
    stringBuffer.append("codeValue=" + codeValue);
    stringBuffer.append(",");
    stringBuffer.append("resourceName=" + resourceName);
    stringBuffer.append("}");
    return stringBuffer.toString();
  }

  // -----------------------------------------------------------------------

  public String getCodeValue()
  {
    return codeValue;
  }

  public String getResourceName()
  {
    return resourceName;
  }
}