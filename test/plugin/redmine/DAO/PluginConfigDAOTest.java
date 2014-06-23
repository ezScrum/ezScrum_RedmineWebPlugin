package plugin.redmine.DAO;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

import org.junit.Test;

import plugin.redmine.dataModel.RedmineConfig;

public class PluginConfigDAOTest extends TestCase{

	private PluginConfigDAO pluginConfigDAO;
	private Connection connection;
	private String pluginRoot = "resource";
	private String redmineUrl = "http://127.0.0.1";
	private String redmineAccessKey = "1234";
	private String projectID = "test";
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		pluginConfigDAO = new PluginConfigDAO();
		pluginConfigDAO.setPluginRootForTest( this.pluginRoot );
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		pluginConfigDAO = null;
	}
	
	/**
	 * 測試資料庫是否存在
	 */
	@Test
	public void testDBFile(){
		String dbPath = this.pluginRoot  + "/redmine.db";
		File dbFile = new File( dbPath );
		assertEquals( true, dbFile.exists() );
		dbFile = null;
	}
	
	/**
	 * 測試 get Config
	 */
	@Test
	public void testGetRedmineConfig(){
		//	create mock data
		RedmineConfig redmineConfig;
		redmineConfig = new RedmineConfig(redmineUrl, redmineAccessKey);
		pluginConfigDAO.insertConfig(projectID, redmineConfig);
		
		//	get
		redmineConfig = pluginConfigDAO.getRedmineConfig( this.projectID );
		assertEquals( redmineUrl, redmineConfig.getRedmineUrl() );
		assertEquals( redmineAccessKey, redmineConfig.getRedmineAccessKey() );
		
		//	delete mock data
		this.deleteRedmineConfigTable();
		redmineConfig = pluginConfigDAO.getRedmineConfig( this.projectID );
		assertNull(redmineConfig);
	}
	
	/**
	 * 測試 add Config
	 */
	@Test
	public void testInsertConfig(){
		//	create mock data
		RedmineConfig redmineConfig;
		redmineConfig = pluginConfigDAO.getRedmineConfig( this.projectID );
		assertNull(redmineConfig);
		
		//	insert
		redmineConfig = new RedmineConfig(redmineUrl, redmineAccessKey);
		pluginConfigDAO.insertConfig(projectID, redmineConfig);
		redmineConfig = pluginConfigDAO.getRedmineConfig( this.projectID );
		assertEquals(redmineUrl, redmineConfig.getRedmineUrl());
		assertEquals(redmineAccessKey, redmineConfig.getRedmineAccessKey());
		
		//	delete mock data
		this.deleteRedmineConfigTable();
		redmineConfig = pluginConfigDAO.getRedmineConfig( this.projectID );
		assertNull(redmineConfig);
	}
	
	/**
	 * 測試 update Config
	 */
	@Test
	public void testUpdateConfig(){
		//	create mock data
		RedmineConfig redmineConfig;
		redmineConfig = new RedmineConfig(redmineUrl, redmineAccessKey);
		pluginConfigDAO.insertConfig(projectID, redmineConfig);
		
		//	update
		String updateUrl = "http://8.8.8.8";
		String updateAccessKey = "8888";
		redmineConfig = new RedmineConfig(updateUrl, updateAccessKey);
		pluginConfigDAO.updateConfig(projectID, redmineConfig);
		redmineConfig = pluginConfigDAO.getRedmineConfig( this.projectID );
		assertEquals(updateUrl, redmineConfig.getRedmineUrl());
		assertEquals(updateAccessKey, redmineConfig.getRedmineAccessKey());
		
		//	delete mock data
		this.deleteRedmineConfigTable();
		redmineConfig = pluginConfigDAO.getRedmineConfig( this.projectID );
		assertNull(redmineConfig);
	}
	
	/**
	 * 測試 delete Config
	 */
	@Test
	public void testDeleteConfig(){
		//	create mock data
		RedmineConfig redmineConfig;
		redmineConfig = new RedmineConfig(redmineUrl, redmineAccessKey);
		pluginConfigDAO.insertConfig(projectID, redmineConfig);
		
		//	delete
		pluginConfigDAO.deleteConfig( this.projectID );
		redmineConfig = pluginConfigDAO.getRedmineConfig( this.projectID );
		assertNull(redmineConfig);
		
		//	delete mock data
		redmineConfig = pluginConfigDAO.getRedmineConfig( this.projectID );
		assertNull(redmineConfig);
	}
	
	private void deleteRedmineConfigTable(){
		this.openConnection();
		try {
			Statement statement = connection.createStatement();
			String deleteConfigSQL = "DELETE " +
										"FROM configTable " +
										"WHERE " +
											"projectID = " + "'" + this.projectID +"'"; // " AND " +
											//"redmineUrl = " + "'" + url + "' AND " + 
											//"redmineAccessKey = " + "'" + key + "'";
			statement.executeUpdate( deleteConfigSQL );
		} catch (SQLException e) {
			System.err.println( e.getMessage() );
		} finally {
			this.closeConnection();
		}
	}
	
	private void openConnection(){
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:"+this.pluginRoot+"/redmine.db");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void closeConnection(){
		try
	      {
	        if(connection != null)
	          connection.close();
	      }
	      catch(SQLException e)
	      {
	        // connection close failed.
	        System.err.println(e);
	      }
	}
}
