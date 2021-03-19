package com.malihanan.compressor.services;

import com.malihanan.compressor.algorithms.Compressor;
import com.malihanan.compressor.algorithms.Decompressor;
import com.malihanan.compressor.algorithms.deflate.Deflate;
import com.malihanan.compressor.algorithms.deflate.Inflate;
import com.malihanan.compressor.algorithms.dictionary_based.LZSSCompressor;
import com.malihanan.compressor.algorithms.dictionary_based.LZSSDecompressor;
import com.malihanan.compressor.algorithms.dictionary_based.LZWCompressor;
import com.malihanan.compressor.algorithms.dictionary_based.LZWDecompressor;
import com.malihanan.compressor.algorithms.statistical.StatisticalDecompressor;
import com.malihanan.compressor.algorithms.statistical.HuffmanCompressor;
import com.malihanan.compressor.algorithms.statistical.ShannonCompressor;
import com.malihanan.compressor.algorithms.util.Extensions;
import com.malihanan.compressor.exceptions.InvalidExtensionException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.*;

@Service
public class CompressorServiceImpl implements CompressorService {

    private double inSize, outSize;
    @Override
    public File doAction(MultipartFile file, String action, String algo) throws IOException {
        File in_file = new File(file.getOriginalFilename());
        File out_file = null;
        try (FileOutputStream fos = new FileOutputStream(in_file)) {
            file.getInputStream().transferTo(fos);
            if (action.equals("compress")) {
                Compressor compressor = getCompressor(algo, in_file);
                out_file = compressor.callCompress();
                inSize = compressor.getInFileSize();
                outSize = compressor.getOutFileSize();
            } else if (action.equals("decompress")) {
                Decompressor decompressor = getDecompressor(algo, in_file);
                out_file = decompressor.callDecompress();
                inSize = decompressor.getInFileSize();
                outSize = decompressor.getOutFileSize();
            }
        } finally {
            in_file.delete();
        }
        return out_file;
    }

    @Override
    public void writeToOut(File file, ServletOutputStream out) throws IOException {
        try(FileInputStream fis = new FileInputStream(file)) {
            fis.transferTo(out);
            out.flush();
        } finally {
            file.delete();
        }
    }

    public double getCompressionRatio(String action, MultipartFile in_file, File out_file) {
        System.err.println("In File Size: " + inSize);
        System.err.println("Out File Size: " + outSize);

        double ratio = 0;
        if (action.equals("compress"))
            ratio = (1.0 - (outSize / inSize)) * 100.0;
        else if (action.equals("decompress"))
            ratio = (1.0 - (inSize / outSize)) * 100.0;

        System.err.println("Compression Ratio: " + ratio);
        return ratio;
    }

    private Compressor getCompressor(String algo, File in_file) {
        Compressor compressor = null;
        if (algo.equals("huffman")) {
            compressor = new HuffmanCompressor(in_file);
        } else if (algo.equals("shannon")) {
            compressor = new ShannonCompressor(in_file);
        } else if (algo.equals("lzw")) {
            compressor = new LZWCompressor(in_file);
        } else if (algo.equals("lzss")) {
            compressor = new LZSSCompressor(in_file);
        } else if (algo.equals("deflate")) {
            compressor = new Deflate(in_file);
        }
        return compressor;
    }

    private Decompressor getDecompressor(String algo, File in_file) {
        Decompressor decompressor = null;
        if (algo.equals("huffman")) {
            if (!in_file.getName().endsWith(Extensions.HUFFMAN))
                throw new InvalidExtensionException("File should be of type " + Extensions.HUFFMAN);
            decompressor = new StatisticalDecompressor(in_file);
        } else if (algo.equals("shannon")) {
            if (!in_file.getName().endsWith(Extensions.SHANNON_FANO))
                throw new InvalidExtensionException("File should be of type " + Extensions.SHANNON_FANO);
            decompressor = new StatisticalDecompressor(in_file);
        } else if (algo.equals("lzw")) {
            if (!in_file.getName().endsWith(Extensions.LZW))
                throw new InvalidExtensionException("File should be of type " + Extensions.LZW);
            decompressor = new LZWDecompressor(in_file);
        } else if (algo.equals("lzss")) {
            if (!in_file.getName().endsWith(Extensions.LZSS))
                throw new InvalidExtensionException("File should be of type " + Extensions.LZSS);
            decompressor = new LZSSDecompressor(in_file);
        } else if (algo.equals("deflate")) {
            if (!in_file.getName().endsWith(Extensions.DEFLATE))
                throw new InvalidExtensionException("File should be of type " + Extensions.DEFLATE);
            decompressor = new Inflate(in_file);
        }
        return decompressor;
    }
}
