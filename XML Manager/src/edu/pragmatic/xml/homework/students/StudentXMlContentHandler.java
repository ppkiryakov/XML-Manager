package edu.pragmatic.xml.homework.students;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

class StudentXMlContentHandler implements ContentHandler {

		public static final String TEACHER_TAG = "teacher";
		public static final String STUDENT_TAG = "student";
		public static final String NAME_ATTRIBUTE = "name";
		public static final String SCHOOL_TAG = "school";
	
		private List<String> studentNames;
		private List<String> teacherNames;
		private String schoolName;
		
		public StudentXMlContentHandler() {
			studentNames = new ArrayList<>();
			teacherNames = new ArrayList<>();
		}
		
		public void setDocumentLocator(Locator locator) {}
		public void startDocument() throws SAXException {}
		public void endDocument() throws SAXException {}
		public void startPrefixMapping(String prefix, String uri) throws SAXException {}
		public void endPrefixMapping(String prefix) throws SAXException {}
		public void endElement(String uri, String localName, String qName) throws SAXException {}
		public void characters(char[] ch, int start, int length) throws SAXException {}
		public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
		public void processingInstruction(String target, String data) throws SAXException {}
		public void skippedEntity(String name) throws SAXException {}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			addStudentNames(qName, attributes);
			addTeacherNames(qName, attributes);
			setSchoolName(qName, attributes);
		}

		private void setSchoolName(String qName, Attributes attributes) {
			if (SCHOOL_TAG.equals(qName)){
				String name = readName(attributes);
				if (name != null){
					this.schoolName = name;
				} 
			}
		}

		private void addTeacherNames(String qName, Attributes attributes) {
			if ((TEACHER_TAG).equals(qName)){
				String teacherName = readName(attributes);
				if (teacherName != null){
					this.teacherNames.add(teacherName);
				}
			}
		}

		private void addStudentNames(String qName, Attributes attributes) {
			if (STUDENT_TAG.equals(qName)){
				String studentName = readName(attributes);
				if (studentName != null){
					this.studentNames.add(studentName);
				}
			}
		}

		private String readName(Attributes atts) {
			int nameIndex = atts.getIndex(NAME_ATTRIBUTE);
			String studentName = atts.getValue(nameIndex);
			return studentName;
		}

		
		public List<String> getStudentNames() {
			return studentNames;
		}
		
		public List<String> getTeacherNames() {
			return teacherNames;
		}
		
		public String getSchoolName() {
			return schoolName;
		}
		
	}