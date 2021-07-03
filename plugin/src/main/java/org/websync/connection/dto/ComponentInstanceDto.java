package org.websync.connection.dto;

import org.websync.models.ComponentInstance;

public class ComponentInstanceDto extends BaseDto {
    public String parentId;
    public int fieldIndex;
    public String componentType;
    public String fieldName;
    public String name;
    public AnnotationDto initializationAttribute;

    public ComponentInstanceDto() {
    }

    public ComponentInstanceDto(ComponentInstance componentInstance) {
        super(componentInstance.getId());
        parentId = componentInstance.getParentId();
        fieldIndex = componentInstance.getFieldIndex();
        componentType = componentInstance.getComponentType();
        fieldName = componentInstance.getFieldName();
        name = componentInstance.getName();
        initializationAttribute = new AnnotationDto(componentInstance.getAttributeInstance());
    }
}
