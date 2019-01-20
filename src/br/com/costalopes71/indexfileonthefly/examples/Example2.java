package br.com.costalopes71.indexfileonthefly.examples;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

import br.com.costalopes71.indexfileonthefly.indexer.MemoryIndexer;

public class Example2 {

	private static final String FILE_PATH = "F:\\workspaces\\CodigosDeTeste_workspace\\docs\\arquivo_entrada\\LEITURA_ALIMENTADOR_20180726.dsv";
	
	public static void main(String[] args) throws InterruptedException {
		
		Instant now = Instant.now();
		try {
			long count = MemoryIndexer.getLinesCount(FILE_PATH, StandardCharsets.ISO_8859_1);
			System.out.println("Total lines: " + count + " seconds");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Elapsed time: " + Duration.between(now, Instant.now()).toSeconds());
		
		Instant now2 = Instant.now();
		try {
			long count = MemoryIndexer.getLinesCountUsingStream(Paths.get(FILE_PATH), StandardCharsets.ISO_8859_1);
			System.out.println("Total lines: " + count);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Elapsed time: " + Duration.between(now2, Instant.now()).toSeconds() + " seconds");
		
	}

}
