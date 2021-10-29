package org.websync.utils;

public class ModuleNameUtils {
    private static String MainModuleSuffix="_main";

    public static String getMainModuleName(String projectName) {
        return projectName + MainModuleSuffix;
    }

    public static boolean isMainModule(String moduleName) {
        return moduleName.endsWith(MainModuleSuffix);
    }
}