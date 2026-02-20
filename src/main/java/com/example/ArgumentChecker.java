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
    //message to be shown when errors occurred
    private String errorMessage = "";

    public ArgumentChecker(String[] args){
        this.arguments = new ArrayList<>(Arrays.asList(args));
        CheckFlags();
    }

    private void CheckArgumentsCount(){
        if(this.arguments.size() < 2) {
            this.errorMessage += "Error: Too few arguments! At least must be two!\n";
            PrintUsageRules();
            System.exit(1);
        }
    }

    private void CheckArgumentExistence(String argument, boolean enableLog){
        if(!this.arguments.contains(argument)){
            if(enableLog)
                errorMessage += "Error: Required argument '" + argument + "' is missing!\n";
            PrintUsageRules();
            System.exit(1);
        }
    }

    private void CheckBothStatisticFlags(){
        if(this.arguments.contains("-s") && this.arguments.contains("-f")) {
            this.errorMessage += "Error: Options '-s' and '-f' can not be entered together!\n";
            PrintUsageRules();
            System.exit(1);
        }
    }

    private void CheckFlagOrder() {
        int globalFlagsIndex = -1;
        for (String argument : this.arguments) {
            int localIndex = this.flags.indexOf(argument);
            if(localIndex != -1)
                if(localIndex > globalFlagsIndex) //if found
                    globalFlagsIndex = localIndex;
                else {
                    this.errorMessage += "Error: Order of parameters is invalid!\n";
                    PrintUsageRules();
                    System.exit(1);
                }
        }
    }

    private void CheckParameterAfterArgumentMissing(String argument){ //checks if the <prefix> or <path> aren't found after -p or -o arguments respectively
        int indexOfArgument = this.arguments.indexOf(argument);
        if(indexOfArgument != -1) { // if option exist in arguments, if not - no need to check
            if (this.arguments.size() == (indexOfArgument+1) || this.flags.contains(this.arguments.get(indexOfArgument + 1))) { //and the next one IS IN the flag list (skipped path or prefix)
                this.errorMessage += "Error: Parameter after argument '" + argument + "' is missing!\n";
                PrintUsageRules();
                System.exit(1);
            }
        }
    }

    private void CheckInputFilesMissing(){
        int lastParameterIndex = this.arguments.size() - 1;
        String lastParameter = this.arguments.get(lastParameterIndex);
        String potentialOptionArgument = this.arguments.get(lastParameterIndex - 1);
        if (!lastParameter.matches("(?i).*[.]txt$") || potentialOptionArgument.equals("-o") || potentialOptionArgument.equals("-p")) { // if the last argument ends with ".txt" with insensetive register
            this.errorMessage += "Error: No input files found. Must have at least one!\n";
            PrintUsageRules();
            System.exit(1);
        }
    }

    private void CheckUnknownArguments() {
        ArrayList<String> argumentsClone = (ArrayList<String>) this.arguments.clone();
        int inputFileAllowedIndex = 0;
        for(int indexOfArgument = 0; indexOfArgument < this.arguments.size(); indexOfArgument++){
            String argument = this.arguments.get(indexOfArgument);
            switch (argument){
                case "-s":
                case "-f":
                case "-a":
                    argumentsClone.remove(argument);
                    inputFileAllowedIndex = indexOfArgument + 1;
                    break;
                case "-o" :
                case "-p" :
                    argumentsClone.remove(argument);
                    indexOfArgument++;
                    argument = this.arguments.get(indexOfArgument);
                    argumentsClone.remove(argument);
                    inputFileAllowedIndex = indexOfArgument + 1;
                    break;
            }
        }
        inputFileAllowedIndex = argumentsClone.indexOf(this.arguments.get(inputFileAllowedIndex));
        for(; inputFileAllowedIndex < argumentsClone.size(); ){
            if(argumentsClone.get(inputFileAllowedIndex).matches("(?i).*[.]txt$")){
                argumentsClone.remove(inputFileAllowedIndex); // remove all .txt files that after -p <prefix>
            }
            else
                inputFileAllowedIndex++;
        }
        if(!argumentsClone.isEmpty()){
            errorMessage += "Error: Unknown arguments detected: ";
            errorMessage += String.join(" ", argumentsClone);
            errorMessage += "\n";
            PrintUsageRules();
            System.exit(1);
        }
    }

    private void CheckPrefixNameValidation(){
        int indexP = this.arguments.indexOf("-p"); // check if prefix contains \ / : * ? < > |
        boolean isPrefixNameInvalid = false;
        if(indexP != -1){
            String[] prohibitedSymbols = new String[]{"\\", "/", ":", "*", "?", "<", ">", "|"};
            String prefixName = this.arguments.get(indexP + 1);
            for(String symbol: prohibitedSymbols){
                if(prefixName.contains(symbol)) {
                    if(!isPrefixNameInvalid) {
                        this.errorMessage += "Error: Prohibited symbols in <prefix> parameter are detected: ";
                        isPrefixNameInvalid = true;
                    }
                    this.errorMessage += symbol;
                }
            }
            if(isPrefixNameInvalid) {
                this.errorMessage += "\n";
                PrintUsageRules();
                System.exit(1);
            }
        }
    }

    private void CheckPath(){ // unreachable/prohibited path
        int indexO = this.arguments.indexOf("-o"); // mb move it to content filter?
        if(indexO != -1) {
            String stringPath = this.arguments.get(indexO + 1);
            try{
                Path path = Paths.get(stringPath);
                if(!Files.isDirectory(path)){
                    errorMessage += "Error: Entered <path> parameter '" + stringPath +"' is not a directory!\n";
                    PrintUsageRules();
                    System.exit(1);
                }
            } catch (InvalidPathException e){
                errorMessage += "Error: Entered <path> parameter '" + stringPath + "' is not valid!\n";
                PrintUsageRules();
                System.exit(1);
            }
        }
    }

    private void CheckFlags(){ //mb rename all and do "CheckArgsCount" - like
        CheckArgumentsCount(); // exit with reason: too few arguments
        CheckBothStatisticFlags(); // exit with reason: invalid structure of argument input
        CheckFlagOrder();
        CheckParameterAfterArgumentMissing("-o");
        CheckParameterAfterArgumentMissing("-p");
        CheckUnknownArguments();
        CheckInputFilesMissing(); // exit with reason: no input files detected
        CheckPrefixNameValidation(); // exit with reason: invalid <prefix> name
        CheckPath();
        //CheckInputFilesExistence
        //Check
    }

    private void PrintUsageRules(){
        this.errorMessage += "\nUsage: [-s | -f] [-a] [-o <path>] [-p <prefix>] <file1.txt> <file2.txt> ...\n\n" +
                "Options supported:\n" +
                "\t-s/-f | Show short (for '-s') or full (for '-f') statistic. Short by default.\n" + //mb
                "\t-a | Append results to output files. Overwrite by default.\n" +
                "\t-o <path> | Select <path> as the output directory for files.\n" +
                "\t-p <prefix> | Add <prefix> to the output files. E.g. -p out- -> out-strings.txt out-integers.txt etc. Prohibited symbols: \\ / : * ? < > |\n";
        System.out.println(this.errorMessage);
    }
}
