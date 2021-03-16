package com.malihanan.compressor.algorithms.statistical;

import java.io.File;
import java.util.PriorityQueue;

public class HuffmanCompressor extends StatisticalCompressor {

    private PriorityQueue<Node> minHeap;

    public HuffmanCompressor(File file) {
        super(file);
    }

    @Override
    public File compress() {
        System.out.println("\nCompressing '" + this.file.getName() + "'...");
        this.calculateFrequency();
        this.formMinHeap();
        this.formTree();
        this.assignCodes(this.root, "");
        this.printCodes();
        return this.writeToFile(".hzip");
    }

    private void formMinHeap() {
        minHeap = new PriorityQueue<>();
        int i;
        for (i=0; i<256; i++) {
            if (count[i]!=0) {
                minHeap.add(new Node(i, this.count[i]));
            }
        }
    }

    private void formTree() {
        while (minHeap.size() > 1) {
            Node a = minHeap.poll();
            Node b = minHeap.poll();
            minHeap.add(new Node(a.getCount() + b.getCount(), a, b));
        }
        if (minHeap.size() == 1) {
            this.root = minHeap.poll();
        } else {
            this.root = null;
        }
    }
}
