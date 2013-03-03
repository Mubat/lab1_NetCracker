package Sergi.MVC.Model.parse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Sergi.MVC.Model.Task;

public class XMLreadWrite {
	private static Document document;
	private static Task newTask;
	private static ArrayList<Task> arrList;
	private static DateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");

	public XMLreadWrite() {
		newTask = new Task();
		arrList = new ArrayList<Task>();
	}

	/**
	 * Method, which recursively examines the elements XML document and stores
	 * the final document flow.
	 * 
	 * @param node
	 *            variable of type Node which is analyzed.
	 * @throws ParseException 
	 * @throws DOMException 
	 */
	public void analyze(Node node) throws DOMException, ParseException {
		switch (node.getNodeType()) {
		case Node.DOCUMENT_NODE: {
			document = (Document) node;
			analyze(document.getDocumentElement());
			break;
		}
		case Node.ELEMENT_NODE: {

			if (node.hasChildNodes()) {
				NodeList children = node.getChildNodes();
				if (node.getNodeName().equals("task")) {
					if (newTask != null) {
						newTask = new Task();
						if(!arrList.contains(newTask))
								arrList.add(newTask);
					}
					NamedNodeMap attrs = node.getAttributes();
					for (int i = 0; i < attrs.getLength(); i++)
						analyze(attrs.item(i));

				}
				for (int i = 0; i < children.getLength(); i++) {
					analyze(children.item(i));
				}
			}

			break;
		} // close switch for ELEMENT_NODE
		case Node.ATTRIBUTE_NODE: {
			if ("name".equals(node.getNodeName()))
				newTask.setTitle(node.getNodeValue());
			break;
		} // close switch for ATTRIBUTE_NODE
		case Node.TEXT_NODE: {
			String nodeName = node.getParentNode().getNodeName();
			if("from".equals(nodeName))
				newTask.setStartTime(getDate(node.getNodeValue()));
			else if ("to".equals(nodeName))
				newTask.setEndTime(getDate(node.getNodeValue()));
			else if("repeats".equals(nodeName))
				newTask.setRepeatCount(Integer.parseInt(node.getNodeValue()));
			else if("visibility".equals(nodeName))
				newTask.setActive(Boolean.parseBoolean(node.getNodeValue()));
		}// close switch for type of TEXT_NODE
		}// close switch for type of Node
	}// close switch for method

	// -----------------------------------------------------------------------------
	private static Date getDate(String dateString) throws ParseException {
			return sdf.parse(dateString);
	}

	public Task[] getTaskArray() {
		Task[] massTask = new Task[arrList.size()];
		for (int i = 0; i < massTask.length; i++) {
			massTask[i] = arrList.get(i);
		}
		return massTask;
	}

	public Document createDocument(Task[] taskArray)
			throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		db.setErrorHandler(new MyErrHandler());
		Document doc = db.newDocument();

		Element taskListElement = doc.createElement("taskList");
		doc.appendChild(taskListElement);

		for (int i = 0; i < taskArray.length; i++) {
			Element task = doc.createElement("task");
			taskListElement.appendChild(task);

			task.setAttribute("name", taskArray[i].getTitle());

			Element date = doc.createElement("date");
			task.appendChild(date);

			Element fromDate = doc.createElement("from");
			fromDate.appendChild(doc.createTextNode(
					sdf.format(taskArray[i].getStartTime())));
			date.appendChild(fromDate);
			
			if(taskArray[i].isRepeated()) {
				Element toDate = doc.createElement("to");
				toDate.appendChild(doc.createTextNode(
						sdf.format(taskArray[i].getEndTime())));
				date.appendChild(toDate);
			}
			
			Element repeats = doc.createElement("repeats");
			repeats.appendChild(doc.createTextNode(
					Integer.toString(taskArray[i].getRepeatCount())));
			task.appendChild(repeats);
			
			Element visibility = doc.createElement("visibility");
			visibility.appendChild(doc.createTextNode(
					Boolean.toString(taskArray[i].isActive())));
			task.appendChild(visibility);
				
		}

		return doc;
	}

	/**
	 * 
	 * @param fileName string of file name, where xml.
	 * @return array of parsed Tasks
	 * @throws DOMException
	 * @throws ParseException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public Task[] parseXMLFile(String fileName) 
			throws DOMException, ParseException, SAXException, 
			IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(fileName);
		if (doc != null) {
			analyze(doc);
		}
		return getTaskArray();
		
	}
}