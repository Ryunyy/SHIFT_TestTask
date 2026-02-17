package com.example;

import java.io.*;
import java.util.ArrayList;

public class FileParser {
    String filename;
    BufferedReader bdReader;
    ArrayList<String> fileContent = new ArrayList<>();

    public FileParser(String filename){
        this.filename = filename;

    }

    private void OpenFile(){
        try {
            this.bdReader = new BufferedReader(new FileReader(new File(filename)));
        } catch (FileNotFoundException e) {
            System.out.println("Error: FileParser.java - OpenFile function fail with reason:\n" + e);
            System.exit(1);
        }
    }

    private void CloseFile(){
        try {
            this.bdReader.close();
        } catch (IOException e) {
            System.out.println("Error: FileParser.java - CloseFile function fail with reason:\n" + e);
            System.exit(1);
        }
    }

    public void FileExtractor(){
        OpenFile();
        String line;
        boolean flag = true;
        while(flag){
            try {
                line = this.bdReader.readLine();
                if(line != null)
                    fileContent.add(line);
                else
                    flag = false;
            } catch (IOException e) {
                System.out.println("Error: FileParser.java - FileExtractor function fail with reason:\n" + e);
                System.exit(1);
            }
        }
        CloseFile();
    }

    public ArrayList<String> GetFileContent(){
        return this.fileContent;
    }

    public void PrintFileContent(){
        System.out.println(this.fileContent);
    }
}
