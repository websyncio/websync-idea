package org.websync.connection.dto;

import org.websync.models.ComponentInstance;

public class ComponentInstanceDto extends BaseDto {
    public String componentType;
    public String fieldName;
    public String name;
    public AnnotationDto initializationAttribute;

    public ComponentInstanceDto() {
    }

    public ComponentInstanceDto(ComponentInstance componentInstance) {
        super(componentInstance.getId());
        componentType = componentInstance.getComponentType();
        fieldName = componentInstance.getFieldName();
        name = componentInstance.getName();
        initializationAttribute = new AnnotationDto(componentInstance.getAttributeInstance());
    }

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
        int lastDot = id.lastIndexOf('.');
        className = id.substring(0, lastDot);
        fieldIndex = Integer.parseInt(id.substring(lastDot + 1));
    }
}
