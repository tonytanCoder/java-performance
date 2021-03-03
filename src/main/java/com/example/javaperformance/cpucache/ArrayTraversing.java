package com.example.javaperformance.cpucache;

public class ArrayTraversing {

    public static void traversing1(int array[][]){
        for (int i=0;i<100;i++){
            for (int j=0;j<100;j++){
                //System.out.println(array[i][j]);
                array[i][j]=0;
            }
        }
    }


    public static void traversing2(int array[][]){
        for (int i=0;i<100;i++){
            for (int j=0;j<100;j++){
                //System.out.println(array[j][i]);
                array[j][i]=0;
            }
        }
    }
}
