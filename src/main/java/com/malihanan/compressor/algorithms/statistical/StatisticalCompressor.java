package com.malihanan.compressor.algorithms.statistical;

import com.malihanan.compressor.algorithms.AbstractCompressor;
import com.malihanan.compressor.algorithms.bit_io.*;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public abstract class StatisticalCompressor extends AbstractCompressor {

    protected long fileSize;
    protected int[] count = new int[256];
    protected String[] hcode = new String[256];
    protected Node root;

    public StatisticalCompressor() {}

    public StatisticalCompressor(File file, String extension) {
        super(file, extension);
    }

    protected void calculateFrequency() {
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis =  new BufferedInputStream(fis)) {

            int cur_byte;
            while ( (cur_byte = bis.read()) != -1) {
                this.count[cur_byte] += 1;
                fileSize+=1;
            }
        } catch (FileNotFoundException e) {
            System.out.println("'" + file.getName() + "' not found.");
        } catch (IOException e) {
            System.out.println("Error reading bytes from the file.");
        }
    }

    protected void assignCodes(Node n, String s) {
        if (n.isLeaf()) {
            this.hcode[n.getByteValue()] = s=="" ? "1" : s;
            return;
        }
        assignCodes(n.getLeft(), s+"0");
        assignCodes(n.getRight(), s+"1");
    }

    protected void printCodes() {
        Queue<Node> queue = new LinkedList<>();
        queue.add(this.root);
        System.out.println("\nByte | Count | Code");
        System.out.println("-----+-------+---------");
        while (!queue.isEmpty()) {
            Node n = queue.poll();
            if (n.isLeaf()) {
                System.out.format("%4d | %5d | %s\n", n.getByteValue(), n.getCount(), this.hcode[n.getByteValue()]);
            }
            else {
                queue.add(n.getLeft());
                queue.add(n.getRight());
            }
        }
        System.out.println("-----+-------+---------\n");
    }

    protected void writeToFile() {
        try (FileOutputStream fos = new FileOutputStream(out_file);
             BitOutputStream bos = new BitOutputStream(fos);
             FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            writeTree(bos, this.root);
            bos.writeLong(fileSize);
            writeContent(bis, bos);

        } catch (IOException e) { e.printStackTrace(); }
    }

    protected void writeTree(BitOutputStream out, Node cur) {
        if (cur.isLeaf()) {
            out.writeBit(true);
            out.writeByte(cur.getByteValue());
            return;
        }
        out.writeBit(false);
        writeTree(out, cur.getLeft());
        writeTree(out, cur.getRight());
    }

    protected void writeContent(BufferedInputStream in, BitOutputStream out) throws IOException {
        int b;
        while ((b = in.read()) != -1) {
            String s = hcode[b];
            for (int i=0; i<s.length(); i++) {
                out.writeBit(s.charAt(i)=='1');
            }
        }
    }
}
