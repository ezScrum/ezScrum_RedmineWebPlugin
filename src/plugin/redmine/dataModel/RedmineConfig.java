package plugin.redmine.dataModel;

public class RedmineConfig {
	private String redmineUrl;
	private String redmineAccessKey;

	public RedmineConfig( String redmineUrl, String redmineAccessKey ) {
		this.redmineUrl = redmineUrl;
		this.redmineAccessKey = redmineAccessKey;
	}
	
	public String getRedmineUrl() {
		return redmineUrl;
	}
	
	public void setRedmineUrl(String redmineUrl) {
		this.redmineUrl = redmineUrl;
	}
	

	public String getRedmineAccessKey() {
		return redmineAccessKey;
	}

	public void setRedmineAccessKey(String redmineAccessKey) {
		this.redmineAccessKey = redmineAccessKey;
	}

	public boolean equals( Object object ){
		return this.toString().equals( object.toString() );
	}
	
	public String toString(){
		return "redmineUrl:"+ redmineUrl + " " +"redmineAccessKey:"+ redmineAccessKey ;
	}
}
