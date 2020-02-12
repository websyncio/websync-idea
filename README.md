healenium-plugin
======================

[![JetBrains Plugins](https://img.shields.io/jetbrains/plugin/v/13171-locator-updater.svg)](https://plugins.jetbrains.com/plugin/13171-locator-updater)
[![Build Status](https://github.com/healenium/healenium-plugin/workflows/Java-CI-test/badge.svg)](https://github.com/healenium/healenium-plugin/workflows/Java-CI-test/badge.svg)

## Plugin for [IntelliJ IDEA](http://plugins.jetbrains.com/plugin/13171-locator-updater) to support [Healenium](https://github.com/healenium/healenium-web). ##

Provides support for updating broken selenium locators with healed one via remote call with with IntelliJ IDEA.

**Last version (2.0.1) released on 19.11.2019**

Install it automatically from IntelliJ Idea plugin repository.

Tested and supports IntelliJ versions: 2019.2

Last support for IntelliJ 2019.1 by plugin version 1.9!

Installation
------------
### Plugin Installation
- Using IDE built-in plugin system on Windows:
  - <kbd>File</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Browse repositories...</kbd> > <kbd>Search for "Locator Updater"</kbd> > <kbd>Install Plugin</kbd>
- Using IDE built-in plugin system on MacOs:
  - <kbd>Preferences</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Browse repositories...</kbd> > <kbd>Search for "Locator Updater"</kbd> > <kbd>Install Plugin</kbd>
<!-- 
- Manually:
  - Download the [latest release](https://github.com/mplushnikov/lombok-intellij-plugin/releases/latest) and install it manually using <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Install plugin from disk...</kbd>
-->
Restart IDE.

### Plugin project dependency
Make sure you have Healenium dependency added to your project. This plugin **does not** automatically add it for you.

**Please Note:** Using newest version of the Healenium dependency is recommended, but does not guarantee that all the features introduced will be available. <!-- See [Lombok changelog](https://projectlombok.org/changelog.html) for more details. -->
