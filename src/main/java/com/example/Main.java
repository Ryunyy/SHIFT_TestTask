package com.example;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static FileParser parser;
    private static ContentFilter filter;
    private static ArgumentChecker arguments;
    private static int stringCounter = 0, integerCounter = 0, floatsCounter = 0;
    private static Long intMinVal = Long.MAX_VALUE, intMaxVal = Long.MIN_VALUE, intSum = 0L;
    private static String shortest = null, longest = null;
    private static Double floatMinVal = Double.MAX_VALUE, floatMaxVal = Double.MIN_VALUE, floatMeanVal, intMeanVal, floatSum = 0.0;

    public static void main(String[] args) {
        arguments =  new ArgumentChecker(args);
        for(int fileIndex = 0; fileIndex < arguments.getInputFiles().size(); fileIndex++){
            parser = new FileParser(arguments.getInputFiles().get(fileIndex));
            filter = new ContentFilter(parser.getFileContent());
            writeToFile();
            collectStatistic();
        }
        printStatistic();
    }

    public static void writeToFile(){
        String outPathFile = arguments.getOutputPath() + arguments.getFilePrefix();
        FileWriter fileWriter;
        if(filter.strings.getArrayListSize() != 0){
            try { //create if not exist
                fileWriter = new FileWriter(outPathFile + "strings.txt", !arguments.isOverwriteFiles()); //append or overwrite
                for(String element: filter.strings.getArrayList()){
                    fileWriter.write(element + "\n");
                }
                fileWriter.close();
            } catch(IOException e){
                System.out.println("Error: An error accurred when trying to write in strings.txt file: " + e.getMessage());
            }
        }
        if(filter.integers.getArrayListSize() != 0){
            try { //create if not exist
                fileWriter = new FileWriter(outPathFile + "integers.txt", !arguments.isOverwriteFiles()); //append or overwrite
                for(Long element: filter.integers.getArrayList()){
                    fileWriter.write(element + "\n");
                }
                fileWriter.close();
            } catch(IOException e){
                System.out.println("Error: An error accurred when trying to write in integers.txt file: " + e.getMessage());
            }
        }
        if(filter.floats.getArrayListSize() != 0){
            try { //create if not exist
                fileWriter = new FileWriter(outPathFile + "floats.txt", !arguments.isOverwriteFiles()); //append or overwrite
                for(Double element: filter.floats.getArrayList()){
                    fileWriter.write(element + "\n");
                }
                fileWriter.close();
            } catch(IOException e){
                System.out.println("Error: An error accurred when trying to write in floats.txt file: " + e.getMessage());
            }
        }
    }

    public static void collectStatistic(){
        //short statistic - only count of elements
        stringCounter += filter.strings.getArrayListSize();
        integerCounter += filter.integers.getArrayListSize();
        floatsCounter += filter.floats.getArrayListSize();

        //full statistic
        if(arguments.isFullStatistic()) {
            //integers block
            if(filter.integers.getArrayListSize() > 0) {
                if (intMinVal > filter.integers.getMinValue())
                    intMinVal = filter.integers.getMinValue();

                if (intMaxVal < filter.integers.getMaxValue())
                    intMaxVal = filter.integers.getMaxValue();

                intSum += filter.integers.getSum();
                intMeanVal = (double) intSum / arguments.getInputFiles().size();
            }

            //floats block
            if(filter.floats.getArrayListSize() > 0) {
                if (floatMinVal > filter.floats.getMinValue())
                    floatMinVal = filter.floats.getMinValue();

                if (floatMaxVal < filter.floats.getMaxValue())
                    floatMaxVal = filter.floats.getMaxValue();

                floatSum += filter.floats.getSum();
                floatMeanVal = floatSum / arguments.getInputFiles().size();
            }

            //strings block
            if(filter.strings.getArrayListSize() > 0) {
                if (shortest == null || shortest.length() > filter.strings.getMinValue().length())
                    shortest = filter.strings.getMinValue();

                if (longest == null || longest.length() < filter.strings.getMaxValue().length())
                    longest = filter.strings.getMaxValue();
            }
        }
    }

    public static void printStatistic(){
        System.out.println("=====COUNT OF TYPES=====");
        System.out.println("\tStings: " + stringCounter);
        System.out.println("\tIntegers: " + integerCounter);
        System.out.println("\tFloats: " + floatsCounter);

        if(arguments.isFullStatistic()) {
            if(filter.integers.getArrayListSize() > 0) {
                System.out.println("\n=====INTEGERS STATISTIC=====");
                System.out.println("\tMinimal Integer: " + intMinVal);
                System.out.println("\tMaximum Integer: " + intMaxVal);
                System.out.println("\tMean Integer: " + intMeanVal);
                System.out.println("\tSum of all Integer: " + intSum);
            }

            if(filter.floats.getArrayListSize() > 0) {
                System.out.println("\n=====FLOATS STATISTIC=====");
                System.out.println("\tMinimal Float: " + floatMinVal);
                System.out.println("\tMaximum Float: " + floatMaxVal);
                System.out.println("\tMean Float: " + floatMeanVal);
                System.out.println("\tSum of all Float: " + floatSum);
            }

            if(filter.strings.getArrayListSize() > 0) {
                System.out.println("\n=====STRINGS STATISTIC=====");
                System.out.println("\tShortest string: " + shortest);
                System.out.println("\tLongest string: " + longest);
            }
        }
    }
}