package com.example.td1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.android.rssample.ScriptC_minmax;
import com.android.rssample.ScriptC_contrastRGB;
import com.android.rssample.ScriptC_minmaxRGB;

import androidx.renderscript.Allocation;
import androidx.renderscript.Int2;
import androidx.renderscript.RenderScript;

import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;
import static android.graphics.Color.rgb;

public class Contrast {

    private final Bitmap imageBitmap;
    private final Context context;

    public Contrast(Bitmap imageBitmap, Context context) {
        this.imageBitmap = imageBitmap;
        this.context = context;
    }

    // Image en niveaux de gris augmentation/diminution du contraste par extension du dynamique.

    //Fonctions auxiliaires


    /**
     * TESTER SI L'IMAGE EST DE NIVEAU GRIS
     * @param img
     * @return
     */
    public boolean isGris(Bitmap img){
        int width=img.getWidth();
        int height=img.getHeight();
        int[] pixels = new int[width*height];
        img.getPixels (pixels, 0, width, 0, 0, width, height);
        for(int j=0;j<width*height;j++){
            double newRed = red(pixels[j]);
            double newGreen = green(pixels[j]);
            double newBlue = blue(pixels[j]);
            if(newBlue!=newGreen || newBlue!=newRed || newGreen!=newRed)
                return false;
        }
        return true;
    }


    /**
     * TRANSFORMATIONS D'HISTORGRAMME POUR REGLER LE CONTRASTE
     * @param im
     * @return
     */
    public int[] redScale(Bitmap im) {
        int[] hist = new int[256];

        for (int i = 0; i < im.getWidth() ; i++) {
            for (int j = 0; j < im.getHeight(); j++) {
                int redLevel = red(im.getPixel(i, j));
                hist[redLevel]++;
            }
        }
        return hist;
    }

    /**
     * calcule l'histoggrame du niveau de bleu d'une image
     * @param im
     * @return
     */
    public int[] blueScale(Bitmap im) {
        int[] hist = new int[256];
        for (int i = 0; i < im.getWidth() ; i++) {
            for (int j = 0; j < im.getHeight() ; j++) {
                int blueLevel = blue(im.getPixel(i, j));
                hist[blueLevel]++;
            }
        }
        return hist;
    }

    /**
     * calcule l'histogramme du niveau de vert d'une image
     * @param im
     * @return
     */
    public  int[] greenScale(Bitmap im) {
        int[] hist = new int[256];
        for (int i = 0; i < im.getWidth() ; i++) {
            for (int j = 0; j < im.getHeight(); j++) {
                int greenLevel = green(im.getPixel(i, j));
                hist[greenLevel]++;
            }
        }
        return hist;
    }

    /**
     * diminution du contraste d'une image couleur par égalisation d'histogramme
     * @param im
     */

    public  void downContrasteColor(Bitmap im) {

        int[] histR = redScale(im);
        int[] histG = greenScale(im);
        int[] histB = blueScale(im);
        int width = im.getWidth();
        int height = im.getHeight();
        int i;
        int[] histCR = new int[256];
        int[] histCG = new int[256];
        int[] histCB = new int[256];
        int cmptR = 0;
        int cmptG = 0;
        int cmptB = 0;
        for (i = 0; i < 256; i++) {
            cmptR = cmptR + histR[i];
            histCR[i] = cmptR;
            cmptG = cmptG + histG[i];
            histCG[i] = cmptG;
            cmptB = cmptB + histB[i];
            histCB[i] = cmptB;
        }
        int j;
        int[] pixels = new int[width * height];
        im.getPixels(pixels, 0, width, 0, 0, width, height);
        if(cmptB!=0&&cmptG!=0&&cmptR!=0)
            for (j = 0; j < width * height; j++) {
                double newRed = ((histCR[red(pixels[j])]));
                double newGreen = ((histCG[green(pixels[j])]));
                double newBlue = ((histCB[blue(pixels[j])]));
                pixels[j] = rgb((int) ((newRed * 255) / cmptR), (int) ((newGreen * 255) / cmptG), (int) ((newBlue * 255)) / cmptB);
            }
        im.setPixels(pixels, 0, width, 0, 0, width, height);

    }

