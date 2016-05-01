package by.epam.xmlshedule;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import by.epam.beans.Aircraft;
import by.epam.beans.Flight;
import by.epam.beans.OriginDestination;

public class XmlUtils {

	private static final SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
	
	public static List<Flight> readScheduleFromFile(String fileName) {
		List<Flight> flights = new ArrayList<>();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			// create DOM-analyzer
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			// parse XML-document and create a tree-like structure
			Document doc = docBuilder.parse(fileName);
			Element root = doc.getDocumentElement();
			// get child elements
			NodeList nList = root.getElementsByTagName(Constants.ELEM_FLIGHT);
			
			for (int i = 0; i < nList.getLength(); i++) {
				Element flightEl = (Element) nList.item(i);
				Flight flight = buildFlight(flightEl);
				flights.add(flight);
			}
			
		} catch (ParserConfigurationException e) {
			System.err.println("Parser configuration error: " + e);
			
		} catch (SAXException e) {
			System.err.println("Parsing failure: " + e);
			
		} catch (IOException e) {
			System.err.println("File or I/O error: " + e);
		}
		
		return flights;
	}
	
	private static Flight buildFlight(Element flightEl) {
		Flight flight = new Flight();
		OriginDestination od = new OriginDestination();
		Aircraft aircraft = new Aircraft();
		
		// fill in object originDestination
		Element departureEl = (Element) flightEl.getElementsByTagName(Constants.ELEM_DEPARTURE).item(0);
		od.setDepartureCity(getElementTextContent(departureEl, Constants.ELEM_CITY));
		od.setDepartureDate(getElementTextContent(departureEl, Constants.ELEM_DATE));
		
		Element destinationEl = (Element) flightEl.getElementsByTagName(Constants.ELEM_DESTINATION).item(0);
		od.setArrivalCity(getElementTextContent(destinationEl, Constants.ELEM_CITY));
		od.setArrivalDate(getElementTextContent(destinationEl, Constants.ELEM_DATE));
		
		// fill in object aircraft
		Element aircraftEl = (Element) flightEl.getElementsByTagName(Constants.ELEM_AIRCRAFT).item(0);
		int aircraftNumber = Integer.parseInt(aircraftEl.getAttribute(Constants.ATTR_AIRCRAFT_NUMBER));
		aircraft.setNumber(aircraftNumber);
		aircraft.setType(aircraftEl.getAttribute(Constants.ATTR_AIRCRAFT_TYPE));
		
		// fill in object flight
		int flightNumber = Integer.parseInt(flightEl.getAttribute(Constants.ATTR_FLIGHT_NUMBER));
		flight.setNumber(flightNumber);
		flight.setOriginDestination(od);
		flight.setAircraft(aircraft);
		int gate = Integer.parseInt(getElementTextContent(flightEl, Constants.ELEM_GATE));
		flight.setGate(gate);
		
		return flight;
	}
	
	private static String getElementTextContent(Element element, String childName) {
		
		Node node = element.getElementsByTagName(childName).item(0);
		return node.getTextContent();
	}
	
	public static void exportScheduleToXML(String fileName, List<Flight> flights) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			// create DOM-analyzer
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement(Constants.ELEM_ROOT);
			doc.appendChild(root);
			
			for (Flight flight : flights) {
				Element flightEl = doc.createElement(Constants.ELEM_FLIGHT);
				populateFlightElement(flightEl, doc, flight);
				
				Element departureEl = doc.createElement(Constants.ELEM_DEPARTURE);
				populateDepartureElement(departureEl, doc, flight);
				
				Element destinationEl = doc.createElement(Constants.ELEM_DESTINATION);
				populateDestinationElement(destinationEl, doc, flight);
				
				Element gateEl = doc.createElement(Constants.ELEM_GATE);
				populateGateElement(gateEl, doc, flight);
				
				Element aircraftEl = doc.createElement(Constants.ELEM_AIRCRAFT);
				populateAircraftElement(aircraftEl, doc, flight);
				
				root.appendChild(flightEl);
				flightEl.appendChild(departureEl);
				flightEl.appendChild(destinationEl);
				flightEl.appendChild(gateEl);
				flightEl.appendChild(aircraftEl);
			}
			// write the content into XML-document
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			
			StreamResult result = new StreamResult(new File(fileName));
			transformer.transform(source, result);
			
		} catch (ParserConfigurationException e) {
			System.err.println("Parser configuration error: " + e);
			
		} catch (TransformerConfigurationException e) {
			System.err.println("Transformer configuration error: " + e);
			
		} catch (TransformerException e) {
			System.err.println("Transformation process failure: " + e);
		}
	}
	
	private static void populateFlightElement(Element flightEl, Document doc, Flight flight) {
		String flightNumber = Integer.toString(flight.getNumber());
		// set attribute
		Attr attr = doc.createAttribute(Constants.ATTR_FLIGHT_NUMBER);
		attr.setValue(flightNumber);
		flightEl.setAttributeNode(attr);
	}
	
	private static void populateDepartureElement(Element departureEl, Document doc, Flight flight) {
		OriginDestination od = flight.getOriginDestination();
		String city = od.getDepartureCity();
		Date date = od.getDepartureDate();
		
		departureEl.appendChild(createCityElement(city, doc));
		departureEl.appendChild(createDateElement(date, doc));
	}
	
	private static void populateDestinationElement(Element destinationEl, Document doc, Flight flight) {
		OriginDestination od = flight.getOriginDestination();
		String city = od.getArrivalCity();
		Date date = od.getArrivalDate();
		
		destinationEl.appendChild(createCityElement(city, doc));
		destinationEl.appendChild(createDateElement(date, doc));
	}
	
	private static void populateGateElement(Element gateEl, Document doc, Flight flight) {
		String gate = Integer.toString(flight.getGate());
		
		gateEl.appendChild(doc.createTextNode(gate));
	}
	
	private static void populateAircraftElement(Element aircraftEl, Document doc, Flight flight) {
		Aircraft aircraft = flight.getAircraft();
		String aircraftNumber = Integer.toString(aircraft.getNumber());
		String aircraftType = aircraft.getType();
		// set attributes
		Attr attr = doc.createAttribute(Constants.ATTR_AIRCRAFT_NUMBER);
		attr.setValue(aircraftNumber);
		aircraftEl.setAttributeNode(attr);
		
		attr = doc.createAttribute(Constants.ATTR_AIRCRAFT_TYPE);
		attr.setValue(aircraftType);
		aircraftEl.setAttributeNode(attr);
	}
	
	private static Element createCityElement(String name, Document doc) {
		Element cityEl = doc.createElement(Constants.ELEM_CITY);
		cityEl.appendChild(doc.createTextNode(name));
		return cityEl;
	}
	
	private static Element createDateElement(Date date, Document doc) {
		String strDate = sd.format(date);
		
		Element dateEl = doc.createElement(Constants.ELEM_DATE);
		dateEl.appendChild(doc.createTextNode(strDate));
		return dateEl;
	}

}
