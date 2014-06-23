package plugin.redmine.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

public class RedmineRelationDAOTest extends TestCase{
	private Connection connection;
	private String pluginRoot = "resource";
	private RedmineRelationDAO redmineRelationDAO;
	private String projectID = "TestForRedminePlugin";
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		redmineRelationDAO = new RedmineRelationDAO();
		redmineRelationDAO.setPluginRootForTest( this.pluginRoot );
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.dropTable();
		redmineRelationDAO = null;
	}
	
	/**
	 * 測試 add Relation
	 */
	@Test
	public void testAddRelation(){
		//	create mock data
		String issueID = "1234";
		String storyID = "1";
		
		//	add
		redmineRelationDAO.addRelation( this.projectID, issueID, storyID );
		boolean isStoryExisted = this.isStoryIDExited( issueID, storyID );
		assertEquals( true, isStoryExisted );
	}
	
	/**
	 * 測試 delete relation
	 */
	@Test
	public void testDeleteRelation(){
		//	create mock data
		String issueID = "1111";
		String storyID = "1";
		redmineRelationDAO.addRelation( this.projectID, issueID, storyID );
		
		//	delete
		redmineRelationDAO.deleteRelation( this.projectID, issueID, storyID );
		boolean isStoryExisted = this.isStoryIDExited( issueID, storyID );
		assertEquals( false, isStoryExisted );
	}
	
	/**
	 * 測試 get story id list
	 */
	@Test
	public void testGetStoryIDList(){
		//	mock data
		String issueID = "issueID";
		String storyID = "storyID";
		List<String> storyIDListForTest = new ArrayList<String>();
		for( int i = 1; i <= 2; i++ ){
			storyIDListForTest.add( storyID + String.valueOf(i) );
		}
		for( String id:storyIDListForTest ){
			redmineRelationDAO.addRelation(projectID, issueID, id);
		}
		
		//	get
		List<String> storyIDList = redmineRelationDAO.getStoryIDList(projectID, issueID);
		for( String idForTest:storyIDListForTest ){
			boolean isExisted = false;
			for( String id:storyIDList ){
				if( idForTest.equals(id) ){
					isExisted = true;
				}
			}
			String errorMessage = "storyID = " + idForTest + "did not existed in relationTable.";
			assertEquals( errorMessage, true, isExisted );
		}
	}
	
	/**
	 * 確認story是否存在於資料庫中
	 * @param issueID
	 * @param storyID
	 * @return
	 */
	private boolean isStoryIDExited( String issueID, String storyID ){
		List<String> storyIDList = this.getStoryIDList( this.projectID, issueID );
		for( String id: storyIDList ){
			if( id.equals(storyID) ){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 取得issue相關的所有story的id
	 * @param projectID
	 * @param issueID
	 * @return
	 */
	private List<String> getStoryIDList( String projectID, String issueID ){
		List<String> storyIDList = new ArrayList<String>();
		this.openConnection();
		Statement statement;
		try {
			statement = this.connection.createStatement();
			statement.setQueryTimeout( 30 );
			
			final String createTableCommand = "CREATE TABLE IF NOT EXISTS relationTable (projectID string, issueID string, storyID string)";//, PRIMARY KEY(projectID, issueID, storyID))";
			final String getStorySQL = "SELECT * " +
										"FROM relationTable " +
										"WHERE " +
											"issueID = " + "'" + issueID + "'" + " AND " +
											"projectID = " + "'" + projectID +"'";
			statement.executeUpdate( createTableCommand );
			ResultSet rs = statement.executeQuery( getStorySQL );
			
			while (rs.next()) {
				storyIDList.add( rs.getString("storyID") );
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			this.closeConnection();
		}
		return storyIDList;
	}
	
	/**
	 * drop relationTable table
	 */
	private void dropTable(){
		final String dropTableSQL = "drop TABLE relationTable";
		this.openConnection();
    	Statement statement;
		try {
			statement = this.connection.createStatement();
			statement.setQueryTimeout( 30 ); // set timeout to 30 sec.
			statement.executeUpdate( dropTableSQL );
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
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
