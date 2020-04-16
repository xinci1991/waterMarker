package com.example.test.watermark;


public class Test {
	
	static{
        //加载opencv库
        System.load("C:\\Users\\ryan\\Desktop\\waterMarker\\src\\main\\resources\\lib\\opencv_java411.dll");
    }
	
	public static void main(String[] args) {
		System.out.print("系统当前分配给虚拟机的内存：" + Runtime.getRuntime().totalMemory()/1024/1024 + "M\n" 
				+ "初始已占用的内存：" + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024 + "M\n");
		for(int i = 1; i < 10001; ++i){
			String srcPath = "C:\\Users\\qb\\Desktop\\DigitalWatermark0\\4.jpg";
			String watermarkContent = "测试用水印测试用水印测试用水印测试用水印测试用水印";
			String destPath = Start.embed(srcPath, watermarkContent, 50);
			Start.extract(destPath, 0.9);
			if(i % 100 == 0){
				System.out.print("第" + i + "次循环时，" + "回收前已占用内存：" + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024 + "M;  ");
				System.gc();
				System.out.println("回收后已占用内存：" + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024 + "M");
			}
		}
		System.out.println("当前已占用内存：" + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024 + "M");
	}
}
