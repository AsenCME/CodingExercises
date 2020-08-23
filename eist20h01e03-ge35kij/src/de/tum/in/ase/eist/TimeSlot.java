package de.tum.in.ase.eist;

import java.time.Duration;
import java.time.LocalTime;

public abstract class TimeSlot {
	private LocalTime startTime;
	private LocalTime endTime;

	public TimeSlot(LocalTime _startTime, LocalTime _endTime) {
		startTime = _startTime;
		endTime = _endTime;
	}

	public Duration getDuration() {
		return Duration.between(startTime, endTime);
	}

	/**
	 * @return the startTime
	 */
	public LocalTime getStartTime() {
		return startTime;
	}

	/**
	 * @return the endTime
	 */
	public LocalTime getEndTime() {
		return endTime;
	}
}
