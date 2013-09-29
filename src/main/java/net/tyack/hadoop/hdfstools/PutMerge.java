package net.tyack.hadoop.hdfstools;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class PutMerge {

	public static void main(String... args) throws IOException {
		if (args.length != 2) {
			System.out.println("Usage PutMerge <dir> <outfile>");
			System.exit(1);
		}
		
		System.out.println(System.getProperty("user.dir"));

		Configuration conf = new Configuration();
		conf.addResource(new Path("/usr/local/hadoop/conf/core-site.xml"));
		conf.addResource(new Path("/usr/local/hadoop/conf/hdfs-site.xml"));
		FileSystem hdfs = FileSystem.get(conf);
		FileSystem local = FileSystem.getLocal(conf);
		int filesProcessed = 0;

		Path inputDir = new Path(args[0]);
		Path hdfsFile = new Path(args[1]);

		try {
			FileStatus[] inputFiles = local.listStatus(inputDir);
			FSDataOutputStream out = hdfs.create(hdfsFile);
			for (int i = 0; i < inputFiles.length; i++) {
				if (!inputFiles[i].isDir()) {
					System.out.println("\tnow processing <"
							+ inputFiles[i].getPath().getName() + ">");
					FSDataInputStream in = local.open(inputFiles[i].getPath());

					byte buffer[] = new byte[256];
					int bytesRead = 0;
					while ((bytesRead = in.read(buffer)) > 0) {
						out.write(buffer, 0, bytesRead);
					}
					filesProcessed++;
					in.close();
				}
			}
			out.close();
			System.out.println("\nSuccessfully merged " + filesProcessed
					+ " local files and written to <" + hdfsFile.getName()
					+ "> in HDFS.");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
