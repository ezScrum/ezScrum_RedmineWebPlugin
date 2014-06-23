package plugin.redmine.dataModel;

import java.util.List;

public class Story {
	private String id;
	private String name;
	private String notes;
	private int value;
	private int estimation;
	private int importance;
	private String howToDemo;
	private String status;
	private List<String> tag;
	
	//constructor when retrieve story
	public Story( String id, String name, String notes, int value, int estimation, int importance, String howToDemo, String status, List<String> tagList){
		this.id = id;
		this.name = name;
		this.howToDemo = howToDemo;
		this.importance = importance;
		this.estimation = estimation;
		this.notes = notes;
		this.value = value;
		this.status = status;
		this.tag = tagList;
	}

	//constructor when create story, because no story id is generated 
	public Story( String name, String notes, int value, int estimation, int importance, String howToDemo, List<String> tagList){
		this.name = name;
		this.howToDemo = howToDemo;
		this.importance = importance;
		this.estimation = estimation;
		this.notes = notes;
		this.value = value;
		this.tag = tagList;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public List<String> getTagList() {
		return tag;
	}

	public void setTagList(List<String> tagList) {
		this.tag = tagList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getEstimation() {
		return estimation;
	}
	public void setEstimation(int estimation) {
		this.estimation = estimation;
	}
	public int getImportance() {
		return importance;
	}
	public void setImportance(int importance) {
		this.importance = importance;
	}
	public String getHowToDemo() {
		return howToDemo;
	}
	public void setHowToDemo(String howToDemo) {
		this.howToDemo = howToDemo;
	}
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String toString(){
		return "name:" + this.name 
				+ ", howToDemo:" + this.howToDemo 
				+ ", notes:"+ this.notes 
				+ ", value:" + this.value 
				+ ", estimation:" + this.estimation
				+ ", importance:" + this.importance
				+ ", tagList:" + this.tag; 
	}
}
