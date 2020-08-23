package de.tum.in.ase.eist2020;

public class Instructor {
	
	public String teach() {
		Lecture lecture = new Lecture();
		return lecture.hold();
	}
}
