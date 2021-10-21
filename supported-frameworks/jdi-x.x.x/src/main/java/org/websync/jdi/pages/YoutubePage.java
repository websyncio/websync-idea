package org.websync.jdi.pages;

import com.epam.jdi.light.elements.common.Label;
import com.epam.jdi.light.elements.composite.WebPage;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.Css;
import com.epam.jdi.light.elements.pageobjects.annotations.locators.UI;
import com.epam.jdi.light.ui.html.elements.common.Button;
import com.epam.jdi.light.ui.html.elements.common.Image;
import com.epam.jdi.light.ui.html.elements.common.Text;
import com.epam.jdi.light.ui.html.elements.common.TextField;

public class YoutubePage extends WebPage {
    @UI("input#search")
    public TextField QuestionTitle;

    @UI("#menu yt-formatted-string[1]")
    public Cus LikesCount;

    @UI("#menu yt-formatted-string[2]")
    public Text DislikesCount;

    @UI("#meta-contents #subscribe-button")
    public Button SubscribeButton;

    @UI("ytd-button-renderer ['Поделиться']")
    public Button ShareButton;

    @UI("#masthead-container #logo-icon")
    public Image LogoIcon;
}
