package com.example.td1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import static android.graphics.Color.HSVToColor;
import static android.graphics.Color.RGBToHSV;

public class Colorize {

    private final Bitmap imageBitmap;
    private final Context context;
    private static int width;
    private static int height;
    private static int tmp_color;

    public Colorize(Bitmap imageBitmap, Context context) {
        this.imageBitmap = imageBitmap;
        this.context = context;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public Context getContext() {
        return context;
    }

    /**
     * Methode applique une teinte choisie aleatoirement au Bitmap
     * methode colorize sans re-écrire RGBToHSV()/HSVToRGB()
     */

    public void colorized (Bitmap bmp){
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

            pixels[x] = HSVToColor(hsv);

        }
        bmp.setPixels(pixels,0,width,0,0,width,height);

    }

    /**
     * Function that converts from RGB to HSV
     *
     * @param redInput   the red value
     * @param greenInput the green value
     * @param blueInput  the blue value
     * @param hsv        a floating array of 3 empty boxes
     */
    public static void RGBToHSV_new(int redInput, int greenInput, int blueInput, float[] hsv) {
        float cMax, cMin, delta, saturation, value, hue = 0;
        float red = (float) (redInput / 255.0);
        float green = (float) (greenInput / 255.0);
        float blue = (float) (blueInput / 255.0);
        cMin = Math.min(Math.min(red, green), blue);
        cMax = Math.max(Math.max(red, green), blue);
        delta = cMax - cMin;
        if (cMax == 0) {
            for (int i = 0; i < 3; i++) {
                hsv[i] = 0;
            }
            return;
        } else if (cMax == red)
            hue = (float) (60.0 * (((green - blue) / delta)));
        else if (cMax == green)
            hue = (float) (60.0 * (((blue - red) / delta) + 2));
        else if (cMax == blue)
            hue = (float) (60.0 * (((red - green) / delta) + 4));
        hsv[0] = hue;
        saturation = delta / cMax;
        value = (cMax);
        hsv[1] = saturation;
        hsv[2] = value;
    }

    /**
     * Function that converts from HSV to RGB
     *
     * @param hsvColor An array of float that contains the three HSV values of the color
     * @return Color in RGB
     */
    public static int HSVToColor_new(float[] hsvColor) {
        float r, g, b, k;
        k = (float) (5 + (hsvColor[0] / 60.0)) % 6;
        r = (int) ((hsvColor[2] - hsvColor[2] * hsvColor[1] * Math.max(Math.min(Math.min(k, 4 - k), 1), 0)) * 255.0);
        k = (float) (3 + (hsvColor[0] / 60.0)) % 6;
        g = (int) ((hsvColor[2] - hsvColor[2] * hsvColor[1] * Math.max(Math.min(Math.min(k, 4 - k), 1), 0)) * 255.0);
        k = (float) (1 + (hsvColor[0] / 60.0)) % 6;
        b = (int) ((hsvColor[2] - hsvColor[2] * hsvColor[1] * Math.max(Math.min(Math.min(k, 4 - k), 1), 0)) * 255.0);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    /**
     * Methode applique une teinte choisie aleatoirement au Bitmap
     * methode colorize avec la nouvelle methode RGBToHSV
     * @param bmp
     */

    public void colorize (Bitmap bmp, float new_hue){
        width = bmp.getWidth();
        height = bmp.getHeight();

        int[] pixels = new int[width*height];
        float [] hsv = new float[3];
        int red, green, blue;

        bmp.getPixels(pixels,0,width,0,0,width,height);

        for (int x = 0; x < width*height-1; x++){
            tmp_color = pixels[x];

            red = Color.red(tmp_color);
            green = Color.green(tmp_color);
            blue = Color.blue(tmp_color);

            RGBToHSV_new(red,green,blue,hsv);

            hsv[0] = new_hue;

            pixels[x] = HSVToColor_new(hsv);

        }
        bmp.setPixels(pixels,0,width,0,0,width,height);

    }


    /**
     * Methode qui Conserver la couleur rouge
     * @param img
     */

    public void cannedColor(Bitmap img) {
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


    /**
     * Methode applique une teinte choisie aleatoirement au Bitmap
     * en utilisant le RenderScript.
     * @param bmp
     */

    public void colorizeRS ( Bitmap bmp, float newHue ) {
        // 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript.create (context) ;
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap (rs,bmp) ;
        Allocation output = Allocation.createTyped (rs,input.getType()) ;
        // 3) Creer le script
        ScriptC_colorize colorizeScript = new ScriptC_colorize(rs) ;
        // 4) Copier les donnees dans les Allocations
        // ...
        // 5) Initialiser les variables globales potentielles
        colorizeScript.set_new_hue(newHue);
        // 6) Lancer le noyau
        colorizeScript.forEach_Colorize(input,output) ;
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

    public void cannedColorRS ( Bitmap bmp, float colorToKeep ) {
        // 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript.create (context) ;
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap (rs,bmp) ;
        Allocation output = Allocation.createTyped (rs,input.getType()) ;
        // 3) Creer le script
        ScriptC_cannedColor cannedColorScript = new ScriptC_cannedColor(rs) ;
        // 4) Copier les donnees dans les Allocations

        // 5) Initialiser les variables globales potentielles
        cannedColorScript.set_color_to_keep(colorToKeep);
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
