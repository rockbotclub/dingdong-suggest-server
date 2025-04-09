package cc.rockbot.dds.entity;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testUserCreationWithValidData() {
        User user = new User();
        user.setWxid("wx123456");
        user.setUserName("Test User");
        user.setUserOrg("Test Org");
        user.setUserPhone("13800138000");
        user.setStatus(1);
        user.setOrgId("org123");
        user.setGmtCreate(LocalDateTime.now());
        user.setGmtModified(LocalDateTime.now());

        var violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUserCreationWithInvalidWxid() {
        User user = new User();
        user.setWxid(""); // Empty wxid
        user.setUserName("Test User");
        user.setUserOrg("Test Org");
        user.setUserPhone("13800138000");
        user.setStatus(1);
        user.setOrgId("org123");

        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testUserCreationWithInvalidPhone() {
        User user = new User();
        user.setWxid("wx123456");
        user.setUserName("Test User");
        user.setUserOrg("Test Org");
        user.setUserPhone("123"); // Invalid phone number
        user.setStatus(1);
        user.setOrgId("org123");

        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testUserCreationWithInvalidStatus() {
        User user = new User();
        user.setWxid("wx123456");
        user.setUserName("Test User");
        user.setUserOrg("Test Org");
        user.setUserPhone("13800138000");
        user.setStatus(-1); // Invalid status
        user.setOrgId("org123");

        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testUserCreationWithNullRequiredFields() {
        User user = new User();
        user.setWxid(null);
        user.setUserName(null);
        user.setUserOrg(null);
        user.setUserPhone(null);
        user.setStatus(null);
        user.setOrgId(null);

        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testUserCreationWithMaxLengthFields() {
        User user = new User();
        user.setWxid("x".repeat(255));
        user.setUserName("x".repeat(127));
        user.setUserOrg("x".repeat(255));
        user.setUserPhone("13800138000");
        user.setStatus(1);
        user.setOrgId("x".repeat(255));

        var violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUserCreationWithExceedingMaxLength() {
        User user = new User();
        user.setWxid("x".repeat(256));
        user.setUserName("x".repeat(128));
        user.setUserOrg("x".repeat(256));
        user.setUserPhone("1".repeat(21));
        user.setStatus(1);
        user.setOrgId("x".repeat(256));

        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
} 