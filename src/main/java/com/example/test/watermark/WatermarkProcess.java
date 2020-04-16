package com.example.test.watermark;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class WatermarkProcess {
	
	/**
	 * 添加位置标记
	 * @param watermarkContent  水印数组
	 * @return  添加标记后的水印数组
	 */
	public static byte[][] addLocationTag(byte[] watermarkContent, int contentWidth, int contentHeight, int contentWithTagWidth, int contentWithTagHeight) {
//		int contentWidth = 64;	//水印的宽度
//		int contentHeight = 64;	//水印的高度
//		int contentWithTagWidth = 160;	//添加标记后的水印宽度
//		int contentWithTagHeight = 120;	//添加标记后的水印高度
		
		//添加标记
		byte[][] watermarkContentWithTag = new byte[contentWithTagHeight][contentWithTagWidth];
		int lateralDistance = (contentWithTagWidth - contentWidth) / 2;	//侧边距
		int topDistance = (contentWithTagHeight - contentHeight) / 2;	//顶部间隔
		int count = 0;
		int i;
		for(i = 0; i < topDistance; ++i){
			for (int j = 0; j < contentWithTagWidth; ++j) {
				if(j < lateralDistance || j >= contentWidth + lateralDistance){
					watermarkContentWithTag[i][j] = '0';
				}else{
					watermarkContentWithTag[i][j] = '1';
				}
			}
		}
		for(;i < topDistance + contentHeight; ++i){
			for (int j = 0; j < contentWithTagWidth; ++j) {
				if(j < lateralDistance || j >= contentWidth + lateralDistance){
					watermarkContentWithTag[i][j] = '1';
				}else{
					watermarkContentWithTag[i][j] = watermarkContent[count];
					++count;
				}
			}
		}
		for(;i < contentWithTagHeight; ++i){
			for(int j = 0; j < contentWithTagWidth; ++j){
				if(j < lateralDistance){
					watermarkContentWithTag[i][j] = '0';
				}else{
					watermarkContentWithTag[i][j] = '1';
				}
			}
		}
		return watermarkContentWithTag;
	}
	
	/**
	 * 将水印的二值图转换为一个01byte数组
	 * 水印地址： data\\GlobalDCT\\temporary\\watermark.png
	 */
	public static byte[] transformToArrary(){
		Mat watermark = Imgcodecs.imread("temp\\watermark.png");
		int rows = watermark.rows();
		int cols = watermark.cols();
		byte[] watermarkArray = new byte[rows * cols];
		int count = 0;
		for(int i = 0; i < rows; ++i){
			for(int j = 0; j < cols; ++j){
				double[] temp = watermark.get(i, j);
				watermarkArray[count] = (byte) (temp[0] > 200 ? '1' : '0');
				++count;
			}
		}
		return watermarkArray;
	}
	/**
	 * 将水印的二值图转换为一个01byte数组
	 */
	public static byte[] transformToArrary(Mat watermark){
		int rows = watermark.rows();
		int cols = watermark.cols();
		byte[] watermarkArray = new byte[rows * cols];
		int count = 0;
		for(int i = 0; i < rows; ++i){
			for(int j = 0; j < cols; ++j){
				double[] temp = watermark.get(i, j);
				watermarkArray[count] = (byte) (temp[0] > 200 ? '1' : '0');
				++count;
			}
		}
		return watermarkArray;
	}
	/**
	 * 将二进制bit流转化为一个01byte数组
	 * 并保存在"temp\\watermark.txt"
	 * @param content 二进制流内容
	 * @return 字节流
	 */
	public static byte[] transformToArrary(byte[] content, int width, int height){
		byte[] watermarkArray = new byte[height * width];
		int i = 0;
		for(byte each : content){
			if(each == -1){
				for (int j = 0; j < 8; j++) {
					watermarkArray[i + j] = '0';
				}
				i += 8;
				for (int j = 0; j < 8; j++) {
					watermarkArray[i + j] = '1';
				}
				i += 8;
			}else if(each == 0){
				for (int j = 0; j < 16; j++) {
					watermarkArray[i + j] = '0';
				}
				i += 16;
			}else{
				for(int bit = 7; bit >= 0; --bit){
					if(i < watermarkArray.length){
						watermarkArray[i] = (byte) ((0x1&(each>>bit)) + 48);
					}else {
						break;
					}
					++i;
				}
			}
			if(i >= watermarkArray.length - 8)
				break;
		}
		if(i < watermarkArray.length - 8){
			for (int j = 0; j < 8; j++) {
				watermarkArray[i + j] = '1';
			}
		}
		File f = new File("temp\\watermark.txt");
		try(FileOutputStream fos = new FileOutputStream(f)){
			fos.write(watermarkArray);
		}catch(IOException e){
			e.printStackTrace();
		}
		return watermarkArray;
	}
	/**
	 * 将字节流转换为二进制bit流
	 * @param info 字节流的内容
	 * @return bit流
	 */
	public static byte[] transformToBitStream(byte[] info) {
		byte[] bitStream = new byte[(info.length-1)/8+1];
		int count = 0;
		int i = 0;
		for(; i < bitStream.length; ++i){
			for(int bit = 7; bit >= 0; --bit){
				if(count >= info.length)
					break;
				if(info[count] == '1')
					bitStream[i] += 0x1<<bit;
				++count;
			}
			if(count >= info.length)
				break;
			if(bitStream[i] == -1){
				break;
			}else if(bitStream[i] == 0){
				for(int bit = 7; bit >= 0; --bit){
					if(count >= info.length)
						break;
					if(info[count] == '1')
						bitStream[i] += 0x1<<bit;
					++count;
				}
				if(count >= info.length)
					break;
			}
		}
		byte[] bitStream1 = Arrays.copyOf(bitStream, i);
		return bitStream1;
	}
	
	/**
	 * 生成一张水印图片
	 * @param width  水印的宽度
	 * @param height  水印的高度
	 */
	public static void getWatermark(String content, int width, int height){
		String destPath = "temp\\watermark.png";
		File destFile = new File(destPath);	
		if(!destFile.getParentFile().exists()){
			destFile.getParentFile().mkdirs();
		}
		//创建width*height的二值图
		BufferedImage img = new BufferedImage(width,height, BufferedImage.TYPE_BYTE_BINARY);
	    Graphics2D g = img.createGraphics();
	    // 填充白色背景
	    g.setColor(Color.white);
	    g.fillRect(0, 0, img.getWidth(), img.getHeight());
	    //字体       
	    g.setFont(new Font("微软黑体", Font.PLAIN, 12));
	    g.setPaint(new Color(0, 0, 0));
	    int i;
	    for(i = 0; i * 5 + 5 < content.length() - 1; ++i){
	    	g.drawString(content.substring(i * 5, i * 5 + 5), 2, 12 + i * 12);
	    }
	    g.drawString(content.substring(i * 5), 2, 12 + i * 12);
	    g.dispose();
	    // 输出
	    try {
			ImageIO.write(img, "png", destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 生成一张默认大小的水印图片
	 * @param content  水印内容
	 */
	public static void getWatermark(String content){
		int width = 64, height = 64;
		String destPath = "temp\\watermark.png";
		File destFile = new File(destPath);	
		if(!destFile.getParentFile().exists()){
			destFile.getParentFile().mkdirs();
		}
		//创建64*64的二值图
		BufferedImage img = new BufferedImage(width,height, BufferedImage.TYPE_BYTE_BINARY);
	    Graphics2D g = img.createGraphics();
	    // 填充白色背景
	    g.setColor(Color.white);
	    g.fillRect(0, 0, img.getWidth(), img.getHeight());
	    //字体       
	    g.setFont(new Font("微软黑体", Font.PLAIN, 12));
	    g.setPaint(new Color(0, 0, 0));
	    int i;
	    for(i = 0; i * 5 + 5 < content.length() - 1; ++i){
	    	g.drawString(content.substring(i * 5, i * 5 + 5), 2, 12 + i * 12);
	    }
	    g.drawString(content.substring(i * 5), 2, 12 + i * 12);
	    g.dispose();
	    // 输出
	    try {
			ImageIO.write(img, "png", destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 生成一张默认的水印图片
	 */
	public static void getWatermark(){
		int width = 64, height = 64;
		String content = "测试用水印测试用水印测试用水印测试用水印测试用水印";
		String destPath = "temp\\watermark.png";
		File destFile = new File(destPath);	
		if(!destFile.getParentFile().exists()){
			destFile.getParentFile().mkdirs();
		}
		//创建64*64的二值图
		BufferedImage img = new BufferedImage(width,height, BufferedImage.TYPE_BYTE_BINARY);
	    Graphics2D g = img.createGraphics();
	    // 填充白色背景
	    g.setColor(Color.white);
	    g.fillRect(0, 0, img.getWidth(), img.getHeight());
	    //字体       
	    g.setFont(new Font("微软黑体", Font.PLAIN, 12));
	    g.setPaint(new Color(0, 0, 0));
	    int i;
	    for(i = 0; i * 5 + 5 < content.length() - 1; ++i){
	    	g.drawString(content.substring(i * 5, i * 5 + 5), 2, 12 + i * 12);
	    }
	    g.drawString(content.substring(i * 5), 2, 12 + i * 12);
	    g.dispose();
	    // 输出
	    try {
			ImageIO.write(img, "png", destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
