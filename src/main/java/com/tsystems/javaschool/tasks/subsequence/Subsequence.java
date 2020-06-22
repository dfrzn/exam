package com.tsystems.javaschool.tasks.subsequence;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Subsequence {

    public boolean find(List x, List y) {
        if (x == null || y == null) throw new IllegalArgumentException(); //check on null
        if (x.isEmpty()) return true; // check if first list is empty

        // queue better remove elemnt
        Queue xQueue = new LinkedList<>(x);
        Queue yQueue = new LinkedList<>(y);
        //objects for items from lists
        Object xItem;
        Object yItem;

        xItem = xQueue.remove(); //get the first item of the X list

        while (!yQueue.isEmpty()) {
            yItem = yQueue.remove();
                if (yItem.equals(xItem)) {
                if (xQueue.isEmpty()) return true; // if xQueue is empty, we found x in y
                xItem = xQueue.remove();
            }
        }
        return false;
    }
}
