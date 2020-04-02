package org.websync.websession.models;

import org.websync.websession.psimodels.psi.AnnotationInstance;

public interface ComponentInstance extends CodeModelWithId {
    String getName();

    String getComponentTypeId();
//    Scss.Scss RootSelector { get; }

    AnnotationInstance getAttributeInstance();
}
