/**
*
* Copyright 2013-2014 ShenZhen Mlizhi Technology Co., Ltd. All Rights Reserved. 
*
*/
package com.mlz.decode;

public class DecryptUtil {
	// 定义JNI函数
	public native int decrypt(byte[] arrs);

	// 加载jni
	static {
		// 这名跟库名去掉lib
		System.loadLibrary("DecryptUtils");
	}
}