package com.example;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class ArgumentChecker {
    //options flags in priority order
    private ArrayList<String> flags = new ArrayList<>(Arrays.asList("-s", "-f", "-a", "-o", "-p"));
    //array list of input arguments
    private ArrayList<String> arguments;
    //array list of input files
    private ArrayList<String> inputFiles = new ArrayList<>();
    private String outputPath = System.getProperty("user.dir");
    private String filePrefix = "";
    private boolean fullStatistic = false; // true if -f, false is -s
    private boolean overwriteFiles = true; // false if -a
    //message to be shown when errors occurred
    private String errorMessage = "";
    //index of first input file
    private int inputFilesStartIndex = 0;

    public ArgumentChecker(String[] args){
        this.arguments = new ArrayList<>(Arrays.asList(args));
        argumentSplitter();
    }

    public ArrayList<String> getInputFiles() {
        return inputFiles;
    }

    private void setInputFiles(ArrayList<String> inputFiles) {
        this.inputFiles = inputFiles;
    }

    public String getOutputPath() {
        return outputPath;
    }

    private void setOutputPath(String outputPath) {
        if(outputPath.equals(""))
            outputPath = this.outputPath;
        if(isPathValid(outputPath)) {
            if (!outputPath.endsWith("\\"))
                outputPath += "\\";
            this.outputPath = outputPath;
        }
        else{
            printUsageRules();
            System.exit(1);
        }
    }

    public String getFilePrefix() {
        return filePrefix;
    }

    private void setFilePrefix(String filePrefix) {
        if(isPrefixNameValid(filePrefix))
            this.filePrefix = filePrefix;
        else{
            printUsageRules();
            System.exit(1);
        }
    }

    public boolean isFullStatistic() {
        return fullStatistic;
    }

    private void setFullStatistic(boolean fullStatistic) {
        this.fullStatistic = fullStatistic;
    }

    public boolean isOverwriteFiles() {
        return overwriteFiles;
    }

    private void setOverwriteFiles(boolean overwriteFiles) {
        this.overwriteFiles = overwriteFiles;
    }

    private void argumentSplitter(){
        if(this.arguments.isEmpty()) {
            this.errorMessage += "Error: Too few arguments! At least must be one!\n";
            printUsageRules();
            System.exit(1);
        }
        extractStatisticFlag();
        сheckFlagOrder();
        setOverwriteFiles(!isArgumentExist("-a", false));
        setOutputPath(extractParameterAfterArgument("-o"));
        setFilePrefix(extractParameterAfterArgument("-p"));
        checkUnknownArguments();
        setInputFiles(extractInputFiles());

//        System.out.println("Full statistic: " + isFullStatistic());
//        System.out.println("Overwrite files: " + isOverwriteFiles());
//        System.out.println("Output path: " + getOutputPath());
//        System.out.println("Output files prefix: " + getFilePrefix());
//        System.out.println("Input files: " + getInputFiles());
    }

    private boolean isArgumentExist(String argument, boolean enableLog){
        if(!this.arguments.contains(argument)){
            if(enableLog)
                errorMessage += "Error: Required argument '" + argument + "' is missing!\n";
            return false;
        }
        return true;
    }

    private boolean isPrefixNameValid(String prefixName){ // check if prefix contains \ / : * ? < > |boolean isPrefixNameInvalid = false;
        String[] prohibitedSymbols = new String[]{"\\", "/", ":", "*", "?", "<", ">", "|"};
        String foundSymbols = "";
        for(String symbol: prohibitedSymbols){
            if(prefixName.contains(symbol))
                foundSymbols += symbol + " ";
        }
        if(!foundSymbols.isEmpty()) {
            this.errorMessage += "Error: Prohibited symbols in <prefix> parameter are detected: " + foundSymbols + "\n";
            return false;
        }
        return true;
    }

    private boolean isPathValid(String outputPath){ // unreachable/prohibited path
        String error = "";
        try{
            Path path = Paths.get(outputPath);
            if(!Files.isDirectory(path))
                error += "Error: Entered <path> parameter '" + outputPath +"' is not a directory!\n";
        } catch (InvalidPathException e){
            error += "Error: Entered <path> parameter '" + outputPath + "' is not valid!\n";
        }
        if(!error.isEmpty()){
            errorMessage += error;
            return false;
        }
        return true;
    }

    private void сheckFlagOrder() {
        int globalFlagsIndex = -1;
        for (String argument : this.arguments) {
            int localIndex = this.flags.indexOf(argument);
            if(localIndex != -1)
                if(localIndex > globalFlagsIndex) //if found
                    globalFlagsIndex = localIndex;
                else {
                    this.errorMessage += "Error: Order of parameters is invalid!\n";
                    printUsageRules();
                    System.exit(1);
                }
        }
    }

    private void checkUnknownArguments() {
        ArrayList<Integer> acceptedArguments = new ArrayList<>();
        int inputFileAllowedIndex = 0;
        for(int indexOfArgument = 0; indexOfArgument < this.arguments.size(); indexOfArgument++){ // filter all options flags with dependencies
            String argument = this.arguments.get(indexOfArgument);
            switch (argument){
                case "-s":
                case "-f":
                case "-a":
                    acceptedArguments.add(indexOfArgument);
                    inputFileAllowedIndex = indexOfArgument + 1;
                    break;
                case "-o" :
                case "-p" :
                    acceptedArguments.add(indexOfArgument);
                    acceptedArguments.add(indexOfArgument + 1);
                    indexOfArgument++;
                    inputFileAllowedIndex = indexOfArgument + 1;
                    break;
            }
        }
        this.inputFilesStartIndex = inputFileAllowedIndex;
        for(; inputFileAllowedIndex < this.arguments.size(); inputFileAllowedIndex++){ // search unknown arguments among input files
            if(this.arguments.get(inputFileAllowedIndex).matches("(?i).*[.]txt$"))
                acceptedArguments.add(inputFileAllowedIndex); // remove all .txt files that after -p <prefix>
        }
        if(this.arguments.size() != acceptedArguments.size()){
            String unknownArguments = "";
            for(int index = 0; index < arguments.size(); index++){
                if(!acceptedArguments.contains(index))
                    unknownArguments += this.arguments.get(index) + " ";
            }
            errorMessage += "Error: Unknown arguments detected: " + String.join(" ", unknownArguments) + "\n";
            printUsageRules();
            System.exit(1);
        }
    }

    private void checkInputFiles(){
        int lastParameterIndex = this.arguments.size() - 1;
        String lastParameter = this.arguments.get(lastParameterIndex);
        String error = "";
        if (!lastParameter.matches("(?i).*[.]txt$")) // if the last argument ends with ".txt" with insensetive register
            error += "Error: No input files found. Must have at least one!\n";
        if(lastParameterIndex != 0) { //if not only one parameter in args
            String potentialOptionArgument = this.arguments.get(lastParameterIndex - 1);
            if (potentialOptionArgument.equals("-o") || potentialOptionArgument.equals("-p"))
                error += "Error: No input files found. Must have at least one!\n";
        }
        if(!error.isEmpty()){
            this.errorMessage += error;
            printUsageRules();
            System.exit(1);
        }
    }

    private void extractStatisticFlag(){
        if(isArgumentExist("-f", false))
            setFullStatistic(true);
        if(isArgumentExist("-s", false) && isFullStatistic()) {
            this.errorMessage += "Error: Options '-s' and '-f' can not be entered together!\n";
            printUsageRules();
            System.exit(1);
        }
    }

    private String extractParameterAfterArgument(String argument){ //extract the <prefix> or <path> after -p or -o arguments respectively
        if(this.arguments.contains(argument)) { // if option exist in arguments
            int indexOfArgument = this.arguments.indexOf(argument);
            if (this.arguments.size() == (indexOfArgument + 1) || this.flags.contains(this.arguments.get(indexOfArgument + 1))) { //and the next one IS IN the flag list (skipped path or prefix)
                this.errorMessage += "Error: Parameter after argument '" + argument + "' is missing!\n";
                printUsageRules();
                System.exit(1);
            }
            return this.arguments.get(indexOfArgument + 1);
        }
        return "";
    }

    private ArrayList<String> extractInputFiles(){ // check if files really exist and readable
        checkInputFiles();
        ArrayList<String> inputFiles = new ArrayList<>();
        String error = "";
        for(int fileIndex = this.inputFilesStartIndex; fileIndex < this.arguments.size(); fileIndex++){
            String filePath = this.arguments.get(fileIndex);
            try{
                Path path = Paths.get(filePath);
                if(!Files.exists(path))
                    error += "Error: File '" + filePath +"' does not exist!\n";
                if(!Files.isReadable(path))
                    error += "Error: File '" + filePath +"' not readable!\n";
            } catch (InvalidPathException e){
                error += "Error: Encountered error when open file '" + filePath + "':\n";
                error += e.getMessage() + "\n";
            }
            if(!error.isEmpty()){
                this.errorMessage += error;
                printUsageRules();
                System.exit(1);
            }
            inputFiles.add(filePath);
        }
        return inputFiles;
    }

    private void printUsageRules(){
        this.errorMessage += "\nUsage: [-s | -f] [-a] [-o <path>] [-p <prefix>] <file1.txt> <file2.txt> ...\n\n" +
                "Options supported:\n" +
                "\t-s/-f | Show short (for '-s') or full (for '-f') statistic. Short by default.\n" + //mb
                "\t-a | Append results to output files. Overwrite by default.\n" +
                "\t-o <path> | Select <path> as the output directory for files.\n" +
                "\t-p <prefix> | Add <prefix> to the output files. E.g. -p out- -> out-strings.txt out-integers.txt etc. Prohibited symbols: \\ / : * ? < > |\n";
        System.out.println(this.errorMessage);
    }
}
