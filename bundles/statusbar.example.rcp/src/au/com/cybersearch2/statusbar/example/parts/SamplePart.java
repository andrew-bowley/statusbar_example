/*******************************************************************************
 * Copyright (c) 2010 - 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package au.com.cybersearch2.statusbar.example.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import au.com.cybersearch2.controls.ImageFactory;
import au.com.cybersearch2.statusbar.example.Events;
import au.com.cybersearch2.statusbar.example.Presence;

/**
 * Application model part to allow user to send events to the status bar to
 * update it's controls. The example has a chat messaging theme.
 * A set of radio buttons set the user's availabilty
 * to be contacted by a messaging system - called "presence".
 * Values are "online", "away" and "do not disturb". A text field allows the
 * user to add a message for users wishing to make contact eg. "Out to lunch".
 * A checkbox is used to indicate if the messaging connection is secure or
 * unsecure.
 * SamplePart
 * @author Andrew Bowley
 * 4 Jun 2016
 */
public class SamplePart 
{
    // Options for the radio buttons
    public static Presence[] presences;

    static
    {
        presences = new Presence[] { Presence.online, Presence.away, Presence.dnd };
    }

	Label messageLabel;
    Text messageText;
    Button menuDemoCheck;
    Label presenceLabel;
    Button secureCheck;

    /** Listener sets presence when selection made by user */
    FocusListener focusListener = new FocusListener(){

        @Override
        public void focusGained(FocusEvent event)
        {
            Button button = (Button)event.getSource();
            Presence presence = (Presence) button.getData();
            eventBroker.post(Events.PRESENCE, presence);
        }

        @Override
        public void focusLost(FocusEvent e)
        {
        }
    };

    /** Listener updates message */
    ModifyListener modifyListener = new ModifyListener() {
        @Override
        public void modifyText(ModifyEvent e) 
        {
              eventBroker.post(Events.USER_MESSAGE, messageText.getText());
        }
    };
    
    /** Listener updates secure status */
    SelectionListener selectionListener = new SelectionAdapter() {
        
        public void widgetSelected(SelectionEvent e) 
        {
            if (e.getSource() == secureCheck)
                eventBroker.post(Events.SECURE, secureCheck.getSelection());
            else
                eventBroker.post(Events.MENU_DEMO, menuDemoCheck.getSelection());
        }
    };
     
    /** Event broker service */
    @Inject IEventBroker eventBroker;

    /**
     * postConstruct
     * @param parent Parent composite
     * @param imageFactory Image factory
     */
	@PostConstruct
	public void createComposite(Composite parent, ImageFactory imageFactory) 
	{
        // Create the top level composite for the login window
	    Composite top = new Composite(parent, SWT.NONE);
        GridLayout topLayout = new GridLayout();
        topLayout.marginHeight = 0;
        topLayout.marginWidth = 0;
        topLayout.verticalSpacing = 0;
        top.setLayout(topLayout);
        top.setLayoutData(new GridData(GridData.FILL_BOTH));
        // Create 2-column layout
        Composite composite = new Composite(top, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        messageLabel = new Label(composite, SWT.NONE);
        messageLabel.setText("Message:");
        GridData messageLabelLayout =
            new GridData(SWT.END, SWT.CENTER, false, false);
        messageLabel.setLayoutData(messageLabelLayout);
        messageText = new Text(composite, SWT.BORDER );
        GridData messageGridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, SWT.FILL, true, false);
        messageGridData.widthHint = convertHeightInCharsToPixels(parent, 25);
        messageText.setLayoutData(messageGridData);
        messageText.addModifyListener(modifyListener);
        presenceLabel = new Label(composite, SWT.NONE);
        presenceLabel.setText("Presence:");
        GridData presenceLabelLayout =
            new GridData(SWT.END, SWT.CENTER, false, false);
        presenceLabel.setLayoutData(presenceLabelLayout);
        Group group = new Group(composite, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(group);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(group);
        for (int i = 0; i < presences.length; i++) 
        {
            Button button = new Button(group, SWT.RADIO);
            button.setText(presences[i].getDisplayText());
            button.setImage(imageFactory.getMappedImage(presences[i]));
            button.setData(presences[i]);
            button.addFocusListener(focusListener);
        }
        secureCheck = new Button(composite, SWT.CHECK);
        secureCheck.setText("Secure:");
        GridData secureCheckLayout =
                new GridData(SWT.BEGINNING, SWT.CENTER, true, true, 2, 1);    
        secureCheck.setLayoutData(secureCheckLayout);
        secureCheck.setSelection(false);
        secureCheck.addSelectionListener(selectionListener);
        menuDemoCheck = new Button(composite, SWT.CHECK);
        menuDemoCheck.setText("Menu demo");
        GridData menuDemoCheckLayout =
                new GridData(SWT.BEGINNING, SWT.CENTER, true, true, 2, 1);    
        menuDemoCheck.setLayoutData(menuDemoCheckLayout);
        menuDemoCheck.setSelection(false);
        menuDemoCheck.addSelectionListener(selectionListener);
	}

	@Focus
	public void setFocus() 
	{
	    messageText.setFocus();
	}

    /**
     * Use font metrics to convert height is characters to pixels
     * @param parent Composite
     * @param count Character count
     * @return Number of pixels
     */
    public int convertHeightInCharsToPixels(Composite parent, int count)
    {
        GC gc = new GC(parent);
        gc.setFont(parent.getFont());
        FontMetrics fontMetrics = gc.getFontMetrics();
        gc.dispose();
        return fontMetrics.getHeight() * count;
    }
}