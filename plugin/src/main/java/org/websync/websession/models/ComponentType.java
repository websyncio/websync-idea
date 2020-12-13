package org.websync.websession.models;

public interface ComponentType extends ComponentContainer {
    String getBaseComponentTypeId();
    boolean getIsCustom();
}