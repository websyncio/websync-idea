package org.websync.websession.psimodels;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import org.websync.jdi.JdiAttribute;
import org.websync.websession.models.ComponentInstance;
import org.websync.websession.psimodels.jdi.Locator;
import org.websync.websession.psimodels.psi.InstanceAnnotation;
import org.websync.websession.psimodels.psi.NameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.websync.jdi.JdiAttribute.JDI_NAME;

public class PsiComponentInstance extends PsiModelWithId<PsiComponentInstance> implements ComponentInstance {
    private final String parentId;
    private final PsiField psiField;

    @Override
    public String getName() {
        return retrieveName();
    }

    @Override
    public String getComponentTypeId() {
        return PsiUtil.resolveClassInType(psiField.getType()).getQualifiedName();
    }

    public PsiComponentInstance(String parentId, PsiField psiField) {
        this.parentId = parentId;
        this.psiField = psiField;
    }

    public void fill() {
        id = parentId + "." + psiField.toString();
    }

    public Locator getLocator() {
        if (psiField.getAnnotations().length == 0) {
            return null;
        }
        return new Locator(psiField.getAnnotations()[0]);
    }

    public String retrieveName() {
        String name = null;
        if (psiField.getAnnotations().length == 0) {
            return null;
        } else for (int i = 0; i < psiField.getAnnotations().length; i++) {
            if (JdiAttribute.valueOfStr(psiField.getAnnotations()[i].getQualifiedName()).equals(JDI_NAME)) {
                name = psiField.getAnnotations()[i].getParameterList().getAttributes()[0].getLiteralValue();
            }
        }
        return name;
    }

    @Override
    public InstanceAnnotation getInstanceAttribute() {
        if (psiField.getAnnotations().length == 0) {
            return null;
        }
        PsiAnnotation annotation = psiField.getAnnotations()[0];

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
                        attribute.getAnnotationParameterList().add(new NameValuePair(identifier, value));
                    }

                    PsiArrayInitializerMemberValue psiArrayInitializerMemberValue =
                            (PsiArrayInitializerMemberValue) psiElements.stream()
                                    .filter(psiElement -> PsiArrayInitializerMemberValue.class.isInstance(psiElement))
                                    .findFirst().orElse(null);

                    if (psiArrayInitializerMemberValue != null) {

                        ArrayList<Object> arrayInitializerMemberValue = new ArrayList<>();

                        Arrays.asList(psiArrayInitializerMemberValue.getInitializers())
                                .forEach(psiAnnotationMemberValue -> {
                                    // This is the dummy for @UI.List and @FindBys
                                    if (PsiAnnotation.class.isInstance(psiAnnotationMemberValue)) {
                                        PsiAnnotation psiAnnotation = (PsiAnnotation) psiAnnotationMemberValue;

                                        String javaCodeReference1 = Arrays.asList(psiAnnotation.getChildren()).stream()
                                                .filter(psiElement -> PsiJavaCodeReferenceElement.class.isInstance(psiElement))
                                                .findFirst().get().getText();
                                        InstanceAnnotation instanceAnnotation = new InstanceAnnotation(javaCodeReference1);

                                        List<PsiNameValuePair> psiNameValuePairs1 = Arrays.asList(psiAnnotation.
                                                getParameterList().getAttributes());

                                        psiNameValuePairs1.stream().forEach(
                                                p -> {
                                                    String n = p.getName();
                                                    List<PsiElement> l = Arrays.asList(p.getChildren());

                                                    PsiLiteralExpression expr = (PsiLiteralExpression) l.stream()
                                                            .filter(e1 -> PsiLiteralExpression.class.isInstance(e1))
                                                            .findFirst().orElse(null);

                                                    if (expr != null) {
                                                        Object value = expr.getValue();
                                                        instanceAnnotation.getAnnotationParameterList().add(new NameValuePair(n, value));
                                                    }
                                                });

                                        arrayInitializerMemberValue.add(instanceAnnotation);
                                    }
                                    // This works for @ByText, @Css, @JDropdown, @JMenu, @JTable, @UI, @WithText, @XPath
                                    if (PsiLiteralExpression.class.isInstance(psiAnnotationMemberValue)) {
                                        Object value = ((PsiLiteralExpression) psiAnnotationMemberValue).getValue();
                                        arrayInitializerMemberValue.add(value);
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