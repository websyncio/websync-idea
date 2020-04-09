package org.websync.react;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.websocket.WebSessionSerializer;
import org.websync.react.dto.*;

import org.websync.browserConnection.WebSessionSerializer;
import org.websync.react.dto.ComponentTypeDto;
import org.websync.react.dto.PageTypeDto;
import org.websync.websession.models.WebSession;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ReactSerializer implements WebSessionSerializer {
    public String serialize(WebSession webSession) {
        ReactDataPayload payload = getReactDataPayload(webSession);
        try {
            return serializePayload(payload);
        } catch (JsonProcessingException e) {
            return "{error:'Error occured during serialization.'}";
        }
    }

    @Nullable
    private String serializePayload(ReactDataPayload payload) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return mapper.writeValueAsString(payload);
    }

    @NotNull
    private ReactDataPayload getReactDataPayload(WebSession web) {
       ReactDataPayload payload = new ReactDataPayload();
        payload.pages = web.getPageTypes().values().stream()
                .map(PageTypeDto::new).collect(Collectors.toList());
        payload.components = web.getComponentTypes().values().stream()
                .map(ComponentTypeDto::new).collect(Collectors.toList());
        return payload;
    }

    @Override
    public ReactDataPayload deserialize(String data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return mapper.readValue(data, ReactDataPayload.class);
    }

}