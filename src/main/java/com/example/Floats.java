package com.example;

public class Floats extends DataType<Double> {
    public Floats(){
        super();
    }

    @Override
    public Double getMinValue() {
        Double minValue = this.getArrayList().get(0);
        Double accuracy = 0.0000000000000000001;
        for(int index = 1; index < this.getArrayListSize(); index++){
            Double currentDouble = this.getArrayList().get(index);
            if((minValue - currentDouble) < accuracy)
                minValue = currentDouble;
        }
        return minValue;
    }

    @Override
    public Double getMaxValue() {
        Double maxValue = this.getArrayList().get(0);
        Double accuracy = 0.0000000000000000001;
        for(int index = 1; index < this.getArrayListSize(); index++){
            Double currentDouble = this.getArrayList().get(index);
            if((currentDouble - maxValue) > accuracy)
                maxValue = currentDouble;
        }
        return maxValue;
    }

    public Double getSum(){
        Double sum = 0.0;
        for(int index = 0; index < this.getArrayListSize(); index++)
            sum += this.getArrayList().get(index);
        return sum;
    }

    public Double getMean(){
        return getSum() / this.getArrayListSize();
    }
}
