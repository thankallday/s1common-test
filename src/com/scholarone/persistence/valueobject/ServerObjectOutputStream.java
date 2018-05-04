package com.scholarone.persistence.valueobject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


public class ServerObjectOutputStream extends ObjectOutputStream
{

  public ServerObjectOutputStream() throws IOException
  {
    super();
    enableReplaceObject(true);
  }

  public ServerObjectOutputStream(OutputStream out) throws IOException
  {
    super(out);
    enableReplaceObject(true);
  }

  public Object replaceObject(Object obj)
  {
    if (obj instanceof ValueObject)
    {
      ((ValueObject) obj).setMode(ValueObject.CLIENT);
    }

    return obj;
  }
}
