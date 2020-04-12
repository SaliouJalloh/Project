#pragma version(1)
#pragma rs java_package_name(com.example.td1)

#include "rs_debug.rsh"

float new_hue ;

static float4 rgbTohsv (float4 pixelf ) {

    float4 hsv ;
    //hsv.a = pixelf.a ;
    float hh = 0 ;
    float r =  pixelf.r ;
    float g =  pixelf.g ;
    float b =  pixelf.b ;
    float cmax = fmax(fmax(r,g),b);
    float cmin = fmin(fmin(r,g),b);

    float diff = cmax - cmin ;

    // Calcule de H
    if (cmax == 0){
        hsv.s0 = 0 ;
        hsv.s0 = 0 ;
        hsv.s0 = 0 ;
        return hsv;
    }

    else if (cmax == r)
        hh = (g - b) / diff ;
    else if (cmax == g)
        hh = (b - r) / diff + 2 ;
    else if (cmax == b)
        hh = (r - g) / diff + 4 ;

    hh*=60.0 ;

    //Calcule de S
    if (hh < 0) hh+= 360 ;
    if (hh == 360) hh = 0 ;

    float s = diff / cmax ;

    hsv.r = hh ;
    hsv.g = s ;
    hsv.b = cmax ;

    return hsv ;
}

static uchar4 hsvTorgb(float4 hsv) {
    double      hh, p, q, t, ff;
    long        i;
    float4      out;

    //out.a = hsv.a ;
    if(hsv.y <= 0.0) {       // < is bogus, just shuts up warnings
        out.r = hsv.b;
        out.g = hsv.b;
        out.b = hsv.b;
        return rsPackColorTo8888(out);
    }
    hh = hsv.r;
    if(hh >= 360.0) hh = 0.0;
    hh /= 60.0;
    i = (long)hh;
    ff = hh - i;
    p = hsv.b * (1.0 - hsv.g);
    q = hsv.b * (1.0 - (hsv.g * ff));
    t = hsv.b * (1.0 - (hsv.g * (1.0 - ff)));

    switch(i) {
        case 0:
            out.r = hsv.b;
            out.g = t;
            out.b = p;
            break;
        case 1:
            out.r = q;
            out.g = hsv.b;
            out.b = p;
            break;
        case 2:
            out.r = p;
            out.g = hsv.b;
            out.b = t;
            break;

        case 3:
            out.r = p;
            out.g = q;
            out.b = hsv.b;
            break;
        case 4:
            out.r = t;
            out.g = p;
            out.b = hsv.b;
            break;
        case 5:
        default:
            out.r = hsv.b;
            out.g = p;
            out.b = q;
            break;
    }
    out.a = 1;

    return rsPackColorTo8888(out);
}


uchar4 RS_KERNEL Colorize ( uchar4 in ) {

    float4 h = rgbTohsv( rsUnpackColor8888(in) );
    h.s0 = new_hue ;
    uchar4 out = hsvTorgb(h);
    return out ;
}