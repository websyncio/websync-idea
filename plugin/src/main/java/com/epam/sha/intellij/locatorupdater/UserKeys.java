package com.epam.sha.intellij.locatorupdater;

import com.epam.sha.intellij.locatorupdater.model.RequestData;
import com.intellij.openapi.util.Key;

public class UserKeys {
    public static final Key<RequestData> CUSTOM_DATA = Key.create("customData");
}
