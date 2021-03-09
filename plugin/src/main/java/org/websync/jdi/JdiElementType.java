package org.websync.jdi;

public enum JdiElementType {
    Undefined(0),
    Base(1),
    Common(2),
    Complex(3),
    Composite(4);

    public int value;

    JdiElementType(int value) {
        this.value = value;
    }
}
