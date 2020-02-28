package com.epam.sha.intellij.locatorupdater.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model
 */
@Data
@NoArgsConstructor
public class RequestData {

  private String target;
  private String oldLocator;
  private String newLocator;

}
