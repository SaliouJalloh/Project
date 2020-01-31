#pragma version (1)
#pragma rs java_package_name (com.example.td1)
#pragma rs_fp_relaxed

uchar4 RS_KERNEL cannedColor (uchar4 in) {
    float4 pixelf = rsUnpackColor8888 ( in ) ;
    RGBToHSV_new(red,green,blue,hsv);


    return rsPackColorTo8888 (gray,gray,gray,pixelf.a) ;
}