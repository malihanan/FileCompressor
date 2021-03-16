package com.malihanan.compressor.algorithms.dictionary_based;

import com.malihanan.compressor.algorithms.Decompressor;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LZWDecompressor implements Decompressor {

    private File file;
    private Map<Integer, String> table;
    private long in_file_size, out_file_size;

    public LZWDecompressor(File file) {
        this.file = file;
        table = new HashMap<>();
        for (int i=0; i<256; i++) {
            table.put(i, ((char)i) + "");
        }
        in_file_size = out_file_size = 0;
    }

    public File decompress() {
        int old;
        char c;
        String op_code;
        int code = 256;
        int cur_char;

        String decomressedPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - file.getName().length() - 1) + "\\decompressed_" + file.getName().substring(0, file.getName().length() - 7);
        File out_file = new File(decomressedPath);

        System.out.println("\nDecompressing '" + file.getName() + "'...");

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, "UTF_16BE");
             BufferedReader reader =  new BufferedReader(isr);
             FileWriter fw = new FileWriter(out_file);
             BufferedWriter writer = new BufferedWriter(fw)) {

            if ((cur_char = reader.read()) != -1) {
                old = cur_char;
                in_file_size+=2;
                op_code = table.get(old);
                c = op_code.charAt(0);
                writer.write(op_code);
                out_file_size+=op_code.length();

                while ( (cur_char = reader.read()) != -1) {
                    in_file_size+=2;
                    if (table.containsKey(cur_char)) {
                        op_code = table.get(cur_char);
                    } else {
                        op_code = table.get(old);
                        op_code += c;
                    }
                    writer.write(op_code);
                    out_file_size += op_code.length();
                    c = op_code.charAt(0);
                    table.put(code++, table.get(old) + c);
                    old = cur_char;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("'" + file.getName() + "' not found.");
        } catch (IOException e) {
            System.out.println("Error reading bytes from the file.");
        }

        System.out.println("Max table entry: " + code);
        System.out.println("Decompressed - " + decomressedPath);
        System.out.println("Original File Size: " + in_file_size + " | Decompressed File Size: " + out_file_size);
        return out_file;
    }
}
