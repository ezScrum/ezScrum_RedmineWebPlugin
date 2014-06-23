package plugin.redmine.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import ch.ethz.ssh2.crypto.Base64;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import plugin.redmine.dataModel.Story;

public class EzScrumWebServiceController {
	private String ezScrumURL;
	public EzScrumWebServiceController( String ezScrumURL ){
		this.ezScrumURL = ezScrumURL;
	}
	
	public Story getStory( String account, String encodedPassword, String projectID, String storyID ){
		String encodedUserName = new String( Base64.encode( account.getBytes() ) );
		
		//需要的帳密為暗碼
		String getStoryWebServiceUrl = "http://"+this.ezScrumURL+"/web-service/"
				+ projectID.replace(" ", "%20") + "/product-backlog/storylist/"+ storyID.replace(" ", "%20")+"?userName="+encodedUserName+"&password="+encodedPassword;
		 
		Client client = Client.create();
		WebResource webResource = client.resource( getStoryWebServiceUrl );
	    
		Builder result = webResource.type(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON);
		JSONObject resultJSONObject = result.get(JSONObject.class);
		Story story = null;
		try {
			JSONArray storyJSONArray= resultJSONObject.getJSONArray("productBacklogList");
			
			if( storyJSONArray.length() == 0 ){
				return null;
			}
			JSONObject storyJSONObject = storyJSONArray.getJSONObject(0).getJSONObject("productBacklog");
			JSONArray tagJSONArray = storyJSONObject.getJSONArray("tag");
			List<String> tagList = new ArrayList<String>();
			for( int i = 0 ; i < tagJSONArray.length() ; i++ ){
				tagList.add( tagJSONArray.getString( i ) );
			}
			story = new Story(
					storyJSONObject.getString("id"),
					storyJSONObject.getString("name"), 
					storyJSONObject.getString("notes"), 
					storyJSONObject.getInt("value"), 
					storyJSONObject.getInt("estimation"), 
					storyJSONObject.getInt("importance"), 
					storyJSONObject.getString("howToDemo"),
					storyJSONObject.getString("status"),
					tagList);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		return story;
	}
	
	public String saveStoryToEzScrum( String account, String encodedPassword, String projectID, Story story ){
		
		byte[] pwd = null;
		try {
			pwd = Base64.decode( encodedPassword.toCharArray() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//解密
		String password = new String( pwd );
		
		//需要的帳密為明碼
		String addStoryWebServiceUrl = "http://"+this.ezScrumURL+"/web-service/"
				+ projectID.replace(" ", "%20") + "/product-backlog/story";
		
		JSONObject storyJSONObject = new JSONObject();
		
		JSONObject userInformation = new JSONObject();
		JSONObject storyProperties = new JSONObject();
		try {
			userInformation.put("account", account);
			userInformation.put("password", password);
			storyJSONObject.put("userInformation", userInformation);
			
			storyProperties.put("name", story.getName());
			storyProperties.put("value", story.getValue());
			storyProperties.put("importance", story.getImportance());
			storyProperties.put("estimation", story.getEstimation());
			storyProperties.put("notes", story.getNotes()); // TOFIX
			storyProperties.put("howToDemo", story.getHowToDemo()); // TOFIX
			storyProperties.put("tag", getTagArray(story));
			storyProperties.put("sprint", "-1");
			
			storyJSONObject.put("storyProperties", storyProperties);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		Client client = Client.create();
		WebResource webResource = client.resource( addStoryWebServiceUrl );
	    
		JSONObject t = webResource.type(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .post(JSONObject.class, storyJSONObject);
		try {
			if( t.getString("status").equals("SUCCESS") ){
				return t.getString("issueID") ;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private JSONArray getTagArray(Story story) {
		JSONArray tagArray = new JSONArray();
		List<String> tagList = story.getTagList();
		if( tagList != null ){
			for (String tag : tagList) {
				tagArray.put(tag);
			}
		}
		return tagArray;
	}
	
}
