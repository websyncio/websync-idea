package org.websync.jdi.pages;
import com.epam.jdi.light.elements.common.Label;
import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.*;
import com.epam.jdi.light.ui.html.elements.common.*;

public class StackoverflowQuestionPage extends WebPage {
    Label1 QuestionTitle;

    Label1 QuestionBody;

    @Css(".owner .user-details")
    public Label AuthorName;

    @Css(".owner .user-info .gravatar-wrapper-32 img")
    public Image AuthorAvatar;
}