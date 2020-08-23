package de.tum.in.ase.eist;

public class LocalTutorGroupMeeting extends TutorGroupMeeting {
	private String room;

	public LocalTutorGroupMeeting(FixedDateTimeSlot _slot, TutorGroup _group, Skill _skill, String _room) {
		super(_slot, _group, _skill);
		room = _room;
	}

	@Override
	public void practice() {
		String saying = String.format("Thank you for coming to %s today.", this.room);
		this.getTutorGroup().getTutor().say(saying);
		this.getTutorGroup().getStudents().forEach(s -> s.learnSkill(this.getSkillToPractice()));
		this.getTutorGroup().getTutor().say("See you next time!");
	}

	/**
	 * @return the room
	 */
	public String getRoom() {
		return room;
	}
}
