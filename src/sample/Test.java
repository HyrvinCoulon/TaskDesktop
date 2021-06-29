package sample;

import java.time.LocalTime;

public class Test {
    public static void main(String[] args){
        LocalTime now  = LocalTime.parse("17:40");
        System.out.println(now);
    }
}
