package uk.ac.ed.notify.groups;

import java.io.Serializable;

public class Member implements Serializable {

	private static final long serialVersionUID = -339684362913276623L;

	private String uun;

	private String name;

	public String getUun() {
		return uun;
	}

	public void setUun(String uun) {
		this.uun = uun;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
