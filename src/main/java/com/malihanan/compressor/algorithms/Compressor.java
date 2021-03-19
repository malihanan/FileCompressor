package com.malihanan.compressor.algorithms;

import java.io.File;

public interface Compressor {
    void compress();
    File callCompress();
    long getInFileSize();
    long getOutFileSize();
}
