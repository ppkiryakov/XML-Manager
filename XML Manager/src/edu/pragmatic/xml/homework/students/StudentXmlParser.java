package edu.pragmatic.xml.homework.students;

import static edu.pragmatic.xml.homework.students.StudentXMlContentHandler.NAME_ATTRIBUTE;
import static edu.pragmatic.xml.homework.students.StudentXMlContentHandler.SCHOOL_TAG;
import static edu.pragmatic.xml.homework.students.StudentXMlContentHandler.STUDENT_TAG;
import static edu.pragmatic.xml.homework.students.StudentXMlContentHandler.TEACHER_TAG;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import edu.pragmatic.xml.homework.students.model.School;
import edu.pragmatic.xml.homework.students.model.School.Exams.Exam;

public class StudentXmlParser {
	
	public List<String> getStudentNamesWithSax(File studentsXml) throws SAXException, IOException {
		StudentXMlContentHandler contentHandler = new StudentXMlContentHandler();
		parseFileWithSax(studentsXml, contentHandler);
		return contentHandler.getStudentNames();
	}

	private void parseFileWithSax(File studentsXml, StudentXMlContentHandler contentHandler) throws SAXException, FileNotFoundException, IOException {
		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		xmlReader.setContentHandler(contentHandler);
		InputSource xml = new InputSource();
		xml.setCharacterStream(new FileReader(studentsXml));
		xmlReader.parse(xml);
	}

	public List<String> getStudentNamesWithDom(File studentsXml) throws ParserConfigurationException, SAXException, IOException {
		Document studentXmlDocument = buildStundentXmlDocument(studentsXml);
		NodeList studentTags = getStudentTags(studentXmlDocument);
		return  readNamesFromTags(studentTags);
	}

	private List<String> readNamesFromTags(NodeList tagList) {
		List<String> studentNames = new ArrayList<>();
		for (int i = 0; i < tagList.getLength(); i++){
			Node studentTag = tagList.item(i);
			String name = readNameFromTag(studentTag);
			if (name != null){
				studentNames.add(name);
			}
		}
		return studentNames;
	}

	private String readNameFromTag(Node tag) {
		NamedNodeMap studentTagAttributes = tag.getAttributes();
		Node nameNode = studentTagAttributes.getNamedItem(NAME_ATTRIBUTE);
		if (nameNode != null){
			return nameNode.getNodeValue();
		}
		
		return null;
	}

	private NodeList getStudentTags(Document studentXmlDocument) {
		return  studentXmlDocument.getElementsByTagName(STUDENT_TAG);
	}

	private Document buildStundentXmlDocument(File studentsXml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document studentXmlDocument = documentBuilder.parse(studentsXml);
		return studentXmlDocument;
	}

	public List<String> getTeacheNamesWithSax(File studentsXml) throws FileNotFoundException, SAXException, IOException {
		StudentXMlContentHandler contentHandler = new StudentXMlContentHandler();
		parseFileWithSax(studentsXml, contentHandler);
		return contentHandler.getTeacherNames();
	}

	public List<String> getTeacheNamesWithDom(File studentsXml) throws ParserConfigurationException, SAXException, IOException {
		Document studentXmlDocument = buildStundentXmlDocument(studentsXml);
		NodeList teacherTags = studentXmlDocument.getElementsByTagName(TEACHER_TAG);
		return readNamesFromTags(teacherTags);
	}

	public String getSchoolNameWithSax(File studentsXml) throws FileNotFoundException, SAXException, IOException {
		StudentXMlContentHandler contentHandler = new StudentXMlContentHandler();
		parseFileWithSax(studentsXml, contentHandler);
		return contentHandler.getSchoolName();
	}

	public String getSchoolNameWithDOM(File studentsXml) throws ParserConfigurationException, SAXException, IOException {
		Document studentXmlDocument = buildStundentXmlDocument(studentsXml);
		NodeList schoolTags = studentXmlDocument.getElementsByTagName(SCHOOL_TAG);
		return readNameFromTag(schoolTags.item(0));
	}

	public void addNewExamWithSax(File studentsXml) {
		throw new UnsupportedOperationException("You can't change xml files with a sax parse");
	}

	public void addNewExamWithDOM(File studentsXml) {
		throw new UnsupportedOperationException("You can't create a new xml file with a DOM parser");
	}

	public void addNewExamWithJAXB(File studentsXml, File destinationFile, Exam newExam) {
		School school = JAXB.unmarshal(studentsXml, School.class);
		school.getExams().getExam().add(newExam);
		JAXB.marshal(school, destinationFile);
	}

	
}
