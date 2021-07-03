package org.websync.models;

import java.util.List;

public interface ComponentContainer extends CodeModelWithId {
    List<ComponentInstance> getComponentInstances();
    boolean updateComponentInstance(String oldName, String newName);
    String getBaseTypeId();
}
