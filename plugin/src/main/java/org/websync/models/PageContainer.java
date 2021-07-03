package org.websync.models;

import java.util.List;

public interface PageContainer extends CodeModelWithId {
    List<PageInstance> getPageInstances();
    boolean updatePageInstance(String oldName, String newName);
}