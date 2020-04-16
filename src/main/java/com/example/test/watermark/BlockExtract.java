package com.example.test.watermark;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class BlockExtract {
	
	static int watermarkWidth = 32;
	static int watermarkHeight = 32;
	static int watermarkWithTagWidth = 60;
	static int watermarkWithTagHeight = 60;
//	static int[][] watermarkSizeSet = {{180, 120}, {100, 70}};
	static double errorRange = 0.8;	//标记检测区域最大可接受误差率
	static int detectionAreaWidth = 8;	//标记检测区域的块数
	static int detectionAreaHeight = 8;	//标记检测区域的块数
	
	/**
	 * 提取水印,抵抗等比例裁剪攻击,返回值是水印图片
	 * @param mode 提取模式(0：抵抗缩放、压缩、涂抹攻击,默认模式;1：比默认模式多一个抵抗固定比例剪切攻击提取)
	 * @return
	 */
	public static String startExtractN(String srcPath, double proportion) {
		final int referencePointX = 2; // 第一个参照点的X轴坐标,可改,修改时需同时修改嵌入函数中的值
		final int referencePointY = 2; // 第一个参照点的Y轴坐标,可改,修改时需同时修改嵌入函数中的值
//		String watermarkPath = "data\\BlockDCT\\"
//				+ srcPath.substring(0, srcPath.lastIndexOf('.')).substring(srcPath.lastIndexOf('\\') + 1)
//				+ "_watermark.jpg";
		String watermarkPath = srcPath.substring(0, srcPath.lastIndexOf('.')) + "_watermark.jpg";
		Mat imgMat = Imgcodecs.imread(srcPath);
		imgMat = watermarkExtractN(imgMat, referencePointX, referencePointY, proportion);
		if(imgMat == null){
			return null;
		}
		Imgcodecs.imwrite(watermarkPath, imgMat);
		return watermarkPath;
	}
	/**
	 * 提取水印,抵抗等比例裁剪攻击,返回值是水印图片的二进制流
	 */
	public static byte[] startExtractN(byte[] img, double proportion) {
		final int referencePointX = 2; // 第一个参照点的X轴坐标,可改,修改时需同时修改嵌入函数中的值
		final int referencePointY = 2; // 第一个参照点的Y轴坐标,可改,修改时需同时修改嵌入函数中的值
		MatOfByte bm = new MatOfByte(img);
		Mat imgMat = Imgcodecs.imdecode(bm, Imgcodecs.IMREAD_COLOR);
		imgMat = watermarkExtractN(imgMat, referencePointX, referencePointY, proportion);
		if(imgMat == null){
			return null;
		}
		Imgcodecs.imencode(".jpg", imgMat, bm);
		return bm.toArray();
	}
	/**
	 * 提取水印,抵抗等比例裁剪攻击,返回值是嵌入信息的二进制bit流
	 */
	public static byte[] startExtractNReturnBitStream(String srcPath, double proportion) {
		final int referencePointX = 2; // 第一个参照点的X轴坐标,可改,修改时需同时修改嵌入函数中的值
		final int referencePointY = 2; // 第一个参照点的Y轴坐标,可改,修改时需同时修改嵌入函数中的值
		Mat imgMat = Imgcodecs.imread(srcPath);
		imgMat = watermarkExtractN(imgMat, referencePointX, referencePointY, proportion);
		if(imgMat == null){
			return null;
		}
		byte[] info = WatermarkProcess.transformToBitStream(WatermarkProcess.transformToArrary(imgMat));
		return info;
	}
	/**
	 * 提取水印,抵抗等比例裁剪攻击,返回值是嵌入信息的二进制bit流
	 */
	public static byte[] startExtractNReturnBitStream(byte[] img, double proportion) {
		final int referencePointX = 2; // 第一个参照点的X轴坐标,可改,修改时需同时修改嵌入函数中的值
		final int referencePointY = 2; // 第一个参照点的Y轴坐标,可改,修改时需同时修改嵌入函数中的值
		MatOfByte bm = new MatOfByte(img);
		Mat imgMat = Imgcodecs.imdecode(bm, Imgcodecs.IMREAD_COLOR);
		imgMat = watermarkExtractN(imgMat, referencePointX, referencePointY, proportion);
		if(imgMat == null){
			return null;
		}
		byte[] info = WatermarkProcess.transformToBitStream(WatermarkProcess.transformToArrary(imgMat));
		return info;
	}
	
	/**
	 * 提取水印,可以抵抗固定比例的裁剪
	 * @param imgMat 宿主图像矩阵
	 * @param referencePointX 第一个参照点的X轴坐标
	 * @param referencePointY 第一个参照点的Y轴坐标
	 * @param proportion 裁剪的比例(面积比)
	 * @return 水印图像矩阵
	 */
	public static Mat watermarkExtractN(Mat imgMat, int referencePointX, int referencePointY, double proportion) {
//		int watermarkWithTagWidth = 160;//含标记的水印的宽度,不可改
//		int watermarkWithTagHeight = 120;//含标记的水印的高度,不可改
		final int blockWidth = 8; // 每一个分块的宽度,不可改
		final int blockHeight = 8; // 每一个分块的高度,不可改
		int scaleDimensionWidth = watermarkWithTagWidth * 8; // 图片处理时的缩放宽度,不可改
		int scaleDimensionHeight = watermarkWithTagHeight * 8; // 图像处理时的缩放高度,不可改

		
		// 处理原图像 
		Imgproc.cvtColor(imgMat, imgMat, Imgproc.COLOR_BGR2YCrCb);
		ArrayList<Mat> allChannels = new ArrayList<>();
		Core.split(imgMat, allChannels);
		Mat yMat = allChannels.get(0);
		yMat.convertTo(yMat, CvType.CV_32FC1);
		// 获取嵌入点位置列表
		final int blocksRows = scaleDimensionHeight / blockHeight;
		final int blocksCols = scaleDimensionWidth / blockWidth;
		int[][] pointList;
		if(null == (pointList = getPointList("temp\\pointList.csv"))){
			int numOfBitInBlock = watermarkWithTagHeight * watermarkWithTagWidth / blocksRows / blocksCols;
			pointList = BlockEmbed.embeddedPosition(referencePointX, referencePointY, scaleDimensionHeight, scaleDimensionWidth, 1, numOfBitInBlock * 2);
		}
		//攻击检测
		int angle = 0;
		boolean haveFound = false;
		while(angle < 360){
			//读取标记
//			final double errorRange = 0.85;	//标记检测区域最大可接受误差率
//			final int detectionAreaWidth = 12;	//标记检测区域的块数
//			final int detectionAreaHeight = 8;	//标记检测区域的块数
			final int total = detectionAreaWidth * detectionAreaHeight;	////标记检测区域的总块数
			final int rx = pointList[0][0], ry = pointList[0][1];	//参照点坐标
			final int ex = pointList[1][0], ey = pointList[1][1];	//提取点坐标
			Mat tempMat = yMat.clone();
			
			//--------------------------------------------------------------------
//			Mat tempMat = new Mat(yMat.rows(), yMat.cols(), CvType.CV_32FC1);
//			yMat.copyTo(tempMat);
			//--------------------------------------------------------------------
			
			Imgproc.resize(tempMat, tempMat, new Size(scaleDimensionWidth, scaleDimensionHeight));
			int tl = 0, tr = 0;	//左上角和右上角标记正确的个数
			int rightDetectionAreaStartCol = tempMat.cols() - tempMat.cols() % 8 - 8 * detectionAreaWidth;	//右侧标记检测区域的开始位置
			for(int i = 0; i < detectionAreaHeight; ++i){
				for(int j = 0; j < detectionAreaWidth; ++j){
					Mat block = tempMat.submat(i * 8, i * 8 + 8, j * 8, j * 8 + 8);
					Core.dct(block, block);
					if(block.get(rx, ry)[0] < block.get(ex, ey)[0]){
						++tl;
					}
					Core.idct(block, block);
					block = tempMat.submat(i * 8, i * 8 + 8, rightDetectionAreaStartCol + j * 8, rightDetectionAreaStartCol + j * 8 + 8);
					Core.dct(block, block);
					if(block.get(rx, ry)[0] < block.get(ex, ey)[0]){
						++tr;
					}
					Core.idct(block, block);
				}
			}
			int count = 0;	//满足要求的黑色的标记检测区域的片数,大于等于2视为找到了要提取的水印区域,选择3作为界限是为了减轻标记检测区域受涂抹攻击的影响
			if(1.0 * tl / total > errorRange)
				++count;
			if(1.0 * tr / total > errorRange)
				++count;
			if(count > 0){
				int bl = 0, br = 0;
				int bottomDetectionAreaStartRow = tempMat.rows() - tempMat.rows() %8 - 8 * detectionAreaHeight;	//底部标记检测区域的开始位置
				for(int i = 0; i < detectionAreaHeight; ++i){
					for(int j = 0; j < detectionAreaWidth; ++j){
						Mat block = tempMat.submat(bottomDetectionAreaStartRow + i * 8, bottomDetectionAreaStartRow + i * 8 + 8, j * 8, j * 8 + 8);
						Core.dct(block, block);
						if(block.get(rx, ry)[0] < block.get(ex, ey)[0]){
							++bl;
						}
						block = tempMat.submat(bottomDetectionAreaStartRow + i * 8, bottomDetectionAreaStartRow + i * 8 + 8, rightDetectionAreaStartCol + j * 8, rightDetectionAreaStartCol + j * 8 + 8);
						Core.dct(block, block);
						if(block.get(rx, ry)[0] > block.get(ex, ey)[0]){
							++br;
						}
					}
				}
				if((1.0 * bl / total > errorRange))
					++count;
				if((count > 1) && (1.0 * br / total > errorRange)){
					haveFound = true;
					break;
				}else{
					angle += 90;
					yMat = yMat.t();
					Core.flip(yMat, yMat, 1);
				}
			}else{
				angle += 90;
				yMat = yMat.t();
				Core.flip(yMat, yMat, 1);
			}
		}
		if(!haveFound && proportion != 1){
			for(int tune = 0; !haveFound && tune < 4; ++tune){
				angle = 0;
				while(angle < 360){
					//读取标记
//					final double errorRange = 0.85;	//标记检测区域最大可接受误差率
//					final int detectionAreaWidth = 12;	//标记检测区域的块数
//					final int detectionAreaHeight = 8;	//标记检测区域的块数
					final int total = detectionAreaWidth * detectionAreaHeight;	////标记检测区域的总块数
					final int rx = pointList[0][0], ry = pointList[0][1];	//参照点坐标
					final int ex = pointList[1][0], ey = pointList[1][1];	//提取点坐标
//					final int fillAreaRows = (watermarkWithTagHeight - (int)(watermarkWithTagHeight * Math.sqrt(proportion))) / 2 * 8 + 8;
//					final int fillAreaCols = (watermarkWithTagWidth - (int)(watermarkWithTagWidth * Math.sqrt(proportion))) / 2 * 8 + 8; 	//填充区域的大体范围
					Mat tempMat = yMat.clone();
					
					//--------------------------
					int rows = (int) (watermarkWithTagHeight * Math.sqrt(proportion));
					int cols = (int) (watermarkWithTagWidth * Math.sqrt(proportion));
					int deltaRow = (watermarkWithTagHeight - rows) / 2 + 1;
					int deltaCol = (watermarkWithTagWidth - cols) / 2 + 1;
					final int fillAreaRows = deltaRow * 8;
					final int fillAreaCols = deltaCol * 8; 	//填充区域的大体范围
					//--------------------------
					
					tempMat = fixedPositionRecovery(tempMat, proportion, tune);
					System.out.println("填充：" + fillAreaRows + "  " + fillAreaCols);//**************************************
					Imgproc.resize(tempMat, tempMat, new Size(scaleDimensionWidth, scaleDimensionHeight));
					System.out.println("图像：" + tempMat.rows() + "  " + tempMat.cols());//*************************************
					int tl = 0, tr = 0;	//左上角和右上角标记正确的个数
					int rightDetectionAreaStartCol = tempMat.cols() - tempMat.cols() % 8 - 8 * detectionAreaWidth;	//右侧标记检测区域的开始位置
					for(int i = 0; i < detectionAreaHeight; ++i){
						for(int j = 0; j < detectionAreaWidth; ++j){
							Mat block = tempMat.submat(i * 8 + fillAreaRows, i * 8 + 8 + fillAreaRows, j * 8 + fillAreaCols, j * 8 + 8 + fillAreaCols);
							Core.dct(block, block);
							if(block.get(rx, ry)[0] < block.get(ex, ey)[0]){
								++tl;
							}
							block = tempMat.submat(i * 8 + fillAreaRows, i * 8 + 8 + fillAreaRows, rightDetectionAreaStartCol + j * 8 - fillAreaCols, rightDetectionAreaStartCol + j * 8 + 8 - fillAreaCols);
							Core.dct(block, block);
							if(block.get(rx, ry)[0] < block.get(ex, ey)[0]){
								++tr;
							}
						}
					}
					System.out.println("左上角和右上角标记正确的个数:" + tl + "  " + tr);//*************************************************
					int count = 0;	//满足要求的黑色的标记检测区域的片数,大于等于2视为找到了要提取的水印区域,选择3作为界限是为了减轻标记检测区域受涂抹攻击的影响
					if(1.0 * tl / total > errorRange)
						++count;
					if(1.0 * tr / total > errorRange)
						++count;
					if(count > 0){
						int bl = 0, br = 0;
						int bottomDetectionAreaStartRow = tempMat.rows() - tempMat.rows() %8 - 8 * detectionAreaHeight;	//底部标记检测区域的开始位置
						for(int i = 0; i < detectionAreaHeight; ++i){
							for(int j = 0; j < detectionAreaWidth; ++j){
								Mat block = tempMat.submat(bottomDetectionAreaStartRow + i * 8 - fillAreaRows, bottomDetectionAreaStartRow + i * 8 + 8 - fillAreaRows, j * 8 + fillAreaCols, j * 8 + 8 + fillAreaCols);
								Core.dct(block, block);
								if(block.get(rx, ry)[0] < block.get(ex, ey)[0]){
									++bl;
								}
								block = tempMat.submat(bottomDetectionAreaStartRow + i * 8 - fillAreaRows, bottomDetectionAreaStartRow + i * 8 + 8 - fillAreaRows, rightDetectionAreaStartCol + j * 8 - fillAreaCols, rightDetectionAreaStartCol + j * 8 + 8 - fillAreaCols);
								Core.dct(block, block);
								if(block.get(rx, ry)[0] > block.get(ex, ey)[0]){
									++br;
								}
							}
						}
						System.out.println("左下角和右下角标记正确的个数:" + bl + "  " + br);//*************************************************
						if((1.0 * bl / total > errorRange))
							++count;
						if((count > 1) && (1.0 * br / total > errorRange)){
							haveFound = true;
							yMat = fixedPositionRecovery(yMat, proportion, tune);
							break;
						}else{
							angle += 90;
							yMat = yMat.t();
							Core.flip(yMat, yMat, 1);
						}
					}else{
						angle += 90;
						yMat = yMat.t();
						Core.flip(yMat, yMat, 1);
					}
				}
			}
			
		}
		if(!haveFound){
			return null;
		}
		Imgproc.resize(yMat, yMat, new Size(scaleDimensionWidth, scaleDimensionHeight));
		// 分块
		Block[][] blocks = new Block[blocksRows][blocksCols];
		for (int i = 0; i < blocksRows; ++i) {
			for (int j = 0; j < blocksCols; ++j) {
				blocks[i][j] = new Block(blockHeight, blockWidth);
				blocks[i][j].putData(yMat.submat(i * blockHeight, i * blockHeight + blockHeight, j * blockWidth,
						j * blockWidth + blockWidth));
			}
		}
		
		//提取水印
		byte[][] watermark = extract(blocks, pointList);
		//输出水印
//		Mat imgOut = new Mat(watermark.length, watermark[0].length, CvType.CV_8UC3);
//		for(int i = 0; i < imgOut.rows(); ++i){
//			for(int j = 0; j < imgOut.cols(); ++j){
//				if(watermark[i][j] == '1'){
//					double[] a = new double[]{0, 0, 0};
//					imgOut.put(i, j, a);
//				}else{
//					double[] a = new double[]{255, 255, 255};
//					imgOut.put(i, j, a);
//				}
//			}
//		}
		Mat imgOut = new Mat(watermarkHeight, watermarkWidth, CvType.CV_8UC3);
		for(int i = 0; i < watermarkHeight; ++i){
			for(int j = 0; j < watermarkWidth; ++j){
				if(watermark[i+(watermarkWithTagHeight-watermarkHeight)/2][j+(watermarkWithTagWidth-watermarkWidth)/2] == '1'){
					double[] a = new double[]{0, 0, 0};
					imgOut.put(i, j, a);
				}else{
					double[] a = new double[]{255, 255, 255};
					imgOut.put(i, j, a);
				}
			}
		}
		return imgOut;
	}
	
	
	/**
	 * 提取水印
	 * @param blocks 所有的块
	 * @param pointList 嵌入点位置
	 * @return 带标记的水印
	 */
	public static byte[][] extract(Block[][] blocks, int[][] pointList) {
		int orderOfSquareMat = (int) Math.sqrt(1.0 * pointList.length / 2);
		byte[][] watermark = new byte[orderOfSquareMat * blocks.length][orderOfSquareMat * blocks[0].length];
		for(int i = 0; i < blocks.length; ++i){
			for(int j = 0; j < blocks[0].length; ++j){
				for(int count = 0; count < pointList.length / 2; ++count){
					blocks[i][j].dct();
					double a = blocks[i][j].getData(pointList[count * 2][0], pointList[count * 2][1]);			//参照点的值
					double b = blocks[i][j].getData(pointList[count * 2 + 1][0], pointList[count * 2 + 1][1]);	//嵌入点的值
					if(a < b){
						watermark[i * orderOfSquareMat + count / orderOfSquareMat][j * orderOfSquareMat + count % orderOfSquareMat] = '1';
					}else{
						watermark[i * orderOfSquareMat + count / orderOfSquareMat][j * orderOfSquareMat + count % orderOfSquareMat] = '0';
					}
				}
			}
		}
		return watermark;
	}
	
	
	/**
	 * 从文件中提取一个int类型的二维数组
	 * 
	 * @param srcPath
	 *            储存数组的csv文件地址
	 * @param numOfInfo
	 *            数组中储存的数据的数目
	 * @return 返回这个二维数组
	 */
	public static int[][] getPointList(String srcPath) {
		File f = new File(srcPath);
		int[][] pointList = null;
		if (f.exists()) {
			try{
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				int count = 0;
				br.mark(0);
				while (br.readLine() != null) {
					++count;
				}
				pointList = new int[count][];
				br.close();
				fr.close();
				fr = new FileReader(f);
				br = new BufferedReader(fr);
				count = 0;
				while (true) {
					String line = br.readLine();
					if (line == null)
						break;
					line.trim();
					String[] temp = line.split(",");
					int[] data = new int[] {Integer.parseInt(temp[0]), Integer.parseInt(temp[1])};
					pointList[count] = data;
					++count;
				}
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return pointList;
	}
	
	/**
	 * 将固定比例剪切后的图片恢复至原大小
	 * @param img  图像矩阵
	 * @param proportion  裁剪比例
	 * @param tune 略微调整恢复后的图片的边长,0:不调整;1:高度加一;2：宽度加一;3:宽高都加一
	 * @return 恢复后的图像矩阵
	 */
	public static Mat fixedPositionRecovery(Mat img, double proportion, int tune) {
		int rows = (int) (img.rows() / Math.sqrt(proportion) - 0.00001) + 1 + tune % 2;
		int cols = (int) (img.cols() / Math.sqrt(proportion) - 0.00001) + 1 + tune / 2;
		int deltaRow = (rows - img.rows()) / 2;
		int deltaCol = (cols - img.cols()) / 2;
		Scalar elem = new Scalar(0);
		Mat imgout = new Mat(rows, cols, CvType.CV_32FC1, elem);
		img.copyTo(imgout.submat(deltaRow, deltaRow + img.rows(), deltaCol, deltaCol + img.cols()));
		System.out.println("修复范围："+deltaRow+"  "+(deltaRow + img.rows())+"  "+deltaCol+"  "+(deltaCol + img.cols()));
		return imgout;
	}
	
	/**
	 * 检测水印的正文的边缘并提取水印
	 * @param mat 待提取的矩阵
	 * @param pointList 参照点和嵌入点的位置
	 * @param watermarkRows 不含标记的水印的高度
	 * @param watermarkCols 不含标记的水印的宽度
	 * @return 提取出来的水印
	 */
	public static WatermarkWithTag extractS(Mat mat, int[][] pointList, int watermarkRows, int watermarkCols) {
		int rpx = pointList[0][0], rpy = pointList[0][1];
		int epx = pointList[1][0], epy = pointList[1][1];
		int blocksRows = mat.rows() / 8;
		int blocksCols = mat.cols() / 8;
		byte[][] watermark = new byte[blocksRows][blocksCols];
		
		boolean[][] colQueue = new boolean[blocksCols][8];	//是否为0,false表示不为0
		int[] colQueueP = new int[blocksCols];	//队列中最后一位的位置
		int[] numOf0InColQueue = new int[blocksCols];	//队列中'0'的数目
		boolean[] stopSearchSide = new boolean[blocksCols];	//是否停止找上下边
		boolean[] searchBottomSide = new boolean[blocksCols];	//正在找下边的边吗
		
		int[][] leftSide = new int[blocksRows][2];	//原水印左边的边的位置
		int[][] rightSide = new int[blocksRows][2];	//原水印右边的边的位置
		int[][] topSide = new int[blocksCols][2];	//原水印上边的边的位置
		int[][] bottomSide = new int[blocksCols][2];	//原水印下边的边的位置
		
		for(int i = 0; i < blocksRows; ++i){
			boolean[] rowQueue = new boolean[8];	//是否为0,false表示不为0
			int rowQueueP = 0, numOf1InRowQueue = 8;	//队列中最后一位的位置和队列中'1'的数目
			boolean continueSearchSide = true;	//是否继续找左右边
			boolean searchRightSide = false;	//正在找右边的边吗
			for(int j = 0; j < blocksCols; ++j){
				Mat block = mat.submat(i * 8, i * 8 + 8, j * 8, j * 8 + 8);
				Core.dct(block, block);
				double[] a = block.get(rpx, rpy);
				double[] b = block.get(epx, epy);
				if(a[0] < b[0]){
					watermark[i][j] = '1';
					rowQueueP = (rowQueueP + 1) % 8;
					if(continueSearchSide && rowQueue[rowQueueP]){
						++numOf1InRowQueue;
						rowQueue[rowQueueP] = false;
					}
					colQueueP[j] = (colQueueP[j] + 1) % 8;
					if(!stopSearchSide[j] && colQueue[j][colQueueP[j]]){
						--numOf0InColQueue[j];
						colQueue[j][colQueueP[j]] = false;
					}
				}else{
					watermark[i][j] = '0';
					rowQueueP = (rowQueueP + 1) % 8;
					if(continueSearchSide && !rowQueue[rowQueueP]){
						--numOf1InRowQueue;
						rowQueue[rowQueueP] = true;
					}
					colQueueP[j] = (colQueueP[j] + 1) % 8;
					if(!stopSearchSide[j] && !colQueue[j][colQueueP[j]]){
						++numOf0InColQueue[j];
						colQueue[j][colQueueP[j]] = true;
					}
				}
				if(continueSearchSide && !searchRightSide && numOf1InRowQueue < 6){
					if(j > 8){
						for(int i1 = 0; i1 < blocksRows; ++i1){
							if(leftSide[i1][0] == j - 3){
								++leftSide[i1][1];
								break;
							}else if(leftSide[i1][0] == 0){
								leftSide[i1][0] = j - 3;
								++leftSide[i1][1];
								break;
							}
						}
						if(i < watermarkRows){
							searchRightSide = true;
							Arrays.fill(rowQueue, true);
							numOf1InRowQueue = 0;
						}else{
							continueSearchSide = false;
						}
					}else if(i < 8){
						searchRightSide = true;
						Arrays.fill(rowQueue, true);
						numOf1InRowQueue = 0;
					}else{
						continueSearchSide = false;
					}
				}else if(continueSearchSide && searchRightSide && numOf1InRowQueue >= 6){
					if(j > watermarkCols){
						for(int i1 = 0; i1 < blocksRows; ++i1){
							if(rightSide[i1][0] == j - 5){
								++rightSide[i1][1];
								break;
							}else if(rightSide[i1][0] == 0){
								rightSide[i1][0] = j - 5;
								++rightSide[i1][1];
								break;
							}
						}
						continueSearchSide = false;
					}
				}
				if(!stopSearchSide[j] && !searchBottomSide[j] && numOf0InColQueue[j] > 2){
					if(i > 8){
						for(int i1 = 0; i1 < blocksCols; ++i1){
							if(topSide[i1][0] == i - 3){
								++topSide[i1][1];
								break;
							}else if(topSide[i1][0] == 0){
								topSide[i1][0] = i - 3;
								++topSide[i1][1];
								break;
							}
						}
						if(j < watermarkCols){
							searchBottomSide[j] = true;
							Arrays.fill(colQueue[j], true);
							numOf0InColQueue[j] = 8;
						}else{
							stopSearchSide[j] = true;
						}
					}else if(j < 8){
						searchBottomSide[j] = true;
						Arrays.fill(colQueue[j], true);
						numOf0InColQueue[j] = 8;
					}else{
						stopSearchSide[j] = true;
					}
				}else if(!stopSearchSide[j] && searchBottomSide[j] && numOf0InColQueue[j] <= 2){
					if(i > watermarkRows){
						for(int i1 = 0; i1 < blocksCols; ++i1){
							if(bottomSide[i1][0] == i - 5){
								++bottomSide[i1][1];
								break;
							}else if(bottomSide[i1][0] == 0){
								bottomSide[i1][0] = i - 5;
								++bottomSide[i1][1];
								break;
							}
						}
						stopSearchSide[j] = true;
					}
				}
				
			}
		}
		int keyL = 0, secondKeyL = 0;
		int keyR = 0;
		int keyT = 0, secondKeyT = 0;
		int keyB = 0;
		for(int i = 0; i < leftSide.length; ++i){
			if(leftSide[i][0] == 0){
				break;
			}else if(leftSide[keyL][1] <= leftSide[i][1]){
				secondKeyL = keyL;
				keyL = i;
			}
		}
		for(int i = 0; i < rightSide.length; ++i){
			if(rightSide[i][0] == 0){
				break;
			}else if(rightSide[keyR][1] < rightSide[i][1]){
				keyR = i;
			}
		}
		for(int i = 0; i < topSide.length; ++i){
			if(topSide[i][0] == 0){
				break;
			}else if(topSide[keyT][1] <= topSide[i][1]){
				secondKeyT = keyT;
				keyT = i;
			}
		}
		for(int i = 0; i < bottomSide.length; ++i){
			if(bottomSide[i][0] == 0){
				break;
			}else if(bottomSide[keyB][1] < bottomSide[i][1]){
				keyB = i;
			}
		}
//		System.out.println("key值: " + leftSide[keyL][0] + "  " + rightSide[keyR][0] + "  " + topSide[keyT][0] + "  " + bottomSide[keyB][0]);
		if(rightSide[keyR][0] - leftSide[keyL][0] + 1 < watermarkCols - 5){
			if(leftSide[keyL][1] > leftSide[secondKeyL][1] * 3 && leftSide[keyL][0] + watermarkCols < blocksCols){
				rightSide[keyR][0] = leftSide[keyL][0] + watermarkCols;
			}else{
				return null;
			}
		}
		if(bottomSide[keyB][0] - topSide[keyT][0] + 1 < watermarkRows - 5){
			if(topSide[keyT][1] > topSide[secondKeyT][1] * 3 && topSide[keyT][0] + watermarkRows < blocksRows){
				bottomSide[keyB][0] = topSide[keyT][0] + watermarkRows;
			}else{
				return null;
			}
		}
		return new WatermarkWithTag(watermark, leftSide[keyL][0] + 1, rightSide[keyR][0] - 1, topSide[keyT][0] + 1, bottomSide[keyB][0] - 1);
	}
	
}
