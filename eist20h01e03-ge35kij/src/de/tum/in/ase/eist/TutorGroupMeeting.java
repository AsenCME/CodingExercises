package de.tum.in.ase.eist;

public abstract class TutorGroupMeeting {
	private FixedDateTimeSlot timeSlot;
	private Skill skillToPractice;
	private TutorGroup tutorGroup;

	public TutorGroupMeeting(FixedDateTimeSlot _slot, TutorGroup _group, Skill _skill) {
		timeSlot = _slot;
		skillToPractice = _skill;
		tutorGroup = _group;

		_group.addMeeting(this);
	}

	public abstract void practice();

	/**
	 * @return the timeSlot
	 */
	public FixedDateTimeSlot getTimeSlot() {
		return timeSlot;
	}

	/**
	 * @return the skillToPractice
	 */
	public Skill getSkillToPractice() {
		return skillToPractice;
	}

	/**
	 * @return the tutorGroup
	 */
	public TutorGroup getTutorGroup() {
		return tutorGroup;
	}
}
