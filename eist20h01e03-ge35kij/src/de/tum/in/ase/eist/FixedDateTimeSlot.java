package de.tum.in.ase.eist;

import java.time.LocalDate;
import java.time.LocalTime;

public class FixedDateTimeSlot extends TimeSlot {
	private LocalDate date;

	public FixedDateTimeSlot(LocalDate _date, LocalTime _start, LocalTime _end) {
		super(_start, _end);
		date = _date;
	}

	/**
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}
}
