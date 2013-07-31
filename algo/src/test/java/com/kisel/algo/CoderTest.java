/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kisel.algo;

import junit.framework.TestCase;

/**
 *
 * @author brainless
 */
public class CoderTest extends TestCase {
    
    public CoderTest(String testName) {
        super(testName);
    }

    public void testCode1() {
        String number = "9";
        Coder instance = new Coder("0123456789", "oF8");
        String expResult = "Foo";
        String result = instance.code(number);
        assertEquals(expResult, result);
    }
    public void testCode2() {
        String number = "Foo";
        Coder instance = new Coder("oF8","0123456789");
        String expResult = "9";
        String result = instance.code(number);
        assertEquals(expResult, result);
    }
    public void testCode3() {
        String number = "13";
        Coder instance = new Coder("0123456789abcdef", "01");
        String expResult = "10011";
        String result = instance.code(number);
        assertEquals(expResult, result);
    }
    public void testCode4() {
        String number = "CODE";
        Coder instance = new Coder("O!CDE?", "A?JM!.");
        String expResult = "JAM!";
        String result = instance.code(number);
        assertEquals(expResult, result);
    }
}
