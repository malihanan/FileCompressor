package com.malihanan.compressor;

import com.malihanan.compressor.algorithms.Compressor;
import com.malihanan.compressor.algorithms.Decompressor;
import com.malihanan.compressor.algorithms.dictionary_based.LZSSCompressor;
import com.malihanan.compressor.algorithms.dictionary_based.LZSSDecompressor;
import com.malihanan.compressor.algorithms.dictionary_based.LZWCompressor;
import com.malihanan.compressor.algorithms.dictionary_based.LZWDecompressor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

//@SpringBootApplication
//public class CompressorApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(CompressorApplication.class, args);
//	}
//
//}

public class CompressorApplication {

	public static void main(String[] args) {
		String filePath = "C:\\Users\\hp\\Downloads\\pnp_temp.txt";
		Compressor compressor = new LZSSCompressor(new File(filePath));
		compressor.compress();

		Decompressor decompressor = new LZSSDecompressor(new File(filePath + ".lzsszip"));
		decompressor.decompress();
	}

}
