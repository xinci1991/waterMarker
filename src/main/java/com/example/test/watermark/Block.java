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
	 * ����һ����ͨ����mat����
	 * @param data  ��ͨ������
	 */
	public void putData(Mat data) {
		this.data = data;
	}
	/**
	 * ��ȡ������ĳһԪ�ص�ֵ
	 * @param row  ��
	 * @param col  ��
	 * @return  Ԫ�ص�ֵ
	 */
	public double getData(int row, int col) {
		double[] value = data.get(row, col);
		return value[0];
	}
	/**
	 * �޸ľ�����ĳһԪ�ص�ֵ
	 * @param row  ��
	 * @param col  �� 
	 * @param value  �޸ĺ��ֵ
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
