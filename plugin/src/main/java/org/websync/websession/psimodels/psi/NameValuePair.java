package org.websync.websession.psimodels.psi;

import lombok.Getter;

import java.util.ArrayList;

public class NameValuePair {
    @Getter
    private final String identifier;
    @Getter
    private LiteralExpression literalExpression;
    @Getter
    private ArrayList<LiteralExpression> arrayInitializerMemberValue;

    public NameValuePair(String identifier, LiteralExpression literalExpression) {
        this.identifier = identifier;
        this.literalExpression = literalExpression;
    }

    public NameValuePair(String identifier, ArrayList<LiteralExpression> arrayInitializerMemberValue) {
        this.identifier = identifier;
        this.arrayInitializerMemberValue = arrayInitializerMemberValue;
    }

    @Override
    public String toString() {
        return "NameValuePair{" +
                "identifier='" + identifier + '\'' +
                ", literalExpression=" + literalExpression +
                ", arrayInitializerMemberValue=" + arrayInitializerMemberValue +
                '}';
    }
}
