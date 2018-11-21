package uk.ac.ed.notify.service;

import uk.ac.ed.notify.entity.LdapGroup;

import java.util.List;

public interface GroupService {

    /**
     * Get a group's name
     *
     * @param base
     * @return name of group
     */
    String getGroupName(String base);


    /**
     * Get a group's members
     *
     * @param base
     * @return a list of member uun
     */
    List<String> getMembers(String base);

    /**
     * Obtain the child groups of the specified group.
     *
     * @param base
     * @return a list of groups
     */
    List<LdapGroup> getNextLevelGroups(String base);

    List<String> getMembersFromParentGroup(String base);

}
