package com.kisel.algo2;

import java.util.ArrayList;
import java.util.List;

public class Matcher {

    private final List<List<Character>> pattern;

    public Matcher(String pattern) {
        List<List<Character>> resPattern = new ArrayList();
        List<Character> list = new ArrayList();
        boolean group = false;
        for (char c : pattern.toCharArray()) {
            if (c == '(') {
                group = true;
            } else if (c == ')') {
                resPattern.add(list);
                list = new ArrayList();
                group = false;
            } else {
                list.add(c);
                if (!group) {
                    resPattern.add(list);
                    list = new ArrayList();
                }
            }
        }
        this.pattern = resPattern;
    }

    public boolean match(String str) {
        int length = str.length();
        if (length != pattern.size()) {
            return false;
        } else {
            for (int i = 0; i < length; i++) {
                if (!pattern.get(i).contains(str.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public int match(String[] strs) {
        int result = 0;
        for (String s : strs) {
            if (match(s)) {
                result++;
            }
        }
        return result;
    }
}
