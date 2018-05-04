package com.scholarone.logging;

// com imports
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.scholarone.configuration.CFactory;

/**
 * Requires the following properties to be defined in Configuration: <code>
 *    persistence.datasource.name = JNDI name of DataSource (for JDBC logger)<br>
 *  </code>
 */
public class Log4JDBAppender extends AppenderSkeleton
{
	private String	dataSourceJNDIName;

	public Log4JDBAppender()
	{
		dataSourceJNDIName = CFactory.instance().getProperty("persistence.datasource.name");
	}

	protected void append(LoggingEvent logEvent)
	{
		String name = logEvent.getLoggerName();
		String level = logEvent.getLevel().toString();
		String context = logEvent.getNDC();
		String message = logEvent.getRenderedMessage();
		String exceptionInfo = concatExceptionInfo(logEvent.getThrowableStrRep());
		Timestamp timestamp = new Timestamp(logEvent.timeStamp);
		int addedBy = 0;

		CallableStatement statement = null;
		StringBuffer sqlStatement = new StringBuffer();
		Connection conn = null;

		try
		{
			conn = openConnection();

			sqlStatement.append("{CALL INSERTERRORLOG(?,?,?,?,?,?,?)}");

			statement = conn.prepareCall(sqlStatement.toString());

			statement.setString(1, name);
			statement.setString(2, level);
			statement.setString(3, context);
			statement.setString(4, message);
			statement.setString(5, exceptionInfo);
			statement.setTimestamp(6, timestamp);
			statement.setInt(7, addedBy);

			statement.executeUpdate();
		}
		catch (SQLException sql)
		{
			System.out.println(sql.getMessage());
		}
		finally
		{
			closeStatement(statement);
			closeConnection(conn);
		}
	}

	public boolean requiresLayout()
	{
		return false;
	}

	public void close()
	{
	}

	private String concatExceptionInfo(String[] exceptionInfo)
	{
		if (exceptionInfo == null)
		{
			return "";
		}

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < exceptionInfo.length; i++)
		{
			if (buf.length() + exceptionInfo[i].length() > 1024)
			{
				break;
			}
			else if (exceptionInfo[i].charAt(0) != '\t')
			{
				buf.append(exceptionInfo[i]);
			}
			else
			{
				buf.append(exceptionInfo[i].substring(1));
			}

			buf.append("  ");
		}

		return buf.toString();
	}

	// NOTE: this object implements openConnection() and closeConnection()
	// on its own so it can operate independently of persistence package

	/**
	 * Gets a connection object from the app server connection pool
	 * 
	 * @return connection object
	 */
	protected Connection openConnection()
			throws SQLException
	{
		Connection connection = null;
		DataSource source = null;

		try
		{
			InitialContext ctx = new InitialContext();

			source = (DataSource) ctx.lookup(dataSourceJNDIName);
		}
		catch (NamingException ne)
		{
			// can't really recover from this - bail hard
			throw new Error("Unable to find DataSource");
		}

		connection = source.getConnection();

		return connection;
	}

	/**
	 * Releases a connection back to the connection pool.
	 * 
	 * @param conn
	 *          connection to be returned to the pool.
	 */
	private void closeConnection(Connection conn)
	{
		// this should never fail and if it does, we're in trouble
		try
		{
			if (conn != null)
			{
				conn.close();
			}
		}
		catch (SQLException se)
		{
			//throw new Error("Unable to close connection: " + se.toString());
		}
	}

	/**
	 * Ensures all result sets that belong to this statement are closed, then closes the statement. ALso closes the
	 * connection that this was using
	 * 
	 * @param statement
	 *          Statement to be closed.
	 */
	private void closeStatement(Statement statement)
	{
		// this should never fail and if it does, we're in trouble
		try
		{
			if (statement != null)
			{
				//statement.close();
				closeConnection(statement.getConnection());
			}
		}
		catch (SQLException se)
		{
			//throw new Error("Unable to close statement: " + se.toString());
		}
	}
}

/*---  ---*/
