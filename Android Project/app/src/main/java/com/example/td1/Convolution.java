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

    /*
     * TODO:
     *  - add "N" parameter to the method
     *  - let the user choose the size of the filter in a list of predefined sizes (n%2 != 0)
     */
    protected static void convolutionGaus(Bitmap bmp /*, int n*/){
        int h = bmp.getHeight(), w = bmp.getWidth();        //Get the size of the bmp
        int pixels[] = new int[h*w];                        //Create a table to contain the pixels of the bmp
        bmp.getPixels(pixels, 0, w,0,0, w, h);  //Get the pixels of the bmp in the table
        int red = 0, green = 0, blue = 0;                   //Create args to get the pixel's color values
        //size : n/2 + 1
        int halfSize = 13, filterSize = 2*halfSize+1, filterSlots = filterSize*filterSize;   //Size of the filter
        int filter[] = new int[filterSlots];        //Create a table to contain the filter
        int sumFilter = 0;                          //Sum of values in the filter -> values of pixels to divide by to be in [0;255]
        double ecartType = halfSize/Math.sqrt(-2*Math.log(0.05));   //Need to calculate filter's values
        for(int i=0; i<filterSize; i++){            //Calculate the values of the filter
            for(int j=0; j<filterSize; j++){
                int val = (int) ((Math.exp(-((i*i+j*j)/2*ecartType*ecartType)))*10000);
                filter[i+j*filterSize] = (10001-val);
                sumFilter+=val;
            }
        }

        for(int i=halfSize; i<w-halfSize; i++) {                 //For each pixels apply the filter
            for (int j = halfSize; j < h - halfSize; j++) {
                int slot=0;
                for (int k = i - halfSize; k <= i + halfSize; k++) {
                    for (int l = j - halfSize; l <= j + halfSize; l++) {
                        System.out.println("TEEEEEST    red "+Color.green(pixels[k+l*w]));
                        System.out.println("TEEEEEST    filter "+filter[slot]/sumFilter);
                        //red = Color.red(pixels[k+l*w])*filter[slot]/sumFilter;
                        green = Color.green(pixels[k+l*w])*filter[slot]/sumFilter;
                        blue = Color.blue(pixels[k+l*w])*filter[slot]/sumFilter;
                        pixels[k+l*w] = Color.rgb(red,green,blue);
                        slot++;
                    }
                }
            }
        }
        bmp.setPixels(pixels, 0, w, 0, 0, w, h);    //Set the pixels in the bmp
    }


}
