package de.tum.in.ase.eist2020;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Course {

	public Course() {
		title = "No title";
		description = "A normal course";
		examDate = LocalDate.now();
		lectures = new ArrayList<Lecture>();
	}

	public Course(String _title) {
		title = _title;
		description = "A normal course";
		examDate = LocalDate.now();
		lectures = new ArrayList<Lecture>();
	}

	public String title;
	public String description;
	public LocalDate examDate;
	public List<Lecture> lectures;

	public abstract Course join();

	public abstract Course drop();
}
