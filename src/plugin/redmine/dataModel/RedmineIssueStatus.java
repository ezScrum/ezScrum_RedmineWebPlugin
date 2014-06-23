package plugin.redmine.dataModel;

public enum RedmineIssueStatus {
	INPROGRESS(2),
	RESOLVED(3);
	
	private final int id;
	
	RedmineIssueStatus(int id) { 
		this.id = id; 
	}
	
    public int getValue(){ 
    	return id; 
    }
}
