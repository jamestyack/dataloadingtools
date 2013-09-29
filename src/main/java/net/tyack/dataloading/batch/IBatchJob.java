/**
 * 
 */
package net.tyack.dataloading.batch;

/**
 * @author jamestyack
 *
 */
public interface IBatchJob {

	void loadFromCSV(String pathAndFilename);
	
}
