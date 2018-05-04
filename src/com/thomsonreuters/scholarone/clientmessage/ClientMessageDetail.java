package com.thomsonreuters.scholarone.clientmessage;

public abstract class ClientMessageDetail
{
  private ClientMessageInfo clientMessageInfo;
  private String fieldName;
  private String messageText;

  // -------------------------------------------------------------------------

  public boolean equals(ClientMessageDetail other)
  {
    boolean result = (
        other != null
        && other instanceof ClientMessageDetail
        && this.getClientMessageInfo().equals(other.getClientMessageInfo())
        && (
            (this.getFieldName() == null && other.getFieldName() == null)
            || (this.getFieldName() != null && this.getFieldName().equals(other.getFieldName()))
           )
        && (
            (this.getMessageText() == null && other.getMessageText() == null)
            ||
            (this.getMessageText() != null && this.getMessageText().equals(other.getMessageText()))
           )
        );
    return result;
  }

  public String toString()
  {
    StringBuffer stringBuffer = new StringBuffer(this.getClass().getSimpleName());
    stringBuffer.append("{");
    stringBuffer.append("clientMessageInfo=" + getClientMessageInfo());
    stringBuffer.append(",");
    stringBuffer.append("fieldName=" + getFieldName());
    stringBuffer.append(",");
    stringBuffer.append("messageText=" + getMessageText());
    stringBuffer.append("}");
    return stringBuffer.toString();
  }

  // -------------------------------------------------------------------------

  public ClientMessageInfo getClientMessageInfo()
  {
    return clientMessageInfo;
  }

  protected void setClientMessageInfo(ClientMessageInfo clientMessageInfo)
  {
    this.clientMessageInfo = clientMessageInfo;
  }

  public String getCodeValue()
  {
    return clientMessageInfo.getCodeValue();
  }

  public String getFieldName()
  {
    return fieldName;
  }

  protected void setFieldName(String fieldName)
  {
    this.fieldName = fieldName;
  }

  public String getMessageText()
  {
    return messageText;
  }

  protected void setMessageText(String messageText)
  {
    this.messageText = messageText;
  }

  public String getResourceName()
  {
    return clientMessageInfo.getResourceName();
  }
}