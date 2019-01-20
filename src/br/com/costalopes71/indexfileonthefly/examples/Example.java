package br.com.costalopes71.indexfileonthefly.examples;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import br.com.costalopes71.indexfileonthefly.indexer.MemoryIndexer;

public class Example {

	public static void main(String[] args) {
		
		Instant now = Instant.now();
		
		MemoryIndexer index;
		try {
			index = MemoryIndexer.getInstance();
			
			System.out.println(index.getKeys().size());
			
			System.out.println("Elapsed time: " + Duration.between(now, Instant.now()).getSeconds() + " seconds");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}