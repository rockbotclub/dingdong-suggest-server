package cc.rockbot.dds.entity;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class OrganizationTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testOrganizationCreationWithValidData() {
        Organization org = new Organization();
        org.setId("org123");
        org.setOrgName("Test Organization");
        org.setAddress("Test Address");
        org.setGmtCreate(LocalDateTime.now());
        org.setGmtModified(LocalDateTime.now());

        var violations = validator.validate(org);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testOrganizationCreationWithEmptyId() {
        Organization org = new Organization();
        org.setId(""); // Empty id
        org.setOrgName("Test Organization");
        org.setAddress("Test Address");

        var violations = validator.validate(org);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testOrganizationCreationWithEmptyOrgId() {
        Organization org = new Organization();
        org.setId(""); // Empty org id
        org.setOrgName("Test Organization");
        org.setAddress("Test Address");

        var violations = validator.validate(org);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testOrganizationCreationWithEmptyOrgName() {
        Organization org = new Organization();
        org.setId("org123");
        
        org.setOrgName(""); // Empty org name
        org.setAddress("Test Address");

        var violations = validator.validate(org);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testOrganizationCreationWithNullRequiredFields() {
        Organization org = new Organization();
        org.setId(null);
        org.setOrgName(null);
        org.setAddress(null);

        var violations = validator.validate(org);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testOrganizationCreationWithMaxLengthFields() {
        Organization org = new Organization();
        org.setId("x".repeat(255));
        org.setOrgName("x".repeat(255));
        org.setAddress("x".repeat(255));

        var violations = validator.validate(org);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testOrganizationCreationWithExceedingMaxLength() {
        Organization org = new Organization();
        org.setId("x".repeat(256));
        org.setOrgName("x".repeat(256));
        org.setAddress("x".repeat(256));

        var violations = validator.validate(org);
        assertFalse(violations.isEmpty());
    }
} 