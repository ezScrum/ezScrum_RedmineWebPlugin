package plugin.redmine.dataModel;

public class RedmineIssue {
	private int id;//redmine issue id
	private String name;//redmine issue name
	private String description;//redmine issue description
	private String tracker;//redmine issue  tracker
	private int statusId;//redmine issue  status id
	private String statusName;//redmine issue  status name
	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public RedmineIssue(int id, String name, String description, int statusId, String tracker) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.statusId = statusId;
		if( RedmineIssueStatus.INPROGRESS.getValue() == this.statusId ){
			this.statusName = "In Progress";
		}
		this.tracker = tracker;
	}
	
	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTracker() {
		return tracker;
	}
	
	public void setTracker(String tracker) {
		this.tracker = tracker;
	}
	
	public String toString(){
		return "id " + this.id  + "name "+ this.name + "statusName " + this.statusName + "description " + this.description + "tracker "+ this.tracker;
	}
	
}
