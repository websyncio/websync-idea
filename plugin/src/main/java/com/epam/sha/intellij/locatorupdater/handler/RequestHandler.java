package com.epam.sha.intellij.locatorupdater.handler;

import com.epam.sha.intellij.locatorupdater.model.RequestData;

/**
 * Responsible for processing incoming requests
 */
public interface RequestHandler {

  /**
   * Handle incoming request
   * @param request - request payload
   */
  void handle(RequestData request);

}
