package com.scholarone.persistence.valueobject;

// com imports
import java.util.Vector;

import org.apache.log4j.Logger;

import com.scholarone.ScholarOneException;
import com.scholarone.cache.CacheCriteria;
import com.scholarone.cache.CacheableObject;

public class ValueObjectListGenerator implements CacheableObject.ObjectGenerator
{

  private static ValueObjectListGenerator instance;

  protected Logger log;

  protected ValueObjectListGenerator()
  {
    log = Logger.getLogger(ValueObjectListGenerator.class);
  }

  public static ValueObjectListGenerator getInstance()
  {
    if (instance == null)
    {
      instance = new ValueObjectListGenerator();
    }

    return instance;
  }

  public CacheableObject regenerateObject(Class objectClass, CacheCriteria criteria)
  {
    ValueObjectList list = null;

    if (objectClass == ValueObjectList.class)
    {
      Object[] inputArray = criteria.getCriteria();

      if ((inputArray.length >= 3) && (inputArray[0] instanceof String) && (inputArray[1] instanceof String)
          && (inputArray[2] instanceof Vector))
      {
        String typeName = (String) inputArray[0];
        String method = (String) inputArray[1];
        Vector inputs = (Vector) inputArray[2];

        try
        {
          com.scholarone.persistence.valueobject.ValueObjectFactory factory = com.scholarone.persistence.valueobject.ValueObjectFactory
              .getInstance();

          list = factory.findListByCriteria(typeName, inputs, method, false);
        }
        catch (ScholarOneException e)
        {
          // report the error
          log.error("ValueObjectListGenerator.regenerateObject : error loading list of " + typeName + " using "
              + method, e);
        }
      }
    }

    return list;
  }
}

/*---  ---*/
