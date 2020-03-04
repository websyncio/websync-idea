package org.websync.jdi;


import com.epam.jdi.light.common.TextTypes;
import com.epam.jdi.light.elements.common.Label;
import com.epam.jdi.light.elements.pageobjects.annotations.*;

import static com.epam.jdi.light.common.ElementArea.CENTER;
import static com.epam.jdi.light.common.SetTextTypes.SET_TEXT;

public class AuxiliaryAttributesInitialization {

    @ClickArea(CENTER)
    public Label initializedWithClickArea;
    @GetAny
    public Label initializedWithGetAny;
    @GetShowInView
    public Label initializedWithGetShowInView;
    @GetTextAs(TextTypes.LABEL)
    public Label initializedWithGetTextAs;
    @GetVisible
    public Label initializedWithGetVisible;
    @GetVisibleEnabled
    public Label initializedWithGetVisibleEnabled;
    @Mandatory
    public Label initializedWithMandatory;
    @NoCache
    public Label initializedWithNoCache;
    @NoWait
    public Label initializedWithNoWait;
    @PageName("test")
    public Label initializedWithPageName;
    @Root
    public Label initializedWithRoot;
    @SetTextAs(SET_TEXT)
    public Label initializedWithSetTextAs;
    @Url("test")
    public Label initializedWithUrl;
    @VisualCheck
    public Label initializedWithVisualCheck;
    @WaitTimeout(2)
    public Label initializedWithWaitTimeout;
}
