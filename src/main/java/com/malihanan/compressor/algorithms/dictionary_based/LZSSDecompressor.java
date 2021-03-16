package com.malihanan.compressor.algorithms.dictionary_based;

import com.malihanan.compressor.algorithms.Decompressor;
import com.malihanan.compressor.algorithms.bit_io.BitInputStream;
import com.malihanan.compressor.algorithms.bit_io.BitOutputStream;

import java.io.*;

public class LZSSDecompressor implements Decompressor {

    private File file;
    private int windowSize;

    public LZSSDecompressor(File file){
        this.file = file;
        this.windowSize = 256;
    }

    @Override
    public File decompress() {

        String decomressedPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - file.getName().length() - 1) + "\\decompressed_" + file.getName().substring(0, file.getName().length() - 8);
        File out_file = new File(decomressedPath);

        System.out.println("\nDecompressing '" + file.getName() + "'...");

        try (FileInputStream fis = new FileInputStream(file);
             BitInputStream in = new BitInputStream(fis);
             FileWriter fw = new FileWriter(out_file);
             BufferedWriter writer = new BufferedWriter(fw);
             RandomAccessFile rand_in = new RandomAccessFile(out_file, "rw")) {

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
                        writer.write(rand_in.read());
                    }
                    i+=length;
                } else {
                    writer.write(in.readByte());
                    i++;
                }
                writer.flush();
            }

        } catch (FileNotFoundException e) {
            System.out.println("'" + file.getName() + "' not found.");
        } catch (IOException e) {
            System.out.println("Error reading bytes from the file.");
        }

        System.out.println("Decompressed - " + decomressedPath);
        return out_file;
    }
}
