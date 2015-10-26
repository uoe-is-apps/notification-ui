package uk.ac.ed.notify.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by rgood on 24/10/15.
 */
@Embeddable
public class UiUserRoleId implements Serializable {

    @Column(name="UUN")
    private String uun;

    @Column(name="ROLE_CODE")
    private String roleCode;

    public String getUun() {
        return uun;
    }

    public void setUun(String uun) {
        this.uun = uun;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}
