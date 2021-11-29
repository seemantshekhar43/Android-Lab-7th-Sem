package com.seemantshekhar.tictactoeapp;

public class Move {

    int row;
    int col;
    int playerID;

    public Move(int row, int col, int playerID) {
        this.row = row;
        this.col = col;
        this.playerID = playerID;
    }

    @Override
    public String toString() {
        return "Move{" +
                "row=" + row +
                ", col=" + col +
                ", playerID=" + playerID +
                '}';
    }


}
