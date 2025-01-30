package com.example.fishing_detection_by_msg;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private Button analyzeButton;
    private TextView resultText;
    private Button shareButton;
    private static final String CHANNEL_ID = "phishing_channel";
    private Button historyButton;
    private ArrayList<String> history;

    private static final String[] PHISHING_KEYWORDS = {"urgente", "clique aqui", "ganhe agora"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        inputText = findViewById(R.id.inputText);
        analyzeButton = findViewById(R.id.analyzeButton);
        resultText = findViewById(R.id.resultText);
        shareButton = findViewById(R.id.shareButton);
        historyButton = findViewById(R.id.historyButton);
        history = new ArrayList<>();

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
                sendNotification("Aviso!", "Este texto pode ser uma tentativa de phishing.");
            } else {
                resultText.setText("Este texto parece seguro.");
            }

            shareButton.setVisibility(View.VISIBLE);
            history.add(resultText.getText().toString());
        });

        shareButton.setOnClickListener(v -> {
            String result = resultText.getText().toString();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, result);
            startActivity(Intent.createChooser(shareIntent, "Compartilhar via"));
        });

        historyButton.setOnClickListener(v -> {
            Intent historyIntent = new Intent(this, HistoryActivity.class);
            historyIntent.putStringArrayListExtra("history", history);
            startActivity(historyIntent);
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Phishing Channel";
            String description = "Channel for phishing alerts";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}