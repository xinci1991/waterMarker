package com.example.test.watermark;


public class Test {
	
	static{
        //����opencv��
        System.load("C:\\Users\\ryan\\Desktop\\waterMarker\\src\\main\\resources\\lib\\opencv_java411.dll");
    }
	
	public static void main(String[] args) {
		System.out.print("ϵͳ��ǰ�������������ڴ棺" + Runtime.getRuntime().totalMemory()/1024/1024 + "M\n" 
				+ "��ʼ��ռ�õ��ڴ棺" + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024 + "M\n");
		for(int i = 1; i < 10001; ++i){
			String srcPath = "C:\\Users\\qb\\Desktop\\DigitalWatermark0\\4.jpg";
			String watermarkContent = "������ˮӡ������ˮӡ������ˮӡ������ˮӡ������ˮӡ";
			String destPath = Start.embed(srcPath, watermarkContent, 50);
			Start.extract(destPath, 0.9);
			if(i % 100 == 0){
				System.out.print("��" + i + "��ѭ��ʱ��" + "����ǰ��ռ���ڴ棺" + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024 + "M;  ");
				System.gc();
				System.out.println("���պ���ռ���ڴ棺" + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024 + "M");
			}
		}
		System.out.println("��ǰ��ռ���ڴ棺" + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024 + "M");
	}
}
