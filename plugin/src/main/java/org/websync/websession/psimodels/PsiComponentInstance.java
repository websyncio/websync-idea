package org.websync.websession.psimodels;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiNameValuePair;
import org.websync.websession.models.ComponentInstance;

import java.util.Arrays;
import java.util.List;

public class PsiComponentInstance extends PsiModelWithId<PsiComponentInstance> implements ComponentInstance {

    private PsiField psiFiled;

    public PsiComponentInstance(PsiField psiFiled) {
        this.psiFiled = psiFiled;
    }

    public void fill() {
        id = psiFiled.toString();
    }

    public String getAttributes() {
        List<PsiAnnotation> psiAnnotations = Arrays.asList(psiFiled.getAnnotations());
        final String[] result = {""};
        psiAnnotations.stream().forEach(
                anno -> {
                    result[0] += anno.getQualifiedName();
                    List<PsiNameValuePair> attributes = Arrays.asList(anno.getParameterList().getAttributes());
                    attributes.stream().forEach(attr -> result[0] += " = '" + attr.getLiteralValue() + "'");
                }
        );
        return result[0];
    }
}
