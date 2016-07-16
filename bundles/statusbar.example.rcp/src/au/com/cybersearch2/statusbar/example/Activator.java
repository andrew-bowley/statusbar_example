package au.com.cybersearch2.statusbar.example;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator 
{

	private static BundleContext context;

    /** Bundle in BundleContext of most recent start() call */
    private static Bundle bundle;
 
	static BundleContext getContext() 
	{
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception
    {
        setContext(bundleContext);
        setBundle(bundleContext.getBundle());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception 
	{
		Activator.context = null;
	}

    /**
     * Set bundle context singleton. Keeps Findbugs happy when set by method call from non-static class member.
     * @return BundleContext
     */
    static void setContext(BundleContext bundleContext)
    {
        context = bundleContext;
    }

   /**
     * Set bundle singleton. Keeps Findbugs happy when set by method call from non-static class member.
     * @return Bundle
     */
    static void setBundle(Bundle value)
    {
        bundle = value;
    }
    
    static public Bundle getBundle()
    {
        return bundle;
    }
    
}
