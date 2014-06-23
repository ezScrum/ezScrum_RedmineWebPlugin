package plugin.redmine.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ntut.csie.ezScrum.plugin.PluginExtensioner;

public class RedmineRelationDAO {
	private Connection _connection;
	private String _pluginRoot;
	private final String createTableCommand = "CREATE TABLE IF NOT EXISTS relationTable (projectID string, issueID string, storyID string, PRIMARY KEY(projectID, issueID, storyID))";
	public RedmineRelationDAO(){
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PluginExtensioner pluginExtensioner = new PluginExtensioner("redminePlugin");
		_pluginRoot = pluginExtensioner.getPluginRoot()+"/resource";
//		_pluginRoot = "resource";//local test path
	}
	
	/**
	 * ´ú¸Õ¥Î¸ô®|
	 * @param pluginRoot
	 */
	public void setPluginRootForTest(String pluginRoot){
		this._pluginRoot = pluginRoot;
	}
	
	private void openConnection(){
		try {
			_connection = DriverManager.getConnection("jdbc:sqlite:"+this._pluginRoot+"/redmine.db");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void closeConnection(){
		try
	      {
	        if(_connection != null)
	          _connection.close();
	      }
	      catch(SQLException e)
	      {
	        // connection close failed.
	        System.err.println(e);
	      }
	}
	
	public void addRelation( String projectID, String issueID, String storyID ){//add relation between issue and story
		this.openConnection();
	    try
	    {
	      Statement statement = _connection.createStatement();
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	     
	      statement.executeUpdate( createTableCommand );
	      statement.executeUpdate("INSERT INTO relationTable VALUES('"+projectID+"', '"+issueID+"', '"+ storyID +"')");
	    }
	    catch(SQLException e)
	    {
	      // if the error message is "out of memory",
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    }
	    finally
	    {
	      this.closeConnection();
	    }
	}
	
	public void deleteRelation( String projectID, String issueID, String storyID ){//delete relation between issue and story
		this.openConnection();
	    try
	    {
	      Statement statement = _connection.createStatement();
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      statement.executeUpdate( createTableCommand );
	      statement.executeUpdate("DELETE FROM relationTable WHERE issueID = '"+issueID+"' AND storyID = '"+storyID+"' AND projectID = '" + projectID +"'");
	    }
	    catch(SQLException e)
	    {
	      // if the error message is "out of memory",
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    }
	    finally
	    {
	      this.closeConnection();
	    }
	}
	
	public List<String> getStoryIDList( String projectID, String issueID ){//get stories by issue id
		List<String> storyIDList = new ArrayList<String>();
		this.openConnection();
	    try
	    {
	      Statement statement = _connection.createStatement();
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      
	      //create table if not exists such table
	      statement.executeUpdate( createTableCommand );
	      ResultSet rs = statement.executeQuery("SELECT storyID FROM relationTable WHERE issueID = " + "'" + issueID +"' AND projectID = '" + projectID +"'");

	      while(rs.next()){
	    	  storyIDList.add( rs.getString("storyID") );
	      }
	    }
	    catch(SQLException e)
	    {
	      // if the error message is "out of memory",
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    }
	    finally
	    {
	      this.closeConnection();
	    }
	    return storyIDList;
	}
}
