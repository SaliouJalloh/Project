#pragma version (1)
#pragma rs java_package_name (com.example.td1)
#pragma rs_fp_relaxed


uchar4 RS_KERNEL cannedColor (uchar4 in) {
    float4 pixelf = rsUnpackColor8888 ( in ) ;
    //RGBToHSV_new(red,green,blue,hsv);


    return in ;
}

/*
uchar4 RS_KERNEL cannedColor ( uchar4 in) {
    float4 pixelf = rsUnpackColor8888 (in);
    if (pixelf .r > 100 && pixelf .g < 100  && pixelf .b < 100) {
        return in;
    }
    float grey = (0.30* pixelf .r + 0.59* pixelf .g + 0.11* pixelf .b);
    return rsPackColorTo8888 (grey , grey , grey , pixelf .a);
}




static float Hue_2_RGB(float v1, float v2, float vHue) {
    if (vHue < 0) {
        vHue += 1;
    }

    if (vHue > 1) {
        vHue -= 1;
    }

    if ((6 * vHue) < 1) {
        return (v1 + (v2 - v1) * 6 * vHue);
    }

    if ((2 * vHue) < 1) {
        return v2;
    }

    if ((3 * vHue) < 2) {
        return (v1 + (v2 - v1) * ((2.0 / 3) - vHue) * 6);
    }

    return v1;
}

static float3 HslToRgb(float3 hsl) {
	float3 rgb;
    if (hsl.x == 0) {
        // gray values
        rgb.r = hsl.z;
        rgb.g = hsl.z;
        rgb.b = hsl.z;
    } else {
        float v1, v2;
        float hue = hsl.x / 360.0f;

        v2 = (hsl.z < 0.5) ? (hsl.z * (1 + hsl.y)) : ((hsl.z + hsl.y) - (hsl.z * hsl.y));
        v1 = 2 * hsl.z - v2;

        rgb.r = Hue_2_RGB(v1, v2, hue + (1.0f / 3));
        rgb.g = Hue_2_RGB(v1, v2, hue);
        rgb.b = Hue_2_RGB(v1, v2, hue - (1.0f / 3));
    }
    return rgb;
}

static float3 RGBToHSL(float4 rgb) {
	float3 hsl;

    float min = fmin(fmin(rgb.r, rgb.g), rgb.b);
    float max = fmax(fmax(rgb.r, rgb.g), rgb.b);
    float delta = max - min;

    // get luminance value
    hsl.z = (max + min) / 2;

    if (delta == 0) {
        // gray color
        hsl.x = 0;
        hsl.y = 0;
    } else {
        // get saturation value
        hsl.y = (hsl.z < 0.5f) ? (delta / (max + min)) : (delta / (2.0f - max - min));

        // get hue value
        float del_r = (((max - rgb.r) / 6) + (delta / 2)) / delta;
        float del_g = (((max - rgb.g) / 6) + (delta / 2)) / delta;
        float del_b = (((max - rgb.b) / 6) + (delta / 2)) / delta;
        float hue;

        if (rgb.r == max) {
            hue = del_b - del_g;
        } else if (rgb.g == max) {
            hue = (1.0f / 3) + del_r - del_b;
        } else {
            hue = (2.0f / 3) + del_g - del_r;
        }

        // correct hue if needed
        if (hue < 0) {
            hue += 1;
        }

        if (hue > 1) {
            hue -= 1;
        }

        hsl.x = hue * 360;
    }

    return hsl;
}
/*

uchar4 RS_KERNEL onlyRed ( uchar4 in) {
    float4 out = rsUnpackColor8888(in);
    float3 outHsl = RGBToHSL (out);
    // Checking the hue value
    if (outHsl[0] > 10 && outHsl[0] < 345)
        return in;

    float grey = (0.30* out.r + 0.59* out.g + 0.11* out.b);
    return rsPackColorTo8888 (grey , grey , grey , in.a);
}*/