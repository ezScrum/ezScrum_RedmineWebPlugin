package plugin.redmine.dataModel;

import java.util.List;

public class RedmineIssueDescriptor {
	private int results;
	private List<RedmineIssue> rows;
	
	public RedmineIssueDescriptor(int results, List<RedmineIssue> rows) {
		this.results = results;
		this.rows = rows;
	}
	public int getResults() {
		return results;
	}
	public void setResult(int results) {
		this.results = results;
	}
	public List<RedmineIssue> getRow() {
		return rows;
	}
	public void setRow(List<RedmineIssue> rows) {
		this.rows = rows;
	}
}
