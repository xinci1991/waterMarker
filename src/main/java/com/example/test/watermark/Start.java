package com.example.test.watermark;

public class Start {
	
	static{
        //����opencv��
        System.load("C:\\Users\\ryan\\Desktop\\waterMarker\\src\\main\\resources\\lib\\opencv_java411.dll");
    }
	
	//********************************************************************************
	//**                              |�̶������ü� |                                **
	//********************************************************************************
	/**
	 * Ƕ��,�ֿ��̶������ü�,Ƕ�����Ϣ��ͼƬ����ʽ
	 */
	public static String embed(String srcPath, String watermarkContent, int p) {
		return BlockEmbed.startEmbed(srcPath, watermarkContent, p);
	}
	public static byte[] embed(byte[] img, String watermarkContent, int p) {
		return BlockEmbed.startEmbed(img, watermarkContent, p);
	}
	/**
	 * Ƕ��,�ֿ��̶������ü�,Ƕ�����Ϣ�Ƕ�����bit������ʽ
	 */
	public static String embed(String srcPath, byte[] watermarkContent, int p) {
		return BlockEmbed.startEmbed(srcPath, watermarkContent, p);
	}
	public static byte[] embed(byte[] img, byte[] watermarkContent, int p) {
		return BlockEmbed.startEmbed(img, watermarkContent, p);
	}
	
	/**
	 * ��ȡ,�ֿ��̶������ü�,Ƕ�����Ϣ��ͼƬ����ʽ
	 */
	public static String extract(String srcPath, double proportion) {
		return BlockExtract.startExtractN(srcPath, proportion);
	}
	public static byte[] extract(byte[] img, double proportion) {
		return BlockExtract.startExtractN(img, proportion);
	}
	/**
	 * ��ȡ,�ֿ��̶������ü�,Ƕ�����Ϣ�Ƕ�����bit������ʽ
	 */
	public static byte[] extractBit(String srcPath, double proportion) {
		return BlockExtract.startExtractNReturnBitStream(srcPath, proportion);
	}
	public static byte[] extractBit(byte[] img, double proportion) {
		return BlockExtract.startExtractNReturnBitStream(img, proportion);
	}
	
}
