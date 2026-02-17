package com.example;

import java.util.ArrayList;
import java.util.Arrays;

public class ArgumentChecker {
    ArrayList<String> flags = new ArrayList<>(Arrays.asList("-o", "-p", "-a", "-s", "-f"));
    // -o output
    // -p prefix
    // -a append
    // -s/-f short/full statistic
    // думаю, стоит обозначить строгую структуру порядка аргументов по типу: -s/-f -a -o [path] -p [prefix] <files> - создать регулярку?
    // т.е. текстовые области не только проверять после флага опции, но и и в определенном порядке - файлы в конце параметров
    // +++ добавить проверку доступности путей - чтобы кому-то не взбрело закинуть в системные папки

    String[] arguments;
    public ArgumentChecker(String[] args){
        this.arguments = args;

    }

    private void FlagsChecker(){
        boolean noExtraFlagsFound = true;
//        for(String argument: this.arguments){
//
//        }
        //Создать регулярку и прогнать команду через нее
    }
}
