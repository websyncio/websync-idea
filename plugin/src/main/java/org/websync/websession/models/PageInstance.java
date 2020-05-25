package org.websync.websession.models;


import org.websync.websession.psimodels.psi.AnnotationInstance;

public interface PageInstance extends CodeModelWithId {
    String getName();

    String getPageTypeId();

    AnnotationInstance getAttributeInstance();
}
