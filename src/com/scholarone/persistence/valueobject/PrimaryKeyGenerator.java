package com.scholarone.persistence.valueobject;

// com imports
import java.io.Serializable;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.scholarone.cache.CacheCriteria;
import com.scholarone.cache.CacheableObject;
import com.scholarone.persistence.ObjectCreationException;
import com.scholarone.persistence.PersistenceException;

public class PrimaryKeyGenerator implements CacheableObject.ObjectGenerator, Serializable
{
  /** Singleton instance of this class. */
  private static PrimaryKeyGenerator instance;

  protected Logger log;

  /**
   * Constructor is protected so class can be a singleton.
   */
  protected PrimaryKeyGenerator()
  {
    log = Logger.getLogger(PrimaryKeyGenerator.class);
  }

  /**
   * Returns the single instance of this class.
   */
  public static PrimaryKeyGenerator getInstance()
  {
    if (instance == null)
    {
      instance = new PrimaryKeyGenerator();
    }

    return instance;
  }

  /**
   * Looks up a ValueObject of the speciried class using the specified criteria. Returns null if there are any errors,
   * including objectClass is null or does not represent a subclass of ValueObject, criteria is null or not an Integer,
   * or there is some sort of exception in the persistence reloading.
   * 
   */
  public CacheableObject regenerateObject(Class objectClass, CacheCriteria criteria) throws ObjectCreationException,
      PersistenceException
  {
    CacheableObject object = null;

    if ((objectClass != null) && (ValueObject.class.isAssignableFrom(objectClass)))
    {
      Object[] keys = criteria.getCriteria();
      Object key = null;
      if (keys.length > 0) key = keys[0];

      Vector inputs = new Vector();
      inputs.add(key);

      ValueObjectFactory factory = ValueObjectFactory.getInstance();

      object = factory.findObjectByCriteria(objectClass.getName(), inputs, ValueObject.FIND_BY_PRIMARY_KEY);
    }

    return object;
  }
}

/*---  ---*/
