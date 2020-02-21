package com.example.td1;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Convolution {

    protected static void convolutionMoy(Bitmap bmp ,int n){
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
    protected  static void convolutionGaussN(Bitmap bmp,int n) {
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
}
