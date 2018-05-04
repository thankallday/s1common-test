package com.scholarone.xml;

// java imports
// javax imports
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.scholarone.file.S3File;

/**
 * Application that takes a valueobject XML descriptor file, and generates a stubbed-out Java source file for it, using
 * XSL
 */
public class VOGen
{

  public VOGen()
  {
  }

  public static void main(String[] args)
  {
    TransformerFactory factory = TransformerFactory.newInstance();

    S3File styleSheet = new S3File(args[0]);
    S3File inputFile = new S3File(args[1]);
    S3File outputFile;

    String inFile = inputFile.toString();

    outputFile = new S3File(inFile.substring(0, inFile.indexOf(".xml")) + ".java");

    try
    {
      // BufferedOutputStream outStream = new BufferedOutputStream(new
      // S3FileOutputStream(outputFile));

      StreamSource styleSrc = new StreamSource(styleSheet);
      StreamSource inputSrc = new StreamSource(inputFile);
      StreamResult result = new StreamResult(System.out);

      Transformer transformer = factory.newTransformer(styleSrc);

      transformer.transform(inputSrc, result);
    }
    catch (TransformerException te)
    {
    }
  }

}

/*---  ---*/
