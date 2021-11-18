package org.websync.utils;

public class StringUtils {
    public static String toCamelCase(String name) {
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }
}
