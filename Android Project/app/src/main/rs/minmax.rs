#pragma version (1)
#pragma rs java_package_name ( com . android . rssample )
#pragma rs_fp_relaxed
# include "rs_debug.rsh"

#define ARRAY_SIZE 256
#define MAX_VALUE 256
#define MIN_VALUE 0

typedef struct {
  int min_value;
  int max_value;
} intMinMax;

#pragma rs reduce(findMinAndMax) \
  initializer(findMinMaxInit) accumulator(findMinMaxAccumulator) \
  combiner(findMinMaxCombiner) outconverter(findMinMaxOutConverter)



static void findMinMaxInit(intMinMax* accum){
    accum->min_value = MAX_VALUE ;
    accum->max_value = MIN_VALUE ;
}


static void findMinMaxAccumulator(intMinMax *accum, uchar4 in) {
    if (in.r <= accum->min_value)
        accum->min_value = in.r;
    if (in.r >= accum->max_value)
        accum->max_value = in.r;
}

static void findMinMaxCombiner(intMinMax *accum, const intMinMax *addend){
    if (accum->min_value < 0 || addend->min_value < accum->min_value)
        accum->min_value = addend->min_value;

    if (accum->max_value < 0 || addend->max_value > accum->max_value)
        accum->max_value = addend->max_value;

}

static void findMinMaxOutConverter(int2* result, const intMinMax *accum){
    result->x = accum->min_value ;
    result->y = accum->max_value ;
}








