package org.websync.utils;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.util.InheritanceUtil;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;
import org.websync.frameworks.jdi.JdiElement;

import java.util.Arrays;

import static org.websync.frameworks.jdi.JdiAttribute.JDI_JSITE;

@UtilityClass
public class PsiUtil {
    @Nullable
    public static PsiClass findPsiClass(@Nullable PsiFile psiFile) {
        if(!(psiFile  instanceof PsiJavaFile)) {
            return null;
        }
        return Arrays.stream(((PsiJavaFile)psiFile).getClasses())
                .filter(c -> c.getModifierList() != null && c.getModifierList().hasModifierProperty(PsiModifier.PUBLIC))
                .findFirst().orElse(null);
    }

    public static boolean isWebsite(@Nullable PsiClass psiClass) {
        return psiClass != null && psiClass.getAnnotation(JDI_JSITE.className) != null;
    }

    public static boolean isComponent(@Nullable PsiClass psiClass) {
        return isInheritor(psiClass, JdiElement.JDI_UI_BASE_ELEMENT);
    }

    public static boolean isPage(@Nullable PsiClass psiClass) {
        return isInheritor(psiClass, JdiElement.JDI_WEB_PAGE);
    }

    private static boolean isInheritor(@Nullable PsiClass psiClass, JdiElement jdiElement) {
        return psiClass != null && Arrays.stream(psiClass.getSuperTypes())
                .anyMatch(s -> InheritanceUtil.isInheritor(s, jdiElement.className));
    }

}
