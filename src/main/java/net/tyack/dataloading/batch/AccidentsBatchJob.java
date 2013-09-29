/**
 * 
 */
package net.tyack.dataloading.batch;

import java.net.UnknownHostException;
import java.util.Map;

import net.tyack.dataloading.batch.data.IRepository;
import net.tyack.dataloading.batch.mapper.ICSVToAccidentMapper;
import net.tyack.dataloading.model.nottinghamtraffic.Accident;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * Batch Job to load the Nottingham Accidents data
 * @author jamestyack
 *
 */
public class AccidentsBatchJob implements IBatchJob {

	private Logger logger = LoggerFactory.getLogger(AccidentsBatchJob.class);
	
	private ICSVToAccidentMapper mapper;
	private IRepository repository;

	@Inject
	public AccidentsBatchJob(ICSVToAccidentMapper mapper, IRepository repository) {
		this.mapper = mapper;
		this.repository = repository;
	}
	
	@Override
	public void loadFromCSV(String pathAndFilename) {
		// get a map of accidents from the CSVMapper
		Map<String, Accident> mapOfAccidents = mapper.map(pathAndFilename);
		// persist the map to the DB
		try {
			repository.save(mapOfAccidents);
		} catch (UnknownHostException e) {
			logger.error("Problem saving accidents to repository " + e.getMessage());
		}
		
	}
	
}
