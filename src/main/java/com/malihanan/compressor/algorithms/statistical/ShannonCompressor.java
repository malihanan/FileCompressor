package com.malihanan.compressor.algorithms.statistical;

import com.malihanan.compressor.algorithms.util.Extensions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShannonCompressor extends StatisticalCompressor {

    private List<Node> nodes;
    private String[] hcode = new String[256];

    public ShannonCompressor(File file) {
        super(file, Extensions.SHANNON_FANO);
    }

    public void compress() {
        calculateFrequency();
        formNodes();
        root = this.formTree("", 0, nodes.size() - 1);
        assignCodes(root, "");
        printCodes();
        writeToFile();
    }

    private void formNodes() {
        nodes = new ArrayList<>();
        int i;
        for (i=0; i<256; i++) {
            if (count[i]!=0) {
                nodes.add(new Node(i, this.count[i]));
            }
        }
        Collections.sort(nodes);
    }

    private Node formTree(String code, int s, int e) {
        int l = s, r = e, lSum=0, rSum=0;
        if (s == e) return nodes.get(s);
        while (true) {
            if (lSum <= rSum) {
                lSum += nodes.get(l).getCount();
                if (l+1 < r) l++;
                else break;
            } else {
                rSum += nodes.get(r).getCount();
                if (r-1 > l) r--;
                else break;
            }
        }
        return new Node(formTree(code+"0", s, l), formTree(code+"1", r, e));
    }

}
