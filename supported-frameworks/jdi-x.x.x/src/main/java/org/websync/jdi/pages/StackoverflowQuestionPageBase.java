package org.websync.jdi.pages;

import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.Css;
import com.epam.jdi.light.ui.html.elements.common.Image;

public class StackoverflowQuestionPageBase extends WebPage {
    @Css(".owner .user-info .gravatar-wrapper-32 img")
    public Image AuthorAvatar;
}
