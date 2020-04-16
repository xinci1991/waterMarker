package com.example.test.watermark;

public class T2 {
	
	static{
        //加载opencv库
        System.load("C:\\Users\\ryan\\Desktop\\waterMarker\\src\\main\\resources\\lib\\opencv_java411.dll");
    }
	
	public static void main(String[] args) {
		System.out.println("Hello world!");
		System.out.println("系统当前分配给虚拟机的内存：" + Runtime.getRuntime().totalMemory()/1024/1024 + "M\n" 
		+ "已占用的内存：" + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024 + "M\n");
	}
}
