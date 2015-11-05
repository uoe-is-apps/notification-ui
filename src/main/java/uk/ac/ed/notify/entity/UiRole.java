package uk.ac.ed.notify.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by rgood on 24/10/15.
 */
@Entity
@Table(name="NOTIFICATION_UI_ROLES", schema="NOTIFY")
public class UiRole {

    @Id
    @Column(name="ROLE_CODE")
    private String roleCode;

    @Column(name="ROLE_DESCRIPTION")
    private String roleDescription;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
}
