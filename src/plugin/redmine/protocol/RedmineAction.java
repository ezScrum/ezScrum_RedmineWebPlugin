package plugin.redmine.protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import ntut.csie.ezScrum.pic.core.IUserSession;
import ntut.csie.protocal.Action;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import plugin.redmine.DAO.PluginConfigDAO;
import plugin.redmine.DAO.RedmineRelationDAO;
import plugin.redmine.dataModel.RedmineConfig;
import plugin.redmine.dataModel.RedmineIssueStatus;
import plugin.redmine.dataModel.Story;
import plugin.redmine.dataModel.StoryDescriptor;
import plugin.redmine.webservice.EzScrumWebServiceController;
import plugin.redmine.webservice.RedmineWebServiceController;

import com.google.gson.Gson;

public class RedmineAction implements Action{

	@Override
	public String getUrlName() {
		return "redmine";
	}
	
	public Object getDynamic( String token, StaplerRequest request, StaplerResponse response  ){
		if( token.equals("config")){
			return new ConfigAction();
		}
		
		return this;
		
	}

	
	public void doGetRedmineIssues( StaplerRequest request, StaplerResponse response ){
		String projectID = SessionManager.getProjectID(request);
    	
    	PluginConfigDAO pluginConfigDAO = new PluginConfigDAO();
    	RedmineConfig redmineConfig = pluginConfigDAO.getRedmineConfig( projectID );
    	RedmineWebServiceController redmineWebServiceController = new RedmineWebServiceController( redmineConfig );
    	String jsonString = redmineWebServiceController.getIssuesJsonFromRedmine( RedmineIssueStatus.INPROGRESS );
    	
    	try {
    		response.setCharacterEncoding("utf-8");//solve Chinese character problem
			response.getWriter().write( jsonString );
	    	response.getWriter().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doChangeRedmineIssueStatus( StaplerRequest request, StaplerResponse response ){
		int issueID = Integer.parseInt(  (String)request.getParameter("issueID") );
		String projectID = SessionManager.getProjectID(request);
    	
    	PluginConfigDAO pluginConfigDAO = new PluginConfigDAO();
    	RedmineConfig redmineConfig = pluginConfigDAO.getRedmineConfig( projectID );
    	RedmineWebServiceController redmineWebServiceController = new RedmineWebServiceController( redmineConfig );
    	//change redmine issue status to resolved
    	redmineWebServiceController.changeRedmineIssueStatus(issueID, RedmineIssueStatus.RESOLVED, redmineConfig.getRedmineAccessKey());
		
	}
	
	public void doCommitStoryToEzScrumServer( StaplerRequest request, StaplerResponse response ){
		HttpSession session = request.getSession();
		IUserSession userSession = (IUserSession)session.getAttribute("UserSession");
		String userName = userSession.getAccount().getName();
 
		//session�����K�X�O�w�g�[�K�L��
		String encodedPassword = (String) session.getAttribute("passwordForPlugin");
		
		String issueID = request.getParameter("issueID");
		String name = request.getParameter("name");
		String notes = request.getParameter("notes");
		String howToDemo = request.getParameter("howToDemo");
		
		String requestValue = request.getParameter("value");
		String requestEstimation = request.getParameter("estimation");
		String requestImportance = request.getParameter("importance");
		System.out.println("requestValue = " + requestValue);
		int value = 0;
		int estimation = 0;
		int importance = 0;
		if( !requestValue.equals("") && requestValue!=null ){
			value = Integer.parseInt( requestValue );
		}
		if( !requestEstimation.equals("") && requestEstimation!=null ){
			estimation = Integer.parseInt( requestEstimation );
		}
		if( !requestImportance.equals("") && requestImportance!=null ){
			importance = Integer.parseInt( requestImportance );
		}
		
		//�إ�story�ɤ��إ�tag
		Story story = new Story(name, notes, value, estimation, importance, howToDemo, null);
	
		String ezScrumURL = request.getServerName() + ":" + request.getLocalPort() +request.getContextPath();
		EzScrumWebServiceController ezScrumWebServiceController = new EzScrumWebServiceController( ezScrumURL );
		
		String projectID = SessionManager.getProjectID(request);
    	
		String storyID = ezScrumWebServiceController.saveStoryToEzScrum(userName,encodedPassword, projectID, story);/**todo replace hardcode*/
		
		//save redmine issue and ezScrum story relation
		RedmineRelationDAO redmineRelationDAO = new RedmineRelationDAO();
		redmineRelationDAO.addRelation(projectID, issueID, storyID);
		
	}
	
	public void doGetEzScrumStories( StaplerRequest request, StaplerResponse response ){
		HttpSession session = request.getSession();
		
		IUserSession userSession = (IUserSession)session.getAttribute("UserSession");
		String userName = userSession.getAccount().getName();
 
		//session�����K�X�O�w�g�[�K�L��
		String encodedpassword = (String) session.getAttribute("passwordForPlugin");
		
		String projectID = SessionManager.getProjectID(request);
    	String issueID   = request.getParameter("issueID");
    	String result    = "";  
    	int totalStoryCount = 0;
    	List<Story> storyList = new ArrayList<Story>();
    	if( issueID != null ){
	    	RedmineRelationDAO redmineRelationDAO = new RedmineRelationDAO();
	    	List<String> storyIDList = redmineRelationDAO.getStoryIDList(projectID, issueID);
	    	
	    	String ezScrumURL = request.getServerName() + ":" + request.getLocalPort() +request.getContextPath();
	    	EzScrumWebServiceController ezScrumWebServiceController = new EzScrumWebServiceController( ezScrumURL );
	    	
	    	for( String storyID : storyIDList ){
	    		Story story = ezScrumWebServiceController.getStory(userName, encodedpassword, projectID, storyID);
	    		//avoid null pointer exception
	    		if( story != null ){
		    		storyList.add( story );
		    		totalStoryCount ++;
	    		}
	    	}
    	
    	}
    	Gson gson = new Gson();
    	StoryDescriptor storyDescriptor = new StoryDescriptor(totalStoryCount, storyList);
    	result = gson.toJson( storyDescriptor );
    	
    	try {
    		response.setCharacterEncoding("utf-8");//solve Chinese character problem
			response.getWriter().write( result );
	    	response.getWriter().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doGetTest( StaplerRequest request, StaplerResponse response ){
		HttpSession session = request.getSession();
		
		IUserSession userSession = (IUserSession)session.getAttribute("UserSession");
		String userName = userSession.getAccount().getName();
		
		//session�����K�X�O�w�g�[�K�L��
		String encodedpassword = (String) session.getAttribute("passwordForPlugin");
		
		String projectID = SessionManager.getProjectID(request);
		String issueID   = request.getParameter("issueID");
		String result    = "";  
		int totalStoryCount = 0;
		List<Story> storyList = new ArrayList<Story>();
		if( issueID != null ){
			RedmineRelationDAO redmineRelationDAO = new RedmineRelationDAO();
			List<String> storyIDList = redmineRelationDAO.getStoryIDList(projectID, issueID);
			
			String ezScrumURL = request.getServerName() + ":" + request.getLocalPort() +request.getContextPath();
			EzScrumWebServiceController ezScrumWebServiceController = new EzScrumWebServiceController( ezScrumURL );
			
			for( String storyID : storyIDList ){
				Story story = ezScrumWebServiceController.getStory(userName, encodedpassword, projectID, storyID);
				//avoid null pointer exception
				if( story != null ){
					storyList.add( story );
					totalStoryCount ++;
				}
			}
			
		}
		Gson gson = new Gson();
		StoryDescriptor storyDescriptor = new StoryDescriptor(totalStoryCount, storyList);
		result = gson.toJson( storyDescriptor );
		
		try {
			response.setCharacterEncoding("utf-8");//solve Chinese character problem
			response.getWriter().write( result );
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
