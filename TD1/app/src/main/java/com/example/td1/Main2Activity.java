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

import static android.os.Build.VERSION_CODES.O;


public class Main2Activity extends AppCompatActivity {

    private TextView text;
    private TextView Size;
    private ImageView img;
    private Bitmap bitmap;
    private Button nextbutton, colorized, colorized2,conserve1, conserve2;
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
        colorized = findViewById(R.id.idcolorize);
        colorized2 = findViewById(R.id.idcolorize2);
        conserve1 = findViewById(R.id.idCanned1);
        conserve2 = findViewById(R.id.idCanned2);

        // Convertion de l'image

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        // convertToMutable(img_bp);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruit,options);

        Size.setText( "Taille : " + bitmap.getWidth() + "*" + bitmap.getHeight());

        //Branchement des bouttons
        nextbutton.setOnClickListener(new View.OnClickListener() {
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
        colorized2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                colorize_Without_Red(bitmap);
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
        conserve2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                conserveColor(bitmap,Color.YELLOW);
                img.setImageBitmap(bitmap);
            }
        });
    }

    //Ecriture des differentes fonctions

    public void colorize (Bitmap bmp){
        width = bitmap.getWidth();
        height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        float [] hsv;
        hsv = new float[3];
        int R, G, B, color;

        int r = (int) (Math.random()* (360-1) );

        bmp.getPixels(pixels,0,width,0,0,width,height);


        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                color = pixels[x*width+y];

                R = Color.red(color);
                G = Color.green(color);
                B = Color.blue(color);

                hsv = RGBToHSV(R,G,B);
                hsv[0] = r;

                pixels[x*width+y] = Color.HSVToColor(hsv);

            }
        }
        bmp.setPixels(pixels,0,width,0,0,width,height);

    }

    public void colorize_Without_Red(Bitmap bmp){
        int width = bmp.getWidth();
        int height=bmp.getHeight();
        int a,r,g,b;
        int color=0;
        float[] hsv = new float[3];
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                color=bmp.getPixel(i,j);
                a = Color.alpha(color);
                r = Color.red(color);
                g = Color.green(color);
                b = Color.blue(color);
                int grv=(r+g+b)/3;

                Color.RGBToHSV(r, g, b, hsv);
                int aleatoir=(int)Math.random()*(350);
                color = Color.HSVToColor(grv, hsv);
                if((hsv[0] > 20) && (hsv[0] < 340)){
                    bmp.setPixel(i , j , Color.rgb(grv,grv,grv));
                }
            }
        }
    }

    public  static float[] RGBToHSV(int red, int green, int bleu){
        float h, s, v;
        float min, max, delta;

        float r = (float) red /255;
        float g = (float) green/255;
        float b = (float) bleu/255;

        min = Math.min(Math.min(r,g), b);
        max = Math.max(Math.max(r,g), b);

        // composante V
        v = max;
        delta = max - min;

        //Composante S
        if(max != 0) {
            s = delta / max;
        }
        else{
            s = 0;
            h = 0;
            v = 0;
            return new float[]{h,s,v};
        }

        //Composante H
        if( r == max){
            h = ((g-b) / delta);
        }
        else if (g == max){
            h = 2 + (b - r) / delta;
        }
        else {
            h = 4 + (r-g) / delta;
        }
        h = h * 60;

        if (h < 0){
            h = h + 360;
        }
        return new float[]{h,s,v};

    }

    public void conserveColor(Bitmap bmp) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();

        int R, G, B, max, color;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                tmp_color = bmp.getPixel(x, y);

                R = Color.red(tmp_color);
                G = Color.green(tmp_color);
                B = Color.blue(tmp_color);

                max = Math.max(Math.max(R, G), B);
                if (R != max) {

                    color = (R + G + B)/3;
                    color = Color.rgb(color,color,color);
                }
                else{
                    color = Color.rgb(R, G, B);
                }
                bmp.setPixel(x,y,color);
            }
        }
    }

    public void conserveColor(Bitmap bmp, int color) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();

        int R, G, B, tmp_color;
        int pixels [] = new int[width*height];
        float[] hsv;
        hsv = new float[3];

        int min = color - 15 + 360;
        int max = (color + 15 + 360) % 360;
        bmp.getPixels(pixels,0,width,0,0,width,height);

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                tmp_color = pixels[width*x+y];

                R = Color.red(tmp_color);
                G = Color.blue(tmp_color);
                B = Color.blue(tmp_color);

                hsv = RGBToHSV(R,G,B);

                if((hsv[0] < min) && (hsv[0] > max)){
                    hsv[1] = 0;
                }
                pixels[x*width+y] = Color.HSVToColor(hsv);
            }
        }
        bmp.setPixels(pixels,0,width,0,0,width,height);
    }


}
