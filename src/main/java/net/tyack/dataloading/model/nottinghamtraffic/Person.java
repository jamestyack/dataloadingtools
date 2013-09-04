package net.tyack.dataloading.model.nottinghamtraffic;

import net.tyack.dataloading.model.CommonBean;

public class Person extends CommonBean {
	
	private int personRef;
	private String severity;
	private String sex;
	private String type;
	private int age;
	
	public int getPersonRef() {
		return personRef;
	}
	public void setPersonRef(int personRef) {
		this.personRef = personRef;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
}
