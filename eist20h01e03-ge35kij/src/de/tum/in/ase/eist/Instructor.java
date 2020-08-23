package de.tum.in.ase.eist;

public class Instructor extends Person {
	public Instructor(String _name, int _age, String _tumId) {
		super(_name, _age, _tumId);
	}

	public void teach(String teaching) {
		String saying = String.format("Please learn \"%s\"", teaching);
		say(saying);
	}
}
