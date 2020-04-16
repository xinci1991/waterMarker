package com.example.test.watermark;

public class Start {
	
	static{
        //加载opencv库
        System.load("C:\\Users\\ryan\\Desktop\\waterMarker\\src\\main\\resources\\lib\\opencv_java411.dll");
    }
	
	//********************************************************************************
	//**                              |固定比例裁剪 |                                **
	//********************************************************************************
	/**
	 * 嵌入,抵抗固定比例裁剪,嵌入的信息是图片的形式
	 */
	public static String embed(String srcPath, String watermarkContent, int p) {
		return BlockEmbed.startEmbed(srcPath, watermarkContent, p);
	}
	public static byte[] embed(byte[] img, String watermarkContent, int p) {
		return BlockEmbed.startEmbed(img, watermarkContent, p);
	}
	/**
	 * 嵌入,抵抗固定比例裁剪,嵌入的信息是二进制bit流的形式
	 */
	public static String embed(String srcPath, byte[] watermarkContent, int p) {
		return BlockEmbed.startEmbed(srcPath, watermarkContent, p);
	}
	public static byte[] embed(byte[] img, byte[] watermarkContent, int p) {
		return BlockEmbed.startEmbed(img, watermarkContent, p);
	}
	
	/**
	 * 提取,抵抗固定比例裁剪,嵌入的信息是图片的形式
	 */
	public static String extract(String srcPath, double proportion) {
		return BlockExtract.startExtractN(srcPath, proportion);
	}
	public static byte[] extract(byte[] img, double proportion) {
		return BlockExtract.startExtractN(img, proportion);
	}
	/**
	 * 提取,抵抗固定比例裁剪,嵌入的信息是二进制bit流的形式
	 */
	public static byte[] extractBit(String srcPath, double proportion) {
		return BlockExtract.startExtractNReturnBitStream(srcPath, proportion);
	}
	public static byte[] extractBit(byte[] img, double proportion) {
		return BlockExtract.startExtractNReturnBitStream(img, proportion);
	}
	
}
