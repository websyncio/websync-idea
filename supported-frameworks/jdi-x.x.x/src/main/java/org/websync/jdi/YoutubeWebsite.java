package org.websync.jdi;

import com.epam.jdi.light.elements.pageobjects.annotations.JSite;
import com.epam.jdi.light.elements.pageobjects.annotations.Url;
import org.websync.jdi.pages.YoutubePage;

@JSite("https://www.youtube.com")
public class YoutubeWebsite {
    @Url("/watch?v=QLo92mDMqCI")
    public org.websync.jdi.pages.YoutubePage YoutubePage;
}
