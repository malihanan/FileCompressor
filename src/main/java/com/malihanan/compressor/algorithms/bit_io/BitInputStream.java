package com.malihanan.compressor.algorithms.bit_io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class BitInputStream implements AutoCloseable {
    int buffer;
    byte count;
    FileInputStream fis;
    BufferedInputStream in;

    public BitInputStream(FileInputStream fis) {
        this.fis = fis;
        this.in = new BufferedInputStream(fis);
        this.count = 0;
        this.buffer = 0;
    }

    public boolean readBit() throws IOException {
        if (count == 0) {
            buffer = readByte();
            if (buffer == -1) throw new IOException("EOF");
            count = 8;
        }
        count-=1;
        return ((buffer >> count) & 1) == 1;
    }

    public int readByte() throws IOException {
        int x = in.read();
        if (x==-1) return -1;
        if (count==0) return x;
        buffer <<= (8 - count);
        buffer &= 255;
        buffer |= (x >> count);
        int returnVal = buffer;
        buffer = x;
        return  returnVal;
    }

    public long readBytes(int bytes) throws IOException {
        long ret = 0;
        for (int i=0; i<bytes; i++) {
            ret <<= 8;
            ret |= readByte();
        }
        return ret;
    }

    public int readInt() throws IOException {
        return (int)readBytes(4);
    }

    public long readLong() throws IOException {
        return readBytes(8);
    }

    @Override
    public void close() throws IOException {
        in.close();
        fis.close();
    }
}
