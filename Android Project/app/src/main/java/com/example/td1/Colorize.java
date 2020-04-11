package com.example.td1;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import static android.graphics.Color.RGBToHSV;

public class Colorize {

    //Saliou
    private static int width;
    private static int height;
    private static int tmp_color;

    /**
     * Methode applique une teinte choisie aleatoirement au Bitmap
     * methode colorize sans re-écrire RGBToHSV()/HSVToRGB()
     */

    public static void colorized (Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();

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

    /**
     * Ré-ecrire de le methode RGBToHSV
     * @param red
     * @param green
     * @param blue
     * @param h
     */


    public static void RGBToHSV_new(int red, int green, int blue, float[] h) {
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

    /**
     * Methode applique une teinte choisie aleatoirement au Bitmap
     * methode colorize avec la nouvelle methode RGBToHSV
     * @param bmp
     */

    public static void colorize (Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();

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

    /**
     * Methode qui Conserver la couleur rouge
     * @param img
     */

    public static void cannedColor(Bitmap img) {
        int width = img.getWidth();
        int height = img.getHeight();
        float[] hsv = new float[3];
        int[] pixels = new int[width * height];

        int red, green, blue, gray, color;

        img.getPixels(pixels, 0, width, 0, 0, width, height);

        for(int i = 0; i < width*height; i++){
            color = pixels[i];

            red = Color.red(color);
            green = Color.green(color);
            blue = Color.blue(color);

            RGBToHSV_new(red, green, blue, hsv);

            if((hsv[0] > 20) && (hsv[0] < 340)){
                gray = (int) (red * 0.3 + green * 0.59 +  blue * 0.11);
                pixels[i] = Color.rgb(gray,gray,gray);
            }
        }
        img.setPixels(pixels, 0, width, 0, 0, width, height);

    }

    private static void HSVtoRGB(float h, float s, float v , int [] rgb)
    {
        int i;
        float f, p, q, t;

        if (rgb == null) {
            rgb = new int[3];
        }

        if( s == 0 ) {
            // achromatic (grey)
            rgb[0] = rgb[1] = rgb[3] = (int) v;
            return;
        }
        h /= 60;			// sector 0 to 5
        i = (int) Math.floor( h );
        f = h - i;			// factorial part of h
        p = v * ( 1 - s );
        q = v * ( 1 - s * f );
        t = v * ( 1 - s * ( 1 - f ) );
        switch( i ) {
            case 0:
                rgb[0] = (int) v;
                rgb[1] = (int) t;
                rgb[2] = (int) p;
                break;
            case 1:
                rgb[0] = (int) q;
                rgb[0] = (int) v;
                rgb[0] = (int) p;
                break;
            case 2:
                rgb[0] = (int) p;
                rgb[0] = (int) v;
                rgb[0]= (int) t;
                break;
            case 3:
                rgb[0] = (int) p;
                rgb[0] = (int) q;
                rgb[0] = (int) v;
                break;
            case 4:
                rgb[0] = (int) t;
                rgb[0] = (int) p;
                rgb[0] = (int) v;
                break;
            default:		// case 5:
                rgb[0] = (int) v;
                rgb[0] = (int) p;
                rgb[0] = (int) q;
                break;
        }
    }

    // r,g,b values are from 0 to 1
    // h = [0,360], s = [0,1], v = [0,1]
    // if s == 0, then h = -1 (undefined)
    /*public  static void RGBToHSV_new(int red, int green, int blue, float[] hsv) {
        float H = 0, S = 0, V = 0;
        float r = (float) red / 255;
        float g = (float) green / 255;
        float b = (float) blue / 255;

        float max = Math.max(Math.max(r, g), b);
        float min = Math.min(Math.min(r, g), b);
        float delta = max - min;

        if (hsv == null) {
            hsv = new float[3];
        }
        // Calcul de V
        V = max;
        // Calcul de S
        if (max != 0){
            S = delta / max;        // S
        }
        else {
            // r = g = b = 0		// S = 0, V is undefined
            S = 0;
            H = -1;
            return;
        }
        // Calcul de H
        if( r == max )
		    H = ( g - b ) / delta;		// between yellow & magenta
        else if( g == max )
            H = 2 + ( r - g ) / delta;	// between cyan & yellow
        else
            H = 4 + ( r - g ) / delta;	// between magenta & cyan

        //Calcule de S
        H *= 60.0;				// degrees
        if( H < 0 )  H += 360;

        hsv[0] = H;
        hsv[1] = S;
        hsv[2] = max;

    }
*/

    /**
     * Methode applique une teinte choisie aleatoirement au Bitmap
     * en utilisant le RenderScript.
     * @param bmp
     */
    /*
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

    /**
     * Methode qui Conserver la couleur rouge
     * en utilisant le RenderScript.
     * @param bmp
     */
/*
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
*/
}
