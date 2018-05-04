package com.scholarone.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.scholarone.ScholarOneException;
import com.scholarone.logging.Logger;

public class AESEncryption
{
  private static AESEncryption aes;

  protected static Logger log = Logger.getLogger(AESEncryption.class);

  private Cipher ecipher;

  private Cipher decipher;

  private final int KEY_LENGTH = 16;

  private AESEncryption(IKey key)
  {
    init(key);
  }

  public static AESEncryption getInstance()
  {
    IKey key = new Key();
    return getInstance(key);
  }
  
  public static AESEncryption getInstance(IKey key)
  {
    if (aes == null)
    {
      synchronized (AESEncryption.class)
      {
        aes = new AESEncryption(key);

      }
    }

    return aes;
  }

  public String encrypt(String str) throws ScholarOneException
  {
    try
    {
      // Compress
      byte[] compress = compress(str);

      // Encrypt
      byte[] encrypt = ecipher.doFinal(compress);

      // Encode
      String encode = Base58.encode(encrypt);

      return EncryptionUtility.ENCRYPTED_PREFIX + encode;
    }
    catch (Exception e)
    {
      log.error("AESEncryption.encrypt", e);
      throw new ScholarOneException(e); // Bubble error up
    }
  }

  /**
   * @param str
   * @return
   */
  public String decrypt(String str) throws ScholarOneException
  {
    try
    {
      int index = str.indexOf(EncryptionUtility.ENCRYPTED_PREFIX);
      if (index > -1) str = str.substring(index + EncryptionUtility.ENCRYPTED_PREFIX.length());

      // Decode
      byte[] decode = Base58.decode(str);

      // Decrypt
      byte[] decrypt = decipher.doFinal(decode);

      // Decompress
      String decompress = decompress(decrypt);

      return decompress;
    }
    catch (Exception e)
    {
      log.error("AESEncryption.decrypt", e);

      throw new ScholarOneException(e); // Bubble error up
    }
  }

  private void init(IKey key)
  {
    Security.addProvider(new com.sun.crypto.provider.SunJCE());

    try
    {
      String sKey = key.getX1Value();

      if (sKey == null || sKey.length() != KEY_LENGTH)
      {
        log.error("AESEncryption.decrypt - bad key");
      }

      SecretKeySpec keySpec = new SecretKeySpec(sKey.getBytes("UTF-8"), "AES");
      ecipher = Cipher.getInstance("AES");
      ecipher.init(Cipher.ENCRYPT_MODE, keySpec);

      decipher = Cipher.getInstance("AES");
      decipher.init(Cipher.DECRYPT_MODE, keySpec);
    }
    catch (NoSuchAlgorithmException e)
    {
      System.out.println("AESEncryption.initialize - " + e.getMessage());
      log.error("AESEncryption.initialize", e);
    }
    catch (InvalidKeyException e)
    {
      System.out.println("AESEncryption.initialize - " + e.getMessage());
      log.error("AESEncryption.initialize", e);
    }
    catch (NoSuchPaddingException e)
    {
      System.out.println("AESEncryption.initialize - " + e.getMessage());
      log.error("AESEncryption.initialize", e);
    }
    catch (UnsupportedEncodingException e)
    {
      System.out.println("AESEncryption.initialize - " + e.getMessage());
      log.error("AESEncryption.initialize", e);
    }
  }

  private static byte[] compress(String text)
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try
    {
      OutputStream out = new DeflaterOutputStream(baos);
      out.write(text.getBytes("UTF-8"));
      out.close();
    }
    catch (IOException e)
    {
      log.error("Error in StringUtility.compress", e);
    }
    return baos.toByteArray();
  }

  private static String decompress(byte[] bytes)
  {
    InputStream in = new InflaterInputStream(new ByteArrayInputStream(bytes));
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try
    {
      byte[] buffer = new byte[8192];
      int len;
      while ((len = in.read(buffer)) > 0)
        baos.write(buffer, 0, len);
      return new String(baos.toByteArray(), "UTF-8");
    }
    catch (IOException e)
    {
      log.error("Error in StringUtility.decompress", e);
    }

    return "";
  }
}
