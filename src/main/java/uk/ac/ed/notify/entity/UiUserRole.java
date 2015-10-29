package uk.ac.ed.notify.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by rgood on 24/10/15.
 */
@Entity
@Table(name="NOTIFICATION_UI_USER_ROLES",schema = "NOTIFY")
public class UiUserRole {

    @EmbeddedId
    UiUserRoleId uiUserRoleId;

    public UiUserRoleId getUiUserRoleId() {
        return uiUserRoleId;
    }

    public void setUiUserRoleId(UiUserRoleId uiUserRoleId) {
        this.uiUserRoleId = uiUserRoleId;
    }
}
