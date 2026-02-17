package com.example;


public class Main {
    public static void main(String[] args) {
        int counter = 0;
        for (String argument: args) {
            System.out.println("Argument on " + counter + " position: " + argument );
            counter++;
        }


    }
}