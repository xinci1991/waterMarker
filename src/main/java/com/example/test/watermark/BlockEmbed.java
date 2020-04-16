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
	 * ��ȡ����ͼ���ˮӡ
	 * @param srcPath ����ͼ��ı����ַ
	 * @param watermarkContent ˮӡ�ַ���������
	 * @param p Ƕ��ǿ��
	 * @return Ƕ��ˮӡ�������ͼ��ı����ַ
	 */
	public static String startEmbed(String srcPath, String watermarkContent, int p) {
//		int watermarkWidth = 64;			//ˮӡ���,�ɸĲ�����160
//		int watermarkHeight = 64;			//ˮӡ�߶�,�ɸĲ�����120
		int referencePointX = 2;			//��һ�����յ��X������,�ɸ�,�޸�ʱ��ͬʱ�޸���ȡ�����е�ֵ
		int referencePointY = 2;			//��һ�����յ��Y������,�ɸ�,�޸�ʱ��ͬʱ�޸���ȡ�����е�ֵ
//		String destPath = "data\\BlockDCT\\" + srcPath.substring(0,srcPath.lastIndexOf(".")).substring(srcPath.lastIndexOf("\\")+1) + "_out" + srcPath.substring(srcPath.lastIndexOf("."));
		String destPath = srcPath.substring(0, srcPath.lastIndexOf(".")) + "_out" + srcPath.substring(srcPath.lastIndexOf("."));
		WatermarkProcess.getWatermark(watermarkContent, watermarkWidth, watermarkHeight);
		//��ȡ��άˮӡ����
		byte[][] watermark = WatermarkProcess.addLocationTag(WatermarkProcess.transformToArrary(), watermarkWidth, watermarkHeight, watermarkWithTagWidth, watermarkWithTagHeight);
		Mat imgMat = Imgcodecs.imread(srcPath);
		imgMat = watermarkEmbed(imgMat, watermark, referencePointX, referencePointY, p);
		Imgcodecs.imwrite(destPath, imgMat);
		return destPath;
	}
	/**
	 * ��ȡ����ͼ���ˮӡ
	 * @param srcPath ����ͼ��Ķ�������
	 * @param watermarkContent ˮӡ�ַ���������
	 * @param p Ƕ��ǿ��
	 * @return Ƕ��ˮӡ�������ͼ��Ķ�������
	 */
	public static byte[] startEmbed(byte[] img, String watermarkContent, int p) {
//		int watermarkWidth = 64;			//ˮӡ���,�ɸĲ�����160
//		int watermarkHeight = 64;			//ˮӡ�߶�,�ɸĲ�����120
		int referencePointX = 2;			//��һ�����յ��X������,�ɸ�,�޸�ʱ��ͬʱ�޸���ȡ�����е�ֵ
		int referencePointY = 2;			//��һ�����յ��Y������,�ɸ�,�޸�ʱ��ͬʱ�޸���ȡ�����е�ֵ
		WatermarkProcess.getWatermark(watermarkContent, watermarkWidth, watermarkHeight);
		//��ȡ��άˮӡ����
		byte[][] watermark = WatermarkProcess.addLocationTag(WatermarkProcess.transformToArrary(), watermarkWidth, watermarkHeight, watermarkWithTagWidth, watermarkWithTagHeight);
		MatOfByte bm = new MatOfByte(img);
		Mat imgMat = Imgcodecs.imdecode(bm, Imgcodecs.IMREAD_COLOR);
		imgMat = watermarkEmbed(imgMat, watermark, referencePointX, referencePointY, p);
		Imgcodecs.imencode(".jpg", imgMat, bm);
		return bm.toArray();
	}
	/**
	 * ��ȡ����ͼ���ˮӡ
	 * @param srcPath ����ͼ��ı����ַ
	 * @param watermarkContent ˮӡ���ݵĶ�������
	 * @param p Ƕ��ǿ��
	 * @return Ƕ��ˮӡ�������ͼ��ı����ַ
	 */
	public static String startEmbed(String srcPath, byte[] watermarkContent, int p) {
//		int watermarkWidth = 64;			//ˮӡ���,�ɸĲ�����160
//		int watermarkHeight = 64;			//ˮӡ�߶�,�ɸĲ�����120
		int referencePointX = 2;			//��һ�����յ��X������,�ɸ�,�޸�ʱ��ͬʱ�޸���ȡ�����е�ֵ
		int referencePointY = 2;			//��һ�����յ��Y������,�ɸ�,�޸�ʱ��ͬʱ�޸���ȡ�����е�ֵ
//		String destPath = "data\\BlockDCT\\" + srcPath.substring(0,srcPath.lastIndexOf(".")).substring(srcPath.lastIndexOf("\\")+1) + "_out" + srcPath.substring(srcPath.lastIndexOf("."));
		String destPath = srcPath.substring(0, srcPath.lastIndexOf(".")) + "_out" + srcPath.substring(srcPath.lastIndexOf("."));
		//��ȡ��άˮӡ����
		byte[][] watermark = WatermarkProcess.addLocationTag(WatermarkProcess.transformToArrary(watermarkContent, watermarkWidth, watermarkHeight), watermarkWidth, watermarkHeight, watermarkWithTagWidth, watermarkWithTagHeight);
		Mat imgMat = Imgcodecs.imread(srcPath);
		imgMat = watermarkEmbed(imgMat, watermark, referencePointX, referencePointY, p);
		Imgcodecs.imwrite(destPath, imgMat);
		return destPath;
	}
	/**
	 * ��ȡ����ͼ���ˮӡ
	 * @param srcPath ����ͼ��Ķ�������
	 * @param watermarkContent ˮӡ���ݵĶ�������
	 * @param p Ƕ��ǿ��
	 * @return Ƕ��ˮӡ�������ͼ��Ķ�������
	 */
	public static byte[] startEmbed(byte[] img, byte[] watermarkContent, int p) {
//		int watermarkWidth = 64;			//ˮӡ���,�ɸĲ�����160
//		int watermarkHeight = 64;			//ˮӡ�߶�,�ɸĲ�����120
		int referencePointX = 2;			//��һ�����յ��X������,�ɸ�,�޸�ʱ��ͬʱ�޸���ȡ�����е�ֵ
		int referencePointY = 2;			//��һ�����յ��Y������,�ɸ�,�޸�ʱ��ͬʱ�޸���ȡ�����е�ֵ
		//��ȡ��άˮӡ����
		byte[][] watermark = WatermarkProcess.addLocationTag(WatermarkProcess.transformToArrary(watermarkContent, watermarkWidth, watermarkHeight), watermarkWidth, watermarkHeight, watermarkWithTagWidth, watermarkWithTagHeight);
		MatOfByte bm = new MatOfByte(img);
		Mat imgMat = Imgcodecs.imdecode(bm, Imgcodecs.IMREAD_COLOR);
		imgMat = watermarkEmbed(imgMat, watermark, referencePointX, referencePointY, p);
		Imgcodecs.imencode(".jpg", imgMat, bm);
		return bm.toArray();
	}
	
	/**
	 * ��ͼ����д���,������Ƕ���Ҫ��
	 * @param imgMat ����ͼƬ��ͼ�����
	 * @param destPath Ƕ��ˮӡ�������ͼ��ı����ַ
	 * @param referencePointX ��һ�����յ��X������
	 * @param referencePointY ��һ�����յ��Y������
	 * @param p Ƕ��ǿ��
	 */
	public static Mat watermarkEmbed(Mat imgMat, byte[][] watermark, int referencePointX, int referencePointY, int p){
		final int blockWidth = 8;				//ÿһ���ֿ�Ŀ��,�ݲ��ɸ�
		final int blockHeight = 8;				//ÿһ���ֿ�ĸ߶�,�ݲ��ɸ�
		final int scaleDimensionWidth = watermarkWithTagWidth * 8;	//ͼƬ����ʱ�����ſ��,���ɸ�
		final int scaleDimensionHeight = watermarkWithTagHeight * 8;	//ͼ����ʱ�����Ÿ߶�,���ɸ�
		
		
		//��ȡ������ԭͼ��
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
		//�ֿ�
		final int blocksRows = scaleDimensionHeight / blockHeight;
		final int blocksCols = scaleDimensionWidth / blockWidth;
		Block[][] blocks = new Block[blocksRows][blocksCols];
		for(int i = 0; i < blocksRows; ++i){
			for(int j = 0; j < blocksCols; ++j){
				blocks[i][j] = new Block(blockHeight, blockWidth);
				blocks[i][j].putData(yMat.submat(i * blockHeight, i * blockHeight + blockHeight, j * blockWidth, j * blockWidth + blockWidth));
			}
		}
		//��ȡǶ���λ���б�
		int numOfBitInBlock = watermark.length * watermark[0].length / blocksRows / blocksCols;
		int[][] pointList = embeddedPosition(referencePointX, referencePointY, scaleDimensionHeight, scaleDimensionWidth, 1, numOfBitInBlock * 2);
		//Ƕ��ˮӡ
		embed(blocks, watermark, pointList, p);
		//���Ƕ��ˮӡ��ͼƬ
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
	 * Ƕ��ˮӡ
	 * @param blocks  ���еķֿ�
	 * @param watermark  ��άˮӡ����
	 * @param pointList  ��άǶ���λ���б�
	 * @param p  Ƕ��ǿ��
	 */
	public static void embed(Block[][] blocks, byte[][] watermark, int[][] pointList, int p) {
		int orderOfSquareMat = (int) Math.sqrt(1.0 * pointList.length / 2);
		for(int i = 0; i < blocks.length; ++i){
			for(int j = 0; j < blocks[0].length; ++j){
				blocks[i][j].dct();
				for(int count = 0; count < pointList.length / 2; ++count){
					byte temp = watermark[i * orderOfSquareMat + count / orderOfSquareMat][j * orderOfSquareMat + count % orderOfSquareMat];
					double a = blocks[i][j].getData(pointList[count * 2][0], pointList[count * 2][1]);			//���յ��ֵ
					double b = blocks[i][j].getData(pointList[count * 2 + 1][0], pointList[count * 2 + 1][1]);	//Ƕ����ֵ
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
	 * ����z��ɨ���˳�򣬴ӳ�ʼ�㿪ʼ����һ��������ȡ���λ����Ϣ
	 * ����������浽"temp\\pointList.csv"��
	 * ���԰ѳ�ʼ����Ϊ(0,0),������Ϊ1����ȡ���������λ�ú�z��ɨ��õ������е���ŵĶ�Ӧ��ϵ
	 * @param startPointX  ��ʼ���X������
	 * @param startPointY  ��ʼ���Y������
	 * @param rows  ԭͼ���������
	 * @param cols  ԭͼ���������
	 * @param step  ����
	 * @param numOfPoint  ��Ҫ��ȡ�ĵ������
	 * @return  ��������Ƕ���λ������Ķ�ά����(���е�һ��Ϊ���յ�����)
	 */
	public static int[][] embeddedPosition(int startPointX, int startPointY, int rows, int cols, int step, int numOfPoint){
		int[][] pointList = new int[numOfPoint][2];
		//zigzagɨ��
		int nextPoint = -1;	//��һ�������б��еĵ�����,���ָ����z��ɨ��õ���һά������,ĳ��Ԫ���ǵڼ���,�����һ��8*8�ľ���(1,2)�ϵ�Ԫ�����Ϊ7
		int remainPoint = numOfPoint;	//��Ҫ�ڻ�ȡ������,��ʼ��Ҳ��һ����
		int i = 0,j = 0;			//�����е�ǰλ�õ�����
		int count = 0;				//�����е�ǰλ�õ����
		boolean move_right_up = true;	//�ж��������ƶ������������ƶ�
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
