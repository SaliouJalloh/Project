#pragma version (1)
#pragma rs java_package_name ( com . android . rssample )
#pragma rs_fp_relaxed
# include "rs_debug.rsh"

#define ARRAY_SIZE 256
typedef int Histogram[ARRAY_SIZE ];
typedef int HistoCum[ARRAY_SIZE];

int size ;

#pragma rs reduce(calculate_histogram) \
  accumulator(AccumHisto) \
  combiner(CombinHisto) \

static void AccumHisto(Histogram *h, uchar4 in){
    ++(*h)[in.r];
}

static void CombinHisto(Histogram *accum, const Histogram *addend) {
  for (int i = 0; i < ARRAY_SIZE; ++i)
    (*accum)[i] += (*addend)[i];
}

#pragma rs reduce(calculate_histogram_CUM) \
  accumulator(AccumHisto) \
  combiner(CombinHisto) \
  outconverter(OutconverterHistogramme)

static void OutconverterHistogramme(HistoCum *result, const Histogram *addend){
    int value = 0 ;
    for (int i = 0 ; i < ARRAY_SIZE ; i++){
        value += (*addend)[i];
        (*result)[i] = ((value * 255) / ( size )) ;
    }
}