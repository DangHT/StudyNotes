package me.danght.activiti.coreapi;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author DangHT
 * @date 2020/07/28
 */
public class IdentityServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void testIdentityService() {
        IdentityService identityService = activitiRule.getIdentityService();
        User jack = identityService.newUser("Jack");
        jack.setEmail("jack@123.com");
        User tom = identityService.newUser("Tom");
        tom.setEmail("tom@123.com");
        identityService.saveUser(jack);
        identityService.saveUser(tom);

        Group group1 = identityService.newGroup("Group1");
        identityService.saveGroup(group1);

        Group group2 = identityService.newGroup("Group2");
        identityService.saveGroup(group2);

        identityService.createMembership("Jack", "Group1");
        identityService.createMembership("Tom", "Group1");
        identityService.createMembership("Jack", "Group2");

        //对用户修改之后版本号会发生变化
        User jack1 = identityService.createUserQuery().userId("Jack").singleResult();
        jack1.setLastName("Jerry");
        identityService.saveUser(jack1);

        List<User> userList = identityService
                .createUserQuery()
                .memberOfGroup("Group1")
                .listPage(0, 100);
        for (User user : userList) {
            LOGGER.info("user = {}", ToStringBuilder.reflectionToString(user, ToStringStyle.JSON_STYLE));
        }

        List<Group> groupList = identityService
                .createGroupQuery()
                .groupMember("Jack")
                .listPage(0, 100);
        for (Group group : groupList) {
            LOGGER.info("group = {}", ToStringBuilder.reflectionToString(group, ToStringStyle.JSON_STYLE));
        }
    }

}
