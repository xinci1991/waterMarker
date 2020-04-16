package com.example.test.watermark;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class Block {
	int rows;
	int cols;
	Mat data;
	
	public Block() {
		this.rows = 8;
		this.cols = 8;
	}
	public Block(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
	}
	/**
	 * 输入一个单通道的mat矩阵
	 * @param data  单通道矩阵
	 */
	public void putData(Mat data) {
		this.data = data;
	}
	/**
	 * 获取矩阵中某一元素的值
	 * @param row  行
	 * @param col  列
	 * @return  元素的值
	 */
	public double getData(int row, int col) {
		double[] value = data.get(row, col);
		return value[0];
	}
	/**
	 * 修改矩阵中某一元素的值
	 * @param row  行
	 * @param col  列 
	 * @param value  修改后的值
	 */
	public void setData(int row, int col, double value) {
		data.put(row, col, value);
	}
	public void dct() {
		Core.dct(data, data);
	}
	public void idct() {
		Core.idct(data, data);
	}
	public String dump() {
		return data.dump();
	}
}
