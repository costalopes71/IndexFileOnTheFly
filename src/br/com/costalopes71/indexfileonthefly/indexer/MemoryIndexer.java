package br.com.costalopes71.indexfileonthefly.indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryIndexer {

	private static final int KEY_POSITION = 4;
	private static final String SEPARATOR = "|";
	private static final String FILE_PATH = "F:\\workspaces\\CodigosDeTeste_workspace\\docs\\arquivo_entrada\\LEITURA_ALIMENTADOR_20180726.dsv";
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
		
		try (RandomAccessFile file = new RandomAccessFile(FILE_PATH, "r");) {
			
			// populate index and read file
			String s;
			do {
				
				long start = file.getFilePointer();
				s = file.readLine();
				
				if (s != null) {
					String key = getKey(s);
					index.computeIfAbsent(key, k -> new ArrayList<>()).add(start);
				}
					
			} while (s != null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private String getKey(String line) {
		return line.split(SEPARATOR)[KEY_POSITION];
	}

	public static long getLinesCountUsingStream(Path filePath, Charset charset) throws IOException {
		return Files.lines(filePath, charset).count();
	}
	
	public static long getLinesCount(String fileName, Charset encoding) throws IOException {
	    long linesCount = 0;
	    File file = new File(fileName);
	    FileInputStream fileIn = new FileInputStream(file);
	    int bufferSize = 4096;
	    try (	Reader fileReader = new InputStreamReader(fileIn, encoding);
	    		Reader reader = new BufferedReader(fileReader, bufferSize);) 
	    {
	        char[] buffer = new char[bufferSize];
	        int prevChar = -1;
	        int readCount = reader.read(buffer);
	        while (readCount != -1) {
	            for (int i = 0; i < readCount; i++) {
	                int nextChar = buffer[i];
	                switch (nextChar) {
	                    case '\r': {
	                        // The current line is terminated by a carriage return or by a carriage return immediately followed by a line feed.
	                        linesCount++;
	                        break;
	                    }
	                    case '\n': {
	                        if (prevChar == '\r') {
	                            // The current line is terminated by a carriage return immediately followed by a line feed.
	                            // The line has already been counted.
	                        } else {
	                            // The current line is terminated by a line feed.
	                            linesCount++;
	                        }
	                        break;
	                    }
	                }
	                prevChar = nextChar;
	            }
	            readCount = reader.read(buffer);
	        }
	        if (prevChar != -1) {
	            switch (prevChar) {
	                case '\r':
	                case '\n': {
	                    // The last line is terminated by a line terminator.
	                    // The last line has already been counted.
	                    break;
	                }
	                default: {
	                    // The last line is terminated by end-of-file.
	                    linesCount++;
	                }
	            }
	        }
	    } finally {
	        fileIn.close();
	    }
	    return linesCount;
	}
	
}
