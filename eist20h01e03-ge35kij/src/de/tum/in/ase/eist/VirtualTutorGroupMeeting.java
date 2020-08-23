package de.tum.in.ase.eist;

public class VirtualTutorGroupMeeting extends TutorGroupMeeting {
	private String url;

	public VirtualTutorGroupMeeting(FixedDateTimeSlot _slot, TutorGroup _group, Skill _skill, String _url) {
		super(_slot, _group, _skill);
		url = _url;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	@Override
	public void practice() {
		String saying = String.format("Thank you for joining using %s today.", this.url);
		this.getTutorGroup().getTutor().say(saying);
		this.getTutorGroup().getStudents().forEach(s -> s.learnSkill(this.getSkillToPractice()));
		this.getTutorGroup().getTutor().say("See you next time!");
	}
}
