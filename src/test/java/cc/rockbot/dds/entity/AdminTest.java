package cc.rockbot.dds.entity;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import static org.junit.jupiter.api.Assertions.*;

class AdminTest {
    private Admin admin;
    private Validator validator;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        now = LocalDateTime.now();

        admin = new Admin();
        admin.setId(1L);
        admin.setGmtCreate(now);
        admin.setGmtModified(now);
        admin.setAdminWxid("test_wxid");
        admin.setAdminName("Test Admin");
        admin.setOrgId("test_org_id");
        admin.setAdminPhone("13800138000");
        admin.setAdminPasswd("password123");
    }

    @Test
    void testValidAdmin() {
        Set<ConstraintViolation<Admin>> violations = validator.validate(admin);
        assertTrue(violations.isEmpty(), "Valid admin should not have any constraint violations");
        
        assertNotNull(admin.getGmtCreate(), "gmtCreate should not be null");
        assertNotNull(admin.getGmtModified(), "gmtModified should not be null");
        assertEquals(now, admin.getGmtCreate(), "gmtCreate should match the set value");
        assertEquals(now, admin.getGmtModified(), "gmtModified should match the set value");
    }

    @Test
    void testInvalidPhoneNumber() {
        admin.setAdminPhone("invalid_phone");
        Set<ConstraintViolation<Admin>> violations = validator.validate(admin);
        assertFalse(violations.isEmpty(), "Invalid phone number should cause violations");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Invalid phone number format")),
                "Should have phone number format violation");
    }

    @Test
    void testPasswordTooShort() {
        admin.setAdminPasswd("12345"); // Less than minimum 6 characters
        Set<ConstraintViolation<Admin>> violations = validator.validate(admin);
        assertFalse(violations.isEmpty(), "Short password should cause violations");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Admin password must be between 6 and 255 characters")),
                "Should have password length violation");
    }

    @Test
    void testEmptyFields() {
        Admin emptyAdmin = new Admin();
        emptyAdmin.setGmtCreate(now);
        emptyAdmin.setGmtModified(now);
        
        Set<ConstraintViolation<Admin>> violations = validator.validate(emptyAdmin);
        assertEquals(5, violations.size(), "Should have violations for all required fields");
        
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Admin wxid cannot be empty")),
                "Should have wxid empty violation");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Admin name cannot be empty")),
                "Should have name empty violation");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Organization ID cannot be empty")),
                "Should have orgId empty violation");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Admin phone cannot be empty")),
                "Should have phone empty violation");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Admin password cannot be empty")),
                "Should have password empty violation");
    }

    @Test
    void testMaxLengthValidation() {
        String tooLongString = "a".repeat(256);
        admin.setAdminWxid(tooLongString);
        admin.setOrgId(tooLongString);
        admin.setAdminPasswd(tooLongString);
        
        String tooLongName = "a".repeat(128);
        admin.setAdminName(tooLongName);
        
        String tooLongPhone = "1".repeat(21);
        admin.setAdminPhone(tooLongPhone);

        Set<ConstraintViolation<Admin>> violations = validator.validate(admin);
        assertTrue(violations.size() >= 5, "Should have violations for all fields exceeding max length");
        
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Admin wxid must be less than 255 characters")),
                "Should have wxid length violation");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Admin name must be less than 127 characters")),
                "Should have name length violation");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Organization ID must be less than 255 characters")),
                "Should have orgId length violation");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Admin phone must be less than 20 characters")),
                "Should have phone length violation");
    }

    @Test
    void testValidPhoneNumbers() {
        String[] validPhones = {"13800138000", "15912345678", "17887654321", "19900001111"};
        for (String phone : validPhones) {
            admin.setAdminPhone(phone);
            Set<ConstraintViolation<Admin>> violations = validator.validate(admin);
            assertTrue(violations.isEmpty(), 
                    String.format("Phone number %s should be valid", phone));
        }
    }

    @Test
    void testInvalidPhoneNumbers() {
        String[] invalidPhones = {"12345678901", "213800138000", "1380013800", "138001380000", "abcdefghijk"};
        for (String phone : invalidPhones) {
            admin.setAdminPhone(phone);
            Set<ConstraintViolation<Admin>> violations = validator.validate(admin);
            assertFalse(violations.isEmpty(),
                    String.format("Phone number %s should be invalid", phone));
        }
    }
} 