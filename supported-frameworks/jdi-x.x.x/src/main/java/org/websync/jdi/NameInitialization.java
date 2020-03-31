package org.websync.jdi;


import com.epam.jdi.light.elements.common.Label;
import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.Name;
import com.epam.jdi.light.ui.html.elements.common.Image;
import com.epam.jdi.light.ui.html.elements.common.Text;

public class NameInitialization extends WebPage {

    @Name("test")
    public Label labelInitializedWithName;
    @Name("test1")
    public Text textInitializedWithName;
    @Name("test2")
    public Image imageInitializedWithName;
}
