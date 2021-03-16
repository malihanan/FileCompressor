package com.malihanan.compressor.algorithms.statistical;

import com.malihanan.compressor.algorithms.Decompressor;
import com.malihanan.compressor.algorithms.bit_io.BitInputStream;

import java.io.*;

public class StatisticalDecompressor implements Decompressor {
    protected File file;
    protected String[] hcode = new String[256];
    protected Node root;
    protected long fileSize;

    public StatisticalDecompressor(File file) {
        this.file = file;
    }

    public File decompress() {
        System.out.println("\nDecompressing '" + this.file.getName() + "'...\n");
        return readFile();
    }

    private File readFile() {
        String decomressedPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - file.getName().length() - 1) + "\\decompressed_" + file.getName().substring(0, file.getName().length() - 5);
        File out_file = new File(decomressedPath);
        try (FileInputStream fis = new FileInputStream(file);
             BitInputStream in = new BitInputStream(fis);
             FileOutputStream fos = new FileOutputStream(out_file);
             BufferedOutputStream out = new BufferedOutputStream(fos)) {
            root = readTree(in);
            fileSize = in.readLong();
            readContent(in, out);
            System.out.println("Decompressed - " + decomressedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out_file;
    }

    private Node readTree(BitInputStream in) throws IOException {
        if (in.readBit()) return new Node(in.readByte());
        return new Node(readTree(in), readTree(in));
    }

    private void readContent(BitInputStream in, BufferedOutputStream out) throws IOException {
        for (int i=0; i<fileSize; i++) {
            Node cur = this.root;
            while (!cur.isLeaf()) {
                cur = in.readBit() ? cur.getRight() : cur.getLeft();
            }
            out.write(cur.getByteValue());
        }
    }
}
