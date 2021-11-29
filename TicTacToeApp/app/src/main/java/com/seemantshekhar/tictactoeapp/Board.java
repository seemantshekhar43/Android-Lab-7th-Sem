package com.seemantshekhar.tictactoeapp;

import java.util.Arrays;

public class Board {
    int[][] matrix;
    int size;

    public Board(int size) {
        this.size = size;
        this.matrix = new int[size][size];
    }

    @Override
    public String toString() {
        return "Board{" +
                "board=" + Arrays.toString(matrix) +
                '}';
    }
}
