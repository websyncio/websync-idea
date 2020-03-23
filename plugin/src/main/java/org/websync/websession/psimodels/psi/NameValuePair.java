package org.websync.websession.psimodels.psi;

import lombok.Getter;

import java.util.ArrayList;

public class NameValuePair {
    @Getter
    private final String identifier;
    @Getter
    private ArrayList<LiteralExpression> arrayInitializerMemberValue;

    public NameValuePair(String identifier, LiteralExpression literalExpression) {
        this.identifier = identifier;
        this.arrayInitializerMemberValue = new ArrayList<>();
        this.arrayInitializerMemberValue.add(literalExpression);
    }

    public NameValuePair(String identifier, ArrayList<LiteralExpression> arrayInitializerMemberValue) {
        this.identifier = identifier;
        this.arrayInitializerMemberValue = arrayInitializerMemberValue;
    }

    @Override
    public String toString() {
        return "NameValuePair{" +
                "identifier='" + identifier + '\'' +
                ", arrayInitializerMemberValue=" + arrayInitializerMemberValue +
                '}';
    }
}