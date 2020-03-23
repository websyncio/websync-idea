package org.websync.websession.psimodels.psi;

import lombok.Getter;

public class LiteralExpression {
    @Getter
    private final Object value;

    public LiteralExpression(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LiteralExpression{" +
                "value=" + value +
                '}';
    }
}
