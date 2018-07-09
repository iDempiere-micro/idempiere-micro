package software.hsharp.idempiere.api;

import org.glassfish.jersey.internal.OsgiRegistry;
import org.glassfish.jersey.internal.util.ReflectionHelper;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import javax.servlet.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Logger;

//import org.glassfish.jersey.server.internal.scanning.CompositeResourceFinder;
//import org.glassfish.jersey.server.internal.scanning.PackageNamesScanner;

@Component(
		property = {
			HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN + "=/idempiere/api/*",
		})
public class Config implements Servlet {

	private transient ServletContainer sc;
	//logger.info() to log
	private static final Logger logger = Logger.getLogger(Config.class.getName());

	public Config() throws URISyntaxException, ClassNotFoundException{
		ResourceConfig rc = new ResourceConfig();
		
		//rc.packages() not working properly atm..., add all classes manually here
		//rc.registerClasses(Status.class /*, AnotherResource.class, ...*/);
		
		String[] packages = {
            "software.hsharp.idempiere.api.servlets.impl"
        };
		ClassLoader cl = getClass().getClassLoader();
		boolean recur = false;
    	OsgiRegistry reg = ReflectionHelper.getOsgiRegistryInstance();
        logger.info("Created! OSGI runtime? " + (reg == null? "No" : "Yes"));
        
        //CompositeResourceFinder finder = new CompositeResourceFinder();
        //BundleSchemeResourceFinderFactory f = new BundleSchemeResourceFinderFactory(); until it is made public...
        
        for(String p: packages){
        	p = p.replace('.', '/');
        	Enumeration<URL> list = reg.getPackageResources(p, cl, recur);
            while(list.hasMoreElements()){
            	URL url = list.nextElement();
            	String path = url.getPath();
            	String cls = (OsgiRegistry.bundleEntryPathToClassName(path, "") + "class").replace(".class", "");
            	logger.info("Found(" + url.toURI().getScheme() + "):" + path + "-->" + cls);
            	
            	rc.registerClasses(reg.classForNameWithException(cls));
            	//finder.push(f.create(url.toURI(), recur));
            }
        }
        //rc.registerFinder(finder);

		//rc.registerFinder(new PackageNamesScanner(cl, packages, false)); !bundleentry is not bundle...
        //INFO: Found(bundleentry):/com/demo/app/restapi/impl/Status.class-->com.demo.app.restapi.impl.Status.
		
        //////////////////////////////////////////////////////////////////////////
		this.sc = new ServletContainer(rc);
	}
	
    @Activate
    void activate(ComponentContext context) { 
		logger.info("Activating... " + getClass()); 
		logger.info("Activated... " + getClass());
	}

    @Deactivate
	void deactivate(ComponentContext context) { logger.info("Deactivated... " + getClass()); }
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		this.sc.destroy();
	}

	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return this.sc.getServletConfig();
	}

	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return this.sc.getServletInfo();
	}

	@Override
	public void init(ServletConfig cfg) throws ServletException {
		// TODO Auto-generated method stub
		this.sc.init(cfg);
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse response) throws ServletException, IOException {
		this.sc.service(arg0, response);
	}


}