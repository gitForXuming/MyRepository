/*
 * Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * This source code is provided to illustrate the usage of a given feature
 * or technique and has been deliberately simplified. Additional steps
 * required for a production-quality application, such as security checks,
 * input validation and proper error handling, might not be present in
 * this sample code.
 */

package com.jdk8Demo.forkjion;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import static java.lang.Integer.parseInt;

/**
 * MergeExample is a class that runs a demo benchmark of the {@code ForkJoin} framework
 * by benchmarking a {@link MergeSort} algorithm that is implemented using
 * {@link java.util.concurrent.RecursiveAction}.
 * The {@code ForkJoin} framework is setup with different parallelism levels
 * and the sort is executed with arrays of different sizes to see the
 * trade offs by using multiple threads for different sizes of the array.
 */
public class MergeDemo {
    // Use a fixed seed to always get the same random values back
    private final Random random = new Random(759123751834L);
    private static final int ITERATIONS = 10;

    
    
    
    /**
     * Represents the formula {@code f(n) = start + (step * n)} for n = 0 & n < iterations

    /**
     * Generates an array of {@code elements} random elements
     * @param elements the number of elements requested in the array
     * @return an array of {@code elements} random elements
     */
    private int[] generateArray(int elements) {
        int[] array = new int[elements];
        for (int i = 0; i < elements; ++i) {
            array[i] = random.nextInt();
        }
      //System.out.println(Arrays.toString(array));
        return array;
    }

    /**
     * Generates 1000 arrays of 1000 elements and sorts them as a warmup
     */
    private void warmup() {
		//分发的子线程不要超过cpu总核数
		/*int i =0;
		while (i<500){
			i++;
			try {
				Thread.sleep(500);*/
				MergeSort mergeSort = new MergeSort(Runtime.getRuntime().availableProcessors());
				mergeSort.sort(generateArray(50000));
			/*	System.out.println(i);
			}catch (Exception e){

			}
		}*/
	}
    
    public void	bubbleSort(){
   	 	int a[] =generateArray(500);
    	int temp=0;
    	for(int i=0;i<a.length-1;i++){
    		for(int j=0;j<a.length-1-i;j++){
    			if(a[j]>a[j+1]){
    				temp=a[j];
    				a[j]=a[j+1];
    				a[j+1]=temp;
    			}
    		}
    	}
   	}
   
    
    public static void main(String[] args) {
    	try {
			Runtime.getRuntime().exec("java -version");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int[] test = new int[]{1,5,3,7,2,5,3,7,6,4,0};
		Arrays.sort(test,0,11);
		System.out.println(Arrays.toString(test));

    	long begin =System.currentTimeMillis();
    	new MergeDemo().warmup();
    	long end = System.currentTimeMillis();
    	System.out.println(end - begin);
    	//new MergeDemo().bubbleSort();
    	//long end1 =System.currentTimeMillis();
    	//System.out.println(end1-end);

    }
}
