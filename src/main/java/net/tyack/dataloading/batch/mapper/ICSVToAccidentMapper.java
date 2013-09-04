/**
 * 
 */
package net.tyack.dataloading.batch.mapper;

import java.util.Map;

import net.tyack.dataloading.model.nottinghamtraffic.Accident;

/**
 * @author jamestyack
 *
 */
public interface ICSVToAccidentMapper {

	/**
	 * @param pathAndFilename
	 * @return
	 */
	Map<String, Accident> map(String pathAndFilename);

}
