websync-plugin
======================

[![CI Status](https://travis-ci.org/websyncio/websync-idea.svg?branch=master)](https://travis-ci.org/websyncio/websync-idea)
[![codecov](https://codecov.io/gh/websyncio/websync-idea/branch/master/graph/badge.svg)](https://codecov.io/gh/websyncio/websync-idea)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/86d6d10008844a7ca3e89984a309f241)](https://app.codacy.com/gh/websyncio/websync-idea?utm_source=github.com&utm_medium=referral&utm_content=websyncio/websync-idea&utm_campaign=Badge_Grade_Dashboard)


## Plugin for [IntelliJ IDEA](http://plugins.jetbrains.com/plugin/13171-locator-updater) to support [WebSync-Chrome](https://github.com/websyncio/websync-chrome). ##

Provides support for updating broken selenium locators with healed one via remote call with with IntelliJ IDEA

**Last version (0.0.1) released on 01.01.2021**
    
## Run plugin ##

- Clone source of websync plugin from https://github.com/websyncio/websync-idea/plugin 
- In IntelliJ Idea select <kbd>Edit Configuration</kbd> > <kbd>Gradle</kbd> with parameters:
    - Gradle project: <kbd>C:\path-to-directory-of\websync-idea</kbd>
    - Tasks: <kbd>:runIde</kbd>
- When the instance of the IntelliJ Idea is run and the dialog window ‘Welcome to IntelliJ IDEA’ is shown:
    - Click button ‘Open’
    - Select directory <kbd>C:\path-to-directory-of\websync-idea\supported-frameworks\jdi-x.x.x</kbd>
- Wait for the project to opened and INDEXED.

## Creating new test project with JDI page objects ##

In the IntelliJ Idea follow next steps: 
- Click <kbd>File</kbd> > <kbd>New</kbd> > <kbd>Project</kbd>
- Select menu point Gradle 
- Click Next 
- Select location directory <kbd>C:\path-to-directory-of\websync-idea\supported-frameworks</kbd> 
- Type name of project 
- Click Finish 
- Select ‘New Window’ in dialog ‘Open Project’ 
- Add next lines to file build.gradle: 
````
dependencies { 
    compile "com.epam.jdi:jdi-light:1.1.15" 
    compile "com.epam.jdi:jdi-light-html:1.1.15" 
} 
````
- Add classes inherited from WebPage and Segment to created project with page objects

## Debug Psi Structure of project
- Add next lines to Help > Edit Custom Properties:
idea.is.internal=true
- Tools -> View PSI Structure in order to research Psi Structure of project. <br/>
See more details https://www.jetbrains.com/help/idea/psi-viewer.html

