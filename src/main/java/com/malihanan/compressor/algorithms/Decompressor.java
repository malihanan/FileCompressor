package com.malihanan.compressor.algorithms;

import java.io.File;

public interface Decompressor {
    void decompress();
    File callDecompress();
    long getInFileSize();
    long getOutFileSize();
}
