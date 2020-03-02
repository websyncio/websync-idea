package com.epam.sha.intellij.locatorupdater.notifier;

import com.epam.sha.intellij.locatorupdater.handler.RequestHandler;

public interface RequestNotifier extends Runnable {

    void addRequestHandler(RequestHandler handler);

}
