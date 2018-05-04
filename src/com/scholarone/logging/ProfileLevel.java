package com.scholarone.logging;

// org imports
import org.apache.log4j.Level;

/**
 * Title: Description: Copyright: Copyright (c) 2002 Company:
 * 
 * @author
 * @version 1.0
 */

public class ProfileLevel extends Level
{

	static public final int						PROFILE_INT	= Level.DEBUG_INT - 1;

	public static final String				PROFILE_STR	= "PROFILE";

	public static final ProfileLevel	PROFILE			= new ProfileLevel(PROFILE_INT, PROFILE_STR, 7);

	protected ProfileLevel(int level, String strLevel, int syslogEquiv)
	{
		super(level, strLevel, syslogEquiv);
	}

	public static Level toLevel(String str)
	{
		return (Level) toLevel(str, ProfileLevel.PROFILE);
	}

	public static Level toLevel(String str, Level defaultLevel)
	{
		if (str == null)
		{
			return defaultLevel;
		}

		String stringVal = str.toUpperCase();

		if (stringVal.equals(PROFILE_STR))
		{
			return ProfileLevel.PROFILE;
		}

		return Level.toLevel(str, defaultLevel);
	}

	public static Level toLevel(int i)
			throws IllegalArgumentException
	{
		if (i == PROFILE_INT)
		{
			return ProfileLevel.PROFILE;
		}

		return Level.toLevel(i);
	}
}

/*---  ---*/
