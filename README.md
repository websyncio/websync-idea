websync-plugin
======================

[![JetBrains Plugins](https://img.shields.io/jetbrains/plugin/v/13171-locator-updater.svg)](https://plugins.jetbrains.com/plugin/13171-locator-updater)
[![Build Status](https://github.com/websyncio/websync-idea/badge.svg)](https://github.com/websyncio/websync-idea/badge.svg)

## Plugin for [IntelliJ IDEA](http://plugins.jetbrains.com/plugin/13171-locator-updater) to support [WebSync-Chrome](https://github.com/websyncio/websync-chrome). ##

Provides support for updating broken selenium locators with healed one via remote call with with IntelliJ IDEA.

**Last version (0.0.1) released on 19.11.2019**

This plugin is based on Healenium plugin (https://github.com/healenium/healenium-plugin)

Compilation
------------
### Plugin Compilation
- Clone source of healenium plugin from https://github.com/healenium/healenium-plugin
- Comment the line 'token intellijPublishToken' in the file src/main/resources/META-INF/plugin.xml
- To run the plugin from IntelliJ Idea choose <kbd>Edit Configuration</kbd> 'Gradle' with parameters:
    - Gradle project: <kbd>C:\path-to-directory-of\websync-idea</kbd>
    - Tasks: <kbd>':runIde'</kbd>

