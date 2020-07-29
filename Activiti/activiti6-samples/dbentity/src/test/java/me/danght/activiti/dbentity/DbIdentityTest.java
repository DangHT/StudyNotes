package me.danght.activiti.dbentity;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * ACT_ID_*
 * @author DangHT
 * @date 2020/07/29
 */
public class DbIdentityTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testIdentity() {
        IdentityService identityService = activitiRule.getIdentityService();
        User user1 = identityService.newUser("user1");
        user1.setFirstName("firstName");
        user1.setLastName("lastName");
        user1.setEmail("user1@123.com");
        user1.setPassword("123");
        identityService.saveUser(user1);

        User user2 = identityService.newUser("user2");
        identityService.saveUser(user2);

        Group group1 = identityService.newGroup("group1");
        group1.setName("testGroup");
        identityService.saveGroup(group1);

        identityService.createMembership(user1.getId(), group1.getId());
        identityService.createMembership(user2.getId(), group1.getId());

        identityService.setUserInfo(user1.getId(), "age", "20");
        identityService.setUserInfo(user1.getId(), "address", "Beijing");
    }

}
