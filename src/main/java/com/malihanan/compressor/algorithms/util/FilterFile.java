package com.malihanan.compressor.algorithms.util;

import java.io.*;

public class FilterFile {

    private File file;

    public FilterFile(File file) {
        this.file = file;
    };

    public File filter() {
        File out_file = new File(file.getParent() + "\\filtered_" + file.getName());
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream in = new BufferedInputStream(fis);
             FileOutputStream fos = new FileOutputStream(out_file);
             BufferedOutputStream out = new BufferedOutputStream(fos))
        {
            int cur;
            while ((cur = in.read())!=-1) {
                if (0 < cur && cur < 128) {
                    out.write(cur);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out_file;
    }
}
