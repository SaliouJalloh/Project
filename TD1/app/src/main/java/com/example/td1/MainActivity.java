package com.example.td1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private TextView text;
    private TextView taille;
    private ImageView img;
    private Bitmap bitmap;
    private Button button, to_gray1, to_gray2;
    private int width;
    private int height;
    private int tmp_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            // Ajout des widgets

            text = findViewById(R.id.idtext);
            taille = findViewById(R.id.idtaille);
            img = findViewById(R.id.idimage);
            button = findViewById(R.id.idbutton);
            to_gray1 = findViewById(R.id.idbuttonGray);
            to_gray2 = findViewById(R.id.idbuttonGrays);

            // Convertion de l'image

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;

            // convertToMutable(img_bp);
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruit,options);

            taille.setText( "Taille : " + bitmap.getWidth() + "*" + bitmap.getHeight());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ActivityIntent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(ActivityIntent);
                }
            });
            to_gray1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    toGray(bitmap);
                    img.setImageBitmap(bitmap);
                }
            });
            to_gray2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toGrays(bitmap);
                    img.setImageBitmap(bitmap);
                }
            });

            width = bitmap.getWidth();
            height = bitmap.getHeight();

            for(int x = 0; x < width; ++x){
                for (int y = height/2; y < height; ++y){
                    bitmap.setPixel(x,y, Color.BLACK);
                }
            }
    }

    public Bitmap toGray(Bitmap bmp){

        width = bmp.getWidth();
        height = bmp.getHeight();
        int R, G, B, R_G_B;

        for(int x = 0; x < width; ++x){
            for (int y = 0; y < height; ++y){
                tmp_color = bmp.getPixel(x,y);
                R = 0; //  0.3R + 0.59G + 0.11B.
                G = 0;
                B = 0;
                R_G_B = (R+G+B)/3;

                bmp.setPixel(x,y,tmp_color);
            }
        }
        return bmp;
    }

    public Bitmap toGrays(Bitmap bmp){

        width = bmp.getWidth();
        height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, 0, 0, 0, width, height);

        for(int x = 0; x < width; ++x){
            for (int y = 0; y < height; ++y){




                bitmap.getPixels(pixels,0,0,0,0,0,0);
            }
        }
        return bmp;
    }

}
