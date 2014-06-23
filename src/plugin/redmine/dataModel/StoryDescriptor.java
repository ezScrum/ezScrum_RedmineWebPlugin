package plugin.redmine.dataModel;

import java.util.List;

public class StoryDescriptor {
	private int results;
	private List<Story> rows;
	
	public StoryDescriptor(int results, List<Story> rows) {
		this.results = results;
		this.rows = rows;
	}
	public int getResults() {
		return results;
	}
	public void setResult(int results) {
		this.results = results;
	}
	public List<Story> getRow() {
		return rows;
	}
	public void setRow(List<Story> rows) {
		this.rows = rows;
	}
}
