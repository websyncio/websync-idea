package org.websync.websession.models;

import org.websync.websession.psimodels.psi.InstanceAnnotation;

public interface ComponentInstance extends CodeModelWithId {
    String getFieldName();

    String getName();

    String getComponentTypeId();
//    Scss.Scss RootSelector { get; }

    InstanceAnnotation getInstanceAttribute();
}
