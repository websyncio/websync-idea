package org.websync.connection.dto;

import org.websync.models.ComponentInstance;
import org.websync.psi.models.AnnotationInstance;

public class ComponentInstanceDto extends BaseDto {
    public String parentId;
    public int fieldIndex;
    public String componentTypeId;
    public String fieldName;
    public String name;
    public AnnotationDto initializationAttribute;

    public ComponentInstanceDto() {
    }

    public ComponentInstanceDto(ComponentInstance componentInstance) {
        super(componentInstance.getId());
        parentId = componentInstance.getParentId();
        fieldIndex = componentInstance.getFieldIndex();
        componentTypeId = componentInstance.getComponentType();
        fieldName = componentInstance.getFieldName();
        name = componentInstance.getName();
        AnnotationInstance attributeInstance = componentInstance.getAttributeInstance();
        if (attributeInstance != null) {
            initializationAttribute = new AnnotationDto(attributeInstance);
        }
    }
}
