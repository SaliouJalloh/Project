#pragma version (1)
#pragma rs java_package_name (com.example.td1)
#pragma rs_fp_relaxed


uchar4 RS_KERNEL cannedColor (uchar4 in) {
    float4 pixelf = rsUnpackColor8888 ( in ) ;

    return in ;
}
#include "rs_debug.rsh"
#include "colorize.rs"

#define Radius 15

float color_to_keep ;

bool static is_like(float h){
    float min_accept;
    float max_accept;
    max_accept = (int) (color_to_keep + Radius)%360 ;
    min_accept = color_to_keep - Radius;
    if (min_accept < 0)
        min_accept = 360+min_accept;

    return (( 0 < h && h < max_accept) || ( min_accept < h && h < 360 ));
}

uchar4 static toGray ( uchar4 in ) {
    float4 pixelf = rsUnpackColor8888 ( in ) ;
    float gray = (0.30* pixelf . r
                + 0.59* pixelf . g
                + 0.11* pixelf . b ) ;
    return rsPackColorTo8888 ( gray , gray , gray , pixelf . a ) ;
}

uchar4 RS_KERNEL KeepColor ( uchar4 in ) {

    float4 h = rgbTohsv( rsUnpackColor8888(in) );

    if (!is_like(h.s0)){
        uchar4 out = hsvTorgb(h);
        out = toGray(out);
        return out;
    }

    uchar4 out = hsvTorgb(h);
    return out ;
}
