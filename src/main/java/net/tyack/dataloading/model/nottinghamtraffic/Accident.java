package net.tyack.dataloading.model.nottinghamtraffic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.tyack.dataloading.model.CommonBean;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;

@Entity
public class Accident extends CommonBean {

	@Id private String id;
	private Date accidentDate;
	private String severity;
	private String time;
	private String timeCategory;
	private int month;
	private int year;
	private int numVeh;
	private String lat;
	private String lng;
	private String pedestrianSeverity;
	private List<Integer> driverAges = new ArrayList<>();
	private List<String> driverSexes = new ArrayList<>();
	private List<Person> persons = new ArrayList<>();
	
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getAccidentDate() {
		return accidentDate;
	}
	public void setAccidentDate(Date accidentDate) {
		this.accidentDate = accidentDate;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getNumVeh() {
		return numVeh;
	}
	public void setNumVeh(int numVeh) {
		this.numVeh = numVeh;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public List<Person> getPersons() {
		return persons;
	}
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String accidentSeverity) {
		this.severity = accidentSeverity;
	}
	public List<Integer> getDriverAges() {
		return driverAges;
	}
	public void setDriverAge(List<Integer> driverAges) {
		this.driverAges = driverAges;
	}
	public List<String> getDriverSexes() {
		return driverSexes;
	}
	public void setDriverSex(List<String> driverSexes) {
		this.driverSexes = driverSexes;
	}
	
	public String getPedestrianSeverity() {
		return pedestrianSeverity;
	}
	
	public void setPedestrianSeverity(String pedestrianSeverity) {
		this.pedestrianSeverity = pedestrianSeverity;
	}
	
	public String getTimeCategory() {
		return timeCategory;
	}
	
	public void setTimeCategory(String timeCategory) {
		this.timeCategory = timeCategory;
	}
}
