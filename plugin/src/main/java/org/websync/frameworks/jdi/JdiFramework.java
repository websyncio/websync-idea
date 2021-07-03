package org.websync.frameworks.jdi;

public enum JdiFramework {
    ELEMENTS_MODULE("com.epam.jdi:jdi-light-html"),
    ELEMENTS_NAMESPACE("com.epam.jdi.light.elements"),
    HTML_ELEMENTS_NAMESPACE("com.epam.jdi.light.ui.html.elements");

    public final String value;

    JdiFramework(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
