import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ed.notify.entity.UiRole;
import uk.ac.ed.notify.entity.UiUser;
import uk.ac.ed.notify.entity.UiUserRole;
import uk.ac.ed.notify.entity.UiUserRoleId;
import uk.ac.ed.notify.repository.UiRoleRepository;
import uk.ac.ed.notify.repository.UiUserRepository;
import uk.ac.ed.notify.repository.UiUserRoleRepository;

import static org.junit.Assert.assertEquals;

/**
 * Created by rgood on 24/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
public class UiUserRepositoryTest {

    @Autowired
    UiUserRepository uiUserRepository;

    @Autowired
    UiUserRoleRepository uiUserRoleRepository;

    @Autowired
    UiRoleRepository uiRoleRepository;

    @Before
    public void setup()
    {
        UiRole uiRole = new UiRole();
        uiRole.setRoleCode("TESTROLE");
        uiRole.setRoleDescription("A Test Role");
        uiRoleRepository.save(uiRole);
    }

    @After
    public void cleanup() {
        uiUserRepository.deleteAll();
        uiRoleRepository.deleteAll();
        uiUserRoleRepository.deleteAll();
    }

    @Test
    public void testCreateUser() {


        UiUser user = new UiUser();
        user.setUun("testuser");
        uiUserRepository.save(user);
        UiUserRole uiUserRole = new UiUserRole();
        UiUserRoleId uiUserRoleId = new UiUserRoleId();
        uiUserRoleId.setUun("testuser");
        uiUserRoleId.setRoleCode("TESTROLE");
        uiUserRole.setUiUserRoleId(uiUserRoleId);
        uiUserRoleRepository.save(uiUserRole);

    }

    @Test
    public void testGetUser() {

        UiUser user = new UiUser();
        user.setUun("testuser");
        uiUserRepository.save(user);
        UiUserRole uiUserRole = new UiUserRole();
        UiUserRoleId uiUserRoleId = new UiUserRoleId();
        uiUserRoleId.setUun("testuser");
        uiUserRoleId.setRoleCode("TESTROLE");
        uiUserRole.setUiUserRoleId(uiUserRoleId);
        uiUserRoleRepository.save(uiUserRole);
        user = uiUserRepository.findOne("testuser");
        assertEquals("testuser",user.getUun());
        assertEquals("A Test Role",user.getUiRoles().iterator().next().getRoleDescription());

    }

    @Test
    public void testDeleteUser()
    {

    }
}
