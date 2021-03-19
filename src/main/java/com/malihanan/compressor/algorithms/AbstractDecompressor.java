package com.malihanan.compressor.algorithms;

import com.malihanan.compressor.algorithms.Decompressor;

import java.io.File;

public abstract class AbstractDecompressor implements Decompressor {
    protected String extension;
    protected File file;
    protected File out_file;

    public AbstractDecompressor() {}
    public AbstractDecompressor(File file, String extension) {
        this.file = file;
        this.extension = extension;
        String decomressedPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - file.getName().length()) + "decompressed_" + file.getName().substring(0, file.getName().length() - extension.length());
        this.out_file = new File(decomressedPath);
    }

    @Override
    public File callDecompress() {
        System.out.println("Decompressing... '" + file.getAbsolutePath() + "'");
        decompress();
        System.out.println("Decompressed '" + out_file.getAbsolutePath() + "'");
        return out_file;
    }

    public String getExtension(String extension) { return this.extension; }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public File getDecompressedFile() { return out_file; }

    public long getInFileSize() { return file.length(); }

    public long getOutFileSize() { return out_file.length(); }
}
