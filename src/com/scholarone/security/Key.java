package com.scholarone.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.scholarone.persistence.valueobject.StoredProcedurePersistence;

public class Key implements IKey
{
  protected Logger log = Logger.getLogger(Key.class);
  
  public String getX1Value()
  {
    Connection conn = StoredProcedurePersistence.getPTConnection(false);
    String X1 = null;
    try
    {
      PreparedStatement prepStmt = conn.prepareStatement("select name from X1 fetch first 1 row only");
      ResultSet rs = prepStmt.executeQuery();

      if (rs != null && rs.next())
      {
        X1 = rs.getString(1);
      }

      StoredProcedurePersistence.closePTConnection(conn);
    }
    catch (SQLException e)
    {
      log.error("Failed to get X1 value", e);
    }
    
    return X1;
  }
}
