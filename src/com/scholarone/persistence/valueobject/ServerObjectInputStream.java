package com.scholarone.persistence.valueobject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;


public class ServerObjectInputStream extends ObjectInputStream
{

  public ServerObjectInputStream() throws IOException
  {
    super();
    enableResolveObject(true);
  }

  public ServerObjectInputStream(InputStream out) throws IOException
  {
    super(out);
    enableResolveObject(true);
  }

  public Object resolveObject(Object obj)
  {
    if (obj instanceof ValueObject)
    {
      ((ValueObject) obj).setMode(ValueObject.SERVER);
    }

    return obj;
  }
}
