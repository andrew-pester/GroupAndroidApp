package com.example.rockpaperscissors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button rock = (Button) findViewById(R.id.buttonRock);
        Button paper = (Button) findViewById(R.id.buttonPaper);
        Button scissors = (Button) findViewById(R.id.buttonScissors);

        rock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ResultsActivity.class);
                i.putExtra("UserChoice", "rock");
                startActivity(i);
            }
        });

        paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ResultsActivity.class);
                i.putExtra("UserChoice", "paper");
                startActivity(i);
            }
        });

        scissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ResultsActivity.class);
                i.putExtra("UserChoice", "scissors");
                startActivity(i);
            }
        });
    }
}