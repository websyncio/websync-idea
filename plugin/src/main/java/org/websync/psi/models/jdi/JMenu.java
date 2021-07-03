package org.websync.psi.models.jdi;

import java.util.List;

public class JMenu {
    public List<String> value;
    public String group;

    @Override
    public String toString() {
        return "JMenu{" +
                "value=" + value +
                ", group='" + group + '\'' +
                '}';
    }
}
