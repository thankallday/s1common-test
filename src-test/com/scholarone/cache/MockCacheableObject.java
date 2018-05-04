package com.scholarone.cache;

import java.io.Serializable;

import junit.framework.Assert;

import com.scholarone.persistence.ObjectCreationException;
import com.scholarone.persistence.PersistenceException;

public class MockCacheableObject extends Assert implements CacheableObject, Serializable
{
	protected static int			lastId			= 0;

	protected Integer					id;

	protected ObjectGenerator	generator		= null;

	protected byte							longevity		= 0;

	protected boolean					isCacheable	= true;

	protected CacheKey				key					= null;

	protected int							flushCount	= 0;

	public MockCacheableObject()
	{
		id = Integer.valueOf(++lastId);
	}

	protected MockCacheableObject(Integer id)
	{
		this.id = id;
	}

	public MockCacheableObject(boolean isCacheable)
	{
		this();
		this.isCacheable = isCacheable;
	}

	/**
	 * Returns the Id of this object. Id should be unique by class.
	 */
	public Integer getId()
	{
		return id;
	}

	/**
	 * Returns a Regenerator that can restore this object from persistence. Is allowed to return null to indicate this
	 * object cannot be restored.
	 */
	public ObjectGenerator getRegenerator()
	{
		if (generator == null)
		{
			generator = new Generator();
		}
		return generator;
	}

	/**
	 * Returns an int representing the longevity of objects in the cache. The meanings of various values is determined by
	 * the caching algorithm.
	 */
	public byte getCacheLongevity()
	{
		return longevity;
	}

	/**
	 * Returns whether or not this is cacheable.
	 */
	public boolean isCacheable()
	{
		return isCacheable;
	}

	public boolean isRefreshCacheOnUse()
	{
		return true;
	}

	/**
	 * Returns a primary key for this object. Could simply return new CacheKey(this), but it need not do so.
	 */
	public CacheKey getPrimaryKey()
	{
		if (key == null)
		{
			key = new CacheKey(this.getClass(), getId());
		}

		return key;
	}

	public void flush()
	{
		++flushCount;
	}

	/**
	 * An ObjectGenerator is an object that can restore an object of the specified class from a persisted state. It uses
	 * the criteria to determine which object to restore.
	 */
	public static class Generator implements ObjectGenerator
	{
		/**
		 * Uses the criteria to decide which object of the specified class to restore.
		 * 
		 * @param objectClass
		 *          The class of the object to be restored.
		 * @param criteria
		 *          The information this object will need to retrieve the specified object.
		 */
		public CacheableObject regenerateObject(Class objectClass, Object criteria)
		{
			return new MockCacheableObject((Integer) criteria);
		}

    public CacheableObject regenerateObject(Class objectClass, CacheCriteria criteria) throws ObjectCreationException,
        PersistenceException
    {
      // TODO Auto-generated method stub
      return null;
    }
	}

	public String toString()
	{
		return getClass().getName() + ":(" + getId() + ")";
	}

	public boolean equals(Object o)
	{
		return ((o != null) && (o.getClass() == this.getClass()) && (((MockCacheableObject) o).getId().equals(this.getId())));
	}

  public CacheKey getSecondaryKey()
  {
    // TODO Auto-generated method stub
    return null;
  }

  public void regenerate(CacheableObject obj)
  {
    // TODO Auto-generated method stub
    
  }

  public void setCacheLongevity(byte scope)
  {
    // TODO Auto-generated method stub
    
  }

}
