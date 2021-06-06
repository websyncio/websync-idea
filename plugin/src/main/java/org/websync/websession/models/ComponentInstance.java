package org.websync.websession.models;

import org.websync.websession.psimodels.psi.AnnotationInstance;

public interface ComponentInstance extends CodeModelWithId {
    String getName();

    String getComponentType();

    String getFieldName();
//    Scss.Scss RootSelector { get; }

    AnnotationInstance getAttributeInstance();
}
