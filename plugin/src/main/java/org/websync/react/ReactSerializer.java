package org.websync.react;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.react.dto.ComponentTypeDto;
import org.websync.react.dto.PageTypeDto;
import org.websync.react.dto.WebSessionDto;
import org.websync.websession.models.WebSession;

import java.io.IOException;
import java.util.stream.Collectors;

public class ReactSerializer {

    @Nullable
    public String serialize(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return mapper.writeValueAsString(o);
    }

    public WebSessionDto deserialize(String data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return mapper.readValue(data, WebSessionDto.class);
    }
}