package com.malihanan.compressor.algorithms.dictionary_based;

import com.malihanan.compressor.algorithms.Compressor;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LZWCompressor implements Compressor {
    private File file;
    private Map<String, Integer> table;
    private long in_file_size, out_file_size;

    public LZWCompressor(File file) {
        this.file = file;
        table = new HashMap<>();
        for (int i=0; i<256; i++) {
            table.put(((char)i) + "", i);
        }
        in_file_size = out_file_size = 0;
    }

    public File compress() {
        String p ="";
        char c;
        int code = 256;
        int cur_byte;

        String compressedPath = file.getAbsolutePath() + ".lzwzip";
        File out_file = new File(compressedPath);

        System.out.println("\nCompressing '" + file.getName() + "'...");

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis =  new BufferedInputStream(fis);
             FileOutputStream fos = new FileOutputStream(out_file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF_16BE");
             BufferedWriter writer = new BufferedWriter(osw)) {

            if ((cur_byte = bis.read()) != -1) {
                p += (char)cur_byte;
                in_file_size+=1;

                while ( (cur_byte = bis.read()) != -1) {
                    in_file_size+=1;
                    c = (char)cur_byte;
                    if (table.containsKey(p+c)) {
                        p = p+c;
                    } else {
                        writer.write(table.get(p));
                        out_file_size+=2;
                        table.put(p+c, code++);
                        p = c + "";
                    }
                }
                writer.write(table.get(p));
                out_file_size+=2;
            }
        } catch (FileNotFoundException e) {
            System.out.println("'" + file.getName() + "' not found.");
        } catch (IOException e) {
            System.out.println("Error reading bytes from the file.");
        }

        System.out.println("Max table entry: " + code);
        System.out.println("Compressed - " + compressedPath);
        System.out.println("Original File Size: " + in_file_size + " | Compressed File Size: " + out_file_size);
        return out_file;
    }
}
