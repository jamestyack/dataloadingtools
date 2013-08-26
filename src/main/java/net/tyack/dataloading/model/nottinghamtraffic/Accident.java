package net.tyack.dataloading.model.nottinghamtraffic;

import java.util.ArrayList;
import java.util.List;

import net.tyack.dataloading.model.CommonBean;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;

@Entity
public class Accident extends CommonBean {

	@Id private String id;
	private String accidentDate;
	private String time;
	private short year;
	private int numVeh;
	private String easting;
	private String northing;
	private float lat;
	private float lng;
	private String body;
	private String bodyName;
	private String createDate;
	private List<Person> persons = new ArrayList<>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccidentDate() {
		return accidentDate;
	}
	public void setAccidentDate(String accidentDate) {
		this.accidentDate = accidentDate;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public short getYear() {
		return year;
	}
	public void setYear(short year) {
		this.year = year;
	}
	public int getNumVeh() {
		return numVeh;
	}
	public void setNumVeh(int numVeh) {
		this.numVeh = numVeh;
	}
	public String getEasting() {
		return easting;
	}
	public void setEasting(String easting) {
		this.easting = easting;
	}
	public String getNorthing() {
		return northing;
	}
	public void setNorthing(String northing) {
		this.northing = northing;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLng() {
		return lng;
	}
	public void setLng(float lng) {
		this.lng = lng;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getBodyName() {
		return bodyName;
	}
	public void setBodyName(String bodyName) {
		this.bodyName = bodyName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public List<Person> getPersons() {
		return persons;
	}
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
}
