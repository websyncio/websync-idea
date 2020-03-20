package org.websync.websession.models;

import java.util.List;

public interface ComponentContainer extends CodeModelWithId {
    List<ComponentInstance> getComponentInstances();
}
