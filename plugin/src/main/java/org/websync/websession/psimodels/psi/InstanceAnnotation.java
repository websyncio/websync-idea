package org.websync.websession.psimodels.psi;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class InstanceAnnotation {
    @Getter
    private final String codeReferenceElement;
    @Getter
    private final List<NameValuePair> annotationParameterList = new ArrayList<>();

    public InstanceAnnotation(String javaCodeReferenceElement) {
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
