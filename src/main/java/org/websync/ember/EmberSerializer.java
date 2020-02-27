package org.websync.ember;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.websync.browserConnection.SessionWebSerializer;
import org.websync.ember.dto.*;
import org.websync.sessionweb.models.ComponentInstance;
import org.websync.sessionweb.models.ComponentsContainer;
import org.websync.sessionweb.models.WebSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class EmberSerializer implements SessionWebSerializer {

    @Override
    public String serialize(Collection<WebSession> webs) {
        EmberDataPayload payload = new EmberDataPayload();
        for (WebSession web : webs) {
            serializeSessionWeb(payload, web);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String result = null;
        try {
            result = mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void serializeSessionWeb(EmberDataPayload payload, WebSession web) {
        payload.websites = web.getWebsites().values().stream()
                .map(s -> new WebsiteDto(s)).collect(Collectors.toList());
        payload.pageTypes = web.getPages().values().stream()
                .map(p -> new PageTypeDto(p)).collect(Collectors.toList());
        payload.componentTypes = web.getComponents().values().stream()
                .map(c -> new ComponentTypeDto(c)).collect(Collectors.toList());
        serializeComponents(payload, web);
    }

    private void serializeComponents(EmberDataPayload payload, WebSession web) {
        payload.components = new ArrayList<ComponentDto>();
        serializeComponents(payload,
                (Collection<ComponentsContainer>) (Collection<?>) web.getPages().values());
        serializeComponents(payload,
                (Collection<ComponentsContainer>) (Collection<?>) web.getComponents().values());
    }

    private void serializeComponents(EmberDataPayload payload, Collection<ComponentsContainer> containers) {
        for (ComponentsContainer container : containers) {
            if (container.components == null) {
                break;
            }
            for (ComponentInstance component : container.components) {
                payload.components.add(new ComponentDto(component));
            }
        }
    }

    @Override
    public Collection<WebSession> deserialize(String data) {
        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        Collection<EmberDataPayload> payload = null;
        try {
//            sessions = mapper.readValue(data, new TypeReference<Collection<PsiSessionWeb>>() {
            payload = mapper.readValue(data, new TypeReference<Collection<EmberDataPayload>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
