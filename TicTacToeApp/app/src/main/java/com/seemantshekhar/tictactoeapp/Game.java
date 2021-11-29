package com.seemantshekhar.tictactoeapp;

 enum GameResponse{
    DRAW, PLAYER_1, PLAYER_2, INVALID_MOVE, CONTINUE;
}
public class Game {
    private  Board board;
    private  int player;
    private int moves;

    private int[] rowResult;
    private int[] colResult;
    private int diagonalResult;
    private int inverseDiagonalResult;

    public Game() {
        resetGame();
    }

    public void resetGame(){
        this.board = new Board(3);
        this.player = 1;
        this.moves = 0;

        this.rowResult = new int[3];
        this.colResult = new int[3];
        this.diagonalResult = 0;
        this.inverseDiagonalResult = 0;
    }

    public GameResponse play(Move move){

        if (moves < board.size * board.size){
            boolean isValidMove = validateMove(move);
            if(!isValidMove){
                return GameResponse.INVALID_MOVE;
            }
            int result = result(move);
            if(result == 1){

                return GameResponse.PLAYER_1;
            }else if(result == -1){
                return  GameResponse.PLAYER_2;
            }else{
                moves++;
                player  = player * -1;
            }
            if(moves == board.size * board.size){
                return GameResponse.DRAW;
            }
        }else{
            return GameResponse.DRAW;
        }
        return  GameResponse.CONTINUE;
    }

    public int result(Move move){

        //update board
        board.matrix[move.row][move.col] = move.playerID;

        //update counters

        rowResult[move.row] += move.playerID;
        colResult[move.col] += move.playerID;

        if(move.row == move.col){
            diagonalResult += move.playerID;
        }

        if(move.row == board.size - 1 - move.col){
            inverseDiagonalResult += move.playerID;
        }

        //check counters
        if(Math.abs(rowResult[move.row]) == board.size || Math.abs(colResult[move.col]) == board.size || Math.abs(diagonalResult) == board.size ||  Math.abs(inverseDiagonalResult) == board.size){
            return move.playerID;
        }

        return 0;
    }

    public boolean validateMove(Move move){
        if(move.row < 0 || move.row >= board.size || move.col < 0 || move.col > board.size || board.matrix[move.row][move.col] != 0){
            return  false;
        }
        return true;
    }
}
