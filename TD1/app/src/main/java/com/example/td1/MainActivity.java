package com.example.td1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private TextView text;
    private TextView taille;
    private ImageView img;
    private Bitmap bitmap;
    private Button button;
    private int width;
    private  int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            // Ajout des widgets

            text = findViewById(R.id.idtext);
            taille = findViewById(R.id.idtaille);
            img = findViewById(R.id.idimage);
            button = findViewById(R.id.idbutton);

            // Convertion de l'image

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruit,options);

            taille.setText( bitmap.getWidth() + "*" + bitmap.getHeight());

            width = bitmap.getWidth();
            height = bitmap.getHeight();

            for(int x = 0; x < width; ++x){
                for (int y = 0; y < height; ++y){

                }
            }
    }

    public void toGray(Bitmap bmp){

        for(int x = 0; x < width; ++x){
            for (int y = 0; y < height; ++y){

            }
        }
    }

    public void toGrays(Bitmap bmp){

        for(int x = 0; x < width; ++x){
            for (int y = 0; y < height; ++y){

            }
        }
    }
}
