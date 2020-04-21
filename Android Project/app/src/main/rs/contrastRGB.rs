#pragma version (1)
#pragma rs java_package_name ( com . android . rssample )
#pragma rs_fp_relaxed
# include "rs_debug.rsh"

#define ARRAY_SIZE 256

int2 MinAndMaxR ;
int2 MinAndMaxG ;
int2 MinAndMaxB ;

uchar LutTableR[ARRAY_SIZE] ;
uchar LutTableG[ARRAY_SIZE] ;
uchar LutTableB[ARRAY_SIZE] ;

void RS_KERNEL FillLutTable_RGB(uchar in, uint32_t x){
    LutTableR[x] = ((255 * (x - MinAndMaxR.x)) / (MinAndMaxR.y - MinAndMaxR.x));
    LutTableG[x] = ((255 * (x - MinAndMaxG.x)) / (MinAndMaxG.y - MinAndMaxG.x));
    LutTableB[x] = ((255 * (x - MinAndMaxB.x)) / (MinAndMaxB.y - MinAndMaxB.x));
}

uchar4 RS_KERNEL ApplyChanges_RGB(uchar4 in){
    uchar4 out ;
    out.r = LutTableR[in.r];
    out.g = LutTableG[in.g];
    out.b = LutTableB[in.b];
    out.a = in.a;
    return out ;
}

void ContrastPlusRGB(rs_allocation in, rs_allocation out){
    rs_allocation tmp_Allocation = rsCreateAllocation_uchar(256);
    rsForEach(FillLutTable_RGB, tmp_Allocation);
    rsForEach(ApplyChanges_RGB, in, out);
}

