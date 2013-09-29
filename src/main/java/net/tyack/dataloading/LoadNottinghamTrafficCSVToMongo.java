package net.tyack.dataloading;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.tyack.dataloading.batch.AccidentsBatchJob;
import net.tyack.dataloading.batch.IBatchJob;
import net.tyack.dataloading.batch.data.IRepository;
import net.tyack.dataloading.batch.data.MongoRepository;
import net.tyack.dataloading.batch.mapper.CSVToAccidentMapper;
import net.tyack.dataloading.batch.mapper.ICSVToAccidentMapper;
import net.tyack.dataloading.model.nottinghamtraffic.Accident;
import net.tyack.dataloading.model.nottinghamtraffic.Person;
import au.com.bytecode.opencsv.CSVReader;

// see github/docs for Morphia at https://github.com/jmkgreen/morphia
import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

/**
 * Loads data from the CSV format file at
 * http://www.opendatanottingham.org.uk/dataset.aspx?id=84 Since data is
 * duplicated for accidents, the code will add a person to an existing record
 * when it comes across the same accident id in another row.
 * 
 * @author jamestyack
 * 
 */
public class LoadNottinghamTrafficCSVToMongo {

	/**
	 * 
	 */
	private static final String PATH_TO_CASUALTIES_FILE = "ncc_Traffic_Accident_Casualties.csv";
	
	public static void main(String[] args) throws IOException {
		
		Injector injector = Guice.createInjector(new AccidentsBatchJobModule());

		ICSVToAccidentMapper mapper = injector.getInstance(ICSVToAccidentMapper.class);
		IRepository repository = injector.getInstance(IRepository.class);
		IBatchJob loadAccidentsJob = new AccidentsBatchJob(mapper, repository);
		loadAccidentsJob.loadFromCSV(PATH_TO_CASUALTIES_FILE);

	}

	
}
