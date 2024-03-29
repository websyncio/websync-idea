package org.websync.psi.models;


import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import org.websync.connection.dto.AnnotationDto;
import org.websync.models.PageInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.websync.frameworks.jdi.JdiAttribute.JDI_NAME;

public class PsiPageInstance extends PsiModelWithId<PsiPageInstance> implements PageInstance {
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
    public String getPageTypeId() {
        return PsiUtil.resolveClassInType(psiField.getType()).getQualifiedName();
    }

    public PsiPageInstance(String parentId, PsiField psiField) {
        this.parentId = parentId;
        this.psiField = psiField;
    }

    public void fill() {
        setId(parentId + "." + psiField.getName());
    }

//    public Locator getLocator() {
//        if (psiField.getAnnotations().length == 0) {
//            return null;
//        }
//        return new Locator(psiField.getAnnotations()[0]);
//    }

    public String retrieveName() {
        String name = null;
        @NotNull PsiAnnotation[] annotations = psiField.getAnnotations();
        if (annotations.length == 0) {
            return null;
        }
        for (PsiAnnotation annotation : annotations) {
            if (JDI_NAME.className.equals(annotation.getQualifiedName())) {
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

    @Override
    public String getUrl() {
        // this code should be in PSI model
        AnnotationInstance annotationInstance = getAttributeInstance();
        if (annotationInstance != null) {
            AnnotationDto initializationAttribute;
            initializationAttribute = new AnnotationDto(getAttributeInstance());
            if (initializationAttribute.getName().equals("Url")) {
                List<AnnotationDto.Parameter> params = initializationAttribute.getParameters();
                return params.get(0).getValues().get(0);
            }
        }
        return "";
    }

    public String getUrlPattern() {
        AnnotationInstance annotationInstance = getAttributeInstance();
        if (annotationInstance != null) {
            AnnotationDto initializationAttribute;
            initializationAttribute = new AnnotationDto(getAttributeInstance());
            if (initializationAttribute.getName().equals("Url")) {
                List<AnnotationDto.Parameter> params = initializationAttribute.getParameters();
                return params.size() > 1 ?
                        params.get(1).getValues().get(0) :
                        null;
            }
        }
        return "";
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

    public void synchronize(PageInstance changedInstance) {
        //temporary comment
            /*TODO: implement
            /*
            not implemented yet
            attempt to fade out Codacy Warning:
            Document empty method bodyn. (PMD 3.4)
             */

    }
}