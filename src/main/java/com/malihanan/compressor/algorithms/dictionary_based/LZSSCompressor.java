package com.malihanan.compressor.algorithms.dictionary_based;

import com.malihanan.compressor.algorithms.bit_io.BitOutputStream;

import java.io.*;

public class LZSSCompressor extends BaseLZSSCompressor {

    public LZSSCompressor(File file){
        super(file);
    }

    @Override
    public long writeSequence(BitOutputStream out, boolean found, long start, int maxLen, int cur_byte, long i) {
        if (found && maxLen > 1) {
            out.writeBit(true);
            out.writeByte((int)(i - start));
            out.writeByte(maxLen);
            i+=maxLen;
        } else {
            out.writeBit(false);
            out.writeByte(cur_byte);
            i++;
        }
        return i;
    }
}
