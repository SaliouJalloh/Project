package com.example.td1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main3);

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
        inflater.inflate(R.menu.main3_menu,menu);
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
                Intent ActivityIntent = new Intent(Main3Activity.this, Main4Activity.class);
                startActivity(ActivityIntent);
                Toast.makeText(this,"next menu selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_increasesContrast:
                increasesContrast(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"increases selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_increasesContrastLut:
                Toast.makeText(this,"increases lut selected",Toast.LENGTH_LONG).show();
                increasesContrastLUT(bitmap);
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_decreasesContrastLut:
                decreasesContrastLUT(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"decreases selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_upContrastLut:
                upContrasteColor(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"up contrast selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_downContrastLut:
                downContrasteColor(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"down contrast selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Image en niveaux de gris augmentation/diminution du contraste par extension du dynamique.

    //Fonctions auxiliaires

    public int[] histogram(Bitmap bmp, int c) {
        width = bmp.getWidth();
        height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int histo[]  = new int[256];

        int R ;

        bmp.getPixels (pixels, 0, width, 0, 0, width, height);

        for (int i = 0 ; i < width*height ; i++){

            tmp_color = pixels[i];

            if ( c == Color.RED)
                R = Color.red(tmp_color);

            if ( c == Color.GREEN)
                R = Color.green(tmp_color);

            if ( c == Color.BLUE)
                R = Color.blue(tmp_color);

            else
                R = ( Color.red(tmp_color) + Color.green(tmp_color) + Color.blue(tmp_color) ) / 3;

            histo[R] ++;
        }
        return histo;
    }

    public int[] minMax(int[] histotab) {
        int tab[]  = new int[2];

        int min = 0, max = 0;

        for (int i = 0 ; i < 256 ; i++){
            if (histotab[i] != 0){
                min = histotab[i];
                break;
            }
        }
        for (int i = 255 ; i >=0 ; i--){
            if (histotab[i] != 0){
                max = histotab[i];
                break;
            }
        }
        tab[0] = min;
        tab[1] = max;
        return tab;
    }

    public void increasesContrast(Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int[] histo = histogram(bmp, Color.RED);
        int[] minMax = minMax(histo);
        int color, R;

        bmp.getPixels (pixels, 0, width, 0, 0, width, height);

        for (int i = 0 ; i < width*height ; i++){
            tmp_color = pixels[i];
            R = Color.red(tmp_color);
            color = ( 255 * (R - minMax[0]) ) / (minMax[1] - minMax[0]);
            pixels[i] = Color.rgb(color, color, color);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    //Utilisation d'une Look Up Table pour augmenter le contraste

    public void increasesContrastLUT(Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int[] histo = histogram(bmp, Color.RED);
        int[] minMax = minMax(histo);
        int color, R = 0;

        bmp.getPixels (pixels, 0, width, 0, 0, width, height);

        // Initialisation de la LUT
        int[] LUT = new  int[256];

        for(int ng = 0; ng < 256; ng++){
            LUT[ng] = (255*(ng-minMax[0]))/(minMax[1]-minMax[0]);
        }

        // Calcul de la transformation
        for (int i = 0 ; i < width*height ; i++){
            tmp_color = pixels[i];
            R = Color.red(tmp_color);
            color = LUT[R];
            pixels[i] = Color.rgb(color, color, color);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    //Utilisation d'une Look Up Table pour diminuer le contraste

    public void decreasesContrastLUT(Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int[] histo = histogram(bmp, Color.RED);
        int[] minMax = minMax(histo);
        int dist = minMax[1] - minMax[0];
        int percent = (dist*10)/100;
        minMax[0] = minMax[0] + percent;
        minMax[1] = minMax[1] - percent;
        int color, R;

        bmp.getPixels (pixels, 0, width, 0, 0, width, height);

        // Initialisation de la LUT
        int[] LUT = new  int[256];

        for(int ng = 0; ng < 256; ng++){
            LUT[ng] = ((ng*(minMax[1]-minMax[0]))/255)+minMax[0];
        }

        // Calcul de la transformation
        for (int i = 0 ; i < width*height ; i++){
            tmp_color = pixels[i];
            R = Color.red(tmp_color);
            color = LUT[R];
            pixels[i] = Color.rgb(color, color, color);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
    }

/***************************************************************************************************/
    // augmentation du contraste d'une image en couleur par extension du dynamique.

    public void upContrasteColor(Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();
        int[] pixels = new int[width * height];

        int[] histoR = histogram(bmp, Color.RED);
        int[] histoG = histogram(bmp,Color.GREEN);
        int[] histoB = histogram(bmp,Color.BLUE);

        int[] minMaxR = minMax(histoR);
        int[] minMaxG = minMax(histoG);
        int[] minMaxB = minMax(histoB);

        int newR=0;
        int newG=0;
        int newB=0;

        int R, G, B;

        bmp.getPixels (pixels, 0, width, 0, 0, width, height);

        // Initialisation des LUT
        int[] LUTR = new  int[256];
        int[] LUTG = new  int[256];
        int[] LUTB = new  int[256];

        for(int ng = 0; ng < 256; ng++){
            LUTR[ng] = (255*(ng-minMaxR[0]))/(minMaxR[1]-minMaxR[0]);
            LUTG[ng] = (255*(ng-minMaxG[0]))/(minMaxG[1]-minMaxG[0]);
            LUTB[ng] = (255*(ng-minMaxB[0]))/(minMaxB[1]-minMaxB[0]);
        }

        // Calcul de la transformation
        for (int i = 0 ; i < width*height ; i++){
            tmp_color = pixels[i];
            R = Color.red(tmp_color);
            G = Color.green(tmp_color);
            B = Color.red(tmp_color);

            newR = LUTR[R];
            newG = LUTG[G];
            newB = LUTB[B];

            pixels[i] = Color.rgb(newR, newG, newB);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    public void downContrasteColor(Bitmap bmp) {
        width = bmp.getWidth();
        height = bmp.getHeight();
        int[] pixels = new int[width * height];

        int[] histoR = histogram(bmp, Color.RED);
        int[] histoG = histogram(bmp, Color.GREEN);
        int[] histoB = histogram(bmp, Color.BLUE);

        int[] minMaxR = minMax(histoR);
        int[] minMaxG = minMax(histoG);
        int[] minMaxB = minMax(histoB);

        //Calcul de la diminution
        int distR = minMaxR[1] - minMaxR[0];
        int distG = minMaxG[1] - minMaxG[0];
        int distB = minMaxB[1] - minMaxB[0];
        int percentR = (distR * 10) / 100;
        int percentG = (distG * 10) / 100;
        int percentB = (distB * 10) / 100;
        minMaxR[0] = minMaxR[0] + percentR;
        minMaxG[0] = minMaxG[0] + percentG;
        minMaxB[0] = minMaxB[0] + percentB;
        minMaxR[1] = minMaxR[1] - percentR;
        minMaxG[1] = minMaxG[1] - percentG;
        minMaxB[1] = minMaxB[1] - percentB;

        int R, G, B;

        int newR, newG, newB;

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        // Initialisation des LUT
        int[] LUTR = new int[256];
        int[] LUTG = new int[256];
        int[] LUTB = new int[256];

        for (int ng = 0; ng < 256; ng++) {
            LUTR[ng] = ((ng * (minMaxR[1] - minMaxR[0])) / 255) + minMaxR[0];
            LUTG[ng] = ((ng * (minMaxG[1] - minMaxG[0])) / 255) + minMaxG[0];
            LUTB[ng] = ((ng * (minMaxB[1] - minMaxB[0])) / 255) + minMaxB[0];

            // Calcul de la transformation
            for (int i = 0; i < width*height; i++) {
                tmp_color = pixels[i];
                R = Color.red(tmp_color);
                G = Color.green(tmp_color);
                B = Color.red(tmp_color);

                newR = LUTR[R];
                newG = LUTG[G];
                newB = LUTB[B];

                pixels[i] = Color.rgb(newR,newG,newB);
            }
            bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        }
    }

    /*# pragma version (1)
    # pragma rs java_package_name (com.example.td1)

    float brightnessScale = 0.5;

    private void RS_KERNEL changeBrightness ( Bitmap bmp ) {
        // 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript . create ( this ) ;
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation . createFromBitmap ( rs , bmp ) ;
        Allocation output = Allocation . createTyped ( rs , input.getType () ) ;
        // 3) Creer le script
        ScriptC_brightness bScript = new ScriptC_brightness (rs) ;
        // 4) Copier les donnees dans les Allocations
        // ...
        // 5) Initialiser les variables globales potentielles
        bScript.set_brightnessScale (0.8) ;
        // 6) Lancer le noyau
        bScript.forEach_changeBrightness ( input , output ) ;
        // 7) Recuperer les donnees des Allocation (s)
        output.copyTo ( bmp ) ;
        // 8) Detruire le context , les Allocation (s) et le script
        input.destroy () ; output.destroy () ;
        bScript.destroy () ; rs.destroy () ;
    }
    */
}
