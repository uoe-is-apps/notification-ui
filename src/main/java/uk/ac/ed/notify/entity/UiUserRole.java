package uk.ac.ed.notify.entity;

import javax.persistence.*;

/**
 * Created by rgood on 24/10/15.
 */
@Entity
@Table(name="NOTIFICATION_UI_USER_ROLES",schema = "NOTIFY")
@NamedQueries({
        @NamedQuery(name = "UiUserRole.findByUun", query = "select a from UiUserRole a where a.uiUserRoleId.uun = (?1)")
})
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
