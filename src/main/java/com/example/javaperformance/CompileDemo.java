package com.example.javaperformance;

public class CompileDemo implements  Runnable{

    @Override
    public void run() {

        int sum=0;
        for (int i=0;i<100000;i++){
            sum+=sum();
        }
        System.out.println(sum);
    }

    public int sum(){
        return 1+2;
    }
}
