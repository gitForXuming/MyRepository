package com.suanfa;

import java.util.Arrays;

/**
 * Created by lenovo on 2017/12/28.
 * Title SufanTest
 * Package  com.suanfa
 * Description
 *
 * @Version V1.0
 */
public class SufanTest {
    public static void main(String[] args) {
        Integer[] test = new Integer[]{3,1,5,0,7,12,3,9,4,6,0};
        BubbleSort<Integer[]> sort = new BubbleSort<Integer[]>(test);
        System.out.println(Arrays.toString(sort.sort()));
    }
}
