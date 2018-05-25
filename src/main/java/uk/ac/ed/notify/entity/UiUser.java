package uk.ac.ed.notify.entity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by rgood on 24/10/15.
 */
@Entity
@Table(name="NOTIFICATION_UI_USERS")
public class UiUser {

    @Id
    @Column(name = "UUN")
    private String uun;

    @Column(name = "ORG_GROUP_DN")
    private String orgGroupDN;        

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name="NOTIFICATION_UI_USER_ROLES", joinColumns = {@JoinColumn (name="UUN")},
            inverseJoinColumns = {@JoinColumn(name="ROLE_CODE",nullable = false,updatable = false)})
    private Collection<UiRole> uiRoles;

    public String getUun() {
        return uun;
    }

    public void setUun(String uun) {
        this.uun = uun;
    }

    public Collection<UiRole> getUiRoles() {
        return uiRoles;
    }

    public void setUiRoles(Collection<UiRole> uiRoles) {
        this.uiRoles = uiRoles;
    }

    public String getOrgGroupDN() {
        return orgGroupDN;
    }

    public void setOrgGroupDN(String orgGroupDN) {
        this.orgGroupDN = orgGroupDN;
    }    
}
