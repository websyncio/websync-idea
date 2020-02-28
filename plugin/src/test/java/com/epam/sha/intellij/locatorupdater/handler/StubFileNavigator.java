package com.epam.sha.intellij.locatorupdater.handler;

import com.epam.sha.intellij.locatorupdater.utils.FileNavigator;
import org.jetbrains.concurrency.AsyncPromise;
import org.jetbrains.concurrency.Promise;

class StubFileNavigator implements FileNavigator {

  private String fileName;
  private int line;
  private int column;

  @Override
  public Promise<Boolean> findAndNavigate(String fileName, int line, int column) {
    this.fileName = fileName;
    this.line = line;
    this.column = column;
    return new AsyncPromise<>();
  }

  public String getFileName() {
    return fileName;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }
}
