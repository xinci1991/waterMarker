package com.example.test.watermark;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class Attack {
	
	/**
	 * ��ͼƬ�Թ̶������ü�
	 * @param scrPath  ͼƬ���λ��
	 * @param proportion  �ü�����
	 */
	public static void fixedPositionCutting(String scrPath, double proportion) {
		Mat img = Imgcodecs.imread(scrPath);
		int rows = (int) (img.rows() * Math.sqrt(proportion));
		int cols = (int) (img.cols() * Math.sqrt(proportion));
		int deltaRow = (img.rows() - rows) / 2;
		int deltaCol = (img.cols() - cols) / 2;
		img = img.submat(deltaRow, deltaRow + rows, deltaCol, deltaCol + cols);
		System.out.println("���з�Χ��"+deltaRow+"  "+(deltaRow + rows)+"  "+deltaCol+"  "+(deltaCol + cols));
		Imgcodecs.imwrite(scrPath, img);
	}
	public static byte[] fixedPositionCutting(byte[] imgStream, double proportion) {
		MatOfByte bm = new MatOfByte(imgStream);
		Mat img = Imgcodecs.imdecode(bm, Imgcodecs.IMREAD_COLOR);
		int rows = (int) (img.rows() * Math.sqrt(proportion));
		int cols = (int) (img.cols() * Math.sqrt(proportion));
		int deltaRow = (img.rows() - rows) / 2;
		int deltaCol = (img.cols() - cols) / 2;
		img = img.submat(deltaRow, deltaRow + rows, deltaCol, deltaCol + cols);
		Imgcodecs.imencode(".jpg", img, bm);
		return bm.toArray();
	}
	
	
}
