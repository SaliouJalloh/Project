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
                decreasesContrast(bitmap);
                img.setImageBitmap(bitmap);
            }
        });
    }


    public void decreasesContrast(Bitmap bmp) {

    }

    /*--------------------Upcontraste pour une image en couleur------------------------------------*/
    public void upContrasteColor(Bitmap bmp){
        final int w = bmp.getWidth();
        final int h = bmp.getHeight();
        final int[] pixels = new int[w * h];

        int[] histoR = histogramme(bmp, Color.RED);
        int[] histoG = histogramme(bmp,Color.GREEN);
        int[] histoB = histogramme(bmp,Color.BLUE);

        int[] minMaxR = minMax(histoR);
        int[] minMaxG = minMax(histoG);
        int[] minMaxB = minMax(histoB);

        int colorR=0;
        int colorG=0;
        int colorB=0;

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        bmp.getPixels (pixels, 0, w, 0, 0, w, h);

        int pixColor=0;

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
        for (int i = 0 ; i < h*w ; i++){
            pixColor = pixels[i];
            pixR = Color.red(pixColor);
            pixG = Color.green(pixColor);
            pixB = Color.red(pixColor);

            colorR = LUTR[pixR];
            colorG = LUTG[pixG];
            colorB = LUTB[pixB];

            pixels[i] = Color.rgb(colorR, colorG, colorB);
        }
        bmp.setPixels(pixels, 0, w, 0, 0, w, h);
    }


    /*-------------------------------------------------------------*/
    /*--------------------lowcontraste pour une image en couleur------------------------------------*/
    public void lowContrasteColor(Bitmap bmp) {
        final int w = bmp.getWidth();
        final int h = bmp.getHeight();
        final int[] pixels = new int[w * h];

        int[] histoR = histogramme(bmp, Color.RED);
        int[] histoG = histogramme(bmp, Color.GREEN);
        int[] histoB = histogramme(bmp, Color.BLUE);

        int[] minMaxR = minMax(histoR);
        int[] minMaxG = minMax(histoG);
        int[] minMaxB = minMax(histoB);
        /*-----------------------Calcul de la diminution----------------------------------------------*/
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

        int colorR = 0;
        int colorG = 0;
        int colorB = 0;

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        bmp.getPixels(pixels, 0, w, 0, 0, w, h);

        int pixColor = 0;

        // Initialisation des LUT
        int[] LUTR = new int[256];
        int[] LUTG = new int[256];
        int[] LUTB = new int[256];

        for (int ng = 0; ng < 256; ng++) {
            LUTR[ng] = ((ng * (minMaxR[1] - minMaxR[0])) / 255) + minMaxR[0];
            LUTG[ng] = ((ng * (minMaxG[1] - minMaxG[0])) / 255) + minMaxG[0];
            LUTB[ng] = ((ng * (minMaxB[1] - minMaxB[0])) / 255) + minMaxB[0];

            // Calcul de la transformation
            for (int i = 0; i < h * w; i++) {
                pixColor = pixels[i];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.red(pixColor);

                colorR = LUTR[pixR];
                colorG = LUTG[pixG];
                colorB = LUTB[pixB];

                pixels[i] = Color.rgb(colorR, colorG, colorB);
            }
            bmp.setPixels(pixels, 0, w, 0, 0, w, h);
        }
    }
    /*-------------------------------------------------------------*/


    /*-------------------------------FONCTION AUXILIAIRES----------------------------------------------*/

    public int[] histogramme(Bitmap bmp, int c) {
        final int w = bmp.getWidth();
        final int h = bmp.getHeight();
        final int[] pixels = new int[w * h];
        int histo[]  = new int[256];


        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;


        bmp.getPixels (pixels, 0, w, 0, 0, w, h);

        for (int i = 0 ; i < h*w ; i++){

            pixColor = pixels[i];

            if ( c == Color.RED){
                pixR = Color.red(pixColor);
            }
            if ( c == Color.GREEN){
                pixR = Color.green(pixColor);
            }
            if ( c == Color.BLUE){
                pixR = Color.blue(pixColor);
            }
            else{
                pixR = ( Color.red(pixColor) + Color.green(pixColor) + Color.blue(pixColor) ) / 3;

            }
            histo[pixR] ++;
        }
        return histo;
    }

    public int[] minMax(int[] histotab) {
        int tab[]  = new int[2];

        int min=0,max=0;

        for (int i = 0 ; i < 256 ; i++){
            if (histotab[i]!=0){
                min = histotab[i];
                break;
            }
        }
        for (int i = 255 ; i >=0 ; i--){
            if (histotab[i]!=0){
                max = histotab[i];
                break;
            }
        }
        tab[0]=min;
        tab[1]=max;
        return tab;
    }
}
