package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	// Test Case 1
	@Test
	void testUpdateEven_Verify_Return_Event() throws StudyUpException {
		int eventID = 1;
		Event updatedEvent = eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", updatedEvent.getName());
	}
	
	// Test Case 2
	@Test
	void testUpdateEvent_NameLength_GreaterThanTwenty(){
		int eventID = 1;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		  });
	}
	
	// Test Case 3: BUG FOUND
	@Test
	void testUpdateEvent_NameLength_EqualTwenty(){
		int eventID = 1;
		Assertions.assertDoesNotThrow(() -> {
			eventServiceImpl.updateEventName(eventID, "ABCDEFGHIJKLMNOPQRST");
		  });
	}
	
	// Test Case 4
	@Test
	void testAddStudentToEvent_Verify_DataStorage() throws StudyUpException{
		int eventID = 1;
		//Create Student
		Student student = new Student();
		student.setFirstName("Louie");
		student.setLastName("Shi");
		student.setEmail("LouieShi@email.com");
		student.setId(2);
		
		int oldStudentSize = DataStorage.eventData.get(eventID).getStudents().size();
		
		eventServiceImpl.addStudentToEvent(student, eventID);
		assertEquals(oldStudentSize + 1, DataStorage.eventData.get(eventID).getStudents().size());	
	}
	
	// Test Case 5
	@Test
	void testAddStudentToEvent_Verify_Return_Event() throws StudyUpException{
		int eventID = 1;
		//Create Student
		Student student = new Student();
		student.setFirstName("Louie");
		student.setLastName("Shi");
		student.setEmail("LouieShi@email.com");
		student.setId(2);
		
		int oldStudentSize = DataStorage.eventData.get(eventID).getStudents().size();
		
		Event returnedEvent = eventServiceImpl.addStudentToEvent(student, eventID);
		assertEquals(oldStudentSize + 1, returnedEvent.getStudents().size());		
	}

	// Test Case 6
	@Test
	void testAddStudentToEvent_No_Student_List() throws StudyUpException{
		Event event = new Event();
		event.setEventID(2);
		event.setDate(new Date());
		event.setName("Event 2");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		
		DataStorage.eventData.put(event.getEventID(), event);
		
		//Create Student
		Student student = new Student();
		student.setFirstName("Louie");
		student.setLastName("Shi");
		student.setEmail("LouieShi@email.com");
		student.setId(2);
		
		int eventID = 2;
		eventServiceImpl.addStudentToEvent(student, eventID);
		assertEquals(1, DataStorage.eventData.get(eventID).getStudents().size());			
	}
	
	// Test Case 7
	@Test
	void testAddStudentToEvent_Invalid_Event_ID() {
		int eventID = 100;
		
		//Create Student
		Student student = new Student();
		student.setFirstName("Louie");
		student.setLastName("Shi");
		student.setEmail("LouieShi@email.com");
		student.setId(2);
		
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student, eventID);
		  });
		
	}
	
	// Test Case 8: FOUND BUG
	@Test
	void testAddStudentToEvent_Three_Students_OneEvent() throws StudyUpException{
		int eventID = 1;
		//Create Student
		Student student2 = new Student();
		student2.setFirstName("Louie");
		student2.setLastName("Shi");
		student2.setEmail("LouieShi@email.com");
		student2.setId(2);
		
		//Create Student
		Student student3 = new Student();
		student3.setFirstName("Ali");
		student3.setLastName("Asif");
		student3.setEmail("AliAsif@email.com");
		student3.setId(3);
		
		eventServiceImpl.addStudentToEvent(student2, eventID);
		
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student3, eventID);
		  });
	}
	

	// Test Case 9: FOUND BUG
	@Test
	void testGetActiveEvents_One_Active_Events() {
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
				
		//Create Event2
		Event event = new Event();
		event.setEventID(2);
		event.setDate(new Date(2060, 01, 03));
		event.setName("Event 2");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
				
		DataStorage.eventData.put(event.getEventID(), event);
		
		List<Event> eventList = eventServiceImpl.getActiveEvents();
		assertEquals(1, eventList.size());
	}
	
	// Test Case 10
	@Test
	void testGetPastEvents_One_Past_One_Active_Event() {
		
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event2
		Event event = new Event();
		event.setEventID(2);
		event.setDate(new Date(2060, 01, 03));
		event.setName("Event 2");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
		
		List<Event> eventList = eventServiceImpl.getPastEvents();
		assertEquals(1, eventList.size());
	}
	
	// Test Case 11
	@Test
	void testDeleteEvent_Remove_Last_Event() {
		int eventID = 1;
		eventServiceImpl.deleteEvent(eventID);
		assertEquals(0, DataStorage.eventData.size());
	}
	
	
}
