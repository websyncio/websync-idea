package org.websync.react;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
import org.websync.websession.psimodels.PsiWebSession;

import java.io.IOException;
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
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            ReactDataPayload payload = mapper.readValue(data, ReactDataPayload.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
//            payload = mapper.readValue(data, new TypeReference<Collection<ReactDataPayload>>() {
//            });
      return null;
    }
}