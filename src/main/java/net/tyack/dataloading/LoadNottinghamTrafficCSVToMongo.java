package net.tyack.dataloading;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.tyack.dataloading.model.nottinghamtraffic.Accident;
import net.tyack.dataloading.model.nottinghamtraffic.Person;
import au.com.bytecode.opencsv.CSVReader;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

/**
 * Loads data from the CSV file at
 * http://www.opendatanottingham.org.uk/dataset.aspx?id=84 Since data is
 * duplicated for accidents, the code will add a person to an existing record
 * when it comes across the same accident id in another row.
 * 
 * @author jamestyack
 * 
 */
public class LoadNottinghamTrafficCSVToMongo {

	private static final short VALID_NUMBER_OF_CSV_FIELDS = 17;

	public static void main(String[] args) throws IOException {

		Map<String, Accident> accidents = new HashMap<>();

		try (Reader reader = new FileReader(
				"ncc_Traffic_Accident_Casualties.csv");
				CSVReader csvReader = new CSVReader(reader)) {
			String[] line;
			csvReader.readNext(); // skip the header row
			while ((line = csvReader.readNext()) != null) {
				Accident accident = convertLineToAccident(line);
				if (accidents.containsKey(accident.getId())) { // add person to
																// existing
																// accident
					Accident accidentToAddPersonTo = accidents.get(accident
							.getId());
					accidentToAddPersonTo.getPersons().add(
							accident.getPersons().get(0));
				} else {
					accidents.put(accident.getId(), accident);
				}
			}
			storeToMongo(accidents);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private static void storeToMongo(Map<String, Accident> accidents)
			throws UnknownHostException {
		Mongo client = new MongoClient("localhost", 27017);
		Datastore ds = new Morphia().createDatastore(client, "ncc");
		Iterator<String> iterator = accidents.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			ds.save(accidents.get(key));
		}
		System.out.println("Mongo load complete, total Accident entries " + ds.getCount(Accident.class));
	}

	private static Accident convertLineToAccident(String[] line) {
		if (line.length == VALID_NUMBER_OF_CSV_FIELDS) {
			Accident accident = new Accident();
			accident.setId(line[0]);
			accident.setAccidentDate(line[6]);
			accident.setTime(line[7]);
			accident.setYear(Short.parseShort(line[8]));
			accident.setNumVeh(Integer.parseInt(line[9]));
			accident.setEasting(line[10]);
			accident.setNorthing(line[11]);
			accident.setLat(Float.parseFloat(line[12]));
			accident.setLng(Float.parseFloat(line[13]));
			accident.setBody(line[14]);
			accident.setBodyName(line[15]);
			accident.setCreateDate(line[16]);
			Person person = new Person();
			person.setPersonRef(Integer.parseInt(line[1]));
			person.setSeverity(line[2]);
			person.setSex(line[3]);
			person.setType(line[4]);
			person.setAge(line[5]);
			accident.getPersons().add(person);
			return accident;
		} else {
			throw new IllegalArgumentException("csv line should have "
					+ VALID_NUMBER_OF_CSV_FIELDS + " but had " + line.length);
		}
	}
}
