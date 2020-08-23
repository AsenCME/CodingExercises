package de.tum.in.ase.eist;

import java.util.ArrayList;
import java.util.List;

public class TutorGroup {
	private String id;
	private WeeklyTimeSlot timeSlot;
	private Person tutor;
	private List<Student> students;
	private List<TutorGroupMeeting> meetings;

	public TutorGroup(String _id, WeeklyTimeSlot _time, Person _tutor) {
		id = _id;
		timeSlot = _time;
		tutor = _tutor;
		students = new ArrayList<Student>();
		meetings = new ArrayList<TutorGroupMeeting>();
	}

	void addMeeting(TutorGroupMeeting meeting) {
		meetings.add(meeting);
	}

	public void register(Student student) {
		this.students.add(student);
	};

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the timeSlot
	 */
	public WeeklyTimeSlot getTimeSlot() {
		return timeSlot;
	}

	/**
	 * @return the tutor
	 */
	public Person getTutor() {
		return tutor;
	}

	/**
	 * @return the students
	 */
	public List<Student> getStudents() {
		return students;
	}
}
