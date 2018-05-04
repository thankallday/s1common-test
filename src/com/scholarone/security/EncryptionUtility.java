package com.scholarone.security;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.scholarone.ScholarOneException;
import com.scholarone.constants.Constants;

public class EncryptionUtility
{
  protected static Logger log = Logger.getLogger(EncryptionUtility.class);

  public static final String ENCRYPTED_PREFIX = "xik_"; 
  
  public static final String ENCRYPT_PARAMETER_PREFIX = "XIK_";
  
  private static Pattern[][] secureParameterPatterns;

  private static int ROWS = 3;

  private static int COLS = 2;

  private static ArrayList<Pattern> embeddedURLPatternList = null;

  private static ArrayList<Pattern> embeddedURLPatternExcludeList = null;

  static
  {
    embeddedURLPatternList = new ArrayList<Pattern>();
    embeddedURLPatternList.add(Pattern.compile("HREF=\"\\s*javascript:popWindow\\('[^,']*\\?([^,']*)',",
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL));
    embeddedURLPatternList.add(Pattern.compile("['\"]HTTP[^'\"]*?\\?([^'\"]*?)['\"]", Pattern.CASE_INSENSITIVE | Pattern.DOTALL));
    embeddedURLPatternList.add(Pattern.compile("javascript: window.open\\(\"[^\"]*?\\?([^\"]*?)\",", Pattern.DOTALL));
    
    embeddedURLPatternExcludeList = new ArrayList<Pattern>();
    // exclude:
    // Pubmed reference links
    // Web of Science reference links
    // WoS QA links(?)
    embeddedURLPatternExcludeList.add(Pattern.compile("(http://www.ncbi.nlm.nih.gov/entrez/query.fcgi|http://gateway.isiknowledge.com/gateway/Gateway.cgi|http://links-qa.isiknowledge.com/gateway/Gateway.cgi)", Pattern.DOTALL));

    // All patterns in column 0 must have the same grouping because there is shared matching and replacement code.
    secureParameterPatterns = new Pattern[ROWS][COLS];
    secureParameterPatterns[0][0] = Pattern.compile("("
        + // group 1
        "("
        + // group 2
        "(&|\\?|%26|%3F)"
        + // group 3
        "\\w*?" + ENCRYPT_PARAMETER_PREFIX + "\\w*(?:=|%3D)" + ")" + ")" + "((?!" + ENCRYPTED_PREFIX
        + ")\\w+)" // group 4 (This group is encrypted and replaced)
    , Pattern.DOTALL);

    secureParameterPatterns[1][0] = Pattern.compile("("
        + // group 1
        "([',\".])"
        + // group 2
        "\\w*?" + ENCRYPT_PARAMETER_PREFIX + "\\w*?[',\".]"
        + "([\\s\\w=]*?[Vv][Aa][Ll][Uu][Ee]\\s*=\\s*|\\s*,\\s*?)" + // group 3
        "[',\"])" + "((?!" + ENCRYPTED_PREFIX + ")[-\\w]+)" // group 4 (This group is encrypted and replaced)
    , Pattern.DOTALL);

    secureParameterPatterns[2][0] = Pattern.compile("(" + // group 1
        "(" + // group 2
        "(<SELECT[^>]*?NAME\\s*=\\s*[',\"]\\w*?" + ENCRYPT_PARAMETER_PREFIX + "\\w*\"?[^>]*>?)" + // .group 3
        ")" + ")" + "(.*?)" + // group 4 (This group is encrypted and replaced by the nested pattern in column 1)
        "(</SELECT>?)" // group 5 (Only used in nested pattern replacement)
    , Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    secureParameterPatterns[2][1] = Pattern.compile("(<[Oo][Pp][Tt][Ii][Oo][Nn].*?[Vv][Aa][Ll][Uu][Ee]\\s*=\\s*[',\"])"
        + // group 1
        "((?!" + ENCRYPTED_PREFIX + ")" + // group 2
        "[-~\\w]+)", Pattern.DOTALL);
  }

  public static Pattern[][] getSecureParameterPatterns()
  {
    return secureParameterPatterns;
  }

  /**
   * @param value
   * @param salt
   * @return
   * @throws ScholarOneException
   */
  public static String encryptString(String value, String salt) throws ScholarOneException
  {
    return encryptString(value, salt, null);
  }
  
  public static String encryptString(String value, String salt, IKey key) throws ScholarOneException
  {
    if (salt != null)
    {
      value = salt + value;
      
     if ( key == null )
     {
       return AESEncryption.getInstance().encrypt(value);
     }
     else
     {
       return AESEncryption.getInstance(key).encrypt(value);
     }
    }

    throw new SecurityException("Failed to encryptString.  No salt provided.");
  }

  /**
   * @param value
   * @param salt
   * @return
   * @throws ScholarOneException
   */
  public static String decryptString(String value, String salt) throws ScholarOneException
  {
    return decryptString(value, salt, null);
  }

  public static String decryptString(String value, String salt, IKey key) throws ScholarOneException
  {
    if (value != null)
    {
      String decryptedValue = null;
      
      if ( key == null )
      {
        decryptedValue = AESEncryption.getInstance().decrypt(value);
      }
      else
      {
        decryptedValue = AESEncryption.getInstance(key).decrypt(value);
      }

      if (salt != null && decryptedValue.startsWith(salt))
      {
        return decryptedValue.substring(salt.length(), decryptedValue.length());
      }
      
      throw new SecurityException("Failed to decryptString.  Salt mismatch.");
    }

    return value;
  }
  /**
   * Performs a search/encrypt/replace supplied string base on the patterns defined in static data of this class
   * 
   * 
   * @param salt
   * @param data
   * @return
   * @throws ScholarOneException
   */
  public static String searchAndEncrypt(String salt, String data)
  {
    StringBuffer sbOut = new StringBuffer();
    StringBuffer sbInner = new StringBuffer();
    
    Pattern[][] patterns = getSecureParameterPatterns(); // List of patterns to search for
    for (int row = 0; row < ROWS; row++)
    {
      data = searchAndEncrypt(patterns[row], salt, data, sbOut, sbInner);
    }

    return data;
  }

  /**
   * Performs a search/encrypt/replace on certain patterns, e.g. XIK_DOCUMENT_ID=<number>
   * 
   * 
   * @param pattern
   * @param salt
   * @param data
   * @return
   * @throws ScholarOneException
   */
  private static String searchAndEncrypt(Pattern[] patterns, String salt, String data, StringBuffer sbOut, StringBuffer sbInner)
  {
    Pattern pattern;
    Pattern nestedPattern;

    if (patterns != null && patterns.length > 0)
    {
      pattern = patterns[0];
      if (pattern != null)
      {
        try
        {
          Matcher matcher = pattern.matcher(data);
          boolean didMatchFl = false;
          sbOut.setLength(0);
          
          while (matcher.find())
          {
            sbOut.ensureCapacity((int)(data.length() * 1.1));
            
            nestedPattern = patterns[1];
            if (nestedPattern != null)
            {              
              sbInner.setLength(0);
              
              String matcherGroup4 = matcher.group(4);
              sbInner.ensureCapacity((int)(matcherGroup4.length() * 1.1));
              
              Matcher nestedMatcher = nestedPattern.matcher(matcherGroup4);
              while (nestedMatcher.find())
              {
                String value = EncryptionUtility.encryptString(nestedMatcher.group(2), salt);

                didMatchFl = true; // Flag to say we did something (return sbOut)
                nestedMatcher.appendReplacement(sbInner, Matcher.quoteReplacement(nestedMatcher.group(1) + value));
              }

              nestedMatcher.appendTail(sbInner);

              matcher.appendReplacement(sbOut, Matcher.quoteReplacement(matcher.group(1) + sbInner.toString()
                  + matcher.group(5)));
            }
            else
            {
              String value = EncryptionUtility.encryptString(matcher.group(4), salt);

              didMatchFl = true; // Flag to say we did something (return sbOut)
              matcher.appendReplacement(sbOut, Matcher.quoteReplacement(matcher.group(1) + value));
            }
          }

          if (didMatchFl)
          {
            matcher.appendTail(sbOut);

            return sbOut.toString();
          }
        }
        catch (Exception e)
        {
          log.error("SecurityUtility.searchAndEncrypt", e);
        }
      }
    }

    // We did not match anything. Return our input string unaltered.
    return data;
  }

  public static String encryptURL(String url, String salt)
  {
    boolean isDownloadFl = false;

    int idx = url.indexOf("?") + 1;
    
    String start = url.substring(0, idx);
    url = url.substring(idx);

    if (url.indexOf("&" + Constants.DOWNLOAD + "=" + Constants.TRUE) >= 0) isDownloadFl = true;

    // Handle double/re-encoding by finding PARAMS, decoding current params, then re-encoding entire string
    /*
     * Keep code. This works, but i do not woant to mess up load test close to release time... Matcher matcher =
     * FIND_PARAMS_PARAMETER_PATTERN.matcher(url); if (matcher.find()) { StringBuffer convertedBuf = new StringBuffer();
     * 
     * String allBeforeValue = matcher.group(1); String leadingSeparator = matcher.group(2); String encryptedValue =
     * matcher.group(3);
     * 
     * StringBuffer decryptedOriginalValue = new StringBuffer(""); // decrypt value try { Map<String, ArrayList<String>>
     * paramMap = SecurityUtility.decryptAndCheckURL(encryptedValue, request);
     * 
     * Iterator<String> keys = paramMap.keySet().iterator(); boolean first = true; while ( keys.hasNext() ) { String key
     * = keys.next(); ArrayList<String> values = paramMap.get(key);
     * 
     * for(int i=0;i<values.size();i++) { if (!first) decryptedOriginalValue.append("&");
     * decryptedOriginalValue.append(key + "=" + values.get(i)); first = false; } } } catch(ScholarOneException s1e) {
     * log.error("URLUtility.encryptAndEncodeURL: Error decrypting previously encrypted part of URL parameters", s1e); }
     * 
     * String newValue = leadingSeparator + decryptedOriginalValue; matcher.appendReplacement(convertedBuf,
     * Matcher.quoteReplacement(newValue)); matcher.appendTail(convertedBuf); url = convertedBuf.toString(); }
     */
    try
    {
      if ( !url.contains(ENCRYPTED_PREFIX) )
      {
        url = encryptString(url, salt);

        if (isDownloadFl)
          url = start + Constants.DOWNLOAD + Constants.EQUALSCHAR + Constants.TRUE + "&" + Constants.PARAMS
              + Constants.EQUALSCHAR + url;
        else
          url = start + Constants.PARAMS + Constants.EQUALSCHAR + url;
      }
    }
    catch (ScholarOneException e)
    {
      log.error("URLUtility.encryptAndEncodeURL", e);
    }

    return url;
  }

  // finds URLs we embed in data (e.g. magic email links and transferred doc review links).
  // Uses patterns to find links, then uses our standard technique to encrypt the embedded link for output
  // Current patterns:
  //   - href="javascript:popWindow('<URL>',     ==> used for: 1) view review details link added to AuditHistory "notes" for transferred documents
  public static String encryptEmbeddedURLs(String origStr, String salt)
  {
    String convertedString = origStr;
    for (int i=0; i < embeddedURLPatternList.size(); i++)
    {
      // go thru each pattern...
      convertedString = encryptEmbeddedURLsForPattern(convertedString, embeddedURLPatternList.get(i), salt);
    }
    return convertedString;
  }
  
  private static String encryptEmbeddedURLsForPattern(String origStr, Pattern pattern, String salt)
  {
    Matcher matcher = pattern.matcher(origStr);
    StringBuffer convertedBuf = new StringBuffer();
    boolean didMatchFl = false;
    
    while (matcher.find())
    {
      boolean doEncryptFl = true;
      if (embeddedURLPatternExcludeList != null && embeddedURLPatternExcludeList.size() > 0)
      {
        String matchStr = matcher.group();
        for (Pattern nextExcludedURLPattern : embeddedURLPatternExcludeList)
        {
          Matcher excludedURLmatcher = nextExcludedURLPattern.matcher(matchStr);
          if (excludedURLmatcher.find())
          {
            doEncryptFl = false;
            break;
          }
        }
      }
      
      if (doEncryptFl)
      {
        didMatchFl = true;
        String url = matcher.group(1);
        String wholeMatch = matcher.group();
        int charsRemainingAfterUrl = matcher.end(0) - matcher.end(1);
        String encryptedURL = EncryptionUtility.encryptURL(url, salt);
        int matchedCharsBeforeURL = matcher.start(1) - matcher.start(0);
      
        // Build new string for entire match - replacing the raw url with its encrypted version
        String newValue = wholeMatch.substring(0, matchedCharsBeforeURL) + encryptedURL;
        if (charsRemainingAfterUrl > 0)
          newValue += wholeMatch.substring(wholeMatch.length() - charsRemainingAfterUrl);
        
        matcher.appendReplacement(convertedBuf, Matcher.quoteReplacement(newValue));
      }
      else
      {
        matcher.appendReplacement(convertedBuf, Matcher.quoteReplacement(matcher.group()));
      }
    }
    
    if (didMatchFl)
    {
      matcher.appendTail(convertedBuf);
      
      return convertedBuf.toString();
    }
    
    // We did not match anything. Return our input string unaltered.
    return origStr; 
  }
  
  public static String generateSalt(String value)
  {
    if ( value == null || value.length() < 2) return value;
    
    char[] valueArray = value.toCharArray();
    char[] salt = new char[valueArray.length];
     
    for ( int i = 0; i < valueArray.length; i++ )
    {
        salt[i] = (char) ((int)valueArray[i] + (int)valueArray[1]);
    }
    
    return new String(salt);
  }
}
