package de.tum.in.ase.eist;

import java.util.HashSet;
import java.util.Set;

public class Student extends Person {
	private String matriculationNumber;
	private int semester;
	private StudyLevel studyLevel;
	private Set<String> knowledge;
	private Set<Skill> skills;

	public Student(String _name, int _age, String _tumId, String _matrNr, StudyLevel _level, int _sem) {
		super(_name, _age, _tumId);
		matriculationNumber = _matrNr;
		semester = _sem;
		studyLevel = _level;
		knowledge = new HashSet<String>();
		skills = new HashSet<Skill>();
	}

	public void learnSkill(Skill skill) {
		skills.add(skill);
	}

	public void acquireKnowledge(String newKnowledge) {
		knowledge.add(newKnowledge);
	}

	/**
	 * @return the matriculationNumber
	 */
	public String getMatriculationNumber() {
		return matriculationNumber;
	}

	/**
	 * @return the semester
	 */
	public int getSemester() {
		return semester;
	}

	/**
	 * @return the studyLevel
	 */
	public StudyLevel getStudyLevel() {
		return studyLevel;
	}

	/**
	 * @return the knowledge
	 */
	public Set<String> getKnowledge() {
		return knowledge;
	}

	/**
	 * @return the skills
	 */
	public Set<Skill> getSkills() {
		return skills;
	}
}