    /**
     * /augmentation du contraste d'une image couleur par extension dynamique ********--BUTTON
     *
     * @param im
     */
    public   void UpContrasteColor(Bitmap im){
        int[] hist1 = redScale(im);
        int[] hist2 = blueScale(im);
        int[] hist3 = greenScale(im);
        int width = im.getWidth();
        int height = im.getHeight();
        int maxr = maxArray(hist1);
        int minr = minArray(hist1);
        int maxg = maxArray(hist2);
        int ming = minArray(hist2);
        int maxb = maxArray(hist3);
        int minb = minArray(hist3);
        int[] LUTr = new int[256];
        int[] LUTg = new int[256];
        int[] LUTb = new int[256];
        for (int n = 0; n < 256; n++) {
            LUTr[n] = (255 * (n - minr)) / (maxr - minr);
            LUTg[n] = (255 * (n - ming)) / (maxg - ming);
            LUTb[n] = (255 * (n - minb)) / (maxb - minb);
        }
        int[] pixels = new int[width * height];
        im.getPixels(pixels, 0, width, 0, 0, width, height);
        int i;
        for (i = 0; i < pixels.length; i++) {
            pixels[i] = rgb(LUTr[red(pixels[i])], LUTg[green(pixels[i])], LUTb[blue(pixels[i])]);
        }
        im.setPixels(pixels, 0, width, 0, 0, width, height);

    }

    /**
     * calcule l'histogramme du niveau de gris d'une image
     * @param im
     * @return
     */
    public  int[] greyScale(Bitmap im) {
        int width = im.getWidth();
        int height = im.getHeight();
        int[] pixels = new int[width * height];
        im.getPixels(pixels,0,width,0,0,width,height);
        int[] hist = new int[256];
        for(int x = 0; x < width*height; x++){

            int greyLevel = red(pixels[x]);
            hist[greyLevel]++;
        }
        return hist;
    }

    public  int minArray(int[] array) {
        int i = 0;
        while (array[i] == 0) {
            i++;
        }
        return i;
    }

    public  int maxArray(int[] array) {
        int i = (array.length) - 1;
        while (array[i] == 0) {
            i--;
        }
        return i;
    }
    /**
     *augmentation du contraste d'une image grise par égalité d'histogramme
     */

    public  void  increasesContrast(Bitmap im) {

        int[] hist = greyScale(im);
        int width = im.getWidth();
        int height = im.getHeight();
        int i;
        int[] histC = new int[256];
        int cmpt=0;
        for (i = 1; i < 256; i++) {
            cmpt = cmpt + hist[i];
            histC[i] = cmpt;
        }
        int[] pixels = new int[width * height];
        im.getPixels(pixels, 0, width, 0, 0, width, height);
        int j;
        for (j = 0; j < pixels.length; j++) {
            int newGrey =(histC[red(pixels[j])]*255)/cmpt;
            pixels[j] = rgb(newGrey, newGrey, newGrey);
        }
        im.setPixels(pixels, 0, width, 0, 0, width, height);

    }

    /**
     * augmentation du contraste d'une image couleur par extension dynamique
     * @param im
     * si l'image de depart est gris max-min=0 .!isGris(im) pour eviter une division de zero
     */
    public void increasesContrastLUT(Bitmap im) {
        if(!isGris(im)) {
            int[] hist1 = redScale(im);
            int[] hist2 = blueScale(im);
            int[] hist3 = greenScale(im);
            int width = im.getWidth();
            int height = im.getHeight();
            int maxr = maxArray(hist1);
            int minr = minArray(hist1);
            int maxg = maxArray(hist2);
            int ming = minArray(hist2);
            int maxb = maxArray(hist3);
            int minb = minArray(hist3);
            int[] LUTr = new int[256];
            int[] LUTg = new int[256];
            int[] LUTb = new int[256];
            for (int n = 0; n < 256; n++) {
                LUTr[n] = (255 * (n - minr)) / (maxr - minr);
                LUTg[n] = (255 * (n - ming)) / (maxg - ming);
                LUTb[n] = (255 * (n - minb)) / (maxb - minb);
            }
            int[] pixels = new int[width * height];
            im.getPixels(pixels, 0, width, 0, 0, width, height);
            int i;
            for (i = 0; i < pixels.length; i++) {
                pixels[i] = rgb(LUTr[red(pixels[i])], LUTg[green(pixels[i])], LUTb[blue(pixels[i])]);
            }
            im.setPixels(pixels, 0, width, 0, 0, width, height);
        }
    }


