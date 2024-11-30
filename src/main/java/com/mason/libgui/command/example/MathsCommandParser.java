package com.mason.libgui.command.example;


import java.util.Scanner;
import java.util.function.Supplier;

import static com.mason.libgui.utils.Utils.R;

public class MathsCommandParser implements Runnable{


    //Commands have format "[symbol] [number of problems] [number of digits]"
    private int questionsRemaining = 0;
    private Supplier<Problem> problemSupplier;
    private Problem currentProblem;

    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;


    private void interpretCommand(String s){
        s = s.trim();
        if(s.equals("exit")){ //exit
            running = false;
        }else if(questionsRemaining == 0){ //set problems
            interpretSettingCommand(s);
        }else{ //answering
            interpretAnswerCommand(s);
        }
    }

    private void interpretAnswerCommand(String s){
        try{
            if(Integer.parseInt(s) == currentProblem.soln){
                System.out.println("Correct!");
                nextQuestion();
            }else{
                System.out.println("Incorrect!");
            }
        }catch(NumberFormatException e){
            System.out.println("Answer " + s + " is not a number!");
        }
    }

    private void interpretSettingCommand(String s){
        String[] tokens = s.split(" ");
        if(tokens.length != 3){
            System.out.println("Malformed setting command " + s);
            return;
        }

        int numDigits;
        try{
            questionsRemaining = Integer.parseInt(tokens[1])+1;
            numDigits = Integer.parseInt(tokens[2]);
        }catch(NumberFormatException e){
            System.out.println("Setting command has NaN: " + s);
            return;
        }

        switch(tokens[0]){
            case "+" -> problemSupplier = () -> {
                int a = randNum(numDigits);
                int b = randNum(numDigits);
                return new Problem(getProbString(a, b, tokens[0]), a + b);
            };
            case "-" -> problemSupplier = () -> {
                int a = randNum(numDigits);
                int b = randNum(numDigits);
                return new Problem(getProbString(a, b, tokens[0]), a - b);
            };
            case "*" -> problemSupplier = () -> {
                int a = randNum(numDigits);
                int b = randNum(numDigits);
                return new Problem(getProbString(a, b, tokens[0]), a * b);
            };
            default -> System.out.println("Malformed symbol: " + s);
        }

        nextQuestion();
    }

    private String getProbString(int a, int b, String symbol){
        return a + " " + symbol + " " + b;
    }

    private int randNum(int numDigits){
        int base = (int) Math.pow(10, numDigits-1);
        return base + R.nextInt(9*base);
    }

    private void nextQuestion(){
        questionsRemaining--;
        System.out.println("Questions remaining " + questionsRemaining);
        if(questionsRemaining > 0){
            currentProblem = problemSupplier.get();
            System.out.println(currentProblem.str);
        }
    }

    @Override
    public void run(){
        while(running){
            interpretCommand(scanner.nextLine());
        }
    }


    private record Problem(String str, int soln){}


    public static void main(String... args){

        System.out.println("Running Maths Command Parser...");
        new MathsCommandParser().run();
    }


}
