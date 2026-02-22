package com.example;

import java.util.ArrayList;

public abstract class DataType<T> {
    private ArrayList<T> typeArrayList = new ArrayList<>();

    public DataType(){}

    public abstract T getMinValue();

    public abstract T getMaxValue();

    public void append(T value){
        this.typeArrayList.add(value);
    }

    public int getArrayListSize(){
        return this.typeArrayList.size();
    }

    public ArrayList<T> getArrayList(){
        return this.typeArrayList;
    }
}

