package org.websync.jdi.pages;
import com.epam.jdi.light.asserts.generic.UIAssert;
import com.epam.jdi.light.elements.base.UIBaseElement;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.UI;
import com.epam.jdi.light.ui.html.elements.common.Text;
import com.epam.jdi.light.ui.html.elements.common.TextField;

public class ProjectsList extends UIBaseElement<UIAssert> {
    @UI("#search")
    public TextField SearchInput;

    @UI("[type='submit']")
    public Text SendButton;

    @UI("button['Cancel']")
    public Text CancelButton;
}