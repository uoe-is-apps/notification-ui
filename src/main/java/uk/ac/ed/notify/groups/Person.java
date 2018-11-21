package uk.ac.ed.notify.groups;

import java.io.Serializable;

class Person implements Serializable {

	private static final long serialVersionUID = -2723409478365865769L;

	private String fullName;

	private String surname;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

}
