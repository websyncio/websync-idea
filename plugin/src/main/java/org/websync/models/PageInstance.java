package org.websync.models;


import org.websync.psi.models.AnnotationInstance;

public interface PageInstance extends CodeModelWithId {
    String getName();

    String getPageTypeId();

    AnnotationInstance getAttributeInstance();

    String getUrl();
}
