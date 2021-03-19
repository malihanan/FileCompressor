package com.malihanan.compressor.algorithms.dictionary_based;

import com.malihanan.compressor.algorithms.AbstractCompressor;
import com.malihanan.compressor.algorithms.bit_io.BitOutputStream;
import com.malihanan.compressor.algorithms.util.Extensions;

import java.io.*;
import java.util.*;

public abstract class BaseLZSSCompressor extends AbstractCompressor {

    private int windowSize;

    protected BaseLZSSCompressor() {}

    public BaseLZSSCompressor(File file){
        super(file, Extensions.LZSS);
        this.windowSize = 256;
    }

    @Override
    public void compress() {
        Map<Integer, List<Long>> indexes = new HashMap<>();

        try (RandomAccessFile rand_in = new RandomAccessFile(file, "r");
             RandomAccessFile rand_in1 = new RandomAccessFile(file, "r");
             RandomAccessFile rand_in2 = new RandomAccessFile(file, "r");
             FileOutputStream fos = new FileOutputStream(out_file);
             BitOutputStream out = new BitOutputStream(fos)) {

            int cur_byte;
            long i = 0;
            rand_in.seek(i);
            while ( (cur_byte = rand_in.read()) != -1) {
                boolean found = false;
                long start = 0;
                int maxLen = 0;
                List<Long> positions = indexes.get(cur_byte);

                if (positions==null) {
                    positions = new LinkedList<>();
                    positions.add(i);
                    indexes.put(cur_byte, positions);
                }
                else {
                    Iterator<Long> it = positions.iterator();

                    while (it.hasNext()) {
                        long pos = it.next();
                        if ((i - pos) >= windowSize) {
                            it.remove();
                            continue;
                        }
                        long i1=i+1, i2=pos+1;
                        rand_in1.seek(i1);
                        rand_in2.seek(i2);

                        int b1, b2, len;
                        for (len=1; len<i-pos; len++) {
                            b1 = rand_in1.read();
                            b2 = rand_in2.read();
                            if (b1 == -1 || b1 != b2) break;
                        }
                        if (len > maxLen) {
                            maxLen = len;
                            start = pos;
                        }
                        found = true;
                    }
                    positions.add(i);
                    int temp_ch;
                    rand_in1.seek(i+1);
                    for (long j=i+1; j<i+maxLen; j++) {
                        if ((temp_ch = rand_in1.read()) != -1) {
                            List<Long> temp_pos = indexes.get(temp_ch);
                            if (temp_pos == null) {
                                temp_pos = new LinkedList<>();
                                indexes.put(temp_ch, temp_pos);
                            }
                            temp_pos.add(j);
                        } else {
                            break;
                        }
                    }
                }
                i = writeSequence(out, found, start, maxLen, cur_byte, i);
                rand_in.seek(i);
            }
        } catch (FileNotFoundException e) {
            System.out.println("'" + file.getName() + "' not found.");
        } catch (IOException e) {
            System.out.println("Error reading bytes from the file.");
        }
    }

    abstract public long writeSequence(BitOutputStream out, boolean found, long start, int maxLen, int cur_byte, long i);
}
