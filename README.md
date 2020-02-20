websync-plugin
======================

[![JetBrains Plugins](https://img.shields.io/jetbrains/plugin/v/13171-locator-updater.svg)](https://plugins.jetbrains.com/plugin/13171-locator-updater)
[![CI Status](https://travis-ci.org/websyncio/websync-idea.svg?branch=master)](https://travis-ci.org/websyncio/websync-idea)
[![codecov](https://codecov.io/gh/websyncio/websync-idea/branch/master/graph/badge.svg)](https://codecov.io/gh/websyncio/websync-idea)


## Plugin for [IntelliJ IDEA](http://plugins.jetbrains.com/plugin/13171-locator-updater) to support [WebSync-Chrome](https://github.com/websyncio/websync-chrome). ##

Provides support for updating broken selenium locators with healed one via remote call with with IntelliJ IDEA.

**Last version (0.0.1) released on 01.01.2020**

## Plugin Compilation
- Clone source of healenium plugin from https://github.com/healenium/healenium-plugin
- Comment the line 'token intellijPublishToken' in the file src/main/resources/META-INF/plugin.xml
- To run the plugin from IntelliJ Idea choose <kbd>Edit Configuration</kbd> > <kbd>Gradle</kbd> with parameters:
    - Gradle project: <kbd>C:\path-to-directory-of\websync-idea</kbd>
    - Tasks: <kbd>:runIde</kbd>
