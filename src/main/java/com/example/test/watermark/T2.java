package com.example.test.watermark;

public class T2 {
	
	static{
        //����opencv��
        System.load("C:\\Users\\ryan\\Desktop\\waterMarker\\src\\main\\resources\\lib\\opencv_java411.dll");
    }
	
	public static void main(String[] args) {
		System.out.println("Hello world!");
		System.out.println("ϵͳ��ǰ�������������ڴ棺" + Runtime.getRuntime().totalMemory()/1024/1024 + "M\n" 
		+ "��ռ�õ��ڴ棺" + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024 + "M\n");
	}
}
