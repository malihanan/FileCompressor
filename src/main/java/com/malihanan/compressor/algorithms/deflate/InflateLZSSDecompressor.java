package com.malihanan.compressor.algorithms.deflate;

import com.malihanan.compressor.algorithms.AbstractDecompressor;
import com.malihanan.compressor.algorithms.bit_io.BitInputStream;
import com.malihanan.compressor.algorithms.util.Extensions;

import java.io.*;

public class InflateLZSSDecompressor extends AbstractDecompressor {

    private int windowSize;

    public InflateLZSSDecompressor(File file) {
        super(file, Extensions.DEFLATE_LZSS);
        this.windowSize = 256;
    }

    @Override
    public void decompress() {
        try (FileInputStream fis = new FileInputStream(file);
             BitInputStream in = new BitInputStream(fis);
             FileWriter fw = new FileWriter(out_file);
             BufferedWriter writer = new BufferedWriter(fw);
             RandomAccessFile rand_in = new RandomAccessFile(out_file, "r")) {

            long start, i=0;
            int length;
            while (true) {
                length = in.readByte();

                if (length == 1) {
                    writer.write(in.readByte());
                    i++;
                } else {
                    start = i - in.readByte();
                    rand_in.seek(start);
                    for (long j=start; j<start+length; j++) {
                        writer.write(rand_in.read());
                    }
                    i+=length;
                }
                writer.flush();
            }

        } catch (FileNotFoundException e) {
            System.out.println("'" + file.getName() + "' not found.");
        } catch (IOException e) {}
    }

}
