package org.websync.jdi;
import com.epam.jdi.light.elements.pageobjects.annotations.JSite;
import com.epam.jdi.light.elements.pageobjects.annotations.Url;

@JSite("https://todoist.com")
public class TodoistWebsite{
    @Url("/app/project/2246944533")
    public TodoistProjectPage todoistProjectPage;

    @Url("/app/project/2231738407")
    public PageEditorPage pageEditorPage;
}