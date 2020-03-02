package org.websync.ember;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang.NotImplementedException;
import org.websync.browserConnection.WebSessionSerializer;
import org.websync.ember.dto.*;
import org.websync.websession.models.ComponentInstance;
import org.websync.websession.models.ComponentsContainer;
import org.websync.websession.models.WebSession;

import java.util.Collection;
import java.util.stream.Collectors;

public class EmberSerializer implements WebSessionSerializer {

    @Override
    public String serialize(Collection<WebSession> webs) {
        EmberDataPayload payload = new EmberDataPayload();
        for (WebSession web : webs) {
            serializeWebSession(payload, web);
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

    private void serializeWebSession(EmberDataPayload payload, WebSession web) {
        payload.websites = web.getWebsites().values().stream()
                .map(s -> new WebsiteDto(s)).collect(Collectors.toList());
        payload.pages = web.getPages().values().stream()
                .map(p -> new PageDto(p)).collect(Collectors.toList());
        payload.components = web.getComponents().values().stream()
                .map(c -> new ComponentDto(c)).collect(Collectors.toList());
        serializeComponents(payload, web);
    }

    private void serializeComponents(EmberDataPayload payload, WebSession web) {
//        payload.components = new ArrayList<ComponentDto>();
        serializeComponents(payload,
                (Collection<ComponentsContainer>) (Collection<?>) web.getPages().values());
        serializeComponents(payload,
                (Collection<ComponentsContainer>) (Collection<?>) web.getComponents().values());
    }

    private void serializeComponents(EmberDataPayload payload, Collection<ComponentsContainer> containers) {
        for (ComponentsContainer container : containers) {
            if (container.getComponentInstances() == null) {
                break;
            }
            for (ComponentInstance componentInstance : container.getComponentInstances()) {
                payload.componentInstances.add(new ComponentInstanceDto(componentInstance));
            }
        }
    }

    @Override
    public Collection<WebSession> deserialize(String data) {
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
