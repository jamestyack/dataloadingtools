/**
 * 
 */
package net.tyack.dataloading;

import net.tyack.dataloading.batch.data.IRepository;
import net.tyack.dataloading.batch.data.MongoRepository;
import net.tyack.dataloading.batch.mapper.CSVToAccidentMapper;
import net.tyack.dataloading.batch.mapper.ICSVToAccidentMapper;

import com.google.inject.AbstractModule;

/**
 * @author jamestyack
 *
 */
public class AccidentsBatchJobModule extends AbstractModule {

	/* (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		
		/*
	      * This tells Guice that whenever it sees a dependency on an ICSVToAccidentMapper,
	      * it should satisfy the dependency using a CSVToAccidentMapper.
	      */
		bind(ICSVToAccidentMapper.class).to(CSVToAccidentMapper.class);
		
		/*
		 * Same with IRepository and MongoRepository
		 */
		bind(IRepository.class).to(MongoRepository.class);

	}
}
