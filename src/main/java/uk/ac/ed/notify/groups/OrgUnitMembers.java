package uk.ac.ed.notify.groups;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrgUnitMembers implements Serializable {

	private static final long serialVersionUID = -3596643333898462959L;

	private String orgunit;

	private List<Member> members = new ArrayList<>();

	public String getOrgunit() {
		return orgunit;
	}

	public void setOrgunit(String orgunit) {
		this.orgunit = orgunit;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

}
