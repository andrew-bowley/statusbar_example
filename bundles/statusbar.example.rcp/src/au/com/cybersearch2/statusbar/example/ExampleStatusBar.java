/**
    Copyright (C) 2016  www.cybersearch2.com.au

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/> */
package au.com.cybersearch2.statusbar.example;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import au.com.cybersearch2.controls.ControlFactory;
import au.com.cybersearch2.controls.ImageFactory;
import au.com.cybersearch2.statusbar.LabelListener;
import au.com.cybersearch2.statusbar.StatusBar;
import au.com.cybersearch2.statusbar.StatusItem;
import au.com.cybersearch2.statusbar.controls.ItemConfiguration;

/**
 * StatusBar
 * Status line control built on Statusbar plugin.
 * <p>
 * The Status bar consists of a StatusLine control, which is right-aligned with the main window and
 * zero or more StatusLineContribution controls which are arranged to fill the remaining Status bar 
 * space. The last control to be contributed is right-aligned with the main window. If there is more than one 
 * contribution, then the second last one is expaned to fill the remaining space. Having more than 3 controls in
 * the Status bar is not recommended, but there is really no limit on how many may be inserted.
 * </p>
 * @author Andrew Bowley
 * 3 Jun 2016
 */
public class ExampleStatusBar
{
    /** 
     * All StatusBar controls are defined using a {@link au.com.cybersearch2.statusbar.controls.ItemConfiguration#} object
     * which has a "hintWidth" value. This parameter is interpreted as the width in number of characters. If set to zero,
     * then then the extent of the control is evaluated using the text to be displayed. The following value is
     * assigned as the width of the first control, which may display "Do not disturb", which has 14 characters. */
    private final static int LEFT_CHAR_WIDTH = 18;

    private static final String LOGGING_ON = "Logging on...";
     
    static ItemConfiguration MIDDLE_CONFIGURATION;
    static ItemConfiguration LOGIN_CONFIGURATION;
     
    static
    {
       MIDDLE_CONFIGURATION = new ItemConfiguration(null, "- Status Middle -", 0);
       LOGIN_CONFIGURATION = new ItemConfiguration(null, LOGGING_ON, LOGGING_ON.length());
    }
     
     /** First item aligned to left of client window area */
     StatusItem left;
     /** Second of 3 items aligned with left item and expands to fill space up to right item */
     StatusItem middle;
     /** Third of 3 items right-aligned with client window area */
     StatusItem right;
     /** Reserve item used to display message while middle is used for menu demo */
     StatusItem reserved;
     /** When menu demo is selected, the middle item acquires an image and a context menu */
     boolean menuDemoSelected;
     /** 
      * Current message field content, which by default is displayed in the middle item, 
      * but when middle item is used for menu demo, the message is displayed in the reserved item */  
     String userText;

     /** Container for StatusItem objects */
     @Inject
     StatusBar statusBar;
     /** Image factory and resource manager */
     @Inject
     ImageFactory imageFactory;
     /** SWT widget factory */
     @Inject
     ControlFactory controlFactory;
     
     /** Synchronizes current thread while executing in the UI-Thread */
     @Inject
     UISynchronize sync;
     
    /**
     * postConstruct
     */
    @PostConstruct
    void postConstruct()
    {
        userText = "";
        LOGIN_CONFIGURATION.setImage(imageFactory.getImage("icons/blank.gif"));
        left = new StatusItem(new ItemConfiguration(null, "Status Left", LEFT_CHAR_WIDTH), 0);
        middle = new StatusItem(MIDDLE_CONFIGURATION, 1);
        middle.setLabelListener(new LabelListener(){

            @Override
            public void onLabelCreate(CLabel label)
            {
                if (menuDemoSelected)
                    middle.setMenu(createMenu(label));
            }});
        right = new StatusItem(new ItemConfiguration(null, "Status Right", 0), 3);
        statusBar.addStatusItem(left);
        statusBar.addStatusItem(middle);
        statusBar.addStatusItem(right);
    }
    
    /**
     * Handle user message modified event
     * @param message The new message to display
     */
    @Inject @Optional
    void onUserMessage(final @UIEventTopic(Events.USER_MESSAGE) String message)
    {
        userText = message;
        if (!menuDemoSelected)
            middle.setText(message);
        else
        {
            if (reserved == null)
            {
                ItemConfiguration messageConfig = new ItemConfiguration(null, message, 0);
                reserved = new StatusItem(messageConfig, 2);
                statusBar.addStatusItem(reserved);
                //reserved.update(messageConfig);
            }
            else
                reserved.setText(message);
        }
    }
 
    /**
     * Handle presence updated event
     * @param presence The new presence value
     */
    @Inject @Optional
    void onPresence(final @UIEventTopic(Events.PRESENCE) Presence presence)
    {
        left.setLabel(presence.getDisplayText(), imageFactory.getMappedImage(presence));
    }

    /**
     * Handle secure status updated
     * @param isSecure Flag set true if secure status achieved
     */
    @Inject @Optional
    void onSecure(final @UIEventTopic(Events.SECURE) Boolean isSecure)
    {
        String imageFile = isSecure ? "icons/secure.gif" : "icons/unsecure.gif";
        Image image = imageFactory.getImage(imageFile);
        if (!right.getText().isEmpty())
            // Remove text as this is an image-only control
            right.update(new ItemConfiguration(image, "", SWT.DEFAULT));
        else
            right.setImage(image);
    }

    @Inject @Optional
    void onMenuDemo(final @UIEventTopic(Events.MENU_DEMO) Boolean  isSelected)
    {
        menuDemoSelected = isSelected;
        if (isSelected)
            onLogin();
        else
        {
            disposeMenu();
            if (reserved != null)
                reserved.setText("");
            if (!userText.isEmpty())
                middle.update(new ItemConfiguration(null, userText, 0));
            else
                middle.update(MIDDLE_CONFIGURATION);
         }
    }
    
    private void disposeMenu()
    {
        if (middle.getMenu() != null)
        {
            middle.getMenu().dispose();
            middle.setMenu(null);
        }
    }

    /**
     * Add "New Login" item to context menu
     */
    private void addNewLogin(Menu newMenu)
    {
        MenuItem menuItem = controlFactory.menuItemInstance(newMenu, SWT.PUSH);
        menuItem.setText("New Login"); 
        menuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) 
            {   // Clear color icon at start of new login
                //logoutPending = true;
                doLogin();
            }
        });
    }

    /**
     * Create context menu
     */
    private Menu createMenu(Control control)
    {
        Menu newMenu = controlFactory.menuInstance(control);
        control.setMenu(newMenu);
        addNewLogin(newMenu);
        return newMenu;
    }
    
    private void doLogin()
    {
        middle.update(LOGIN_CONFIGURATION);
        Job loginTask = new Job("Log in")
        {

            @Override
            public IStatus run(IProgressMonitor monitor)
            {
                try
                {
                    Thread.sleep(500);
                }
                catch (InterruptedException e)
                {
                }
                onLogin();
                return Status.OK_STATUS;
            }
        };
        loginTask.schedule();
    }
    
    private void onLogin()
    {
        sync.asyncExec(new Runnable(){

            @Override
            public void run()
            {
                middle.setLabel("Logged in", imageFactory.getImage("icons/green_circle.gif"));
           }});
    }
}
