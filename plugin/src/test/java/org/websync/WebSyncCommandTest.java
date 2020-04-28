package org.websync;

import org.junit.Test;
import org.websync.websocket.commands.WebSyncCommand;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class WebSyncCommandTest {

    @Test
    public void testMessageParsing() {
        WebSyncCommand command = WebSyncCommand.createByText("update-component-instance");
        try {
            String res = (String) command.execute("{\"command\":\"update-component-instance\", \"data\":" +
                    "{\"id\":\"org.websync.jdi.AttributesInitialization.initializedWithXpath\",\"componentTypeId\":" +
                    "\"com.epam.jdi.light.elements.common.Label\",\"name\":\"initializedWithXpath\"," +
                    "\"initializationAttribute\":{\"name\":\"XPath\",\"parameters\"" +
                    ":[{\"name\":null,\"values\":[\"//testXpath\"]}]}}}");
            assertNull("error message must be null", res);
        } catch (WebSyncException e) {
            fail("thrown: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
