package com.epam.sha.intellij.locatorupdater.handler;

import com.epam.sha.intellij.locatorupdater.model.RequestData;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OpenFileMessageHandlerTest {

    private static OpenFileMessageHandler handler;
    private static StubFileNavigator fileNavigator;
    private static RequestData request;

    @BeforeClass
    public static void setUp() {
        fileNavigator = new StubFileNavigator();
        handler = new OpenFileMessageHandler(fileNavigator);
        request = new RequestData();
    }

    @Test
    public void handlerShouldExtractFilenameAndLineFromMessage() {
        request.setTarget("FileName.java:80");
        handler.handle(request);
        assertEquals("FileName.java", fileNavigator.getFileName());
        assertEquals(79, fileNavigator.getLine());
        assertEquals(0, fileNavigator.getColumn());

        request.setTarget("FileName.java");
        handler.handle(request);
        assertEquals("FileName.java", fileNavigator.getFileName());
        assertEquals(0, fileNavigator.getLine());
        assertEquals(0, fileNavigator.getColumn());

        request.setTarget("FileName.java:error");
        handler.handle(request);
        assertEquals("FileName.java:error", fileNavigator.getFileName());
        assertEquals(0, fileNavigator.getLine());
        assertEquals(0, fileNavigator.getColumn());
    }

    @Test
    public void handlerShouldExtractFileNameFromFullWindowsPath() {
        request.setTarget("c:\\FileName.java");
        handler.handle(request);
        assertEquals("c:\\FileName.java", fileNavigator.getFileName());
        assertEquals(0, fileNavigator.getLine());
        assertEquals(0, fileNavigator.getColumn());

        request.setTarget("c:\\FileName.java:80");
        handler.handle(request);
        assertEquals("c:\\FileName.java", fileNavigator.getFileName());
        assertEquals(79, fileNavigator.getLine());
        assertEquals(0, fileNavigator.getColumn());

        request.setTarget("c:\\FileName.java:80:20");
        handler.handle(request);
        assertEquals("c:\\FileName.java", fileNavigator.getFileName());
        assertEquals(79, fileNavigator.getLine());
        assertEquals(19, fileNavigator.getColumn());
    }

    @Test
    public void handlerShouldExtractLineNumberAfterHashCharacter() {
        request.setTarget("FileName.java#80");
        handler.handle(request);
        assertEquals("FileName.java", fileNavigator.getFileName());
        assertEquals(79, fileNavigator.getLine());
        assertEquals(0, fileNavigator.getColumn());
    }

    @Test
    public void handlerShouldExtractLineAndColumnNumberAfterColon() {
        request.setTarget("FileName.java#80#20");
        handler.handle(request);
        assertEquals("FileName.java", fileNavigator.getFileName());
        assertEquals(79, fileNavigator.getLine());
        assertEquals(19, fileNavigator.getColumn());
    }

    @Test
    public void handlerShouldExtractLineAndColumnNumberAfterHashCharacter() {
        request.setTarget("FileName.java:80:20");
        handler.handle(request);
        assertEquals("FileName.java", fileNavigator.getFileName());
        assertEquals(79, fileNavigator.getLine());
        assertEquals(19, fileNavigator.getColumn());
    }
}

