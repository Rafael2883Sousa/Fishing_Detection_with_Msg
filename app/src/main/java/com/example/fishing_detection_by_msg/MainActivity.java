package com.example.fishing_detection_by_msg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private Button analyzeButton;
    private TextView resultText;
    private Button shareButton;

    private static final String[] PHISHING_KEYWORDS = {"urgente", "clique aqui", "ganhe agora"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputText);
        analyzeButton = findViewById(R.id.analyzeButton);
        resultText = findViewById(R.id.resultText);
        shareButton = findViewById(R.id.shareButton);

        analyzeButton.setOnClickListener(v -> {
            String text = inputText.getText().toString().toLowerCase();
            boolean isPhishing = false;

            for (String keyword : PHISHING_KEYWORDS) {
                if (text.contains(keyword)) {
                    isPhishing = true;
                    break;
                }
            }

            if (isPhishing) {
                resultText.setText("Este texto pode ser uma tentativa de phishing.");
            } else {
                resultText.setText("Este texto parece seguro.");
            }

            shareButton.setVisibility(View.VISIBLE);
        });

        shareButton.setOnClickListener(v -> {
            String result = resultText.getText().toString();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, result);
            startActivity(Intent.createChooser(shareIntent, "Compartilhar via"));
        });
    }
}