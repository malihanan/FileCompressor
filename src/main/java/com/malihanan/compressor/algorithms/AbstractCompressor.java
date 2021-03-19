package com.malihanan.compressor.algorithms;

import java.io.File;

public abstract class AbstractCompressor implements Compressor {
    protected String extension;
    protected File out_file;
    protected File file;

    public AbstractCompressor() {}

    public AbstractCompressor(File file, String extension) {
        this.file = file;
        this.extension = extension;
        this.out_file = new File(file.getAbsolutePath() + extension);
    }

    public File callCompress() {
        System.out.println("Compressing... '" + file.getAbsolutePath() + "'");
        compress();
        System.out.println("Compressed '" + out_file.getAbsolutePath() + "'");
        return out_file;
    }

    public String getExtension(String extension) { return this.extension; }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public File getCompressedFile() { return out_file; }

    public long getInFileSize() { return file.length(); }

    public long getOutFileSize() { return out_file.length(); }
}
