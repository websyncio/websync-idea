package com.epam.sha.intellij.locatorupdater.utils;

import org.jetbrains.concurrency.Promise;

/**
 * Utility class responsible for target file lookup
 */
public interface FileNavigator {

  /**
   * Search desired file in projects, set focus on it target position.
   *
   * @param fileName - target file
   * @param line - line number
   * @param column - position in line
   * @return {@code True} if navigating success, else {@code False}
   */
  Promise<Boolean> findAndNavigate(String fileName, int line, int column);

}
