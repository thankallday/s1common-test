package com.scholarone.xml;

// java imports
// javax imports
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.scholarone.file.S3File;

/**
 *  
 */

public class XmlTransformApp
{

	public XmlTransformApp()
	{
	}

	public static void main(String[] args)
	{
		TransformerFactory factory = TransformerFactory.newInstance();

		S3File styleSheet = new S3File(args[0]);
		S3File inputFile = new S3File(args[1]);
		S3File outputFile = new S3File(args[2]);

		StreamSource styleSrc = new StreamSource(styleSheet);
		StreamSource inputSrc = new StreamSource(inputFile);
		StreamResult result = new StreamResult(outputFile);

		try
		{
			Transformer transformer = factory.newTransformer(styleSrc);

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(inputSrc, result);
		}
		catch (TransformerException te)
		{
			//log.debug("Error = " + te.toString());
		}
	}

}

/*---  ---*/
