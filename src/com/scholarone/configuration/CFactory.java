package com.scholarone.configuration;

/**
 * Factory for configuration objects. Configuration objects encapsulate something like a properties file, read in on
 * application startup to define parameters.
 * <p>
 * 
 * Note that the logging package depends on configuration, so we won't use any loggers in here, to avoid circular
 * dependencies.
 */
public class CFactory
{

	public static final int				PROPERTY_FILE_TYPE	= 0;

	public static final int				XML_FILE_TYPE				= 1;

	private static final int			DEFAULT_TYPE				= PROPERTY_FILE_TYPE;

	private static Configuration	propertyConfig			= null;

	CFactory()
	{
	}

	public static synchronized Configuration instance()
	{
		return instance(DEFAULT_TYPE);
	}

	public static synchronized Configuration instance(int configType)
	{
		if (configType == PROPERTY_FILE_TYPE)
		{
			try
			{
				if (propertyConfig == null)
				{
					propertyConfig = new PropertyConfiguration();
				}

				return propertyConfig;
			}
			// throw a subclass of RuntimeException that doesn't require a throws declaration
			catch (ConfigurationException ce)
			{
				throw new IllegalStateException("Configuration not available: " + ce.getMessage());
			}
		}
		else
		{
			throw new IllegalStateException("not implemented yet");
		}

	}
}

/*---  ---*/
