package com.epam.sha.intellij.locatorupdater.handler;

import com.epam.sha.intellij.locatorupdater.UserKeys;
import com.epam.sha.intellij.locatorupdater.model.RequestData;
import com.epam.sha.intellij.locatorupdater.utils.FileNavigator;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 *
 */
public class OpenFileMessageHandler implements RequestHandler {

    private static final Logger log = Logger.getInstance(OpenFileMessageHandler.class);
    private static final Pattern COLUMN_PATTERN = compile("[:#](\\d+)[:#]?(\\d*)$");
    private final FileNavigator fileNavigator;


    public OpenFileMessageHandler(FileNavigator fileNavigator) {
        this.fileNavigator = fileNavigator;
    }

    public void handle(RequestData request) {
        Matcher matcher = COLUMN_PATTERN.matcher(request.getTarget());

        int line = 0;
        int column = 0;

        if (matcher.find()) {
            line = StringUtil.parseInt(StringUtil.notNullize(matcher.group(1)), 1) - 1;
            final String columnNumberString = matcher.group(2);
            if (StringUtil.isNotEmpty(columnNumberString)) {
                column = StringUtil.parseInt(columnNumberString, 1) - 1;
            }
        }
        //remove extension from file name
        request.setTarget(request.getTarget().substring(0, request.getTarget().indexOf('.')));
        // navigate to file
        fileNavigator.findAndNavigate(matcher.replaceAll(""), line, column)
                .onSuccess(it -> updateLocator(request))
                .onError(t -> log.warn(t.getMessage()));
    }

    private static void updateLocator(RequestData request) {
        ActionManager am = ActionManager.getInstance();
        DataManager dm = DataManager.getInstance();

        dm.getDataContextFromFocusAsync().onSuccess(context -> {
            dm.saveInDataContext(context, UserKeys.CUSTOM_DATA, request);
            AnActionEvent event = new AnActionEvent(null, context,
                    ActionPlaces.UNKNOWN, new Presentation(),
                    ActionManager.getInstance(), 0);
            am.getAction("updateBy").actionPerformed(event);
        });
    }
}
