package org.websync.react.dto;

import lombok.Getter;
import org.websync.websession.psimodels.psi.AnnotationInstance;

import java.util.List;
import java.util.stream.Collectors;

public class AnnotationDto {
    @Getter
    private String name;
    @Getter
    private List<Parameter> parameters;
    public AnnotationDto() {
    }

    public static class Parameter {
        @Getter
        private String name;
        @Getter
        private List<Object> values;
        public Parameter() {
        }

        public Parameter(String name, List<Object> values) {
            this.name = name;
            this.values = values;
        }
    }

    public AnnotationDto(AnnotationInstance attributeInstance) {
        name = attributeInstance.getCodeReferenceElement();

        parameters = attributeInstance.getAnnotationParameterList().stream().map(p -> {
            String name = p.getIdentifier();
            List<Object> values = p.getArrayInitializerMemberValue();

            for (int i = 0; i < values.size(); i++) {
                Object object = values.get(i);
                if (object instanceof AnnotationInstance) {
                    AnnotationDto annotationDto = new AnnotationDto((AnnotationInstance) object);
                    values.set(i, annotationDto);
                }
            }

            return new Parameter(name, values);
        }).collect(Collectors.toList());
    }
}
