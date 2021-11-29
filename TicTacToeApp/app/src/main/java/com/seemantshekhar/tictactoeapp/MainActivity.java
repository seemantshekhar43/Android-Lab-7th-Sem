package com.seemantshekhar.tictactoeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Game game;
    private int current;
    private TextView status;
    private Button playAgainBtn;
    private boolean isActive;

    public void playerTap(View view) {
        if(!isActive){
            return;
        }
        isActive = false;
        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());
        int row = tappedImage / 3;
        int col = tappedImage % 3;
        Move move = new Move(row, col, current);
        GameResponse response = game.play(move);
        switch (response){
            case PLAYER_1:{
                img.setImageResource(current == 1 ? R.drawable.cross : R.drawable.circle);
                status.setText("Player 1 won!!");
                break;
            }
            case PLAYER_2:{
                img.setImageResource(current == 1 ? R.drawable.cross : R.drawable.circle);
                status.setText("Player 2 won!!");
                break;
            }
            case DRAW:{
                img.setImageResource(current == 1 ? R.drawable.cross : R.drawable.circle);
                status.setText("Game Drawn!!");
                break;
            }
            case CONTINUE:{
                //img.setTranslationY(-1000f);
                img.setImageResource(current == 1 ? R.drawable.cross : R.drawable.circle);
                current = current * -1;
                isActive = true;
                break;
            }
            case INVALID_MOVE:{
                status.setText("Invalid Move!");
                isActive = true;
                break;
            }
            default:{
                isActive = true;
            }
        }
    }

    // reset the game
    public void gameReset() {
       game.resetGame();
       current = 1;
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

        isActive = true;
        status.setText("Player 1 turn");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = findViewById(R.id.status);
        playAgainBtn = findViewById(R.id.play_again_btn);
        playAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameReset();
            }
        });
        game = new Game();
        gameReset();
    }
}