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
	 * ���λ�ñ��
	 * @param watermarkContent  ˮӡ����
	 * @return  ��ӱ�Ǻ��ˮӡ����
	 */
	public static byte[][] addLocationTag(byte[] watermarkContent, int contentWidth, int contentHeight, int contentWithTagWidth, int contentWithTagHeight) {
//		int contentWidth = 64;	//ˮӡ�Ŀ��
//		int contentHeight = 64;	//ˮӡ�ĸ߶�
//		int contentWithTagWidth = 160;	//��ӱ�Ǻ��ˮӡ���
//		int contentWithTagHeight = 120;	//��ӱ�Ǻ��ˮӡ�߶�
		
		//��ӱ��
		byte[][] watermarkContentWithTag = new byte[contentWithTagHeight][contentWithTagWidth];
		int lateralDistance = (contentWithTagWidth - contentWidth) / 2;	//��߾�
		int topDistance = (contentWithTagHeight - contentHeight) / 2;	//�������
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
	 * ��ˮӡ�Ķ�ֵͼת��Ϊһ��01byte����
	 * ˮӡ��ַ�� data\\GlobalDCT\\temporary\\watermark.png
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
	 * ��ˮӡ�Ķ�ֵͼת��Ϊһ��01byte����
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
	 * ��������bit��ת��Ϊһ��01byte����
	 * ��������"temp\\watermark.txt"
	 * @param content ������������
	 * @return �ֽ���
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
	 * ���ֽ���ת��Ϊ������bit��
	 * @param info �ֽ���������
	 * @return bit��
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
	 * ����һ��ˮӡͼƬ
	 * @param width  ˮӡ�Ŀ��
	 * @param height  ˮӡ�ĸ߶�
	 */
	public static void getWatermark(String content, int width, int height){
		String destPath = "temp\\watermark.png";
		File destFile = new File(destPath);	
		if(!destFile.getParentFile().exists()){
			destFile.getParentFile().mkdirs();
		}
		//����width*height�Ķ�ֵͼ
		BufferedImage img = new BufferedImage(width,height, BufferedImage.TYPE_BYTE_BINARY);
	    Graphics2D g = img.createGraphics();
	    // ����ɫ����
	    g.setColor(Color.white);
	    g.fillRect(0, 0, img.getWidth(), img.getHeight());
	    //����       
	    g.setFont(new Font("΢�����", Font.PLAIN, 12));
	    g.setPaint(new Color(0, 0, 0));
	    int i;
	    for(i = 0; i * 5 + 5 < content.length() - 1; ++i){
	    	g.drawString(content.substring(i * 5, i * 5 + 5), 2, 12 + i * 12);
	    }
	    g.drawString(content.substring(i * 5), 2, 12 + i * 12);
	    g.dispose();
	    // ���
	    try {
			ImageIO.write(img, "png", destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ����һ��Ĭ�ϴ�С��ˮӡͼƬ
	 * @param content  ˮӡ����
	 */
	public static void getWatermark(String content){
		int width = 64, height = 64;
		String destPath = "temp\\watermark.png";
		File destFile = new File(destPath);	
		if(!destFile.getParentFile().exists()){
			destFile.getParentFile().mkdirs();
		}
		//����64*64�Ķ�ֵͼ
		BufferedImage img = new BufferedImage(width,height, BufferedImage.TYPE_BYTE_BINARY);
	    Graphics2D g = img.createGraphics();
	    // ����ɫ����
	    g.setColor(Color.white);
	    g.fillRect(0, 0, img.getWidth(), img.getHeight());
	    //����       
	    g.setFont(new Font("΢�����", Font.PLAIN, 12));
	    g.setPaint(new Color(0, 0, 0));
	    int i;
	    for(i = 0; i * 5 + 5 < content.length() - 1; ++i){
	    	g.drawString(content.substring(i * 5, i * 5 + 5), 2, 12 + i * 12);
	    }
	    g.drawString(content.substring(i * 5), 2, 12 + i * 12);
	    g.dispose();
	    // ���
	    try {
			ImageIO.write(img, "png", destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ����һ��Ĭ�ϵ�ˮӡͼƬ
	 */
	public static void getWatermark(){
		int width = 64, height = 64;
		String content = "������ˮӡ������ˮӡ������ˮӡ������ˮӡ������ˮӡ";
		String destPath = "temp\\watermark.png";
		File destFile = new File(destPath);	
		if(!destFile.getParentFile().exists()){
			destFile.getParentFile().mkdirs();
		}
		//����64*64�Ķ�ֵͼ
		BufferedImage img = new BufferedImage(width,height, BufferedImage.TYPE_BYTE_BINARY);
	    Graphics2D g = img.createGraphics();
	    // ����ɫ����
	    g.setColor(Color.white);
	    g.fillRect(0, 0, img.getWidth(), img.getHeight());
	    //����       
	    g.setFont(new Font("΢�����", Font.PLAIN, 12));
	    g.setPaint(new Color(0, 0, 0));
	    int i;
	    for(i = 0; i * 5 + 5 < content.length() - 1; ++i){
	    	g.drawString(content.substring(i * 5, i * 5 + 5), 2, 12 + i * 12);
	    }
	    g.drawString(content.substring(i * 5), 2, 12 + i * 12);
	    g.dispose();
	    // ���
	    try {
			ImageIO.write(img, "png", destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
