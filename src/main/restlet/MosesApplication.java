package restlet;  
  
import mlt.moses.MosesEngine;
import mlt.moses.MosesException;

import org.restlet.Application;  
import org.restlet.Restlet;  
import org.restlet.routing.Router;  
  
public class MosesApplication extends Application
{  
	MosesEngine mosesEngine;
	
	public MosesApplication() throws MosesException
	{
		System.err.println("Starting engine");
		mosesEngine = new MosesEngine();
		System.err.println("OK");
	}

	/** 
     * Creates a root Restlet that will receive all incoming calls. 
     */  
    @Override  
    public synchronized Restlet createInboundRoot() {  
        // Create a router Restlet that routes each call to a  
        // new instance of HelloWorldResource.  
        Router router = new Router(getContext());  
  
        // Defines only one route  
        router.attach("/translate", MosesResource.class);
  
        return router;  
    }
    
    public String translate(String segment) throws MosesException
    {
    	//segment = segment.replaceAll("\r", "\n").replaceAll("\n", "<br>");
    	//System.out.println(segment);
    	return mosesEngine.translate(segment);
	}
}