package plugin.redmine.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ntut.csie.ezScrum.plugin.PluginExtensioner;

import plugin.redmine.dataModel.RedmineConfig;

public class PluginConfigDAO {
	
	private Connection _connection;
	private String _pluginRoot;

	public PluginConfigDAO(){
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PluginExtensioner pluginExtensioner = new PluginExtensioner("redminePlugin");
		_pluginRoot = pluginExtensioner.getPluginRoot()+"/resource";
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
	
	
	public RedmineConfig getRedmineConfig( String projectID ){
		RedmineConfig redmineConfig = null;
		
		this.openConnection();
	    try
	    {
	      Statement statement = _connection.createStatement();
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      
	      //create table if not exists such table
	      statement.executeUpdate("CREATE TABLE IF NOT EXISTS configTable (projectID string, redmineUrl string, redmineAccessKey string)");
	      ResultSet rs = statement.executeQuery("SELECT * FROM configTable WHERE projectID = " + "'" + projectID +"'");

	      while(rs.next())
	      {
	    	redmineConfig = new RedmineConfig( rs.getString("redmineUrl"), rs.getString("redmineAccessKey"));
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
		return redmineConfig;
	}
	/**@
	 * 
	 * */
	public void insertConfig( String projectID, RedmineConfig redmineConfig ){
		this.openConnection();
	    try
	    {
	      Statement statement = _connection.createStatement();
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	     
	      //statement.executeUpdate("create table configTable (projectID string, redmineUrl string, redmineAccount string, redminePassword string, PRIMARY KEY(projectID) )");
	      statement.executeUpdate("INSERT INTO configTable VALUES('"+projectID+"', '"+redmineConfig.getRedmineUrl()+"', '"+redmineConfig.getRedmineAccessKey()+"')");
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
	
	public void updateConfig( String projectID, RedmineConfig redmineConfig ){
		this.openConnection();
	    try
	    {
	      Statement statement = _connection.createStatement();
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      statement.executeUpdate("UPDATE configTable SET redmineUrl = '"+redmineConfig.getRedmineUrl()+"' ,"+"redmineAccessKey = '"+redmineConfig.getRedmineAccessKey()+"'" + " WHERE projectID = '"+projectID+"'");
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
	
	public void deleteConfig( String projectID ){
		this.openConnection();
	    try
	    {
	      Statement statement = _connection.createStatement();
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      statement.executeUpdate("DELETE FROM configTable WHERE projectID = '"+projectID+"'");
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
}
