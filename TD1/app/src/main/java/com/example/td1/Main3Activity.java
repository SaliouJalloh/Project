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

public class Main3Activity extends AppCompatActivity {

    private TextView text;
    private TextView Size;
    private ImageView img;
    private Bitmap bitmap;
    private Button nextbutton, button5;

    private int width;
    private int height;
    private int tmp_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Ajout des widgets
        text = findViewById(R.id.idtext);
        Size = findViewById(R.id.idtaille2);
        img = findViewById(R.id.idimage);
        nextbutton = findViewById(R.id.button);
        button5 = findViewById(R.id.idbutton5);

        // Convertion de l'image

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        // convertToMutable(img_bp);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruit,options);

        Size.setText( "Taille : " + bitmap.getWidth() + "*" + bitmap.getHeight());

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ActivityIntent = new Intent(Main3Activity.this, Main4Activity.class);
                startActivity(ActivityIntent);
            }
        });
        button5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                decreases_Contrast(bitmap);
                img.setImageBitmap(bitmap);
            }
        });
    }


    public void decreases_Contrast(Bitmap bmp) {

        width = bitmap.getWidth();
        height = bitmap.getHeight();

        int R, G, B, color, max = 0, min = 0;
        int hist[] = new int[256];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tmp_color = bmp.getPixel(x, y);

                R = Color.red(tmp_color);
                G = Color.green(tmp_color);
                B = Color.blue(tmp_color);

                hist[R] = hist[R] + 1;
                color = (R + G + B) / 3;

                bmp.setPixel(x, y, Color.rgb(color, color, color));


                if (color > max) {
                    max = color;
                }
                if (color < min) {
                    min = color;
                }
            }
        }

        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                tmp_color = bmp.getPixel(x,y);
                color = Color.red(tmp_color);

                int tmp_color2 = ((color*(max-min))/255) + min;

                bmp.setPixel(x,y,Color.rgb(tmp_color2,tmp_color2,tmp_color2));
            }
        }
    }

}
