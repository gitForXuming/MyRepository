package com.suanfa;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lenovo on 2017/12/28.
 * Title BubbleSort
 * Package  com.suanfa
 * Description 冒泡排序
 *
 * @Version V1.0
 */
public class BubbleSort<T> {
    private T src;
    public BubbleSort(T t){
        this.src =t;
    }
    public T sort(){
        Set<String> set = new HashSet<String>();
        System.out.println(src.getClass().getComponentType());
      //  ((Object[]) src)[0];
        if(src.getClass().isArray()){//数组
            Object k = ((Object[])src)[1];
            for (int i = 0; i < ((Object[]) src).length-1; i++) {
                for (int j = 0; j < ((Object[]) src).length-1-i; j++) {
                    if(((Object[]) src)[j].hashCode() > ((Object[]) src)[j+1].hashCode() ){
                        Object temp = ((Object[]) src)[j];
                        ((Object[]) src)[j] = ((Object[]) src)[j+1] ;
                        ((Object[]) src)[j+1] =temp;
                    }
                }
            }
        }else if(src instanceof Collection){//

        }else{

        }
        return src;
    }
}
