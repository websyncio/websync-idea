package org.websync.models;

import org.websync.psi.models.AnnotationInstance;

public interface ComponentInstance extends CodeModelWithId {
    String getName();

    String getComponentType();

    String getFieldName();
//    Scss.Scss RootSelector { get; }

    AnnotationInstance getAttributeInstance();
}
