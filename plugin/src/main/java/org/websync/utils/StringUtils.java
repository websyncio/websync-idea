package org.websync.utils;

public class StringUtils {
    public static String toCamelCase(String name) {
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    public static String cutFirst(String text, String cutValue){
        if(!cutValue.isEmpty() && text.startsWith(cutValue)){
            return text.substring(cutValue.length());
        }
        return text;
    }
}
