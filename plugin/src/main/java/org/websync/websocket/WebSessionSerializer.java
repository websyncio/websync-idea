package org.websync.websocket;

import org.websync.react.ReactSerializer;
import org.websync.react.dto.ComponentTypeDto;
import org.websync.react.dto.PageTypeDto;
import org.websync.react.dto.WebsiteDto;
import org.websync.websession.models.WebSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface WebSessionSerializer {

    String serialize(WebSession webSession);

    ReactSerializer.ReactDataPayload deserialize(String json) throws IOException;

    class ReactDataPayload {
        public List<WebsiteDto> websites = new ArrayList<>();
        public List<PageTypeDto> pages = new ArrayList<>();
        public List<ComponentTypeDto> components = new ArrayList<>();
    }

}