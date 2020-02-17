package com.epam.sha.intellij.websync.sessionweb.psimodels;

import lombok.Getter;
import lombok.Setter;

public abstract class PsiModelWithId<T> {
    @Getter
    @Setter
    public String id;
}
