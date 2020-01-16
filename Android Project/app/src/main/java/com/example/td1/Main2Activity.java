package com.example.td1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.graphics.Color.RGBToHSV;


public class Main2Activity extends AppCompatActivity {

    private TextView text;
    private TextView size;
    private ImageView img;
    private Bitmap bitmap,bitmap1;
    private int width;
    private int height;
    private int tmp_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Ajout des widgets
        text = findViewById(R.id.idtext);
        size = findViewById(R.id.idtaille2);
        img = findViewById(R.id.idimage);

        // Convertion de l'image

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruit,options);
        bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.fruit,options);

        size.setText( "SIZE : " + bitmap.getWidth() + "*" + bitmap.getHeight());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main2_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_reset:
                img.setImageBitmap(bitmap1);
                Toast.makeText(this,"reset menu selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_next:
                Intent ActivityIntent = new Intent(Main2Activity.this, Main3Activity.class);
                startActivity(ActivityIntent);
                Toast.makeText(this,"next menu selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_colorized:
                colorized(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"colorized selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_colorize:
                Toast.makeText(this,"colorize selected",Toast.LENGTH_LONG).show();
                colorize(bitmap);
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_canned_color:
                conserveColor(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"canned color selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
