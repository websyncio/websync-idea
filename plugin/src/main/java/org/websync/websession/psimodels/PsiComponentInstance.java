package org.websync.websession.psimodels;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import org.websync.websession.models.ComponentInstance;
import org.websync.websession.psimodels.jdi.Locator;
import org.websync.websession.psimodels.psi.InstanceAnnotation;
import org.websync.websession.psimodels.psi.LiteralExpression;
import org.websync.websession.psimodels.psi.NameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PsiComponentInstance extends PsiModelWithId<PsiComponentInstance> implements ComponentInstance {
    private final String parentId;
    private final PsiField psiFiled;

    @Override
    public String getFieldName() {
        return psiFiled.getName();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getComponentTypeId() {
        return PsiUtil.resolveClassInType(psiFiled.getType()).getQualifiedName();
    }

    public PsiComponentInstance(String parentId, PsiField psiFiled) {
        this.parentId = parentId;
        this.psiFiled = psiFiled;
    }

    public void fill() {
        id = parentId + "." + psiFiled.toString();
    }

    public Locator getLocator() {
        if (psiFiled.getAnnotations().length == 0) {
            return null;
        }
        return new Locator(psiFiled.getAnnotations()[0]);
    }

    public InstanceAnnotation getInstanceAttribute() {
        if (psiFiled.getAnnotations().length == 0) {
            return null;
        }
        PsiAnnotation annotation = psiFiled.getAnnotations()[0];

        // Get tag name of annotation
        String javaCodeReference = Arrays.asList(annotation.getChildren()).stream()
                .filter(psiElement -> PsiJavaCodeReferenceElement.class.isInstance(psiElement))
                .findFirst().get().getText();
        InstanceAnnotation attribute = new InstanceAnnotation(javaCodeReference);

        // Processing of values of annotation
        List<PsiNameValuePair> psiNameValuePairs = Arrays.asList(annotation.getParameterList().getAttributes());
        psiNameValuePairs.stream().forEach(
                psiNameValuePair -> {
                    String identifier = psiNameValuePair.getName();
                    List<PsiElement> psiElements = Arrays.asList(psiNameValuePair.getChildren());

                    PsiLiteralExpression psiLiteralExpression = (PsiLiteralExpression) psiElements.stream()
                            .filter(psiElement -> PsiLiteralExpression.class.isInstance(psiElement))
                            .findFirst().orElse(null);

                    if (psiLiteralExpression != null) {
                        Object value = psiLiteralExpression.getValue();
                        attribute.getAnnotationParameterList().add(
                                new NameValuePair(identifier, new LiteralExpression(value)));
                    }

                    PsiArrayInitializerMemberValue psiArrayInitializerMemberValue =
                            (PsiArrayInitializerMemberValue) psiElements.stream()
                                    .filter(psiElement -> PsiArrayInitializerMemberValue.class.isInstance(psiElement))
                                    .findFirst().orElse(null);

                    if (psiArrayInitializerMemberValue != null) {

                        ArrayList<LiteralExpression> arrayInitializerMemberValue = new ArrayList<>();

                        Arrays.asList(psiArrayInitializerMemberValue.getInitializers())
                                .forEach(psiAnnotationMemberValue -> {
                                    // This is the dummy for @UI.List
                                    if (PsiAnnotation.class.isInstance(psiAnnotationMemberValue)) {
                                        LiteralExpression literalExpression = new LiteralExpression(null);
                                        arrayInitializerMemberValue.add(literalExpression);
                                    }
                                    // This works for @ByText, @Css, @JDropdown, @JMenu, @JTable, @UI, @WithText, @XPath
                                    if (PsiLiteralExpression.class.isInstance(psiAnnotationMemberValue)) {
                                        Object value = ((PsiLiteralExpression) psiAnnotationMemberValue).getValue();
                                        LiteralExpression literalExpression = new LiteralExpression(value);
                                        arrayInitializerMemberValue.add(literalExpression);
                                    }
                                });

                        attribute.getAnnotationParameterList().add(
                                new NameValuePair(identifier, arrayInitializerMemberValue));
                    }
                }
        );
        return attribute;
    }

}