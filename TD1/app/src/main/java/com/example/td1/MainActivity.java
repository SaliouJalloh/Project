package com.example.td1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        private TextView text;
        private ImageView img;
        private Bitmap bitmap;
        private Button button;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            // Ajout des widgets

            text = findViewById(R.id.idText);
            img = findViewById(R.id.idImage);
            button = findViewById(R.id.idButton);

            // Convertion de l'image

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img,options);

            text.setText("Width : " + bitmap.getWidth() + "\n");
            text.setText("Height : " + bitmap.getHeight() + "\n");

    }
}
