package com.epam.sha.intellij.locatorupdater.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
  name = "LocatorUpdater",
  storages = {@Storage("/locator-updater.xml")})
public class LocatorUpdaterSettings implements PersistentStateComponent<LocatorUpdaterSettings> {
  private int myPortNumber = 8091;
  private boolean myAllowRequestsFromLocalhostOnly = true;

  public int getPortNumber() {
    return myPortNumber;
  }

  public void setPortNumber(int portNumber) {
    myPortNumber = portNumber;
  }

  public boolean isAllowRequestsFromLocalhostOnly() {
    return myAllowRequestsFromLocalhostOnly;
  }

  public void setAllowRequestsFromLocalhostOnly(boolean allowRequestsFromLocalhostOnly) {
    myAllowRequestsFromLocalhostOnly = allowRequestsFromLocalhostOnly;
  }

  @Nullable
  @Override
  public LocatorUpdaterSettings getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull LocatorUpdaterSettings remoteCallSettings) {
    XmlSerializerUtil.copyBean(remoteCallSettings, this);
  }
}
