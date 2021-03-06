package com.malihanan.compressor.algorithms.dictionary_based;

import com.malihanan.compressor.algorithms.AbstractDecompressor;
import com.malihanan.compressor.algorithms.bit_io.BitInputStream;
import com.malihanan.compressor.algorithms.util.Extensions;

import java.io.*;

public class LZSSDecompressor extends AbstractDecompressor {

    private int windowSize;

    public LZSSDecompressor(File file){
        super(file, Extensions.LZSS);
        this.windowSize = 256;
    }

    @Override
    public void decompress() {
        try (FileInputStream fis = new FileInputStream(file);
             BitInputStream in = new BitInputStream(fis);
             FileWriter fw = new FileWriter(out_file);
             BufferedWriter writer = new BufferedWriter(fw);
             RandomAccessFile rand_in = new RandomAccessFile(out_file, "r")) {

            boolean sequenced;
            long start, i=0;
            int length;
            while (true) {
                try { sequenced = in.readBit(); }
                catch (IOException e) { break; }

                if (sequenced) {
                    start = i - in.readByte();
                    length = (int) in.readByte();
                    rand_in.seek(start);
                    for (long j=start; j<start+length; j++) {
                        int cur_byte = rand_in.read();
                        writer.write(cur_byte);
                    }
                    i+=length;

                } else {
                    int cur_byte = in.readByte();
                    if (cur_byte == -1) break;
                    writer.write(cur_byte);
                    i++;
                }
                writer.flush();
            }

        } catch (FileNotFoundException e) {
            System.out.println("'" + file.getName() + "' not found.");
        } catch (IOException e) {
            System.out.println("Error reading bytes from the file.");
        }
    }
}
