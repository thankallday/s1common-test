package com.scholarone.context;

import java.util.HashMap;

public class CallContext implements ICallContext
{
  // Global Unique Call ID
  private String guid = null;

  // API Key
  private String apiKey = null;

  // Acting as User
  private Integer userId;

  // Version Number
  private String versionNo = null;

  // External ID (Supplied by Caller)
  private String externalId = null;

  // Audit Text (Supplied by Caller)
  private String auditText = null;

  // Operating Environment (Stack)
  private Integer stackId = null;

  // Exception Management
  private Exception exception = null;
  
  // ConfigId 
  private Integer configId = null;
  
  // Current locale id
  private Integer localeId = null;
  
  // Site name 
  private String siteName = null;
  
  // Acting as user
  private Integer personId = null;
  
  //string to hold override txDataSource
  private String txDSName = null;
  

  // HashMap to store generic context attributes
  private HashMap<String, Object> map = new HashMap<String, Object>();



  public String getGuid()
  {
    return guid;
  }

  public void setGuid(String guid)
  {
    this.guid = guid;
  }

  public String getApiKey()
  {
    return apiKey;
  }

  public void setApiKey(String apiKey)
  {
    this.apiKey = apiKey;
  }

  public Integer getUserId()
  {
    return userId;
  }

  public void setUserId(Integer userId)
  {
    this.userId = userId;
  }

  public String getVersionNo()
  {
    return versionNo;
  }

  public void setVersionNo(String versionNo)
  {
    this.versionNo = versionNo;
  }

  public String getExternalId()
  {
    return externalId;
  }

  public void setExternalId(String externalId)
  {
    this.externalId = externalId;
  }

  public String getAuditText()
  {
    return auditText;
  }

  public void setAuditText(String auditText)
  {
    this.auditText = auditText;
  }

  public Integer getStackId()
  {
    return stackId;
  }

  public void setStackId(Integer stackId)
  {
    this.stackId = stackId;
  }

  public Exception getException()
  {
    return exception;
  }

  public void setException(Exception e)
  {
    this.exception = e;
  }

  public Integer getConfigId()
  {
    return configId;
  }

  public void setConfigId(Integer configId)
  {
    this.configId = configId;
  }

  public Integer getLocaleId()
  {
    return localeId;
  }

  public void setLocaleId(Integer localeId)
  {
    this.localeId = localeId;
  }

  public String getSiteName()
  {
    return siteName;
  }

  public void setSiteName(String siteName)
  {
    this.siteName = siteName;
  }

  public Integer getPersonId()
  {
    return personId;
  }

  public void setPersonId(Integer personId)
  {
    this.personId = personId;
  }

 
  public String getTxDataSourceName()
  {
    
    return txDSName;
  }

  
  public void setTxDataSourceName(String txDSName)
  {
    this.txDSName = txDSName;
    
  }
  
  public Object getContextAttribute(String key)
  {
    return map.get(key);
  }
  
  public void setContextAttribute(String key, Object value)
  {
    map.put(key, value);
  }
  
  public void removeContextAttribute(String key)
  {
    map.remove(key);
  }
}
