package io.albert_gee.robort.helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class URIHelperTest {

    // parseScheme()

    @Test
    void parseSchemeSuccess() {
        String https = URIHelper.parseScheme("https://www.site.com");
        assertEquals(https, "https");
    }

    @Test
    void parseSchemeWithoutSchemePrefixReturnsNullSuccess() {
        String https = URIHelper.parseScheme("www.site.com");
        assertNull(https);
    }

    @Test
    void parseSchemeNotEqualFail() {
        String https = URIHelper.parseScheme("https://www.site.com");
        assertNotEquals(https, "http");
    }

    @Test
    void parseSchemeNullFail() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> URIHelper.parseScheme(null)
        );

        String expectedMessage = "Couldn't determine scheme - provided URI is null";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void parseSchemeEmptyFail() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> URIHelper.parseScheme("")
        );

        String expectedMessage = "Couldn't determine scheme - provided URI is null";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }


    // parseAuthority()

    @Test
    void parseAuthoritySuccess() {
        String expectedAuthority = "www.site.com";
        String actualAuthority = URIHelper.parseAuthority("http://www.site.com/index.php?param=1");
        assertEquals(expectedAuthority, actualAuthority);
    }

    @Test
    void parseAuthorityWithoutSchemeSuccess() {
        String expectedAuthority = "www.site.com";
        String actualAuthority = URIHelper.parseAuthority("www.site.com/index.php?param=1");
        assertEquals(expectedAuthority, actualAuthority);
    }

    @Test
    void parseAuthorityPrefixedWithOneSlashSuccess() {
        String authority = URIHelper.parseAuthority("/www.site.com/index.php?param=1");
        assertNull(authority);
    }

    @Test
    void parseAuthorityPrefixedWithTwoSlashesSuccess() {
        String expectedAuthority = "www.site.com";
        String actualAuthority = URIHelper.parseAuthority("//www.site.com/index.php?param=1");
        assertEquals(expectedAuthority, actualAuthority);
    }

    @Test
    void parseAuthorityIncorrectFail() {
        String expectedAuthority = "www.site.com1";
        String actualAuthority = URIHelper.parseAuthority("http://www.site.com/index.php?param=1");
        assertNotEquals(expectedAuthority, actualAuthority);
    }

    @Test
    void parseAuthorityNullFail() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            URIHelper.parseAuthority(null);
        });
        String expectedMessage = "Couldn't get authority from empty uri";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void parseAuthorityEmptyFail() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            URIHelper.parseAuthority("");
        });
        String expectedMessage = "Couldn't get authority from empty uri";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}