    /**
     * diminution du contraste d'une image grise
     * @param im
     */
    public   void decreasesContrastLUT(Bitmap im) {
        int[] hist = greyScale(im);
        int width = im.getWidth();
        int height = im.getHeight();
        int[] LUT = new int[256];
        int[] newLUT = new int[256];
        int size=0;
        for(int j=0;j<256;j++){
            if(hist[j]>50){
                LUT[size]=hist[j];
                size++;
            }
        }
        for(int z=0;z<size;z++){
            newLUT[z]=LUT[z];
        }
        int[] pixels = new int[width * height];
        im.getPixels(pixels, 0, width, 0, 0, width, height);
        int i;
        for (i = 0; i < pixels.length; i++) {
            int grey=red(pixels[i]);
            int newGrey=newLUT[grey];
            pixels[i]=rgb(newGrey,newGrey,newGrey);

        }
        im.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    /**
     *Calcul de l'histogramme cumulé
     * @param img
     * @return histogram
     */
    public  int[] histogram_acumulate(Bitmap img){

        int[] histogram = greyScale(img);

        for(int i=1; i<256; i++){

            histogram[i] = histogram[i-1] + histogram[i];

        }

        return histogram;

    }

    /**
     * Egalisation d'histogramme et amélioration du contraste
     * @param img
     * @return
     */
    public  Bitmap histogram_equalize(Bitmap img){

        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = new int[width * height];
        img.getPixels(pixels,0,width,0,0,width,height);

        Bitmap new_image = Bitmap.createBitmap(width, height, img.getConfig());

        int[] histogram_acumulate = histogram_acumulate(img);

        int MN = img.getHeight() * img.getWidth();
        int G1 = 256-1;
        double fator = (double)G1/MN;

        int[] T = new int[256];


        for(int i=1; i<256; i++){

            T[i] = (int)(fator*histogram_acumulate[i]);

        }
        for(int x = 0; x < width*height; x++){
            int pixel = pixels[x];
            int red = Color.red(pixel);
            pixel = Color.rgb(T[red], T[red], T[red]);
            pixels[x] = Color.rgb(pixel,pixel,pixel);

        }

        new_image.setPixels(pixels,0,width,0,0,width,height);

        return new_image;
    }
    /**
     * Function that increases the contrast of a color image (RGB)
     * (Using RenderScript)
     * @param imageBitmap a Bitmap image
     */
    public void contrastPlusRGB_RS(Bitmap imageBitmap){
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs,imageBitmap);
        Allocation output = Allocation.createTyped(rs,input.getType());

        ScriptC_minmaxRGB FindMinMaxRGBScript = new ScriptC_minmaxRGB(rs);

        Int2[] MinMax = FindMinMaxRGBScript.reduce_findMinAndMaxRGB(input).get();
        FindMinMaxRGBScript.destroy();

        ScriptC_contrastRGB LutRGBScript = new ScriptC_contrastRGB(rs);

        LutRGBScript.set_MinAndMaxR(MinMax[0]);
        LutRGBScript.set_MinAndMaxG(MinMax[1]);
        LutRGBScript.set_MinAndMaxB(MinMax[2]);

        LutRGBScript.invoke_ContrastPlusRGB(input,output);

        LutRGBScript.destroy();

        output.copyTo(imageBitmap);

        input.destroy();
        output.destroy();
        rs.destroy();
    }

}
