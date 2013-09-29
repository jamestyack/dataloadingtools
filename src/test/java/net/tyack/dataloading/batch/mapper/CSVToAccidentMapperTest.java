/**
 * 
 */
package net.tyack.dataloading.batch.mapper;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import net.tyack.dataloading.model.nottinghamtraffic.Accident;
import net.tyack.dataloading.model.nottinghamtraffic.Person;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jamestyack
 *
 */
public class CSVToAccidentMapperTest {

	private CSVToAccidentMapper mapper = new CSVToAccidentMapper();
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testMap() throws ParseException {
		Map<String, Accident> map = mapper.map("src/test/resources/csv/ncc_Traffic_accident_Casualties_test_data.csv");
		assertEquals(15, map.size());
		
		// check all details for single slight accident with 1 person
		Accident accident1 = map.get("2A254912");
		assertEquals("2A254912", accident1.getId());
		assertEquals("Slight", accident1.getSeverity());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date expectedDate = sdf.parse("11/12/2012");
		assertEquals(expectedDate, accident1.getAccidentDate());
		assertEquals(12, accident1.getMonth());
		assertEquals("07:55", accident1.getTime());
		assertEquals("AM_PEAK", accident1.getTimeCategory());
		assertEquals(2, accident1.getNumVeh());
		assertEquals("53.10078394", accident1.getLat());
		assertEquals("-1.26663601", accident1.getLng());
		assertEquals(1, accident1.getPersons().size());
		assertEquals(17, accident1.getDriverAges().get(0));
		assertEquals("Male", accident1.getDriverSexes().get(0));
		Person person = accident1.getPersons().get(0);
		assertEquals(1, person.getPersonRef());
		assertEquals("Slight", person.getSeverity());
		assertEquals("Male", person.getSex());
		assertEquals("Driver or rider", person.getType());
		assertEquals(17, person.getAge());
		
		// check fatal accident with 2 persons
		Accident accident2 = map.get("4B267112");
		assertEquals("PM_OFF_PEAK", accident2.getTimeCategory());
		assertEquals("Fatal", accident2.getSeverity());
		assertEquals(1, accident2.getDriverAges().size());
		assertEquals(26, accident2.getDriverAges().get(0));
		assertEquals("Male", accident2.getDriverSexes().get(0));
		assertEquals(2, accident2.getPersons().size());
		
		// check slight accident with 2 drivers. Decided to choose youngest one
		Accident accident3 = map.get("4C248312");
		assertEquals("EARLY_EVENING", accident3.getTimeCategory());
		// check for slight severity
		assertEquals(2, accident3.getDriverAges().size());
		assertEquals(2, accident3.getDriverSexes().size());
		assertEquals("Slight", accident3.getSeverity());
		assertEquals(46, accident3.getDriverAges().get(0));
		assertEquals("Male", accident3.getDriverSexes().get(0));
		assertEquals(37, accident3.getDriverAges().get(1));
		assertEquals("Male", accident3.getDriverSexes().get(1));
		assertEquals(2, accident3.getPersons().size());
		
		// check serious accident with 2 persons
		Accident accident4 = map.get("2A247312");
		assertEquals("LATE_EVENING", accident4.getTimeCategory());
		// check for serious severity
		assertEquals("Serious", accident4.getSeverity());
		assertEquals(2, accident4.getPersons().size());	
		
		// check serious accident with 2 persons
		Accident accident5 = map.get("4D263712");
		assertEquals("NIGHT", accident5.getTimeCategory());
		// check for fatal severity
		assertEquals("Fatal", accident5.getSeverity());
		assertEquals(3, accident5.getPersons().size());	
		assertEquals(50, accident5.getDriverAges().get(0));
		assertEquals("Female", accident5.getDriverSexes().get(0));
		
		// check accident with person that has 'Unknown' age and no driver
		Accident accident6 = map.get("2C222612");
		assertEquals("AM_OFF_PEAK", accident6.getTimeCategory());
		assertEquals("Slight", accident6.getSeverity());
		assertEquals(1, accident6.getPersons().size());
		assertEquals(-1, accident6.getPersons().get(0).getAge());
		assertEquals(0, accident6.getDriverAges().size());
		assertEquals(0, accident6.getDriverSexes().size());
		assertTrue(StringUtils.isEmpty(accident6.getPedestrianSeverity()));
		
		// check accident where pedestrian injured and severity slight
		Accident accident7 = map.get("2D255112");
		assertEquals("PM_PEAK", accident7.getTimeCategory());
		assertEquals("Slight", accident7.getPedestrianSeverity());
		
		// check accident where pedestrian injured and severity slight
		Accident accident8 = map.get("2C164312");
		assertEquals("EARLY_MORNING", accident8.getTimeCategory());
		assertEquals("Serious", accident8.getPedestrianSeverity());
		
		// check accident where pedestrian injured and severity slight
		Accident accident9 = map.get("4B191412");
		assertEquals("Fatal", accident9.getPedestrianSeverity());
		
		
		
		
	}
}
