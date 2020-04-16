package com.example.test.watermark;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class WatermarkWithTag {
	byte[][] watermark;
	
	int leftSide;
	int rightSide;
	int topSide;
	int bottomSide;
	double errorRate;
	
	public WatermarkWithTag(byte[][] watermark, int leftSide, int rightSide, int topSide, int bottomSide) {
		this.watermark = watermark;
		this.leftSide = leftSide;
		this.rightSide = rightSide;
		this.topSide = topSide;
		this.bottomSide = bottomSide;
		int count = 0;
		for(int i = 0; i < topSide; ++i){
			for(int j = leftSide; j <= rightSide; ++j){
				if(watermark[i][j] == '1'){
					++count;
				}
			}
		}
		for(int i = topSide; i <= bottomSide; ++i){
			for(int j = 0; j < leftSide; ++j){
				if(watermark[i][j] == '1'){
					++count;
				}
			}
		}
		for(int i = topSide; i <= bottomSide; ++i){
			for(int j = rightSide + 1; j < watermark[0].length; ++j){
				if(watermark[i][j] == '1'){
					++count;
				}
			}
		}
		for(int i = bottomSide + 1; i < watermark.length; ++i){
			for(int j = leftSide; j <= rightSide; ++j){
				if(watermark[i][j] == '1'){
					++count;
				}
			}
		}
		this.errorRate = 1.0 * count / ((topSide + watermark.length - 1 - bottomSide) * (rightSide - leftSide + 1) + (leftSide + watermark[0].length - 1 - rightSide) * (bottomSide - topSide + 1));
	}
	
	public int rows() {
		return bottomSide - topSide + 1;
	}
	public int cols() {
		return rightSide - leftSide + 1;
	}
	
	public Mat getWatermark() {
		Mat imgOut = new Mat(bottomSide - topSide + 1, rightSide - leftSide + 1, CvType.CV_8UC3);
		double[] a = new double[]{0, 0, 0};
		double[] b = new double[]{255, 255, 255};
		for(int i = 0; i <= bottomSide - topSide + 1; ++i){
			for(int j = 0; j <= rightSide - leftSide + 1; ++j){
				if(watermark[i + topSide][j + leftSide] == '1'){
					imgOut.put(i, j, a);
				}else{
					imgOut.put(i, j, b);
				}
			}
		}
		return imgOut;
	}
}
