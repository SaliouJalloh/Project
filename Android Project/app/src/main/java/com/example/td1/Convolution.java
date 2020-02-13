package com.example.td1;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Convolution {


    protected void convolution(Bitmap bmp){
        int h = bmp.getHeight();
        int w = bmp.getWidth();
        int pixels[] = new int[h*w];
        int red=0;
        int green=0;
        int blue=0;
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);
        //for(int k=0; k<10; k++) {
        for (int i = 3; i < w - 3; i++) {
            for (int j = 3; j < h - 3; j++) {
                red = (Color.red(pixels[i - 1 + (j - 1) * w]) + Color.red(pixels[i + (j - 1) * w]) + Color.red(pixels[i + 1 + (j - 1) * w]) + Color.red(pixels[i - 1 + j * w]) + Color.red(pixels[i + j * w]) + Color.red(pixels[i + 1 + j * w]) + Color.red(pixels[i - 1 + (j + 1) * w]) + Color.red(pixels[i + (j + 1) * w]) + Color.red(pixels[i + 1 + (j + 1) * w])) / 9;
                green = (Color.green(pixels[i - 1 + (j - 1) * w]) + Color.green(pixels[i + (j - 1) * w]) + Color.green(pixels[i + 1 + (j - 1) * w]) + Color.green(pixels[i - 1 + j * w]) + Color.green(pixels[i + j * w]) + Color.green(pixels[i + 1 + j * w]) + Color.green(pixels[i - 1 + (j + 1) * w]) + Color.green(pixels[i + (j + 1) * w]) + Color.green(pixels[i + 1 + (j + 1) * w])) / 9;
                blue = (Color.blue(pixels[i - 1 + (j - 1) * w]) + Color.blue(pixels[i + (j - 1) * w]) + Color.blue(pixels[i + 1 + (j - 1) * w]) + Color.blue(pixels[i - 1 + j * w]) + Color.blue(pixels[i + j * w]) + Color.blue(pixels[i + 1 + j * w]) + Color.blue(pixels[i - 1 + (j + 1) * w]) + Color.blue(pixels[i + (j + 1) * w]) + Color.blue(pixels[i + 1 + (j + 1) * w])) / 9;
                pixels[i + j * w] = Color.rgb(red, green, blue);
                /*
                red = 0;
                green = 0;
                blue = 0;
                */
            }
        }
        // }
        bmp.setPixels(pixels, 0, w, 0, 0, w, h);

    }

    protected void convolution5(Bitmap bmp){
        int h = bmp.getHeight();
        int w = bmp.getWidth();
        int pixels[] = new int[h*w];
        int red=0;
        int green=0;
        int blue=0;
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);
        for(int i=5; i<w-5; i++) {
            for (int j = 5; j < h - 5; j++) {
                for (int k = i - 2; k <= i + 2; k++) {
                    for (int l = j - 2; l <= j + 2; l++) {
                        red += Color.red((pixels[k + l * w]));
                        green += Color.green((pixels[k + l * w]));
                        blue += Color.blue((pixels[k + l * w]));
                    }
                }
                red/=25; green/=25; blue/=25;
                pixels[i + j * w] = Color.rgb(red, green, blue);
                red=0;
                green=0;
                blue=0;
            }
        }
    }


}
