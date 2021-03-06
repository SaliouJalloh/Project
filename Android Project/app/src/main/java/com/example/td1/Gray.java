package com.example.td1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

public class Gray {

    private final Bitmap imageBitmap;
    private final Context context;

    /**
     * @author Saliou Diallo
     * Constructeur de la classe gray
     * @param imageBitmap
     * @param context
     */
    public Gray(Bitmap imageBitmap, Context context) {
        this.imageBitmap = imageBitmap;
        this.context = context;
    }


    /**
     * @author Saliou Diallo
     * Methode qui grise tous les pixels du Bitmap en
     * utilisant les m´ethodes getPixel et setPixel de la classe Bitmap.
     * @param bmp
     */
    public static void toGray(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int red, green, blue, gray;
        int tmp_color;

        for(int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                tmp_color = bmp.getPixel(x,y);
                red = Color.red(tmp_color);
                green = Color.green(tmp_color);
                blue = Color.blue(tmp_color);

                gray =  (int) (0.3*red + 0.59*green + 0.11*blue);
                gray = Color.rgb(gray,gray,gray);

                bmp.setPixel(x,y,gray);
            }
        }
    }

    /**
     * @author Saliou Diallo
     * Methode qui grise tous les pixels du Bitmap en
     * utilisant les methodes getPixels et setPixels de la classe Bitmap.
     * @param bmp
     */
    public static void toGray2(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int red, green, blue, gray;
        int tmp_color;

        int[] pixels = new int[width * height];
        bmp.getPixels(pixels,0,width,0,0,width,height);

        for(int x = 0; x < width*height; x++){
            tmp_color = pixels[x];

            red = Color.red(tmp_color);
            green = Color.green(tmp_color);
            blue = Color.blue(tmp_color);

            gray = (int) (0.3*red + 0.59*green + 0.11*blue);
            pixels[x] = Color.rgb(gray,gray,gray);

        }

        bmp.setPixels(pixels,0,width,0,0,width,height);
    }

    /**
     * @author Saliou Diallo
     * Methode qui grise tous les pixels du Bitmap en
     * utilisant le RenderScript.
     * @param bmp
     */
    public void toGrayRS(Bitmap bmp) {
        // 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript.create(context);
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        // 3) Creer le script
        ScriptC_gray grayScript = new ScriptC_gray(rs);
        // 4) Copier les donnees dans les Allocations
        // ...
        // 5) Initialiser les variables globales potentielles
        // ...
        // 6) Lancer le noyau
        grayScript.forEach_toGray(input, output);
        // 7) Recuperer les donnees des Allocation (s)
        output.copyTo(bmp);
        // 8) Detruire le context , les Allocation (s) et le script
        input.destroy();
        output.destroy();
        grayScript.destroy();
        rs.destroy();
    }
}
