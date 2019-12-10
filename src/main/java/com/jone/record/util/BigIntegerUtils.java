package com.jone.record.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BigIntegerUtils {

    public static BigInteger sumRights(List<Integer> rights){
        BigInteger num = new BigInteger("0");
        for(int i=0; i<rights.size(); i++){
            num = num.setBit(rights.get(i));
        }
        return num;
    }

    public static boolean testRights(BigInteger sum,int targetRights){
        if(sum==null)
            sum = new BigInteger("0");
        return sum.testBit(targetRights);
    }

    public  static void main(String[] args){
        List<Integer> rights = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            rights.add(i);
        }
        rights.add(73);
        System.out.println(sumRights(rights));

        System.out.println(testRights(BigInteger.valueOf(17592180801534L), 20));
    }
}
