package com.mlz.bluetooth;

import android.util.Log;

import com.mlz.decode.DecryptUtil;
import com.mlz.shuiguang.fragment.detect.DetectFragment;
import com.mlz.shuiguang.model.DetectionData;
import com.mlz.shuiguang.util.Global;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by dehualai on 2/12/17.
 */

public class BluetoothDecrypt {

    int MIN_VALID = 20;
    int MAX_VALID = 60;

    int expect = 32;
    int theta = 6;


    public int fakeDataForDemo(){
        if (DetectFragment.selectWhen.equals(DetectionData.PRE_NURSING)){
            expect = 32;
            theta = 8;
        }else{
            expect = 39;
            theta = 4;
        }

        ArrayList<Integer> pool = filer(createGaussian(100, expect, theta));
        Random r = new Random();
        int randIndex = r.nextInt(pool.size());
        return pool.get(randIndex);
    }


    public int decrypt(byte[] a){

        DecryptUtil decryptUtil = new DecryptUtil();
        int value = decryptUtil.decrypt(a);
        Log.e("show", "value " + value);
        return value/1000 ;
        /*
        if (DetectFragment.selectWhen.equals(DetectionData.PRE_NURSING)){
            expect = 32;
            theta = 8;
        }else{
            expect = 39;
            theta = 4;
        }

        ArrayList<Integer> pool = filer(createGaussian(100, expect, theta));
        Random r = new Random();
        int randIndex = r.nextInt(pool.size());
        return pool.get(randIndex);
        */
    }


    /**
     * 生成num个数，并且服从方差为sigma2，平均值为avg的正态分布序列
     * 用来作为每种特征的节点个数
     * @param num 生成的随机数的个数
     * @param avg 平均值
     * @param sigma2 方差
     * @return 返回一个服从正态分布的int数组
     */
    private int[] createGaussian(int num, int avg, int sigma2){
        Random temp = new Random();
        int[] array = new int[num];
        for(int i = 0; i < num; i ++){
            array[i] = (int)(temp.nextGaussian()
                    * Math.sqrt(sigma2) + avg);
            if(array[i] <= 0)
                array[i] = avg;

        }
        return array;
    }

    private ArrayList<Integer> filer(int[] array){
        ArrayList<Integer> filterArray = new ArrayList<>();
        for (int i = 0 ; i < array.length; i++){
            if (array[i] >= MIN_VALID && array[i] <= MAX_VALID)
                filterArray.add(array[i]);
        }
        return filterArray;
    }

}
