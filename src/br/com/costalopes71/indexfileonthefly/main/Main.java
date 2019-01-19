package br.com.costalopes71.indexfileonthefly.main;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import br.com.costalopes71.indexfileonthefly.indexer.MemoryIndexer;

public class Main {

	public static void main(String[] args) {
		
		Instant now = Instant.now();
		
		MemoryIndexer index;
		try {
			index = MemoryIndexer.getInstance();
			
			index.getKeys().forEach(key -> {
				System.out.println("Key: " + key + " with " + index.getPositions(key).size() + " positions");
			});
			
			Duration elapsed = Duration.between(now, Instant.now());
			
			Instant now2 = Instant.now();
			System.out.println("\nUsing index:");
			List<Long> positions = index.getPositions("AAA");
			
			RandomAccessFile file = new RandomAccessFile("F:\\workspaces\\CodigosDeTeste_workspace\\docs\\testeFile.csv", "r");
			for (Long pos : positions) {
				file.seek(pos);
				System.out.println(file.readLine());
			}
			
			file.close();
			System.out.println("Elapsed time for reading indexed file: " + Duration.between(now2, Instant.now()).getSeconds() + " seconds");
			System.out.println("Elapsed time: " + elapsed.getSeconds() + " seconds");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}