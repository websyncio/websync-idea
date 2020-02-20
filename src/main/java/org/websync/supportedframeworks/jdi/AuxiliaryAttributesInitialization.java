package org.websync.supportedframeworks.jdi;


import com.epam.jdi.light.common.TextTypes;
import com.epam.jdi.light.elements.common.Label;
import com.epam.jdi.light.elements.pageobjects.annotations.*;

import static com.epam.jdi.light.common.ElementArea.CENTER;
import static com.epam.jdi.light.common.SetTextTypes.SET_TEXT;

public class AuxiliaryAttributesInitialization {

    @ClickArea(CENTER)
    private Label initializedWithClickArea;
    @GetAny
    private Label initializedWithGetAny;
    @GetShowInView
    private Label initializedWithGetShowInView;
    @GetTextAs(TextTypes.LABEL)
    private Label initializedWithGetTextAs;
    @GetVisible
    private Label initializedWithGetVisible;
    @GetVisibleEnabled
    private Label initializedWithGetVisibleEnabled;
    @Mandatory
    private Label initializedWithMandatory;
    @NoCache
    private Label initializedWithNoCache;
    @NoWait
    private Label initializedWithNoWait;
    @PageName("test")
    private Label initializedWithPageName;
    @Root
    private Label initializedWithRoot;
    @SetTextAs(SET_TEXT)
    private Label initializedWithSetTextAs;
    @Url("test")
    private Label initializedWithUrl;
    @VisualCheck
    private Label initializedWithVisualCheck;
    @WaitTimeout(2)
    private Label initializedWithWaitTimeout;
}
