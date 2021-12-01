package org.websync.utils;

import com.intellij.psi.PsiFile;

public class ModuleStructureUtils {
    private static String MainModuleSuffix="_main";

    private static String MainModulePathPrefix="/src/main";

    public static String getMainModuleName(String projectName) {
        return projectName + MainModuleSuffix;
    }

    public static boolean isFileInMainModule(PsiFile file) {
        String filePath = file.getVirtualFile().getCanonicalPath();
        String projectBasePath = file.getProject().getBasePath();
        filePath = filePath.substring(projectBasePath.length());
        return filePath.startsWith(MainModulePathPrefix);
    }
}