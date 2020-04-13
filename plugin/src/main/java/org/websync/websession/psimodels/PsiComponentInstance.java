package org.websync.websession.psimodels;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import org.websync.jdi.JdiAttribute;
import org.websync.websession.models.ComponentInstance;
import org.websync.websession.psimodels.jdi.Locator;
import org.websync.websession.psimodels.psi.AnnotationInstance;
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
        String name = retrieveName();
        return name != null ? name : psiField.getName();
    }

    public void setFieldName(String name) {
        psiField.setName(name);
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
        id = parentId + ":" + psiField.getName();
    }

    public Locator getLocator() {
        if (psiField.getAnnotations().length == 0) {
            return null;
        }
        return new Locator(psiField.getAnnotations()[0]);
    }

    public String retrieveName() {
        String name = null;
        @NotNull PsiAnnotation[] annotations = psiField.getAnnotations();
        if (annotations.length == 0) {
            return null;
        }
        for (PsiAnnotation annotation : annotations) {
            if (JdiAttribute.valueOfStr(annotation.getQualifiedName()).equals(JDI_NAME)) {
                name = annotation.getParameterList().getAttributes()[0].getLiteralValue();
            }
        }
        return name;
    }

    @Override
    public AnnotationInstance getAttributeInstance() {
        if (psiField.getAnnotations().length == 0) {
            return null;
        }
        PsiAnnotation annotation = psiField.getAnnotations()[0];

        return getAttributeInstance(annotation);
    }

    private AnnotationInstance getAttributeInstance(PsiAnnotation psiAnnotation) {
        return getAnnotationInstance(psiAnnotation);
    }

    private AnnotationInstance getAnnotationInstance(PsiAnnotation psiAnnotation) {
        String javaCodeReference = Arrays.asList(psiAnnotation.getChildren()).stream()
                .filter(psiElement -> PsiJavaCodeReferenceElement.class.isInstance(psiElement))
                .findFirst().get().getText();
        AnnotationInstance attribute = new AnnotationInstance(javaCodeReference);

        List<PsiNameValuePair> psiNameValuePairs = Arrays.asList(psiAnnotation.
                getParameterList().getAttributes());

        psiNameValuePairs.stream().forEach(
                pair -> {
                    String name = pair.getName();
                    List<PsiElement> psiElements = Arrays.asList(pair.getChildren());

                    PsiLiteralExpression psiLiteralExpression = (PsiLiteralExpression) psiElements.stream()
                            .filter(psiElement -> PsiLiteralExpression.class.isInstance(psiElement))
                            .findFirst().orElse(null);

                    if (psiLiteralExpression != null) {
                        Object value = psiLiteralExpression.getValue();
                        attribute.getAnnotationParameterList().add(new NameValuePair(name, value));
                    }

                    PsiArrayInitializerMemberValue psiArrayInitializerMemberValue =
                            (PsiArrayInitializerMemberValue) psiElements.stream()
                                    .filter(psiElement -> PsiArrayInitializerMemberValue.class.isInstance(psiElement))
                                    .findFirst().orElse(null);

                    if (psiArrayInitializerMemberValue != null) {

                        ArrayList<Object> arrayInitializerMemberValue = new ArrayList<>();

                        Arrays.asList(psiArrayInitializerMemberValue.getInitializers())
                                .forEach(psiAnnotationMemberValue -> {
                                    // If the annotation contains other annotations
                                    // This works for  @UI.List and @FindBys
                                    if (PsiAnnotation.class.isInstance(psiAnnotationMemberValue)) {
                                        PsiAnnotation psiAnnotation1 = (PsiAnnotation) psiAnnotationMemberValue;
                                        AnnotationInstance annotationInstance = getAnnotationInstance(psiAnnotation1);
                                        arrayInitializerMemberValue.add(annotationInstance);
                                    }
                                    // If the annotation contains pairs of values
                                    // This works for @ByText, @Css, @JDropdown, @JMenu, @JTable, @UI, @WithText, @XPath
                                    if (PsiLiteralExpression.class.isInstance(psiAnnotationMemberValue)) {
                                        Object value = ((PsiLiteralExpression) psiAnnotationMemberValue).getValue();
                                        arrayInitializerMemberValue.add(value);
                                    }
                                });

                        attribute.getAnnotationParameterList().add(
                                new NameValuePair(name, arrayInitializerMemberValue));
                    }
                });
        return attribute;
    }

    public void synchronize(ComponentInstance changedInstance) {

    }
}