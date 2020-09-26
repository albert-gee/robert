package io.albert_gee.robort.helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class URLHelperTest {

    @Test
    void makeUrlAbsoluteSuccess() {
        String expectedResult = "http://www.site.com/index.php?param=1";
        String actualResult = URLHelper.makeUrlAbsolute("index.php?param=1", "http://www.site.com");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void makeUrlAbsoluteStartsWithOneSlashSuccess() {
        String expectedResult = "http://www.site.com/index.php?param=1";
        String actualResult = URLHelper.makeUrlAbsolute("/index.php?param=1", "http://www.site.com");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void makeUrlAbsoluteStartsWithTwoSlashesSuccess() {
        String expectedResult = "http://www.site.com/index.php?param=1";
        String actualResult = URLHelper.makeUrlAbsolute("//www.site.com/index.php?param=1", "http://www.site.com");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void makeUrlAbsoluteStartsWithHttpAndTwoSlashesSuccess() {
        String expectedResult = "http://www.site.com/index.php?param=1";
        String actualResult = URLHelper.makeUrlAbsolute("http://www.site.com/index.php?param=1", "http://www.site.com");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void makeUrlAbsoluteStartsWithHttpsAndTwoSlashesSuccess() {
        String expectedResult = "https://www.site.com/index.php?param=1";
        String actualResult = URLHelper.makeUrlAbsolute("https://www.site.com/index.php?param=1", "http://www.site.com");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void makeUrlAbsoluteStartsWithSchemaWithoutColonSuccess() {
        String expectedResult = "http://www.site.com/https//www.site.com/index.php?param=1";
        String actualResult = URLHelper.makeUrlAbsolute("https//www.site.com/index.php?param=1", "http://www.site.com");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void makeUrlAbsoluteIncorrectFail() {
        String expectedResult = "http://www.com.site/index.php?param=1";
        String actualResult = URLHelper.makeUrlAbsolute("index.php?param=1", "http://www.site.com");
        assertNotEquals(expectedResult, actualResult);
    }

    @Test
    void makeUrlAbsoluteUrlIsNullFail() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            URLHelper.makeUrlAbsolute(null, "http://www.site.com");
        });

        String expectedMessage = "URL of host were not provided";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void makeUrlAbsoluteParentIsNullFail() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            URLHelper.makeUrlAbsolute("http://www.site.com", null);
        });

        String expectedMessage = "URL of host were not provided";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void makeUrlAbsoluteUrlAndParentAreNullFail() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            URLHelper.makeUrlAbsolute(null, null);
        });

        String expectedMessage = "URL of host were not provided";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void makeUrlAbsoluteUrlIsEmptyFail() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            URLHelper.makeUrlAbsolute(null, "http://www.site.com");
        });

        String expectedMessage = "URL of host were not provided";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void makeUrlAbsoluteParentIsEmptyFail() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            URLHelper.makeUrlAbsolute("http://www.site.com", null);
        });

        String expectedMessage = "URL of host were not provided";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void makeUrlAbsoluteUrlAndParentAreEmptyFail() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            URLHelper.makeUrlAbsolute(null, null);
        });

        String expectedMessage = "URL of host were not provided";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void isAbsoluteHttpSuccess() {
        assertTrue(URLHelper.isAbsolute("http://www.site.com"));
    }

    @Test
    void isAbsoluteHttpsSuccess() {
        assertTrue(URLHelper.isAbsolute("https://www.site.com"));
    }

    @Test
    void isAbsoluteWrongSchemeFail() {
        assertFalse(URLHelper.isAbsolute("ldap://www.site.com"));
    }

    @Test
    void isAbsoluteWithoutSchemeFail() {
        assertFalse(URLHelper.isAbsolute("www.site.com"));
    }

    @Test
    void isAbsoluteWithoutSchemeWithTwoSlashesFail() {
        assertFalse(URLHelper.isAbsolute("//www.site.com"));
    }

    @Test
    void isAbsoluteWithoutSchemeWithOneSlashesFail() {
        assertFalse(URLHelper.isAbsolute("/www.site.com"));
    }
}