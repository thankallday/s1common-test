package com.scholarone.persistence.validator;

import org.apache.log4j.Logger;

import com.scholarone.persistence.valueobject.ValueObject;

public abstract class BasePersistenceValidator
{
  public abstract boolean validate(ValueObject object);
    
  public abstract boolean validateSave(ValueObject object);
  
  public abstract boolean validateDelete(ValueObject object);
  
  protected static Logger log = Logger.getLogger(BasePersistenceValidator.class);

  public static BasePersistenceValidator getValidator(String validatorName)
  {
    BasePersistenceValidator validator = null;
    
    Class<?> xClass;
    try
    {
      xClass = Class.forName(validatorName);
      validator = (BasePersistenceValidator) xClass.getConstructor(new Class[] {}).newInstance();
    }
    catch (Exception e)
    {
      log.error("Error creating validator [" + validatorName + "] " + e.getMessage());
    }
    
    return validator;
  }
}
