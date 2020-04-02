package org.websync.websession.psimodels.psi;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class AnnotationInstance {
    @Getter
    private final String codeReferenceElement;
    @Getter
    private final List<NameValuePair> annotationParameterList = new ArrayList<>();

    public AnnotationInstance(String javaCodeReferenceElement) {
        this.codeReferenceElement = javaCodeReferenceElement;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "codeReferenceElement='" + codeReferenceElement + '\'' +
                ", annotationParameterList=" + annotationParameterList +
                '}';
    }
}
