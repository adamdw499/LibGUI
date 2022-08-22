package com.mason.libgui.utils;

import java.util.List;
import java.util.function.Function;

import static com.mason.libgui.utils.Utils.R;

public class Distribution<T>{


    private final List<T> items;
    private final double[] pmf;
    private double totalMass;


    public Distribution(List<T> items, double[] pmf){
        this.items = items;
        this.pmf = pmf;
        totalMass = 0;
        for(double d : pmf) totalMass += d;
    }

    public Distribution(List<T> items, Function<? super T, Double> pmf){
        this(items, calculatePmf(items, pmf));
    }


    private static <E> double[] calculatePmf(List<E> items, Function<? super E, Double> func){
        double[] ret = new double[items.size()];
        for(int n=0; n<ret.length; n++){
            ret[n] = func.apply(items.get(n));
        }
        return ret;
    }


    public T get(){
        double chance = R.nextDouble()*totalMass;
        for(int n=0; n<pmf.length; n++){
            if(chance<pmf[n]) return items.get(n);
            else chance -= pmf[n];
        }
        throw new IllegalStateException();
    }

}
