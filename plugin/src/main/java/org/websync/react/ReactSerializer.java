package org.websync.react;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.browserConnection.WebSessionSerializer;
import org.websync.react.dto.*;
import org.websync.websession.models.ComponentContainer;
import org.websync.websession.models.ComponentInstance;
import org.websync.websession.models.WebSession;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReactSerializer implements WebSessionSerializer {
    public String serialize(List<WebSession> webSessions) {
        ReactDataPayload payload = getReactDataPayload(webSessions.get(0));
        try {
            return serializePayload(payload);
        } catch (JsonProcessingException e) {
            return "{error:'Error occured during serialization.'}";
        }
    }

    @Nullable
    private String serializePayload(ReactDataPayload payload) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper.writeValueAsString(payload);
    }

    @NotNull
    private ReactDataPayload getReactDataPayload(WebSession web) {
        ReactDataPayload payload = new ReactDataPayload();
//        payload.websites = web.websites.values().stream()
//                .map(s -> new WebsiteDto(s)).collect(Collectors.toList());
        payload.pages = web.getPageTypes().values().stream()
                .map(p -> new PageTypeDto(p)).collect(Collectors.toList());
        payload.components = web.getComponentTypes().values().stream()
                .map(c -> new ComponentTypeDto(c)).collect(Collectors.toList());
        return payload;
    }

//    private void serializeWebSession(ReactDataPayload payload, List<WebSession> webSessions) {
        // TODO: refactor this
//        WebSession web = webSessions.get(0);
//        payload.websites = web.websites.values().stream()
//                .map(s -> new WebsiteDto(s)).collect(Collectors.toList());
//        payload.pages = web.getPageTypes().values().stream()
//                .map(p -> new PageTypeDto(p)).collect(Collectors.toList());
//        payload.components = web.getComponentTypes().values().stream()
//                .map(c -> new ComponentTypeDto(c)).collect(Collectors.toList());
//        serializeComponents(payload, web);
//    }

//    private void serializeComponents(ReactDataPayload payload, WebSession web) {
//        serializeComponents(payload,
//                (Collection<ComponentContainer>) (Collection<?>) web.getPageTypes().values());
//        serializeComponents(payload,
//                (Collection<ComponentContainer>) (Collection<?>) web.getComponentTypes().values());
//    }

//    private void serializeComponents(ReactDataPayload payload, Collection<ComponentContainer> containers) {
//        for (ComponentContainer container : containers) {
//            if (container.getComponentInstances() == null) {
//                break;
//            }
//            for (ComponentInstance componentInstance : container.getComponentInstances()) {
//                payload.componentInstances.add(new ComponentInstanceDto(componentInstance));
//            }
//        }
//    }

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