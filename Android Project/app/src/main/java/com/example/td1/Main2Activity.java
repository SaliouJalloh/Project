package com.example.td1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import static android.graphics.Color.RGBToHSV;


public class Main2Activity extends AppCompatActivity {

    private TextView text,size;
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
                colorize(bitmap);
                Toast.makeText(this,"colorize selected",Toast.LENGTH_LONG).show();
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_canned_color:
                cannedColor(bitmap);
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
        int red, green, blue;

        int random = (int) (Math.random()*(360+1));
        float rand = (float) random;
        bmp.getPixels(pixels,0,width,0,0,width,height);

        for (int x = 0; x < width*height; x++){
            tmp_color = pixels[x];

            red = Color.red(tmp_color);
            green = Color.green(tmp_color);
            blue = Color.blue(tmp_color);

            RGBToHSV(red,green,blue,hsv);

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
        int red, green, blue;

        int random = (int) (Math.random()*(360+1));
        float rand = (float) random;
        bmp.getPixels(pixels,0,width,0,0,width,height);

        for (int x = 0; x < width*height; x++){
            tmp_color = pixels[x];

            red = Color.red(tmp_color);
            green = Color.green(tmp_color);
            blue = Color.blue(tmp_color);

            RGBToHSV_new(red,green,blue,hsv);

            hsv[0] = rand;

            pixels[x] = Color.HSVToColor(hsv);

        }
        bmp.setPixels(pixels,0,width,0,0,width,height);

    }

    /**********************************************************************/

    public void cannedColor(Bitmap bmp) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();

        int red, green, blue, gray;
        int pixels [] = new int[width*height];
        float[] hsv = new float[3];

        bmp.getPixels(pixels,0,width,0,0,width,height);

        for (int x = 0; x < width*height; x++) {

            tmp_color = pixels[x];

            red = Color.red(tmp_color);
            green = Color.blue(tmp_color);
            blue = Color.blue(tmp_color);

            RGBToHSV(red,green,blue,hsv);

            if (hsv[0]>20 && hsv[0]<345){
                gray = (int) ((0.3 * red) + (0.59 * green) + (0.11 * blue));
                pixels[x] = Color.rgb( gray, gray, gray);
            }

        }
        bmp.setPixels(pixels,0,width,0,0,width,height);
    }

    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float)Math.floor(hue)) * 6.0f;
            float f = h - (float)java.lang.Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 1:
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 2:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                    break;
                case 3:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 4:
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 5:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                    break;
            }
        }
        return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
    }

    private void colorizeRS ( Bitmap bmp ) {
        // 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript.create (this) ;
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap (rs,bmp) ;
        Allocation output = Allocation.createTyped (rs,input.getType()) ;
        // 3) Creer le script
        ScriptC_colorize colorizeScript = new ScriptC_colorize(rs) ;
        // 4) Copier les donnees dans les Allocations
        // ...
        // 5) Initialiser les variables globales potentielles
        // ...
        // 6) Lancer le noyau
        colorizeScript.forEach_colorized(input,output) ;
        // 7) Recuperer les donnees des Allocation (s)
        output.copyTo (bmp) ;
        // 8) Detruire le context , les Allocation (s) et le script
        input.destroy();
        output.destroy();
        colorizeScript.destroy();
        rs.destroy();
    }

    private void cannedColorRS ( Bitmap bmp ) {
        // 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript.create (this) ;
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap (rs,bmp) ;
        Allocation output = Allocation.createTyped (rs,input.getType()) ;
        // 3) Creer le script
        ScriptC_cannedColor cannedColorScript = new ScriptC_cannedColor(rs) ;
        // 4) Copier les donnees dans les Allocations
        // ...
        // 5) Initialiser les variables globales potentielles
        // ...
        // 6) Lancer le noyau
        cannedColorScript.forEach_cannedColor(input,output) ;
        // 7) Recuperer les donnees des Allocation (s)
        output.copyTo (bmp) ;
        // 8) Detruire le context , les Allocation (s) et le script
        input.destroy();
        output.destroy();
        cannedColorScript.destroy();
        rs.destroy();
    }


}
