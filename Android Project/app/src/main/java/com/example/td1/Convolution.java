package com.example.td1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import static com.example.td1.Gray.toGray2;

public class Convolution {
    private final Bitmap imageBitmap;
    private final Context context;

    /**
     * @author Saliou Diallo
     * Constructeur de la classe gray
     * @param imageBitmap
     * @param context
     */
    public Convolution(Bitmap imageBitmap, Context context) {
        this.imageBitmap = imageBitmap;
        this.context = context;
    }


    protected  void convolutionMoy(Bitmap bmp ,int n){
        int h = bmp.getHeight(), w = bmp.getWidth();    //Get the size of the bmp
        int pixels[] = new int[h*w];                    //Create a table to contain the pixels of the bmp
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);    //Get the pixels of the bmp in the table
        int red=0, green=0, blue=0;                     //Create args to get the pixel's color values
        int filterSize = n, halfSize = filterSize/2, filterSlots = filterSize*filterSize; //Size of the filter
        for(int i=halfSize; i<w-halfSize; i++) {                 //For each pixels apply the filter
            for (int j = halfSize; j < h - halfSize; j++) {
                for (int k = i - halfSize; k <= i + halfSize; k++) {
                    for (int l = j - halfSize; l <= j + halfSize; l++) {
                        red += Color.red((pixels[k + l * w]));
                        green += Color.green((pixels[k + l * w]));
                        blue += Color.blue((pixels[k + l * w]));
                    }
                }
                red/=filterSlots; green/=filterSlots; blue/=filterSlots;
                pixels[i + j * w] = Color.rgb(red, green, blue);
                red=0;
                green=0;
                blue=0;
            }
        }
        bmp.setPixels(pixels, 0, w, 0, 0, w, h);    //Set the pixels in the bmp
    }


    //n the half size of the filter
    protected   void convolutionGaussN(Bitmap bmp,int n) {
        int height = bmp.getHeight();
        int width = bmp.getWidth();
        int size = height * width;
        int pixel[] = new int[size];
        bmp.getPixels(pixel, 0, width, 0, 0, width, height);

        int size2 = 2 * n + 1;
        int sum = 0;
        int[] matrice = new int[size2 * size2];

        /**********************     Fill the filter     ***************************************/
        for (int i = 0; i <= size2 / 2; i++) {
            for (int j = 0; j <= size2 / 2; j++) {
                sum += i + j + 1;
                matrice[i + j * size2] = i + j + 1;
            }
        }
        for (int i = size2 / 2 + 1; i < size2; i++) {
            for (int j = 0; j <= size2 / 2; j++) {
                sum += matrice[i - 1 + j * size2] - 1;
                matrice[i + j * size2] = matrice[i - 1 + j * size2] - 1;
            }
        }
        for (int i = 0; i < size2 / 2 + 1; i++) {
            for (int j = size2 / 2 + 1; j < size2; j++) {
                sum += matrice[i + (j - 1) * size2] - 1;
                matrice[i + j * size2] = matrice[i + (j - 1) * size2] - 1;
            }
        }
        for (int i = size2 / 2 + 1; i < size2; i++) {
            for (int j = size2 / 2 + 1; j < size2; j++) {
                sum += matrice[i - 1 + j * size2] - 1;
                matrice[i + j * size2] = matrice[i - 1 + j * size2] - 1;
            }
        }
        /**************************************************************************************/

        /**********************     Apply filter     *******************************************/
        for (int i = n; i < width - n; i++) {
            for (int j = n; j < height - n; j++) {
                int tmp = 0;
                int red = 0, green = 0, blue = 0;
                for (int k = i - n; k < i + n; k++) {
                    for (int l = j - n; l < j + n; l++) {
                        red += Color.red(pixel[k + l * width]) * matrice[tmp];
                        green += Color.green(pixel[k + l * width]) * matrice[tmp];
                        blue += Color.blue(pixel[k + l * width]) * matrice[tmp];
                        tmp++;
                    }
                }
                pixel[i + j * width] = Color.rgb(red / sum, green / sum, blue / sum);
            }
        }
        bmp.setPixels(pixel, 0, width, 0, 0, width, height);
    }


    protected  void teeeeest(Bitmap bmp,int n) {
        int height = bmp.getHeight();
        int width = bmp.getWidth();
        int size = height * width;
        int pixel[] = new int[size];
        bmp.getPixels(pixel, 0, width, 0, 0, width, height);

        int size2 = 2 * n + 1;
        int squareSize = size2*size2;
        int sum = 0;
        int[] matrice = new int[squareSize];
        int x=n-1, y=n-1;

        /**********************     Fill the filter     ***************************************/
        for (int i = 0; i <= size2 / 2; i++) {
            for (int j = 0; j <= size2 / 2; j++) {
                int tmp = squareSize/(size2-(i+j));
                sum += tmp;
                matrice[i + j * size2] = tmp;
            }
        }
        for(int i = 1+size2/2; i<size2; i++){
            for(int j = 0; j<=size2/2; j++){
                int tmp = matrice[x+j*size2];
                sum += tmp;
                matrice[i+j*size2] = tmp;
            }
            x-=1;
        }
        for(int i = 0; i<size2; i++){
            y=n-1;
            for(int j=1+size2/2; j<size2; j++){
                int tmp = matrice[i+y*size2];
                sum += tmp;
                matrice[i+j*size2] = tmp;
                y-=1;
            }
        }
        /**************************************************************************************/

        /**********************     Apply filter     *******************************************/
        for (int i = n; i < width - n; i++) {
            for (int j = n; j < height - n; j++) {
                int tmp = 0;
                int red = 0, green = 0, blue = 0;
                for (int k = i - n; k < i + n; k++) {
                    for (int l = j - n; l < j + n; l++) {
                        red += Color.red(pixel[k + l * width]) * matrice[tmp];
                        green += Color.green(pixel[k + l * width]) * matrice[tmp];
                        blue += Color.blue(pixel[k + l * width]) * matrice[tmp];
                        tmp++;
                    }
                }
                pixel[i + j * width] = Color.rgb(red / sum, green / sum, blue / sum);
            }
        }
        bmp.setPixels(pixel, 0, width, 0, 0, width, height);
    }

    public void convolutionSobel(Bitmap bmp){
        toGray2(bmp);
        int height = bmp.getHeight();
        int width = bmp.getWidth();
        int size =height*width;
        int pixel[] = new int[size];
        int pixel2[] = new  int[size];
        bmp.getPixels(pixel,0,width,0,0,width,height);
        int[] matriceV = {-1,0,1,-2,0,2,-1,0,1};
        int[] matriceH = {-1,-2,-1,0,0,0,1,2,1};
        for (int i=1;i<width-1;i++){
            for (int j=1;j<height-1;j++){
                int tmp=0;
                int grayV = 0, grayH = 0;
                for (int k=i-1;k<=i+1;k++){
                    for (int l=j-1;l<=j+1;l++){
                        grayV+=Color.blue(pixel[k+l*width])*matriceV[tmp];
                        grayH+=Color.blue(pixel[k+l*width])*matriceH[tmp];
                        tmp++;
                    }
                }
                grayV = (int) Math.sqrt( (grayV*grayV)+ (grayH*grayH));
                if(grayV>255){
                    grayV=255;
                }
                pixel2[i+j*width] = Color.rgb(grayV,grayV,grayV);
            }
        }
        bmp.setPixels(pixel2,0,width,0,0,width,height);
    }
}
