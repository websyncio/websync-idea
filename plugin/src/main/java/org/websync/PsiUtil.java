package org.websync;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.util.InheritanceUtil;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;
import org.websync.jdi.JdiElement;

import java.util.Arrays;

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

    public static boolean isComponent(@Nullable PsiClass psiClass) {
        return psiClass != null && Arrays.stream(psiClass.getSuperTypes())
                .anyMatch(s -> InheritanceUtil.isInheritor(s, JdiElement.JDI_UI_BASE_ELEMENT.className));
    }

    public static boolean isPage(@Nullable PsiClass psiClass) {
        return psiClass != null && Arrays.stream(psiClass.getSuperTypes())
                .anyMatch(s -> InheritanceUtil.isInheritor(s, JdiElement.JDI_WEB_PAGE.className));
    }

}
