package com.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileParser {
    private String filename;
    private Scanner scanner;
    private ArrayList<String> fileContent = new ArrayList<>();

    public FileParser(String filename){
        this.filename = filename;
        fileExtractor();
    }

    private void fileExtractor(){
        try {
            this.scanner = new Scanner(new FileReader(new File(this.filename)));
            while(this.scanner.hasNextLine())
                this.fileContent.add(this.scanner.nextLine());
        } catch (FileNotFoundException e) {
            System.out.println("Error: FileParser.java - File are not found!\n");
            System.exit(1);
        } catch(Exception e){
            System.out.println("Error: FileParser.java - Errors occurred when trying to read file " + this.filename + ": " + e.getMessage() + "\n");
            System.exit(1);
        }
        this.scanner.close();
    }

    public ArrayList<String> getFileContent(){
        return this.fileContent;
    }

    public void printFileContent(){
        System.out.println(this.fileContent);
    }
}
