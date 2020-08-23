package de.tum.in.ase.eist;

public abstract class Person {
	private String name;
	private int age;
	private String tumId;

	public Person(String _name, int _age, String _id) {
		name = _name;
		age = _age;
		tumId = _id;
	}

	public void say(String saying) {
		System.out.println(String.format("%s said: %s", name, saying));
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @return the tumId
	 */
	public String getTumId() {
		return tumId;
	}
}
