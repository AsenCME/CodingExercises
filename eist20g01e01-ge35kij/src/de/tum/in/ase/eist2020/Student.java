package de.tum.in.ase.eist2020;

public class Student {

	public String matriculationNumber;
	public String name;
	public int age;

	public Course study() {
		Course course = new OnlineCourse();
		course.title = "EIST";
		course.join();
		return course;
	}
}
