package com.example.javaperformance;

public class EscapeTest {
    public static void main(String[] args){
        for(int i = 0; i < 5_000_000; i++){
            createObject();
        }
    }

    public static void createObject(){
        new Object();
    }
}
