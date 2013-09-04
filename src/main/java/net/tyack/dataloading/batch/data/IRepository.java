/**
 * 
 */
package net.tyack.dataloading.batch.data;

import java.net.UnknownHostException;
import java.util.Map;

import net.tyack.dataloading.model.CommonBean;
import net.tyack.dataloading.model.nottinghamtraffic.Accident;

/**
 * @author jamestyack
 *
 */
public interface IRepository {

	/**
	 * @param mapOfAccidents
	 */
	void save(Map<String, Accident> map) throws UnknownHostException;

}
