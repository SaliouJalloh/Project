#pragma version (1)
#pragma rs java_package_name ( com . android . rssample )
#pragma rs_fp_relaxed
# include "rs_debug.rsh"

#define ARRAY_SIZE 256
#define MAX_VALUE 256
#define MIN_VALUE 0

typedef int2 MinMaxReturn[3];

typedef struct {
    int min_value;
    int max_value;
} intMinMax;

typedef struct {
    intMinMax R, G, B;
}intMinMaxRGB;

#pragma rs reduce(findMinAndMaxRGB) \
  initializer(findMinMaxInitRGB) accumulator(findMinMaxAccumulatorRGB) \
  combiner(findMinMaxCombinerRGB) outconverter(findMinMaxOutConverterRGB)



static void findMinMaxInitRGB(intMinMaxRGB* accum){
    accum->R.min_value = MAX_VALUE ;
    accum->R.max_value = MIN_VALUE ;
    accum->G.min_value = MAX_VALUE ;
    accum->G.max_value = MIN_VALUE ;
    accum->B.min_value = MAX_VALUE ;
    accum->B.max_value = MIN_VALUE ;
}


static void findMinMaxAccumulatorRGB(intMinMaxRGB* accum, uchar4 in) {
    if (in.r <= accum->R.min_value)
        accum->R.min_value = in.r;
    if (in.r >= accum->R.max_value)
        accum->R.max_value = in.r;
    if (in.g <= accum->G.min_value)
        accum->G.min_value = in.g;
    if (in.g >= accum->G.max_value)
        accum->G.max_value = in.g;
    if (in.b <= accum->B.min_value)
        accum->B.min_value = in.b;
    if (in.b >= accum->B.max_value)
        accum->B.max_value = in.b;
}

static void findMinMaxCombinerRGB(intMinMaxRGB* accum, const intMinMaxRGB* addend){
    if (accum->R.min_value < 0 || addend->R.min_value < accum->R.min_value)
        accum->R.min_value = addend->R.min_value;
    if (accum->R.max_value < 0 || addend->R.max_value > accum->R.max_value)
        accum->R.max_value = addend->R.max_value;

    if (accum->G.min_value < 0 || addend->G.min_value < accum->G.min_value)
        accum->G.min_value = addend->G.min_value;
    if (accum->G.max_value < 0 || addend->G.max_value > accum->G.max_value)
        accum->G.max_value = addend->G.max_value;

    if (accum->B.min_value < 0 || addend->B.min_value < accum->B.min_value)
        accum->B.min_value = addend->B.min_value;
    if (accum->B.max_value < 0 || addend->B.max_value > accum->B.max_value)
        accum->B.max_value = addend->B.max_value;
}

static void findMinMaxOutConverterRGB(MinMaxReturn* result, const intMinMaxRGB* accum){
    (*result)[0].x = accum->R.min_value ;
    (*result)[0].y = accum->R.max_value ;

    (*result)[1].x = accum->G.min_value ;
    (*result)[1].y = accum->G.max_value ;

    (*result)[2].x = accum->B.min_value ;
    (*result)[2].y = accum->B.max_value ;
}

