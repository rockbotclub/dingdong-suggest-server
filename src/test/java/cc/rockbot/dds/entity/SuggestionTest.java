package cc.rockbot.dds.entity;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SuggestionTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testSuggestionCreationWithValidData() {
        Suggestion suggestion = new Suggestion();
        suggestion.setTitle("Test Title");
        suggestion.setProblemDescription("Test Problem Description");
        suggestion.setProblemAnalysis("Test Problem Analysis");
        suggestion.setSuggestion("Test Suggestion");
        suggestion.setExpectedOutcome("Test Expected Outcome");
        suggestion.setImageUrls(Arrays.asList("http://example.com/image1.jpg", "http://example.com/image2.jpg"));
        suggestion.setUserWxid("wx123456");
        suggestion.setStatus(1);
        suggestion.setOrgId("org123");
        suggestion.setGmtCreate(LocalDateTime.now());
        suggestion.setGmtModified(LocalDateTime.now());

        var violations = validator.validate(suggestion);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testSuggestionCreationWithEmptyTitle() {
        Suggestion suggestion = new Suggestion();
        suggestion.setTitle(""); // Empty title
        suggestion.setProblemDescription("Test Problem Description");
        suggestion.setSuggestion("Test Suggestion");
        suggestion.setUserWxid("wx123456");
        suggestion.setStatus(1);
        suggestion.setOrgId("org123");

        var violations = validator.validate(suggestion);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSuggestionCreationWithEmptyProblemDescription() {
        Suggestion suggestion = new Suggestion();
        suggestion.setTitle("Test Title");
        suggestion.setProblemDescription(""); // Empty problem description
        suggestion.setSuggestion("Test Suggestion");
        suggestion.setUserWxid("wx123456");
        suggestion.setStatus(1);
        suggestion.setOrgId("org123");

        var violations = validator.validate(suggestion);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSuggestionCreationWithEmptySuggestion() {
        Suggestion suggestion = new Suggestion();
        suggestion.setTitle("Test Title");
        suggestion.setProblemDescription("Test Problem Description");
        suggestion.setSuggestion(""); // Empty suggestion
        suggestion.setUserWxid("wx123456");
        suggestion.setStatus(1);
        suggestion.setOrgId("org123");

        var violations = validator.validate(suggestion);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSuggestionCreationWithInvalidStatus() {
        Suggestion suggestion = new Suggestion();
        suggestion.setTitle("Test Title");
        suggestion.setProblemDescription("Test Problem Description");
        suggestion.setSuggestion("Test Suggestion");
        suggestion.setUserWxid("wx123456");
        suggestion.setStatus(-1); // Invalid status
        suggestion.setOrgId("org123");

        var violations = validator.validate(suggestion);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSuggestionCreationWithNullRequiredFields() {
        Suggestion suggestion = new Suggestion();
        suggestion.setTitle(null);
        suggestion.setProblemDescription(null);
        suggestion.setSuggestion(null);
        suggestion.setUserWxid(null);
        suggestion.setStatus(null);
        suggestion.setOrgId(null);

        var violations = validator.validate(suggestion);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSuggestionCreationWithMaxLengthFields() {
        Suggestion suggestion = new Suggestion();
        suggestion.setTitle("x".repeat(255));
        suggestion.setProblemDescription("x".repeat(65535));
        suggestion.setProblemAnalysis("x".repeat(65535));
        suggestion.setSuggestion("x".repeat(65535));
        suggestion.setExpectedOutcome("x".repeat(65535));
        suggestion.setUserWxid("x".repeat(255));
        suggestion.setStatus(1);
        suggestion.setOrgId("x".repeat(255));

        var violations = validator.validate(suggestion);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testSuggestionCreationWithInvalidImageUrls() {
        Suggestion suggestion = new Suggestion();
        suggestion.setTitle("Test Title");
        suggestion.setProblemDescription("Test Problem Description");
        suggestion.setSuggestion("Test Suggestion");
        suggestion.setUserWxid("wx123456");
        suggestion.setStatus(1);
        suggestion.setOrgId("org123");
        suggestion.setImageUrls(Arrays.asList("invalid-url")); // Invalid URL

        var violations = validator.validate(suggestion);
        assertFalse(violations.isEmpty());
    }
} 