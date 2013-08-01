package com.kisel.algo2;

import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;

/**
 *
 * @author brainless
 */
public class MatcherTest extends TestCase {

    private final String[] data = {"abc", "bca", "dac", "dbc", "cba"};

    public void testMatch_Simple() {
        Matcher instance = new Matcher("(ab)(bc)(ca)");
        boolean result = instance.match("abc");
        assert (result);
    }

    public void testMatch_SimpleNeg() {
        Matcher instance = new Matcher("(ab)(bc)(ca)");
        boolean result = instance.match("zyx");
        assertFalse(result);
    }

    public void testMatch_SimpleDif() {
        Matcher instance = new Matcher("(abuikfmdkdkf)e(bc)(ca)a");
        boolean result = instance.match("decaa");
        assert (result);
    }

    public void testMatch1_StringArr() {
        Matcher instance = new Matcher("(ab)(bc)(ca)");
        int expResult = 2;
        int result = instance.match(data);
        assertEquals(expResult, result);
    }

    public void testMatch2_StringArr() {
        Matcher instance = new Matcher("abc");
        int expResult = 1;
        int result = instance.match(data);
        assertEquals(expResult, result);
    }

    public void testMatch3_StringArr() {
        Matcher instance = new Matcher("(abc)(abc)(abc)");
        int expResult = 3;
        int result = instance.match(data);
        assertEquals(expResult, result);
    }

    public void testMatch4_StringArr() {
        Matcher instance = new Matcher("(zyx)bc");
        int expResult = 0;
        int result = instance.match(data);
        assertEquals(expResult, result);
    }
}
