#pragma version (1)
#pragma rs java_package_name ( com . android . rssample )
#pragma rs_fp_relaxed

#include "rs_debug.rsh"

int new_value ;

uchar4 RS_KERNEL Brightness (uchar4 in) {

    in.r = in.r + new_value ;
    in.g = in.g + new_value ;
    in.b = in.b + new_value ;

    if (in.r < 0) in.r = 0;
    else if (in.r > 255) in.r = 255;

    if (in.g < 0) in.g = 0;
    else if (in.g > 255) in.g = 255;

    if (in.b < 0) in.b = 0;
    else if (in.b > 255) in.b = 255;

    return in ;

}