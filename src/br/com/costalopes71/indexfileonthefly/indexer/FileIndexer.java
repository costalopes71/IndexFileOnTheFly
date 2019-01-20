package br.com.costalopes71.indexfileonthefly.indexer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileIndexer {

	private static FileIndexer fileIndexer;
	private static Map<String, List<Long>> indexedInfo;
	private PrintWriter pw;
	
	private FileIndexer() throws IOException {

	}
	
	public static FileIndexer getInstance() throws IOException {
		if (fileIndexer == null) {
			fileIndexer = new FileIndexer();
			indexedInfo = MemoryIndexer.getInstance().getMapping();
		}
		
		return fileIndexer;
	}
	
	public void createIndexedFile(String indexedFilePath) throws FileNotFoundException {
		System.out.println("Building indexed file...");
		Instant now = Instant.now();
		
		pw = new PrintWriter(indexedFilePath);
		
		indexedInfo.forEach((key, list) -> {
			
			List<String> collect = list.stream().map(pos -> String.valueOf(pos)).collect(Collectors.toList());
			collect.add(collect.set(0, key));
			pw.println(String.join(";", collect));
			
		});
		
		pw.close();
		System.out.println("File indexed built. Elapsed time: " + Duration.between(now, Instant.now()).toSeconds() + " seconds");
	}
	
}
