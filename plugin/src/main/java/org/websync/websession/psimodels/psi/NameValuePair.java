package org.websync.websession.psimodels.psi;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class NameValuePair {
    @Getter
    private final String identifier;
    @Getter
    private ArrayList<Object> arrayInitializerMemberValue;
    @Getter
    private ArrayList<InstanceAnnotation> arrayInitializerMemberValueAnnotation;

    public NameValuePair(String identifier, Object literalExpression) {
        this.identifier = identifier;
        this.arrayInitializerMemberValue = new ArrayList<>();
        this.arrayInitializerMemberValue.add(literalExpression);
    }

    public NameValuePair(String identifier, ArrayList<Object> arrayInitializerMemberValue) {
        this.identifier = identifier;
        this.arrayInitializerMemberValue = arrayInitializerMemberValue;
    }

    public NameValuePair(ArrayList<InstanceAnnotation> arrayInitializerMemberValue) {
        this.identifier = null;
        this.arrayInitializerMemberValueAnnotation = arrayInitializerMemberValue;
    }

    @Override
    public String toString() {
        return "NameValuePair{" +
                "identifier='" + identifier + '\'' +
                ", arrayInitializerMemberValue=" + arrayInitializerMemberValue +
                '}';
    }
}