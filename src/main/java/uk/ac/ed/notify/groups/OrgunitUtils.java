package uk.ac.ed.notify.groups;

import java.util.List;

class OrgunitUtils {

	public static String buildLdapPathByOrgHierarchy(List<OrgHierarchy> orgunitHierarchy, int size,
                                                     String ldapContextBasepath) {

		StringBuilder sb = new StringBuilder();
		if (size > 0) {

			for (int i = size - 1; i >= 0; i--) {
				sb.append("ou=");
				sb.append(orgunitHierarchy.get(i).getCode());
				sb.append(",");
			}
			sb.append(ldapContextBasepath);
		}
		return sb.toString();
	}

}
