package com.epam.websync.ember;

import com.epam.websync.browserConnection.SessionWebSerializer;
import com.epam.websync.ember.dto.*;
import com.epam.websync.sessionweb.models.ComponentInstance;
import com.epam.websync.sessionweb.models.ComponentsContainer;
import com.epam.websync.sessionweb.models.SessionWeb;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class EmberSerializer implements SessionWebSerializer {

    @Override
    public String serialize(Collection<SessionWeb> webs) {
        EmberDataPayload payload = new EmberDataPayload();
        for (SessionWeb web : webs) {
            serializeSessionWeb(payload, web);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String result = null;
        try {
            result = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void serializeSessionWeb(EmberDataPayload payload, SessionWeb web) {
        payload.services = web.getWebsites().values().stream()
                .map(s -> new WebsiteDto(s)).collect(Collectors.toList());
        payload.pageTypes = web.getPageTypes().values().stream()
                .map(p -> new PageTypeDto(p)).collect(Collectors.toList());
        payload.componentTypes = web.getComponentTypes().values().stream()
                .map(c -> new ComponentTypeDto(c)).collect(Collectors.toList());
        serializeComponents(payload, web);
    }

    private void serializeComponents(EmberDataPayload payload, SessionWeb web) {
        payload.components = new ArrayList<ComponentDto>();
        serializeComponents(payload,
                (Collection<ComponentsContainer>) (Collection<?>) web.getPageTypes().values());
        serializeComponents(payload,
                (Collection<ComponentsContainer>) (Collection<?>) web.getComponentTypes().values());
    }

    private void serializeComponents(EmberDataPayload payload, Collection<ComponentsContainer> containers) {
        for (ComponentsContainer container : containers) {
            for (ComponentInstance component : container.components) {
                payload.components.add(new ComponentDto(component));
            }
        }
    }

    @Override
    public Collection<SessionWeb> deserialize(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        Collection<SessionWeb> sessions = null;
        try {
            sessions = objectMapper.readValue(data, new TypeReference<Collection<SessionWeb>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessions;
    }
}