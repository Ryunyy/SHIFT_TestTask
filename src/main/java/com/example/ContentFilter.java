package com.example;

import java.util.ArrayList;

public class ContentFilter {
    private ArrayList<String> fileContent = new ArrayList<>();
    Strings strings = new Strings();
    Integers integers = new Integers();
    Floats floats = new Floats();

    public ContentFilter(ArrayList<String> fileContent){
        this.fileContent = (ArrayList<String>) fileContent.clone();
        this.integersFilter();
        this.floatsFilter();
        this.stringsFilter();
//        this.printDetectedTypes();
    }

    private void stringsFilter(){
        for(int lineIndex = 0; lineIndex < this.fileContent.size(); lineIndex++)
            strings.append(fileContent.get(lineIndex));
    }

    private void integersFilter(){
        for(int lineIndex = 0; lineIndex < this.fileContent.size(); ){
            try{
                Long newInteger = Long.valueOf(fileContent.get(lineIndex));
                integers.append(newInteger);
                fileContent.remove(lineIndex);
            } catch(NumberFormatException e){
                //not an integer type, skip
                lineIndex++;
            }
        }
    }

    private void floatsFilter(){
        for(int lineIndex = 0; lineIndex < this.fileContent.size(); ){
            try{
                Double newFloat = Double.valueOf(fileContent.get(lineIndex));
                floats.append(newFloat);
                fileContent.remove(lineIndex);
            } catch(NumberFormatException e){
                //not a double type, skip
                lineIndex++;
            }
        }
    }

    private void printDetectedTypes(){
        System.out.println("Integers: ");
        System.out.println(integers.getArrayList());

        System.out.println("Floats: ");
        System.out.println(floats.getArrayList());

        System.out.println("Strings: ");
        System.out.println(strings.getArrayList());
    }
}
