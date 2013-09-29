/**
 * 
 */
package net.tyack.dataloading.batch.mapper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

import net.tyack.dataloading.batch.AccidentsBatchJob;
import net.tyack.dataloading.model.nottinghamtraffic.Accident;
import net.tyack.dataloading.model.nottinghamtraffic.Person;

/**
 * @author jamestyack
 * 
 */
public class CSVToAccidentMapper implements ICSVToAccidentMapper {

	private Logger logger = LoggerFactory.getLogger(CSVToAccidentMapper.class);

	private static final int VALID_NUMBER_OF_CSV_FIELDS = 17;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.tyack.dataloading.batch.mapper.CSVObjectMapper#transform(java.lang
	 * .String)
	 */
	@Override
	public Map<String, Accident> map(String pathAndCsvFilename) {
		Map<String, Accident> accidents = new HashMap<>();
		try (Reader reader = new FileReader(pathAndCsvFilename); CSVReader csvReader = new CSVReader(reader)) {
			String[] line;
			csvReader.readNext(); // skip the header row
			while ((line = csvReader.readNext()) != null) {
				updateMapWithLine(accidents, line);
			}
		} catch (FileNotFoundException e) {
			logger.error("Problem with filename " + pathAndCsvFilename);
			e.printStackTrace();
		} catch (IOException e1) {
			logger.error("Problem when reading file " + pathAndCsvFilename);
			e1.printStackTrace();
		}
		return accidents;
	}

	private void updateMapWithLine(Map<String, Accident> accidents, String[] line) {
		Accident accident = null;
		try {
			accident = convertLineToAccident(line);
			if (accidents.containsKey(accident.getId())) {
				// accident already in map - add person to existing accident and
				// update accident metadata
				Accident accidentToUpdate = accidents.get(accident.getId());
				Person personOnThisLine = accident.getPersons().get(0);
				accidentToUpdate.getPersons().add(personOnThisLine);
				accidentToUpdate.setSeverity(getHighestSeverity(accidentToUpdate.getSeverity(), personOnThisLine.getSeverity()));
				if (personOnThisLine.getType().toLowerCase().contains("pedestrian")) {
					accidentToUpdate.setPedestrianSeverity(getHighestSeverity(personOnThisLine.getSeverity(), accidentToUpdate.getPedestrianSeverity()));
				}
				setDriverMetadata(accidentToUpdate, personOnThisLine);
			} else {
				// this accident is new to the map
				if (accident.getPersons().size() > 0) {
					// accident contains a person... set accident level metadata
					Person person = accident.getPersons().get(0);
					accident.setSeverity(person.getSeverity());
					if (person.getType().toLowerCase().contains("pedestrian")) {
						accident.setPedestrianSeverity(person.getSeverity());
					}
					setDriverMetadata(accident, person);

				} else {
					throw new IllegalArgumentException("No person avaiable at row " + accident);
				}
				// and put the new accident into the map
				accidents.put(accident.getId(), accident);
			}
		} catch (IllegalArgumentException iae) {
			logger.error("Problem at line " + ArrayUtils.toString(line) + " : " + iae.getMessage(), iae);
			System.exit(1);
		}
	}

	/*
	 * Checks if the person is a driver and if so adds their details to the
	 * accident driver metadata
	 */
	private void setDriverMetadata(Accident accident, Person person) {
		if (person.getType().contains("Driver")) {
			// the person is a driver, add their details to the driver metadata
			// top level
			accident.getDriverAges().add(new Integer(person.getAge()));
			accident.getDriverSexes().add(person.getSex());
		}
	}

	/**
	 * @param severity
	 * @param severity2
	 * @return
	 */
	private String getHighestSeverity(String currentSeverity, String newSeverity) {
		if (StringUtils.equals("Fatal", currentSeverity) || StringUtils.equals("Fatal", newSeverity)) {
			return "Fatal";
		} else if (StringUtils.equals("Serious", currentSeverity) || StringUtils.equals("Serious", newSeverity)) {
			return "Serious";
		} else if (StringUtils.equals("Slight", currentSeverity) || StringUtils.equals("Slight", newSeverity)) {
			return "Slight";
		} else {
			throw new IllegalArgumentException("Invalid severity " + currentSeverity + " / " + newSeverity);
		}
	}

	private Accident convertLineToAccident(String[] line) {
		if (line.length == VALID_NUMBER_OF_CSV_FIELDS) {
			Accident accident = new Accident();
			accident.setId(line[0]);
			Date nccDateAsObject = parseNccDateString(line[6]);
			accident.setAccidentDate(nccDateAsObject);
			accident.setMonth(getMonthInt(nccDateAsObject));
			accident.setTime(line[7]);
			accident.setTimeCategory(getTimeCategory(line[7]));
			accident.setYear(Integer.parseInt(line[8]));
			accident.setNumVeh(Integer.parseInt(line[9]));
			accident.setLat(line[12]);
			accident.setLng(line[13]);
			Person person = new Person();
			person.setPersonRef(Integer.parseInt(line[1]));
			person.setSeverity(line[2]);
			person.setSex(line[3]);
			person.setType(line[4]);
			try {
				person.setAge(Integer.parseInt(line[5]));
			} catch (NumberFormatException nfe) {
				// age not a number, assume unkonwn and set to -1
				person.setAge(-1);
			}
			accident.getPersons().add(person);
			return accident;
		} else {
			throw new IllegalArgumentException("csv line should have " + VALID_NUMBER_OF_CSV_FIELDS + " but had " + line.length);
		}

	}

	/**
	 * @param string
	 * @return
	 */
	private String getTimeCategory(String time) {
		// only interested in the hour part...
		int hour = Integer.parseInt(time.substring(0,2));
		if (hour >= 0 && hour <=3) {
			return "NIGHT";
		} else if (hour >= 4 && hour <=6) {
			return "EARLY_MORNING";
		} else if (hour >= 7 && hour <=8) {
			return "AM_PEAK";
		} else if (hour >= 9 && hour <= 11) {
			return "AM_OFF_PEAK";
		} else if (hour >= 12 && hour <= 15) {
			return "PM_OFF_PEAK";
		} else if (hour >= 16 && hour <= 17) {
			return ("PM_PEAK");
		} else if (hour >= 18 && hour <=20) {
			return ("EARLY_EVENING");
		} else if (hour >= 21 && hour <= 23) {
			return ("LATE_EVENING");
		} else {
			throw new IllegalArgumentException("Bad time, couldn't work out time category");
		}

	}

	/**
	 * @param nccDateAsObject
	 * @return
	 */
	private static int getMonthInt(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * @param dateString
	 * @return
	 * @throws ParseException
	 */
	private static Date parseNccDateString(String dateString) {
		if (dateString != null && dateString.length() == 8) {
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date result;
			try {
				result = df.parse(dateString);
			} catch (ParseException e) {
				throw new IllegalArgumentException("Invalid date string " + dateString, e);
			}
			return result;
		}
		throw new IllegalArgumentException("Invalid date string " + dateString);
	}

}
