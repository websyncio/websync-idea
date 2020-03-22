package org.websync.ember;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang.NotImplementedException;
import org.websync.browserConnection.WebSessionSerializer;
import org.websync.ember.dto.*;
import org.websync.websession.models.ComponentContainer;
import org.websync.websession.models.ComponentInstance;
import org.websync.websession.models.WebSession;

import java.util.Collection;
import java.util.stream.Collectors;

public class ReactSerializer implements WebSessionSerializer {
    public String serialize(WebSession webSession) {
        EmberDataPayload payload = new EmberDataPayload();
        serializeWebSession(payload, webSession);
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

    private void serializeWebSession(EmberDataPayload payload, WebSession web) {
        payload.websites = web.websites.values().stream()
                .map(s -> new WebsiteDto(s)).collect(Collectors.toList());
        payload.pages = web.getPages().values().stream()
                .map(p -> new PageDto(p)).collect(Collectors.toList());
        payload.components = web.getComponents().values().stream()
                .map(c -> new ComponentDto(c)).collect(Collectors.toList());
        serializeComponents(payload, web);
    }

    private void serializeComponents(EmberDataPayload payload, WebSession web) {
        serializeComponents(payload,
                (Collection<ComponentContainer>) (Collection<?>) web.getPages().values());
        serializeComponents(payload,
                (Collection<ComponentContainer>) (Collection<?>) web.getComponents().values());
    }

    private void serializeComponents(EmberDataPayload payload, Collection<ComponentContainer> containers) {
        for (ComponentContainer container : containers) {
            if (container.getComponentInstances() == null) {
                break;
            }
            for (ComponentInstance componentInstance : container.getComponentInstances()) {
                payload.componentInstances.add(new ComponentInstanceDto(componentInstance));
            }
        }
    }

    @Override
    public WebSession deserialize(String data) {
        throw new NotImplementedException();
//        ObjectMapper mapper = new ObjectMapper();
////        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//        Collection<EmberDataPayload> payload = null;
//        try {
////            sessions = mapper.readValue(data, new TypeReference<Collection<PsiWebSession>>() {
//            payload = mapper.readValue(data, new TypeReference<Collection<EmberDataPayload>>() {
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
    }
}
