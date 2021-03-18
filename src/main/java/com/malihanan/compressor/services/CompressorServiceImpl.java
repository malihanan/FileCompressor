package com.malihanan.compressor.services;

import com.malihanan.compressor.algorithms.Compressor;
import com.malihanan.compressor.algorithms.Decompressor;
import com.malihanan.compressor.algorithms.dictionary_based.LZSSCompressor;
import com.malihanan.compressor.algorithms.dictionary_based.LZSSDecompressor;
import com.malihanan.compressor.algorithms.dictionary_based.LZWCompressor;
import com.malihanan.compressor.algorithms.dictionary_based.LZWDecompressor;
import com.malihanan.compressor.algorithms.statistical.StatisticalCompressor;
import com.malihanan.compressor.algorithms.statistical.StatisticalDecompressor;
import com.malihanan.compressor.algorithms.statistical.HuffmanCompressor;
import com.malihanan.compressor.algorithms.statistical.ShannonCompressor;
import com.malihanan.compressor.exceptions.InvalidExtensionException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.*;

@Service
public class CompressorServiceImpl implements CompressorService {
    @Override
    public File doAction(MultipartFile file, String action, String algo) throws IOException {
        File in_file = new File(file.getOriginalFilename());
        File out_file = null;
        try (FileOutputStream fos = new FileOutputStream(in_file)) {
            file.getInputStream().transferTo(fos);
            if (action.equals("compress")) {
                Compressor compressor = null;
                if (algo.equals("huffman")) {
                    compressor = new HuffmanCompressor(in_file);
                } else if (algo.equals("shannon")) {
                    compressor = new ShannonCompressor(in_file);
                } else if (algo.equals("lzw")) {
                    compressor = new LZWCompressor(in_file);
                } else if (algo.equals("lzss")) {
                    compressor = new LZSSCompressor(in_file);
                }
                out_file = compressor.compress();
            } else if (action.equals("decompress")) {
                Decompressor decompressor = null;
                if (algo.equals("huffman")) {
                    if (!in_file.getName().endsWith(".hzip"))
                        throw new InvalidExtensionException("File should be of type .hzip");
                    decompressor = new StatisticalDecompressor(in_file);
                } else if (algo.equals("shannon")) {
                    if (!in_file.getName().endsWith(".szip"))
                        throw new InvalidExtensionException("File should be of type .szip");
                    decompressor = new StatisticalDecompressor(in_file);
                } else if (algo.equals("lzw")) {
                    if (!in_file.getName().endsWith(".lzwzip"))
                        throw new InvalidExtensionException("File should be of type .lzwzip");
                    decompressor = new LZWDecompressor(in_file);
                } else if (algo.equals("lzss")) {
                    if (!in_file.getName().endsWith(".lzsszip"))
                        throw new InvalidExtensionException("File should be of type .lzsszip");
                    decompressor = new LZSSDecompressor(in_file);
                }
                out_file = decompressor.decompress();
            }
        }

        System.out.println("Deleted " + in_file.getAbsolutePath() + " : " + in_file.delete());
        return out_file;
    }

    @Override
    public void writeToOut(File file, ServletOutputStream out) throws IOException {
        try(FileInputStream fis = new FileInputStream(file)) {
            fis.transferTo(out);
            out.flush();
        }
        System.out.println("Deleted " + file.getAbsolutePath() + " : " + file.delete());
    }
}
