package com.example.rockpaperscissors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Random rand = new Random();
        int computerChoice = rand.nextInt(3);
        TextView computerLabel = (TextView) findViewById(R.id.textViewResults);
        TextView result = (TextView) findViewById(R.id.textViewResults2);
        String userChoice;
        Intent intent = getIntent();
        userChoice = intent.getStringExtra("UserChoice");

        if(computerChoice == 0){
            computerLabel.setText("Computer chose Rock!");
            if(userChoice.equals("rock")){
                result.setText("You tied!");
            } else if (userChoice.equals("paper")){
                result.setText("You win!");
            } else {
                result.setText("You lose!");
            }
        } else if(computerChoice == 1){
            computerLabel.setText("Computer chose Paper!");
            if(userChoice.equals("rock")){
                result.setText("You lose!");
            } else if(userChoice.equals("paper")){
                result.setText("You tied!");
            } else {
                result.setText("You win!");
            }
        } else {
            computerLabel.setText("Computer chose Scissors!");
            if(userChoice.equals("rock")){
                result.setText("You win!");
            } else if(userChoice.equals("paper")){
                result.setText("You lose!");
            } else {
                result.setText("You tied!");
            }
        }

        Button tryAgain = (Button) findViewById(R.id.buttonTryAgain);

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResultsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}