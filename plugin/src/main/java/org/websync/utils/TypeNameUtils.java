package org.websync.utils;

public class TypeNameUtils {
    public static String getNamespaceFromFullName(String fullName){
        return fullName.substring(0, fullName.lastIndexOf('.'));
    }
}
