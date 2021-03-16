package com.malihanan.compressor.algorithms.bit_io;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitOutputStream implements AutoCloseable {
    int buffer;
    byte count;
    BufferedOutputStream out;
    FileOutputStream fos;

    public BitOutputStream(FileOutputStream fos) {
        this.fos = fos;
        this.out = new BufferedOutputStream(fos);
        this.count = 0;
        this.buffer = 0;
    }

    public void writeBit(boolean set) {
        buffer <<= 1;
        if (set) buffer |= 1;
        count++;
        if (count == 8) {
            count = 0;
            writeByte(buffer);
            buffer = 0;
        }
    }

    public void writeByte(int val) {
        if (count==0) {
            try {
                out.write(val);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            for (int i=1; i<=8; i++) {
                writeBit(((val >>> (8-i)) & 1) == 1);
            }
        }
    }

    public void writeBytes(long val, int bytes) {
        for (int i=1; i<=bytes; i++) {
            writeByte((int)((val >> (8 * (8-i))) & 255));
        }
    }

    public void writeInt(int val) {
        writeBytes(val, 4);
    }

    public void writeLong(long val) {
        writeBytes(val, 8);
    }

    public void flush() {
        if (count==0) return;
        buffer <<= (8 - count);
        buffer &= 255;
        count=0;
        writeByte(buffer);
    }

    @Override
    public void close() throws IOException {
        flush();
        out.close();
        fos.close();
    }
}


