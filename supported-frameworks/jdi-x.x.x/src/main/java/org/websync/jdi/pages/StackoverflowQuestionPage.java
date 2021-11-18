package org.websync.jdi.pages;

import com.epam.jdi.light.elements.common.Label;
import com.epam.jdi.light.elements.common.UIElement;
import com.epam.jdi.light.elements.complex.Checklist;
import com.epam.jdi.light.elements.complex.Menu;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.*;
import com.epam.jdi.light.ui.html.elements.common.*;

import static com.epam.jdi.light.elements.init.UIFactory.$;

public class StackoverflowQuestionPage extends StackoverflowQuestionPageBase {
    @Css(".owner .user-details")
    public WebElement QuestionTitle;

    @Css(".owner .user-details")
    public WebElement QuestionBody;

    @Css(".owner .user-details")
    public Checklist AuthorName;

    @Css(".owner .user-info .gravatar-wrapper-32 img")
    public WebImage AuthorAvatar;

    @UI("#filters_list")
    public FiltersList FiltersList;

    @UI("[data-testid='project-list-view']")
    public TodoList TodoList;

    @UI("a[>.action_label['Комментарии']]")
    public WebElement CommentsButton;

    @UI("button[>.action_label['Общий доступ']]")
    public WebElement ShareButton;

    @UI("button[.action_label['Сортировать']]")
    public WebButton Button;

    void aaa(){
        UIElement element = $("");
    }

    @UI(".markdown_content")
    public WebElement TaskContent;
}