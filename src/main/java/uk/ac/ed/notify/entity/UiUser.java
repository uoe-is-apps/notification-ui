package uk.ac.ed.notify.entity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by rgood on 24/10/15.
 */
@Entity
@Table(name="NOTIFICATION_UI_USERS",schema = "NOTIFY")
public class UiUser {

    @Id
    @Column(name = "UUN")
    private String uun;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name="NOTIFICATION_UI_USER_ROLES", joinColumns = {@JoinColumn (name="uun")},
            inverseJoinColumns = {@JoinColumn(name="roleCode",nullable = false,updatable = false)})
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

}
