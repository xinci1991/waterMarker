package com.example.test.watermark;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class BlockEmbed {
	
	static int watermarkWidth = 32;
	static int watermarkHeight = 32;
	static int watermarkWithTagWidth = 60;
	static int watermarkWithTagHeight = 60;
	
	/**
	 * 获取宿主图像和水印
	 * @param srcPath 宿主图像的保存地址
	 * @param watermarkContent 水印字符串的内容
	 * @param p 嵌入强度
	 * @return 嵌入水印后的宿主图像的保存地址
	 */
	public static String startEmbed(String srcPath, String watermarkContent, int p) {
//		int watermarkWidth = 64;			//水印宽度,可改不超过160
//		int watermarkHeight = 64;			//水印高度,可改不超过120
		int referencePointX = 2;			//第一个参照点的X轴坐标,可改,修改时需同时修改提取函数中的值
		int referencePointY = 2;			//第一个参照点的Y轴坐标,可改,修改时需同时修改提取函数中的值
//		String destPath = "data\\BlockDCT\\" + srcPath.substring(0,srcPath.lastIndexOf(".")).substring(srcPath.lastIndexOf("\\")+1) + "_out" + srcPath.substring(srcPath.lastIndexOf("."));
		String destPath = srcPath.substring(0, srcPath.lastIndexOf(".")) + "_out" + srcPath.substring(srcPath.lastIndexOf("."));
		WatermarkProcess.getWatermark(watermarkContent, watermarkWidth, watermarkHeight);
		//读取二维水印数组
		byte[][] watermark = WatermarkProcess.addLocationTag(WatermarkProcess.transformToArrary(), watermarkWidth, watermarkHeight, watermarkWithTagWidth, watermarkWithTagHeight);
		Mat imgMat = Imgcodecs.imread(srcPath);
		imgMat = watermarkEmbed(imgMat, watermark, referencePointX, referencePointY, p);
		Imgcodecs.imwrite(destPath, imgMat);
		return destPath;
	}
	/**
	 * 获取宿主图像和水印
	 * @param srcPath 宿主图像的二进制流
	 * @param watermarkContent 水印字符串的内容
	 * @param p 嵌入强度
	 * @return 嵌入水印后的宿主图像的二进制流
	 */
	public static byte[] startEmbed(byte[] img, String watermarkContent, int p) {
//		int watermarkWidth = 64;			//水印宽度,可改不超过160
//		int watermarkHeight = 64;			//水印高度,可改不超过120
		int referencePointX = 2;			//第一个参照点的X轴坐标,可改,修改时需同时修改提取函数中的值
		int referencePointY = 2;			//第一个参照点的Y轴坐标,可改,修改时需同时修改提取函数中的值
		WatermarkProcess.getWatermark(watermarkContent, watermarkWidth, watermarkHeight);
		//读取二维水印数组
		byte[][] watermark = WatermarkProcess.addLocationTag(WatermarkProcess.transformToArrary(), watermarkWidth, watermarkHeight, watermarkWithTagWidth, watermarkWithTagHeight);
		MatOfByte bm = new MatOfByte(img);
		Mat imgMat = Imgcodecs.imdecode(bm, Imgcodecs.IMREAD_COLOR);
		imgMat = watermarkEmbed(imgMat, watermark, referencePointX, referencePointY, p);
		Imgcodecs.imencode(".jpg", imgMat, bm);
		return bm.toArray();
	}
	/**
	 * 获取宿主图像和水印
	 * @param srcPath 宿主图像的保存地址
	 * @param watermarkContent 水印内容的二进制流
	 * @param p 嵌入强度
	 * @return 嵌入水印后的宿主图像的保存地址
	 */
	public static String startEmbed(String srcPath, byte[] watermarkContent, int p) {
//		int watermarkWidth = 64;			//水印宽度,可改不超过160
//		int watermarkHeight = 64;			//水印高度,可改不超过120
		int referencePointX = 2;			//第一个参照点的X轴坐标,可改,修改时需同时修改提取函数中的值
		int referencePointY = 2;			//第一个参照点的Y轴坐标,可改,修改时需同时修改提取函数中的值
//		String destPath = "data\\BlockDCT\\" + srcPath.substring(0,srcPath.lastIndexOf(".")).substring(srcPath.lastIndexOf("\\")+1) + "_out" + srcPath.substring(srcPath.lastIndexOf("."));
		String destPath = srcPath.substring(0, srcPath.lastIndexOf(".")) + "_out" + srcPath.substring(srcPath.lastIndexOf("."));
		//读取二维水印数组
		byte[][] watermark = WatermarkProcess.addLocationTag(WatermarkProcess.transformToArrary(watermarkContent, watermarkWidth, watermarkHeight), watermarkWidth, watermarkHeight, watermarkWithTagWidth, watermarkWithTagHeight);
		Mat imgMat = Imgcodecs.imread(srcPath);
		imgMat = watermarkEmbed(imgMat, watermark, referencePointX, referencePointY, p);
		Imgcodecs.imwrite(destPath, imgMat);
		return destPath;
	}
	/**
	 * 获取宿主图像和水印
	 * @param srcPath 宿主图像的二进制流
	 * @param watermarkContent 水印内容的二进制流
	 * @param p 嵌入强度
	 * @return 嵌入水印后的宿主图像的二进制流
	 */
	public static byte[] startEmbed(byte[] img, byte[] watermarkContent, int p) {
//		int watermarkWidth = 64;			//水印宽度,可改不超过160
//		int watermarkHeight = 64;			//水印高度,可改不超过120
		int referencePointX = 2;			//第一个参照点的X轴坐标,可改,修改时需同时修改提取函数中的值
		int referencePointY = 2;			//第一个参照点的Y轴坐标,可改,修改时需同时修改提取函数中的值
		//读取二维水印数组
		byte[][] watermark = WatermarkProcess.addLocationTag(WatermarkProcess.transformToArrary(watermarkContent, watermarkWidth, watermarkHeight), watermarkWidth, watermarkHeight, watermarkWithTagWidth, watermarkWithTagHeight);
		MatOfByte bm = new MatOfByte(img);
		Mat imgMat = Imgcodecs.imdecode(bm, Imgcodecs.IMREAD_COLOR);
		imgMat = watermarkEmbed(imgMat, watermark, referencePointX, referencePointY, p);
		Imgcodecs.imencode(".jpg", imgMat, bm);
		return bm.toArray();
	}
	
	/**
	 * 对图像进行处理,以满足嵌入的要求
	 * @param imgMat 宿主图片的图像矩阵
	 * @param destPath 嵌入水印后的宿主图像的保存地址
	 * @param referencePointX 第一个参照点的X轴坐标
	 * @param referencePointY 第一个参照点的Y轴坐标
	 * @param p 嵌入强度
	 */
	public static Mat watermarkEmbed(Mat imgMat, byte[][] watermark, int referencePointX, int referencePointY, int p){
		final int blockWidth = 8;				//每一个分块的宽度,暂不可改
		final int blockHeight = 8;				//每一个分块的高度,暂不可改
		final int scaleDimensionWidth = watermarkWithTagWidth * 8;	//图片处理时的缩放宽度,不可改
		final int scaleDimensionHeight = watermarkWithTagHeight * 8;	//图像处理时的缩放高度,不可改
		
		
		//获取并处理原图像
		boolean flag = false;
		if(imgMat.rows() > imgMat.cols()){
			imgMat = imgMat.t();
			Core.flip(imgMat, imgMat, 1);
			flag = true;
		}
		final int originalMatRows = imgMat.rows();
		final int originalMatCols = imgMat.cols();
		Imgproc.resize(imgMat, imgMat, new Size(scaleDimensionWidth, scaleDimensionHeight));
		Imgproc.cvtColor(imgMat, imgMat, Imgproc.COLOR_BGR2YCrCb);
		ArrayList<Mat> allChannels = new ArrayList<>();
		Core.split(imgMat, allChannels);
		Mat yMat = allChannels.get(0);
		yMat.convertTo(yMat, CvType.CV_32FC1);
		//分块
		final int blocksRows = scaleDimensionHeight / blockHeight;
		final int blocksCols = scaleDimensionWidth / blockWidth;
		Block[][] blocks = new Block[blocksRows][blocksCols];
		for(int i = 0; i < blocksRows; ++i){
			for(int j = 0; j < blocksCols; ++j){
				blocks[i][j] = new Block(blockHeight, blockWidth);
				blocks[i][j].putData(yMat.submat(i * blockHeight, i * blockHeight + blockHeight, j * blockWidth, j * blockWidth + blockWidth));
			}
		}
		//获取嵌入点位置列表
		int numOfBitInBlock = watermark.length * watermark[0].length / blocksRows / blocksCols;
		int[][] pointList = embeddedPosition(referencePointX, referencePointY, scaleDimensionHeight, scaleDimensionWidth, 1, numOfBitInBlock * 2);
		//嵌入水印
		embed(blocks, watermark, pointList, p);
		//输出嵌入水印的图片
		yMat.convertTo(yMat, CvType.CV_8UC1);
		Core.merge(allChannels, imgMat);
		Imgproc.cvtColor(imgMat, imgMat, Imgproc.COLOR_YCrCb2BGR);
		Imgproc.resize(imgMat, imgMat, new Size(originalMatCols, originalMatRows));
		if(flag){
			Core.flip(imgMat, imgMat, 1);
			imgMat = imgMat.t();
		}
		return imgMat;
	}
	
	/**
	 * 嵌入水印
	 * @param blocks  所有的分块
	 * @param watermark  二维水印数组
	 * @param pointList  二维嵌入点位置列表
	 * @param p  嵌入强度
	 */
	public static void embed(Block[][] blocks, byte[][] watermark, int[][] pointList, int p) {
		int orderOfSquareMat = (int) Math.sqrt(1.0 * pointList.length / 2);
		for(int i = 0; i < blocks.length; ++i){
			for(int j = 0; j < blocks[0].length; ++j){
				blocks[i][j].dct();
				for(int count = 0; count < pointList.length / 2; ++count){
					byte temp = watermark[i * orderOfSquareMat + count / orderOfSquareMat][j * orderOfSquareMat + count % orderOfSquareMat];
					double a = blocks[i][j].getData(pointList[count * 2][0], pointList[count * 2][1]);			//参照点的值
					double b = blocks[i][j].getData(pointList[count * 2 + 1][0], pointList[count * 2 + 1][1]);	//嵌入点的值
					if(temp == '0'){
						if(b < a + p){
							blocks[i][j].setData(pointList[count * 2 + 1][0], pointList[count * 2 + 1][1], a+p);
						}
					}else{
						if(b > a - p){
							blocks[i][j].setData(pointList[count * 2 + 1][0], pointList[count * 2 + 1][1], a-p);
						}
					}
				}
				blocks[i][j].idct();
			}
		}
	}
	
	/**
	 * 按照z型扫描的顺序，从初始点开始，以一定步长获取点的位置信息
	 * 并将结果保存到"temp\\pointList.csv"中
	 * 可以把初始点设为(0,0),步长设为1来获取整个矩阵的位置和z型扫描得到的数列的序号的对应关系
	 * @param startPointX  初始点的X轴坐标
	 * @param startPointY  初始点的Y轴坐标
	 * @param rows  原图矩阵的行数
	 * @param cols  原图矩阵的列数
	 * @param step  步长
	 * @param numOfPoint  需要获取的点的总数
	 * @return  返回所有嵌入点位置坐标的二维矩阵(其中第一行为对照点坐标)
	 */
	public static int[][] embeddedPosition(int startPointX, int startPointY, int rows, int cols, int step, int numOfPoint){
		int[][] pointList = new int[numOfPoint][2];
		//zigzag扫描
		int nextPoint = -1;	//下一个加入列表中的点的序号,序号指的是z型扫描得到的一维数组中,某个元素是第几个,比如对一个8*8的矩阵(1,2)上的元素序号为7
		int remainPoint = numOfPoint;	//还要在获取几个点,初始点也算一个点
		int i = 0,j = 0;			//矩阵中当前位置的坐标
		int count = 0;				//矩阵中当前位置的序号
		boolean move_right_up = true;	//判断向右上移动还是向左下移动
		while(remainPoint > 0){
			if(i == startPointX && j == startPointY){
				pointList[0][0] = startPointX;
				pointList[0][1] = startPointY;
				--remainPoint;
				nextPoint = count + step;
			}
			if(count == nextPoint){
				pointList[numOfPoint - remainPoint][0] = i;
				pointList[numOfPoint - remainPoint][1] = j;
				--remainPoint;
				nextPoint += step;
			}
			if(move_right_up){
				if(i == 0){
					move_right_up = false;
					if(j == cols - 1){
						++i;
					}else{
						++j;
					}
				}else if(j == cols - 1){
					move_right_up = false;
					++i;
				}else{
					--i;
					++j;
				}
			}else{
				if(j == 0){
					move_right_up = true;
					if(i == rows - 1){
						++j;
					}else{
						++i;
					}
				}else if(i == rows - 1){
					move_right_up = true;
					++j;
				}else{
					--j;
					++i;
				}
			}
			++count;
		}
		File f = new File("temp\\pointList.csv");
		try(
				FileWriter fw = new FileWriter(f);
				PrintWriter pw = new PrintWriter(fw);
		){
			for(int[] each : pointList){
					pw.println(each[0] + "," + each[1]);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return pointList;
	}
	
}
