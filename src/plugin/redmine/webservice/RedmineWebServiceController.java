package plugin.redmine.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import plugin.redmine.dataModel.RedmineConfig;
import plugin.redmine.dataModel.RedmineIssue;
import plugin.redmine.dataModel.RedmineIssueDescriptor;
import plugin.redmine.dataModel.RedmineIssueStatus;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RedmineWebServiceController {
	private RedmineConfig _redmineConfig;
	public RedmineWebServiceController( RedmineConfig redmineConfig ){
		_redmineConfig = redmineConfig;
	}
	
	//call redmine web service to get issues by status
	public String getIssuesJsonFromRedmine( RedmineIssueStatus redmineIssueStatus ){
		Client client = Client.create();
		//get in progress issues from redmine
		WebResource webResource = client.resource( _redmineConfig.getRedmineUrl() + ".json?status_id=" + redmineIssueStatus.getValue() );
		String jsonString = webResource.accept("application/json").get(String.class);
		List<RedmineIssue> redmineIssueList = new ArrayList<RedmineIssue>();
		JsonParser parser = new JsonParser();
		JsonObject o = (JsonObject)parser.parse(jsonString);
		JsonArray array = o.getAsJsonArray("issues");
		//arrange json data from redmine web service
		for( JsonElement jsonElement : array ){
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			int storyId = jsonObject.get("id").getAsInt();
			String redmineIssueName = jsonObject.get("subject").getAsString();
			String redmineIssueDescription = jsonObject.get("description").getAsString();
			
			JsonObject redmineIssueTrackerJsonObject = jsonObject.get("tracker").getAsJsonObject();
			String redmineIssueTag = redmineIssueTrackerJsonObject.get("name").getAsString();
			
			JsonObject redmineIssueStatusJsonObject = jsonObject.get("status").getAsJsonObject();
			int redmineIssueStatusId = redmineIssueStatusJsonObject.get("id").getAsInt();
			
			RedmineIssue redmineIssue = new RedmineIssue( storyId, redmineIssueName, redmineIssueDescription, redmineIssueStatusId, redmineIssueTag );
			redmineIssueList.add( redmineIssue );
		}
		Gson gson = new Gson();
		int totalCount = o.get("total_count").getAsInt();
		RedmineIssueDescriptor redmineIssueDescriptor = new RedmineIssueDescriptor( totalCount, redmineIssueList );
		String redmineIssueDescriptorString = gson.toJson( redmineIssueDescriptor );

		return redmineIssueDescriptorString;
	}
	
	//call redmine web service to update issues status
	public void changeRedmineIssueStatus( int redmineIssueId , RedmineIssueStatus redmineIssueStatus ,String redmineAccessKey ){
		Client client = Client.create();
		WebResource webResource = client.resource( _redmineConfig.getRedmineUrl() + "/"+redmineIssueId+".json?key="+ redmineAccessKey );
		String redmineJSONString = "{    \"issue\":{ \"status_id\":"+redmineIssueStatus.getValue()+"} }";
		webResource.accept(new String[] {MediaType.APPLICATION_JSON}).entity(redmineJSONString, MediaType.APPLICATION_JSON).put(ClientResponse.class);
	}
}
