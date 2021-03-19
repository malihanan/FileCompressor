package com.malihanan.compressor.algorithms.deflate;

import com.malihanan.compressor.algorithms.AbstractDecompressor;
import com.malihanan.compressor.algorithms.statistical.StatisticalDecompressor;
import com.malihanan.compressor.algorithms.util.Extensions;

import java.io.File;

public class Inflate extends AbstractDecompressor {

    public Inflate(File file) {
        super(file, Extensions.DEFLATE);
    }

    @Override
    public void decompress() {
        StatisticalDecompressor huffman = new StatisticalDecompressor(file);
        huffman.decompress();
        File huffDecompressedFileRenamed = new File(huffman.getDecompressedFile().getAbsolutePath() + ".lzsszip");
        huffman.getDecompressedFile().renameTo(huffDecompressedFileRenamed);

        InflateLZSSDecompressor lzss = new InflateLZSSDecompressor(huffDecompressedFileRenamed);
        lzss.decompress();

        lzss.getDecompressedFile().renameTo(out_file);
        huffDecompressedFileRenamed.delete();
        lzss.getDecompressedFile().delete();
    }
}
