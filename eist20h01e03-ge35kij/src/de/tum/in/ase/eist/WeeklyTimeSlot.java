package de.tum.in.ase.eist;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class WeeklyTimeSlot extends TimeSlot {
	private DayOfWeek dayOfWeek;

	public WeeklyTimeSlot(DayOfWeek _day, LocalTime _start, LocalTime _end) {
		super(_start, _end);
		dayOfWeek = _day;
	}

	/**
	 * @return the dayOfWeek
	 */
	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}
}
