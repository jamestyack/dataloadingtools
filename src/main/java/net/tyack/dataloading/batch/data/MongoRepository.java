/**
 * 
 */
package net.tyack.dataloading.batch.data;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

import net.tyack.dataloading.model.nottinghamtraffic.Accident;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;

/**
 * @author jamestyack
 *
 */
public class MongoRepository implements IRepository {

	/* (non-Javadoc)
	 * @see net.tyack.dataloading.batch.data.IRepository#save(java.util.Map)
	 */
	public void save(Map<String, Accident> accidents) throws UnknownHostException {
		Mongo client = new MongoClient("localhost", 27017);
		Datastore ds = new Morphia().createDatastore(client, "tyack");
		Iterator<String> iterator = accidents.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			ds.save(accidents.get(key));
		}
		System.out.println("Mongo load complete, total Accident entries " + ds.getCount(Accident.class));
	}
}
