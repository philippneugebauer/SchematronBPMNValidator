package de.uniba.dsg.ppn.ba.validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ch.qos.logback.classic.Logger;
import de.uniba.dsg.bpmn.ValidationResult;
import de.uniba.dsg.bpmn.Violation;

/**
 * 
 * Does the xsd validation step
 * 
 * @author Andreas Vorndran, Philipp Neugebauer
 * @version 1.0
 * 
 */
public class XsdValidator {

	private SchemaFactory schemaFactory;
	private Schema schema;
	private Logger logger;

	{
		schemaFactory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		schemaFactory.setResourceResolver(new ResourceResolver());
		logger = (Logger) LoggerFactory.getLogger(getClass().getSimpleName());
		try {
			schema = schemaFactory.newSchema(new Source[] {
					resolveResourcePaths("DC.xsd"),
					resolveResourcePaths("DI.xsd"),
					resolveResourcePaths("BPMNDI.xsd"),
					resolveResourcePaths("BPMN20.xsd") });
		} catch (FileNotFoundException | SAXException e) {
			logger.error("schemafactory couldn't create schema, cause: {}", e);
		}
	}

	/**
	 * Validates the given xmlFile with the xsd bpmn files and writes violations
	 * to the given validation result
	 * 
	 * @param xmlFile
	 * @param validationResult
	 * @throws IOException
	 *             when xmlFile can't be read
	 * @throws SAXException
	 *             when validation process fails somehow
	 */
	public void validateAgainstXsd(File xmlFile,
			ValidationResult validationResult) throws IOException, SAXException {
		logger.info("xsd validation started: {}", xmlFile.getName());
		List<SAXParseException> xsdErrorList = new ArrayList<>();
		Validator validator = schema.newValidator();
		validator.setErrorHandler(new XsdValidationErrorHandler(xsdErrorList));
		validator.validate(new StreamSource(xmlFile));
		for (SAXParseException saxParseException : xsdErrorList) {
			validationResult.getViolations().add(
					new Violation("XSD-Check", xmlFile.getName(),
							saxParseException.getLineNumber(), "",
							saxParseException.getMessage()));
			logger.info("xsd violation in {} at {} found", xmlFile.getName(),
					saxParseException.getLineNumber());
		}
	}

	/**
	 * 
	 * The method simplifies the search for a resource and returns the
	 * streamsource with the searched source
	 * 
	 * @param resourceName
	 * @return the streamsource of the file, which needed to be load
	 * @throws FileNotFoundException
	 *             if the resource file doesn't exist there
	 */
	private StreamSource resolveResourcePaths(String resourceName)
			throws FileNotFoundException {
		return new StreamSource(this.getClass().getResourceAsStream(
				"/" + resourceName));
	}
}