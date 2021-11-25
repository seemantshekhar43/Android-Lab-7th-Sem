package com.seemantshekhar.tictactoeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    boolean gameActive = true;
    private Button playAgainBtn;


    int activePlayer = 0;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};


    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}};
    public static int counter = 0;


    public void playerTap(View view) {
        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());


        if (!gameActive) {
            gameReset(view);
        }


        if (gameState[tappedImage] == 2) {

            counter++;


            if (counter == 9) {

                gameActive = false;
            }


            gameState[tappedImage] = activePlayer;


            img.setTranslationY(-1000f);

            // change the active player
            // from 0 to 1 or 1 to 0
            if (activePlayer == 0) {
                // set the image of x
                img.setImageResource(R.drawable.cross);
                activePlayer = 1;
                TextView status = findViewById(R.id.status);

                // change the status
                status.setText("Player 2's turn");
            } else {
                // set the image of o
                img.setImageResource(R.drawable.circle);
                activePlayer = 0;
                TextView status = findViewById(R.id.status);

                // change the status
                status.setText("Player 1's Turn");
            }
            img.animate().translationYBy(1000f).setDuration(300);
        }
        int flag = 0;
        // Check if any player has won
        for (int[] winPosition : winPositions) {
            if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                    gameState[winPosition[1]] == gameState[winPosition[2]] &&
                    gameState[winPosition[0]] != 2) {
                flag = 1;

                // Somebody has won! - Find out who!
                String winnerStr;

                // game reset function be called
                gameActive = false;
                if (gameState[winPosition[0]] == 0) {
                    winnerStr = "Player 1 is winner!!";
                } else {
                    winnerStr = "Player 2 is winner!!";
                }
                // Update the status bar for winner announcement
                TextView status = findViewById(R.id.status);
                status.setText(winnerStr);
            }
        }
        // set the status if the match draw
        if (counter == 9 && flag == 0) {
            TextView status = findViewById(R.id.status);
            status.setText("It's a draw!!");
        }
    }

    // reset the game
    public void gameReset(View view) {
        gameActive = true;
        activePlayer = 0;
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;
        }
        // remove all the images from the boxes inside the grid
        ((ImageView) findViewById(R.id.box_0)).setImageResource(R.drawable.blank);
        ((ImageView) findViewById(R.id.box_1)).setImageResource(R.drawable.blank);
        ((ImageView) findViewById(R.id.box_2)).setImageResource(R.drawable.blank);
        ((ImageView) findViewById(R.id.box_3)).setImageResource(R.drawable.blank);
        ((ImageView) findViewById(R.id.box_4)).setImageResource(R.drawable.blank);
        ((ImageView) findViewById(R.id.box_5)).setImageResource(R.drawable.blank);
        ((ImageView) findViewById(R.id.box_6)).setImageResource(R.drawable.blank);
        ((ImageView) findViewById(R.id.box_7)).setImageResource(R.drawable.blank);
        ((ImageView) findViewById(R.id.box_8)).setImageResource(R.drawable.blank);

        TextView status = findViewById(R.id.status);
        status.setText("Player 1 turn");

        playAgainBtn = findViewById(R.id.play_again_btn);

        playAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameReset(v);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}