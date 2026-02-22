package com.example;

public class Strings extends DataType<String>{
    public Strings(){
        super();
    }

    @Override
    public String getMinValue() {
        String minValue = null;
        for(int index = 0; index < this.getArrayListSize(); index++){
            String currentString = this.getArrayList().get(index);
            if(minValue == null || currentString.length() < minValue.length())
                    minValue = currentString;
        }
        return minValue;
    }

    @Override
    public String getMaxValue() {
        String maxValue = null;
        for(int index = 0; index < this.getArrayListSize(); index++){
            String currentString = this.getArrayList().get(index);
            if(maxValue == null || currentString.length() > maxValue.length())
                maxValue = currentString;
        }
        return maxValue;
    }
}
