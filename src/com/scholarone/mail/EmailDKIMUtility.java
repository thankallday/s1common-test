package com.scholarone.mail;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import net.markenwerk.utils.mail.dkim.Canonicalization;
import net.markenwerk.utils.mail.dkim.DkimMessage;
import net.markenwerk.utils.mail.dkim.DkimSigner;
import net.markenwerk.utils.mail.dkim.SigningAlgorithm;

import org.apache.log4j.Logger;

public class EmailDKIMUtility
{
  private final static String PRIVATE_KEY = "private.key";

  private final static String SELECTOR = "selector";

  private final static String DOMAIN = "domain";

  protected static Logger log = Logger.getLogger(EmailDKIMUtility.class.getName());

  public static MimeMessage dkimSignMessage(MimeMessage message, boolean checkDomainKey, boolean unfoldHeaders) throws Exception
  {
    InputStream privateKey = EmailDKIMUtility.class.getClassLoader().getResourceAsStream(
        DKIMPropertyValues.getProperty(PRIVATE_KEY));

    InternetAddress[] addresses = (InternetAddress[]) message.getFrom();
    String from = addresses[0].getAddress();

    if (privateKey != null)
    {
      DkimSigner dkimSigner = new DkimSigner(DKIMPropertyValues.getProperty(DOMAIN),
          DKIMPropertyValues.getProperty(SELECTOR), privateKey);
      dkimSigner.setIdentity(from);
      dkimSigner.setHeaderCanonicalization(Canonicalization.SIMPLE);
      dkimSigner.setBodyCanonicalization(Canonicalization.RELAXED);
      dkimSigner.setSigningAlgorithm(SigningAlgorithm.SHA256_WITH_RSA);
      dkimSigner.setLengthParam(true);
      dkimSigner.setZParam(false);
      if (!checkDomainKey)
      {
        dkimSigner.setCheckDomainKey(false);
      }

      if ( unfoldHeaders )
      {
        headerUnfolding(message, dkimSigner);
      }
      
      return new DkimMessage(message, dkimSigner);
    }

    log.error("Failed to load private key.  Message does not have a dkim signature.");

    return message;
  }

  // Header unfolding according to RFC2822 3.2.3. Folding white space and comments
  static void headerUnfolding(MimeMessage msg, DkimSigner signer) throws MessagingException
  {
    HashMap<String, String> headerMap = new HashMap<String, String>();
    Set<String> headersToSign = signer.getHeadersToSign();
    
    Enumeration headers = msg.getAllHeaders();
    while (headers.hasMoreElements())
    {
      Header header = (Header) headers.nextElement();
      if ( headersToSign.contains(header.getName())) 
      {
        String value = header.getValue();
        if ( value == null || value.length() == 0 ) continue;
  
        String unfoldedValue = MimeUtility.unfold(value);
        unfoldedValue = unfoldedValue.trim().replaceAll(" +", " ");
        if ( !unfoldedValue.equals(value) )
          headerMap.put(header.getName(), unfoldedValue);
      }
    }
    
    Set<String> keys = headerMap.keySet();
    for ( String name : keys )
    {
      msg.setHeader(name, headerMap.get(name));
    }
    
    msg.saveChanges();
  }
}
