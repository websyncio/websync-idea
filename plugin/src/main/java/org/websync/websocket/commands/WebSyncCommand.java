package org.websync.websocket.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.intellij.openapi.application.ApplicationManager;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.WebSyncException;
import org.websync.WebSyncService;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WebSyncCommand {

    @Getter
    private WebSyncService webSyncService;

    /**
     * Takes the input JSON message received from the WebSync browser plugin.
     * Does required processing and returns an error message, null means no error
     *
     * @param inputMessageString the input JSON message received from the WebSync browser plugin.
     * @return an error message, null means no error.
     */
    @Nullable
    public Object execute(@NotNull String inputMessageString) throws WebSyncException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            Message message = mapper.readValue(inputMessageString, getMessageClass());
            return execute(message);
        } catch (IOException e) {
            throw new WebSyncException("Cannot read command from '" + inputMessageString + "'", e);
        }
    }


    /**
     * Takes the text with low-cased command id like 'get-modules'
     * and looks up the corresponding command class in this package.
     * Returns a requested command instance, null means no command found
     *
     * @param text the input JSON message received from the WebSync browser plugin.
     * @return a requested command instance, null means no command found.
     */
    @Nullable
    public static WebSyncCommand createByText(@NotNull String text) {
        String id = extractId(text);
        if (id == null) {
            return null;
        }
        final StringBuilder classNameBuffer = new StringBuilder("." + id.substring(0, 1).toUpperCase());
        for (int i = 1; i < id.length(); i++) {
            char charToAdd = id.charAt(i);
            if (charToAdd == '-') {
                charToAdd = Character.toUpperCase(id.charAt(++i));
            }
            classNameBuffer.append(charToAdd);
        }

        String className = classNameBuffer.append("Command")
                .insert(0, WebSyncCommand.class.getPackage().getName()).toString();
        try {
            WebSyncCommand command = (WebSyncCommand) Class.forName(className).getConstructor().newInstance();
            command.webSyncService = ApplicationManager.getApplication().getService(WebSyncService.class);
            return command;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String extractId(String text) {
        Pattern p = Pattern.compile("\"command\"\\s*:\\s*\"([a-z](-?[a-z])*)\"");
        Matcher m = p.matcher(text);
        return m.find() ? m.group(1) : null;
    }


    private Class<? extends Message> getMessageClass() {
        for (Class<?> inner : getClass().getDeclaredClasses()) {
            if (inner.getSimpleName().equals("Message") && inner.getSuperclass().equals(Message.class)) {
                return (Class<? extends Message>) inner;
            }
        }
        return Message.class;
    }

    @Nullable
    protected abstract Object execute(@NotNull Message inputMessage) throws WebSyncException;

    static class Message {
        public String command;
    }

    public static void main(String[] args) {
        String res = "{\"command\"  :  \"d-f-f-ddfds\", \"data\":{\"id\":\"org.websync.jdi.AttributesInitialization.initializedWithXpath\",\"componentTypeId\":\"com.epam.jdi.light.elements.common.Label\",\"name\":\"initializedWithXpath\",\"initializationAttribute\":{\"name\":\"XPath\",\"parameters\":[{\"name\":null,\"values\":[\"//testXpath\"]}]}}}";

        try {
            System.out.println(":::'" + extractId(res) + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
