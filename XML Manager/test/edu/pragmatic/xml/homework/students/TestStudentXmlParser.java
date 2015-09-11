package edu.pragmatic.xml.homework.students;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.pragmatic.xml.homework.students.model.ObjectFactory;
import edu.pragmatic.xml.homework.students.model.School.Exams.Exam;
import edu.pragmatic.xml.homework.students.model.School.Exams.Exam.Students;


public class TestStudentXmlParser {

	private StudentXmlParser parser;
	private File studentsXml;
	private File studentsXmlUpdated;
	private List<String> expectedStudentNames;
	private List<String> expectedTeacherNames;
	
	@Before
	public void setUp(){
		parser = new StudentXmlParser();
		studentsXml = Paths.get("resources/students.xml").toFile();
		studentsXmlUpdated = Paths.get("resources/students_updated.xml").toFile();
		expectedStudentNames = Arrays.asList("John","Anny","Peter","Alex","Christina","Steve");
		expectedTeacherNames = Arrays.asList("John","Anny","Stan");
	}
	
	private void assertStudentXmlParserIsBuild(){
		Assert.assertNotNull(parser);
	}
	
	@Test
	public void listAllStudentNamesWithSAX() throws SAXException, IOException{
		assertStudentXmlParserIsBuild();
		List<String> studentNames = parser.getStudentNamesWithSax(studentsXml);
		assertNamesListExistsAndNotEmpty(studentNames);
		assertNames(studentNames, expectedStudentNames);
	}

	private void assertNamesListExistsAndNotEmpty( List<String> names) {
		Assert.assertNotNull(names);
		Assert.assertFalse("No elements were found. List shouldn't be empty" , names.isEmpty());
	}

	private void assertNames(List<String> studentNames, List<String> expected) {
		for (String name : studentNames){
			Assert.assertTrue(expected.contains(name));
		}
	}
	
	@Test
	public void listAllStudentNamesWithDOM() throws SAXException, IOException, ParserConfigurationException{
		List<String> studentNames = parser.getStudentNamesWithDom(studentsXml);
		assertNamesListExistsAndNotEmpty(studentNames);
		assertNames(studentNames, expectedStudentNames);
	}
	
	@Test
	public void listAllTeacherNamesWithSax() throws FileNotFoundException, SAXException, IOException{
		List<String> teacherNames = parser.getTeacheNamesWithSax(studentsXml);
		assertNamesListExistsAndNotEmpty(teacherNames);
		assertNames(teacherNames, expectedTeacherNames);
	}

	@Test
	public void listAllTeacherNamesWithDom() throws ParserConfigurationException, SAXException, IOException{
		List<String> teacherNames = parser.getTeacheNamesWithDom(studentsXml);
		assertNamesListExistsAndNotEmpty(teacherNames);
		assertNames(teacherNames, expectedTeacherNames);
	}
	
	@Test
	public void readSchoolNameWithSax() throws FileNotFoundException, SAXException, IOException{
		String schoolName = parser.getSchoolNameWithSax(studentsXml);
		Assert.assertNotNull(schoolName);
		Assert.assertEquals("High School For the extremly insane", schoolName);
	}
	
	@Test
	public void readSchoolNameWithDom() throws ParserConfigurationException, SAXException, IOException{
		String schoolName = parser.getSchoolNameWithDOM(studentsXml);
		Assert.assertNotNull(schoolName);
		Assert.assertEquals("High School For the extremly insane", schoolName);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void addNewExamWithSax(){
		parser.addNewExamWithSax(studentsXml);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void addNewExamWithDom(){
		parser.addNewExamWithDOM(studentsXml);
	}
	
	@Test
	public void addNewExamWithJAXB(){
		ObjectFactory factory = new ObjectFactory();
		Exam newExam = factory.createExam();
		Students newStudents = factory.createSchoolExamsExamStudents();
		for (byte id : new byte[]{3, 5, 6, 8 , 11, 4})
			newStudents.getId().add(id);
		newExam.setStudents(newStudents);
		newExam.setTeacher((byte) 3);
		
		parser.addNewExamWithJAXB(studentsXml, studentsXmlUpdated, newExam);
	}
}
