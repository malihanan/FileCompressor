package com.malihanan.compressor.algorithms.deflate;

import com.malihanan.compressor.algorithms.AbstractCompressor;
import com.malihanan.compressor.algorithms.statistical.HuffmanCompressor;
import com.malihanan.compressor.algorithms.util.Extensions;

import java.io.*;

public class Deflate extends AbstractCompressor {

    public Deflate(File file) {
        super(file, Extensions.DEFLATE);
    }

    @Override
    public void compress() {
        DeflateLZSSCompressor lzss = new DeflateLZSSCompressor(file);
        lzss.compress();

        HuffmanCompressor huffman = new HuffmanCompressor(lzss.getCompressedFile());
        huffman.compress();

        huffman.getCompressedFile().renameTo(out_file);
        huffman.getCompressedFile().delete();
        lzss.getCompressedFile().delete();
    }

}
