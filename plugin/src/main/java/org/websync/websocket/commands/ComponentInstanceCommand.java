package org.websync.websocket.commands;

import org.websync.react.dto.ComponentInstanceDto;

public abstract class ComponentInstanceCommand  extends WebSyncCommand {
    static class Message extends WebSyncCommand.Message {
        public String projectName;
        public ComponentInstanceDto data;

        private String className = null;
        private int fieldIndex = -1;

        public String getContainerClassName() {
            if (className == null) {
                parseComponentId();
            }
            return className;
        }

        public int getFieldIndex() {
            if (fieldIndex < 0) {
                parseComponentId();
            }
            return fieldIndex;
        }

        private void parseComponentId() {
            int lastDot = data.id.lastIndexOf('.');
            className = data.id.substring(0, lastDot);
            fieldIndex = Integer.parseInt(data.id.substring(lastDot + 1));
        }
    }

    protected String getNameFromTypeId(String componentType) {
        return componentType.substring(componentType.lastIndexOf('.') + 1);
    }
}
