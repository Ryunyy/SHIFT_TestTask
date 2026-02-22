package com.example;

public class Integers extends DataType<Long>{
    public Integers(){
        super();
    }

    @Override
    public Long getMinValue() {
        Long minValue = this.getArrayList().get(0);
        for(int index = 1; index < this.getArrayListSize(); index++){
            Long currentInteger = this.getArrayList().get(index);
            if(currentInteger < minValue)
                minValue = currentInteger;
        }
        return minValue;
    }

    @Override
    public Long getMaxValue() {
        Long maxValue = this.getArrayList().get(0);
        for(int index = 1; index < this.getArrayListSize(); index++){
            Long currentInteger = this.getArrayList().get(index);
            if(currentInteger > maxValue)
                maxValue = currentInteger;
        }
        return maxValue;
    }

    public Long getSum(){
        Long sum = 0L;
        for(int index = 0; index < this.getArrayListSize(); index++)
            sum += this.getArrayList().get(index);
        return sum;
    }

    public Double getMean(){
        return (double) getSum() / this.getArrayListSize();
    }
}
