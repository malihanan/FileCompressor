package com.malihanan.compressor.algorithms.statistical;

import com.malihanan.compressor.algorithms.AbstractDecompressor;
import com.malihanan.compressor.algorithms.bit_io.BitInputStream;

import java.io.*;

public class StatisticalDecompressor extends AbstractDecompressor {

    protected String[] hcode = new String[256];
    protected Node root;
    protected long fileSize;

    public StatisticalDecompressor(File file) {
        super(file, "._zip");
    }

    @Override
    public void decompress() {
        try (FileInputStream fis = new FileInputStream(file);
             BitInputStream in = new BitInputStream(fis);
             FileOutputStream fos = new FileOutputStream(out_file);
             BufferedOutputStream out = new BufferedOutputStream(fos)) {
            root = readTree(in);
            fileSize = in.readLong();
            readContent(in, out);
        } catch (IOException e) { e.printStackTrace(); }
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
