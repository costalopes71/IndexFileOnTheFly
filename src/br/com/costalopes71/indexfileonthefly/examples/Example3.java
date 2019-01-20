package br.com.costalopes71.indexfileonthefly.examples;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import br.com.costalopes71.indexfileonthefly.indexer.FileIndexer;

public class Example3 {

	private static final String INDEXED_FILE_PATH = "F:\\workspaces\\CodigosDeTeste_workspace\\docs\\indexedFile.csv";
	
	public static void main(String[] args) {
		
		Instant now = Instant.now();
		try {
			FileIndexer.getInstance().createIndexedFile(INDEXED_FILE_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Elapsed time: " + Duration.between(now, Instant.now()).toSeconds());
		
		System.out.println("\nBuild it again");
		Instant now2 = Instant.now();
		try {
			FileIndexer.getInstance().createIndexedFile("F:\\workspaces\\CodigosDeTeste_workspace\\docs\\indexedFile2.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Elapsed time for second build: " + Duration.between(now2, Instant.now()).toSeconds() + " seconds");
		
	}
	
}
