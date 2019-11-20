package com.example.td1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {

    private TextView text;
    private TextView Size;
    private ImageView img;
    private Bitmap bitmap;
    private Button nextbutton;
    private int width;
    private int height;
    private int tmp_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Ajout des widgets
        text = findViewById(R.id.idtext);
        Size = findViewById(R.id.idtaille2);
        img = findViewById(R.id.idimage);
        nextbutton = findViewById(R.id.button);

        // Convertion de l'image

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        // convertToMutable(img_bp);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruit,options);

        Size.setText( "Taille : " + bitmap.getWidth() + "*" + bitmap.getHeight());

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ActivityIntent = new Intent(Main2Activity.this, Main3Activity.class);
                startActivity(ActivityIntent);
            }
        });
    }

    public void colorize (Bitmap bmp){

    }

  /*  public  void RGBToHSV(Bitmap bitmap){

        width = bitmap.getWidth();
        height = bitmap.getHeight();
        // The 3 basic color values in RGB

        int red, green, bleu;

        // Convert RGB to HSB

        for(int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                tmp_color = bitmap.getPixel(x,y);
                red = Color.red(tmp_color);
                green = Color.green(tmp_color);
                bleu = Color.blue(tmp_color);

                //float[] hsb = Color.RGBToHSV(red, green, bleu, null);
/*
                float hue = hsb[0];

                float saturation = hsb[1];

                float brightness = hsb[2];

            }

        }

    }

    public void HSVToColor(Bitmap bitmap) {

        // Convert HSB to RGB value

        width = bitmap.getWidth();
        height = bitmap.getHeight();
        // The 3 basic color values in RGB

        int R, G, B;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = Color.HSBtoRGB(hue, saturation, brightness);

                R = (rgb >> 16) & 0xFF;

                G = (rgb >> 8) & 0xFF;

                B = rgb & 0xFF;
            }
        }
    }*/

}
