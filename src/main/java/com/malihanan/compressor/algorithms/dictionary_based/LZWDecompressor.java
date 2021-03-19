package com.malihanan.compressor.algorithms.dictionary_based;

import com.malihanan.compressor.algorithms.AbstractDecompressor;
import com.malihanan.compressor.algorithms.util.Extensions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LZWDecompressor extends AbstractDecompressor {

    private Map<Integer, String> table;

    public LZWDecompressor(File file) {
        super(file, Extensions.LZW);
        table = new HashMap<>();
        for (int i=0; i<256; i++) {
            table.put(i, ((char)i) + "");
        }
    }

    @Override
    public void decompress() {
        char c;
        String op_code;
        int code = 256, cur_char, old;

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_16BE);
             BufferedReader reader =  new BufferedReader(isr);
             FileWriter fw = new FileWriter(out_file);
             BufferedWriter writer = new BufferedWriter(fw)) {

            if ((cur_char = reader.read()) != -1) {
                old = cur_char;
                op_code = table.get(old);
                c = op_code.charAt(0);
                writer.write(op_code);

                while ( (cur_char = reader.read()) != -1) {
                    if (table.containsKey(cur_char)) {
                        op_code = table.get(cur_char);
                    } else {
                        op_code = table.get(old);
                        op_code += c;
                    }
                    writer.write(op_code);
                    c = op_code.charAt(0);
                    if (code <= 50000) {
                        table.put(code++, table.get(old) + c);
                    }
                    old = cur_char;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("'" + file.getName() + "' not found.");
        } catch (IOException e) {
            System.out.println("Error reading bytes from the file.");
        }
    }
}
