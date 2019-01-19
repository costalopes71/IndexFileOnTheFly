package br.com.costalopes71.indexfileonthefly.indexer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryIndexer {

	private static final int KEY_POSITION = 0;
	private static final String SEPARATOR = ";";
	private static final String FILE_PATH = "F:\\workspaces\\CodigosDeTeste_workspace\\docs\\testeFile.csv";
	private static MemoryIndexer memoryIndexer;
	private ConcurrentHashMap<String, List<Long>> index = new ConcurrentHashMap<>();
	
	private MemoryIndexer() throws IOException {
		// build index
		buildIndex();
	}
	
	public static MemoryIndexer getInstance() throws IOException {
		if (memoryIndexer == null) {
			memoryIndexer = new MemoryIndexer();
		}
		return memoryIndexer;
	}

	public List<Long> getPositions(String key) {
		return index.get(key);
	}

	public Set<String> getKeys() {
		return index.keySet();
	}
	
	private void buildIndex() throws IOException {
		
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(FILE_PATH, "r");
			
			// populate index and read file
			String s;
			do {
				
				long start = file.getFilePointer();
				s = file.readLine();
				
				if (s != null) {
					String key = getKey(s);

					if (index.containsKey(key)) {
						index.get(key).add(start);
					} else {
						List<Long> positions = new ArrayList<>();
						positions.add(start);
						index.put(key, positions);
					}
					
				}
					
			} while (s != null);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				file.close();
			} catch (IOException e) {
				throw new IOException(e);
			}
		}
		
	}
	
	private String getKey(String line) {
		return line.split(SEPARATOR)[KEY_POSITION];
	}
	
}
