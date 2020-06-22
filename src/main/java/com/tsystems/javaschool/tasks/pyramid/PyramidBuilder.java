package com.tsystems.javaschool.tasks.pyramid;

import java.util.*;
import static java.lang.Math.sqrt;

public class PyramidBuilder {

    public int[][] buildPyramid(List<Integer> input) {
        if(input.contains(null)) throw new CannotBuildPyramidException();
        int strings = stringCounter(input); //calc string count
        if(strings == -1) throw new CannotBuildPyramidException();
        int columns = strings * 2 - 1; // calc column count
        int[][] pyramid = new int[strings][columns]; // create new 2D array
        Collections.sort(input); // sort input
        Queue<Integer> queue = new LinkedList<>(input); // create queue with linked list input
        int startPoint = (pyramid[0].length) / 2; // calc start position

        // start building pyramid
        for(int i = 0; i < pyramid.length; i++) {
            int start = startPoint;
            for(int j = 0; j <= i; j++) {
                pyramid[i][start] = queue.remove();
                start += 2; //shifting forward 2 positions
            }
            startPoint --;

        }
        return pyramid;
    }

    // method of calculation count of strings
    private static int stringCounter(List<Integer> input) {
        int	listSize = input.size();
        double result = (sqrt(1+ 8 * listSize) - 1)/2;
        if(result == Math.ceil(result)) // we need only positive int
            return (int)result;
        return -1;
    }
}
