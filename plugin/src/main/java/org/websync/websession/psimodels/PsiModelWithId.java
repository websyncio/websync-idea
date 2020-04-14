package org.websync.websession.psimodels;

import lombok.Getter;
import lombok.Setter;

public abstract class PsiModelWithId<T> {
    @Getter
    @Setter
    private String id;
}
