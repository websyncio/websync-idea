package org.websync.websession.models;

public interface ComponentInstance extends CodeModelWithId {
    String getFieldName();

    String getName();

    String getComponentTypeId();
//    Scss.Scss RootSelector { get; }
}
