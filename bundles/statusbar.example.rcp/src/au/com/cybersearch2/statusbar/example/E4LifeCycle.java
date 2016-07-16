package au.com.cybersearch2.statusbar.example;

import javax.annotation.PreDestroy;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;

import au.com.cybersearch2.controls.ControlFactory;
import au.com.cybersearch2.controls.ImageFactory;
import au.com.cybersearch2.statusbar.StatusBar;

/**
 * This is a stub implementation containing e4 LifeCycle annotated methods.<br />
 * There is a corresponding entry in <em>plugin.xml</em> (under the
 * <em>org.eclipse.core.runtime.products' extension point</em>) that references
 * this class.
 **/
public class E4LifeCycle {

    ImageFactory imageFactory;
    ExampleStatusBar exampleStatusBar;
    
	@PostContextCreate
	void postContextCreate(IEclipseContext workbenchContext) 
	{
	    workbenchContext.set(ControlFactory.class, new ControlFactory());
        workbenchContext.set(StatusBar.class, new StatusBar());
        imageFactory = ContextInjectionFactory.make(ImageFactory.class, workbenchContext);
        workbenchContext.set(ImageFactory.class, imageFactory);
        // Install image and SWT widget factories
        imageFactory.setResourceBundle(Activator.getBundle());
        imageFactory.registerCustomFactory(new PresenceImageFactory());
        exampleStatusBar = ContextInjectionFactory.make(ExampleStatusBar.class, workbenchContext);
        
        // Window static method used to get logo in top left windows 
        Image[] imageArray = new Image[]{imageFactory.getImage("icons/sample.png")};
        Window.setDefaultImages(imageArray);
	}

	@PreSave
	void preSave(IEclipseContext workbenchContext) 
	{
	}

	@ProcessAdditions
	void processAdditions(IEclipseContext workbenchContext) 
	{
	}

	@ProcessRemovals
	void processRemovals(IEclipseContext workbenchContext) 
	{
	}
	
	@PreDestroy
	void preDestroy()
	{
	    if (imageFactory != null)
	        imageFactory.dispose();
	}
}
