package com.malihanan.compressor.algorithms.dictionary_based;

import com.malihanan.compressor.algorithms.AbstractCompressor;
import com.malihanan.compressor.algorithms.util.Extensions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LZWCompressor extends AbstractCompressor {

    private Map<String, Integer> table;

    public LZWCompressor(File file) {
        super(file, Extensions.LZW);
        table = new HashMap<>();
        for (int i=0; i<256; i++) {
            table.put(((char)i) + "", i);
        }
    }

    public void compress() {
        String p ="";
        char c;
        int code = 256;
        int cur_byte;

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis =  new BufferedInputStream(fis);
             FileOutputStream fos = new FileOutputStream(out_file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_16BE);
             BufferedWriter writer = new BufferedWriter(osw)) {

            if ((cur_byte = bis.read()) != -1) {
                p += (char)cur_byte;

                while ( (cur_byte = bis.read()) != -1) {
                    c = (char)cur_byte;
                    if (table.containsKey(p+c)) {
                        p = p+c;
                    } else {
                        writer.write(table.get(p));
                        if (code <= 50000) {
                            table.put(p + c, code++);
                        }
                        p = c + "";
                    }
                }
                writer.write(table.get(p));
            }
        } catch (FileNotFoundException e) {
            System.out.println("'" + file.getName() + "' not found.");
        } catch (IOException e) {
            System.out.println("Error reading bytes from the file.");
        }
    }
}
