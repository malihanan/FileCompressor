package com.malihanan.compressor.algorithms.deflate;

import com.malihanan.compressor.algorithms.bit_io.BitOutputStream;
import com.malihanan.compressor.algorithms.dictionary_based.BaseLZSSCompressor;
import com.malihanan.compressor.algorithms.util.Extensions;

import java.io.File;

public class DeflateLZSSCompressor extends BaseLZSSCompressor {

    public DeflateLZSSCompressor(File file) {
        super(file);
        setExtension(Extensions.DEFLATE_LZSS);
    }

    @Override
    public long writeSequence(BitOutputStream out, boolean found, long start, int maxLen, int cur_byte, long i) {
        out.writeByte(maxLen);
        if (maxLen == 1) out.writeByte(cur_byte);
        else out.writeByte((int)(i - start));

        i+=maxLen;
        return i;
    }
}
