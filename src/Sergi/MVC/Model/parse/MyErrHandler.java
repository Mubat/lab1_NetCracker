package Sergi.MVC.Model.parse;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * implementation of the class ErrorHandler
 * @author Mubat
 *
 */
public class MyErrHandler implements ErrorHandler {

	@Override
	public void warning(SAXParseException exception) throws SAXException {
		System.err.println("Warning: " + exception.getMessage());
	}

	@Override
	public void error(SAXParseException exception) throws SAXException {
		System.err.println("Error. Line " + 
				exception.getLineNumber() + ": " + 
				exception.getMessage());
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		System.err.println("Fatal Error. Line " + 
				exception.getLineNumber() + ": " + 
				exception.getMessage());
		System.exit(0);
	}

}
