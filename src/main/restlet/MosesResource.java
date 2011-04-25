package restlet;  
  
import mlt.moses.MosesException;

import org.restlet.resource.Get;  
import org.restlet.resource.ServerResource;  
  
/** 
 * Resource which has only one representation. 
 *  
 */  
public class MosesResource extends ServerResource
{  
    @Get  
    public String represent() throws MosesException
    {
    	System.err.println(getQuery().getFirst("segment"));
    	String result = "";
    	if (getQuery().getFirst("segment") == null) result = "-- Wrong Query --";
    	else result =((MosesApplication) this.getApplication()).translate(getQuery().getFirst("segment").getValue());
    	return result;
    }
}  