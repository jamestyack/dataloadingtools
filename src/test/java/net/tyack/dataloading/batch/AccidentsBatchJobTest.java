/**
 * 
 */
package net.tyack.dataloading.batch;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import net.tyack.dataloading.batch.data.IRepository;
import net.tyack.dataloading.batch.mapper.ICSVToAccidentMapper;
import net.tyack.dataloading.model.nottinghamtraffic.Accident;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jamestyack
 *
 */
public class AccidentsBatchJobTest {
	

	private ICSVToAccidentMapper mockCsvObjectMapper;
	private AccidentsBatchJob accidentsBatchJob;
	private IRepository mockRepository;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		mockCsvObjectMapper = mock(ICSVToAccidentMapper.class);
		mockRepository = mock(IRepository.class);
		accidentsBatchJob = new AccidentsBatchJob(mockCsvObjectMapper, mockRepository);
	}

	@Test
	public void testLoadFromCsv() throws UnknownHostException {
		Map<String, Accident> mockAccidentsMap = createMockAccidentMap();
		when(mockCsvObjectMapper.map("someFileName.csv")).thenReturn((Map<String, Accident>) mockAccidentsMap);
		accidentsBatchJob.loadFromCSV("someFileName.csv");
		verify(mockCsvObjectMapper, only()).map("someFileName.csv");
		verify(mockRepository, only()).save(mockAccidentsMap);
	}

	private Map<String, Accident> createMockAccidentMap() {
		Map<String, Accident> mockAccidentsMap = new HashMap<String, Accident>();
		Accident accident = new Accident();
		accident.setId("id");
		accident.setYear((short)2011);
		mockAccidentsMap.put("id", accident );
		return mockAccidentsMap;
	}

}
