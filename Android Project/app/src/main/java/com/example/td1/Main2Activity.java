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

import static android.graphics.Color.RGBToHSV;


public class Main2Activity extends AppCompatActivity {

    private TextView text;
    private TextView Size;
    private ImageView img;
    private Bitmap bitmap;
    private Button nextbutton2, colorized, colorized2,conserve1;
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
        nextbutton2 = findViewById(R.id.button);
        colorized = findViewById(R.id.idcolorize);
        colorized2 = findViewById(R.id.idcolorize2);
        conserve1 = findViewById(R.id.idCanned1);

        // Convertion de l'image

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruit,options);

        Size.setText( "SIZE : " + bitmap.getWidth() + "*" + bitmap.getHeight());

        //Branchement des bouttons
        nextbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ActivityIntent = new Intent(Main2Activity.this, Main3Activity.class);
                startActivity(ActivityIntent);
            }
        });

        colorized.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                colorize(bitmap);
                img.setImageBitmap(bitmap);
            }
        });

        colorized.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                colorized(bitmap);
                img.setImageBitmap(bitmap);
            }
        });

        conserve1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                conserveColor(bitmap);
                img.setImageBitmap(bitmap);
            }
        });
    }

    /*****************************************************/
    // methode colorize sans ré-ecrire RGBToHSV() .

    public void colorized (Bitmap bmp){
        width = bitmap.getWidth();
        height = bitmap.getHeight();

        int[] pixels = new int[width*height];
        float [] hsv = new float[3];
        int R, G, B;

        int random = (int) (Math.random()*(360+1));
        float rand = (float) random;
        bmp.getPixels(pixels,0,width,0,0,width,height);

        for (int x = 0; x < width*height; x++){
            tmp_color = pixels[x];

            R = Color.red(tmp_color);
            G = Color.green(tmp_color);
            B = Color.blue(tmp_color);

            RGBToHSV(R,G,B,hsv);

            hsv[0] = rand;

            pixels[x] = Color.HSVToColor(hsv);

        }
        bmp.setPixels(pixels,0,width,0,0,width,height);

    }

    /*********************************************************************************/

    //Ré-ecrire de le methode RGBToHSV

    public void RGBToHSV_new(int red, int green, int blue, float[] h) {
        float hh = 0;
        float r = (float) red / 255;
        float g = (float) green / 255;
        float b = (float) blue / 255;
        float cmax = Math.max(Math.max(r, g), b);
        float cmin = Math.min(Math.min(r, g), b);
        float diff = cmax - cmin;

        // Calcule de H
        if (cmax == 0) {
            h[0] = 0;
            h[1] = 0;
            h[2] = 0;
            return;
        } else if (cmax == r)
            hh = (g - b) / diff;
        else if (cmax == g)
            hh = (r - g) / diff + 2;
        else if (cmax == b)
            hh = 4 + (r - g) / diff;

        hh *= 60.0;

        //Calcule de S
        if (hh < 0)
            hh += 360;

        float s = diff / cmax;

        h[0] = hh;
        h[1] = s;
        h[2] = cmax;

    }

    /*********************************************************************************/

    //Ré-ecrire de le methode colorize avec la nouvelle methode RGBToHSV

    public void colorize (Bitmap bmp){
        width = bitmap.getWidth();
        height = bitmap.getHeight();

        int[] pixels = new int[width*height];
        float [] hsv = new float[3];
        int R, G, B;

        int random = (int) (Math.random()*(360+1));
        float rand = (float) random;
        bmp.getPixels(pixels,0,width,0,0,width,height);

        for (int x = 0; x < width*height; x++){
            tmp_color = pixels[x];

            R = Color.red(tmp_color);
            G = Color.green(tmp_color);
            B = Color.blue(tmp_color);

            RGBToHSV_new(R,G,B,hsv);

            hsv[0] = rand;

            pixels[x] = Color.HSVToColor(hsv);

        }
        bmp.setPixels(pixels,0,width,0,0,width,height);

    }

    /**********************************************************************/

    public void conserveColor(Bitmap bmp) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();

        int R, G, B, gray;
        int pixels [] = new int[width*height];
        float[] hsv = new float[3];

        bmp.getPixels(pixels,0,width,0,0,width,height);

        for (int x = 0; x < width*height; ++x) {

            tmp_color = pixels[x];

            R = Color.red(tmp_color);
            G = Color.blue(tmp_color);
            B = Color.blue(tmp_color);

            RGBToHSV(R,G,B,hsv);

            if (hsv[0]>20 && hsv[0]<345){
                gray = (int) ((0.3 * R) + (0.59 * G) + (0.11 * B));
                pixels[x] = Color.rgb( gray, gray, gray);
            }
            pixels[x] = Color.HSVToColor(hsv);

        }
        bmp.setPixels(pixels,0,width,0,0,width,height);
    }


}
