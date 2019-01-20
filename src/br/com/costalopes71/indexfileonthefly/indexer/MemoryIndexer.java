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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton that builds an index for over a given file. It does it in memory using a map where the key is a <code>String</code>
 * and the value is a <code>List</code> of <code>Long</code> that contains all the byte positions in the file where that key was found. 
 * @author Joao Lopes
 *
 */
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

	/**
	 * Returns an unmodifiable <code>List</code> containing all positions in the file that are from a given key. Keep
	 * in mind that this method is <bold>NOT</bold> returning the line number of a given key, indeed it returns the byte position
	 * of the line that contains the key, so you can randomly access that line without needing to iterate the whole file.
	 * @param <code>String</code> key, the key that you want the positions of lines in the file to be returned
	 * @return <code>List<Long></code>, an unmodifiable list containing all the byte positions of a certain key.
	 */
	public List<Long> getPositions(String key) {
		return Collections.unmodifiableList(index.get(key));
	}

	/**
	 * Returns an unmodifiable <code>Set</code> of <code>String</code> containing all the existing keys in the file. 
	 * @return <code>Set</code> of <code>String</code> with all the keys in the file. Keep in mind that the returned <code>Set</code> is
	 * unmodifiable.
	 */
	public Set<String> getKeys() {
		return Collections.unmodifiableSet(index.keySet());
	}

	/**
	 * This method count the total of lines in a file. This version of this method uses java 8 approach to do so. 
	 * @param <code>String</code> filePath, the path to the file that the lines are to be counted.
	 * @param <code>Charset</code> encoding, the encode of the file.
	 * @return long, the total of lines in the file.
	 * @throws IOException
	 */
	public static long getLinesCountUsingStream(Path filePath, Charset encoding) throws IOException {
		return Files.lines(filePath, encoding).count();
	}
	
	/**
	 * This method count the total of lines in a file. 
	 * @param <code>String</code> filePath, the path to the file that the lines are to be counted.
	 * @param <code>Charset</code> encoding, the encode of the file.
	 * @return long, the total of lines in the file.
	 * @throws IOException
	 */
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

	private void buildIndex() throws IOException {
		System.out.println("Building index...");
		
		try (RandomAccessFile file = new RandomAccessFile(FILE_PATH, "r");) {
			
			//skip header
			file.readLine();
			
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
		
		System.out.println("Finished building index...");
	}
	
	private String getKey(String line) {
		return line.split(SEPARATOR)[KEY_POSITION];
	}

}