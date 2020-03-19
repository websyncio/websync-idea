package org.websync.websession.psimodels.psi;

import lombok.Getter;

public class LiteralExpression {
    @Getter
    private final Class<?> clazz;
    @Getter
    private final Object value;

    public LiteralExpression(Object value) {
        this.value = value;
        this.clazz = (value != null) ? value.getClass() : null;
    }

    @Override
    public String toString() {
        return "LiteralExpression{" +
                "clazz=" + clazz +
                ", value=" + value +
                '}';
    }
}
