package com.example.td1;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

public class Gray extends MainActivity{

    public static int width, height, tmp_color;

    public static void toGray(Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();
        int red, green, blue, gray;

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

    public static void toGray2(Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();
        int red, green, blue, gray;

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

   public void toGrayRS(Bitmap bmp) {
       // 1) Creer un contexte RenderScript
       RenderScript rs = RenderScript.create(this);
       // 2) Creer des Allocations pour passer les donnees
       Allocation input = Allocation.createFromBitmap(rs, bmp);
       Allocation output = Allocation.createTyped(rs, input.getType());
       // 3) Creer le script
       ScriptC_grays grayScript = new ScriptC_grays(rs);
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
