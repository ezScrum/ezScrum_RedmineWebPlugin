package plugin.redmine.protocol;

import java.io.IOException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import plugin.redmine.DAO.PluginConfigDAO;
import plugin.redmine.dataModel.RedmineConfig;

import com.google.gson.Gson;

public class ConfigAction {
	public void doGetPluginConfig(  StaplerRequest request, StaplerResponse response ){    	
    	String projectID = SessionManager.getProjectID(request);
    	
    	//find plugin config by projectId
    	PluginConfigDAO pluginConfigDAO = new PluginConfigDAO();
    	RedmineConfig redmineConfig = pluginConfigDAO.getRedmineConfig( projectID );
    	Gson gson = new Gson();
    	String jsonString = gson.toJson( redmineConfig );
    	try {
    		response.setCharacterEncoding("utf-8");//solve Chinese character problem
			response.getWriter().write( jsonString );
	    	response.getWriter().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
	
	public void doSavePluginConfig( StaplerRequest request, StaplerResponse response ){	
    	String projectID = SessionManager.getProjectID(request);
		
    	String redmineUrl      = request.getParameter("redmineUrl");
		String redmineAccessKey = request.getParameter("redmineAccessKey");
		
		PluginConfigDAO pluginConfigDAO = new PluginConfigDAO();

		RedmineConfig expectedAddPluginConfig = new RedmineConfig( redmineUrl, redmineAccessKey);
		
		RedmineConfig exitedPluginConfig = pluginConfigDAO.getRedmineConfig( projectID );
		
		if( exitedPluginConfig == null ){ 
			//if pluginConfig is not existed, insert it
			pluginConfigDAO.insertConfig( projectID, expectedAddPluginConfig );
		}else{ 
			//if pluginConfig existed, update it
			pluginConfigDAO.updateConfig( projectID, expectedAddPluginConfig);
		}
		
	}
}
