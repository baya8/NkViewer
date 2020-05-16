package com.kwbt.nk.scraiper.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ScraipingUtilsTest {

    @Test
    public void test() {

        String actual = "1234kg(-1)";
        String expected = "kg";

        assertTrue(actual.contains(expected));
        assertFalse(!actual.contains(expected));
    }

}
