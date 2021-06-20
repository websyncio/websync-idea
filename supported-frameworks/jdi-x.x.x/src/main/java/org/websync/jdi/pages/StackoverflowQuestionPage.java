package org.websync.jdi.pages;
import com.epam.jdi.light.elements.common.Label;
import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.*;
import com.epam.jdi.light.ui.html.elements.common.*;

public class StackoverflowQuestionPage extends WebPage {
    @Css(".owner .user-details")
    Label QuestionTitle;

    @Css(".owner .user-details")
    Label QuestionBody;

    @Css(".owner .user-details")
    public Label AuthorName;

    @Css(".owner .user-info .gravatar-wrapper-32 img")
    public Image AuthorAvatar;
}