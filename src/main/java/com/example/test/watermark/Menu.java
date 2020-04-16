package com.example.test.watermark;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Menu {
	
	static{
        //加载opencv库
        System.load("C:\\Users\\ryan\\Desktop\\waterMarker\\src\\main\\resources\\lib\\opencv_java411.dll");
    }
	
	public static void main(String[] args) {
		_mainMenu();
	}
	
	public static void _mainMenu() {
		JFrame f = new JFrame("DigitalWatermark");
        f.setLayout(null);
        
        f.setSize(560, 440);
        f.setLocation(200, 200);
        
        JLabel total = new JLabel("数字水印系统");
        total.setBounds(135, 20, 300, 50);
        total.setFont(new Font("宋体", Font.BOLD, 40));
        f.add(total);
        
        int gap = 10;
        JPanel pEmbed = new JPanel();
        pEmbed.setLayout(new GridLayout(3, 2, gap, gap));
        pEmbed.setBounds(gap, 100, 500, 90);
         
        JLabel lImage =new JLabel("图片地址：");
        JLabel lWatermark =new JLabel("水印内容：");
        JLabel lP =new JLabel("嵌入强度：");
         
        JTextField tfImage = new JTextField("");
        JTextField tfWatermark = new JTextField("啦啦啦啊啦哈 ");
        JTextField tfP = new JTextField("40");
        
        pEmbed.add(lImage);
        pEmbed.add(tfImage);
        
        pEmbed.add(lWatermark);
        pEmbed.add(tfWatermark);
        
        pEmbed.add(lP);
        pEmbed.add(tfP);
        
        
        JPanel pExtract = new JPanel();
        pExtract.setLayout(new GridLayout(1, 2, gap, gap));
         
        JLabel lStegaImage =new JLabel("嵌入后图片的地址：");
        lStegaImage.setBounds(gap, 280, 250, 23);
         
        JTextField tfStegaImage = new JTextField();
        tfStegaImage.setBounds(gap + 250, 280, 250, 23);
        
        JButton browse1 = new JButton("...");
        browse1.setBounds(510, 100, 20, 22);
        JButton browse2 = new JButton("...");
        browse2.setBounds(510, 280, 20, 22);
     
        JButton b = new JButton("嵌入");
        b.setBounds(215, 210, 100, 30);
        pExtract.setBounds(gap, 275, 500, 30);
        
        JButton c = new JButton("提取");
        c.setBounds(285, 320, 100, 30);
        
        JButton d = new JButton("裁剪攻击");
        d.setBounds(145, 320, 100, 30);
        
        f.add(pEmbed);
        f.add(b);
        f.add(lStegaImage);
        f.add(tfStegaImage);
        f.add(c);
        f.add(d);
        f.add(browse1);
        f.add(browse2);
 
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        f.setVisible(true);
 
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String image = tfImage.getText();
            	File file = new File(image);
            	if(file.exists()){
            		String watermark = tfWatermark.getText();
	            	int p = Integer.parseInt(tfP.getText());
	            	String output = Start.embed(image, watermark, p);
	            	tfStegaImage.setText(output);
	            	JOptionPane.showMessageDialog(f, "嵌入后的地址：" + output);
            	}else{
            		JOptionPane.showMessageDialog(f, "请检查图片的地址");	
            	}
            }
        });
        
        c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String image = tfStegaImage.getText();
            	File file = new File(image);
            	if(file.exists()){
            		System.out.println("提取前已分配内存大小：" + Runtime.getRuntime().totalMemory()/1024/1024 + "M");
            		String output = Start.extract(image, 0.9);
	            	if(output == null)
	            		JOptionPane.showMessageDialog(f, "未提取出水印");
	            	else
	            		JOptionPane.showMessageDialog(f, "水印地址："+output);
	            	System.gc();
	            	System.out.println("已分配内存大小：" + Runtime.getRuntime().totalMemory()/1024/1024 + "M");
            	}else{
            		JOptionPane.showMessageDialog(f, "请检查嵌入后图片的地址");
            	}
            }
        });
        
        d.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String image = tfStegaImage.getText();
            	Attack.fixedPositionCutting(image, 0.9);
            	JOptionPane.showMessageDialog(f, "裁剪成功");
            }
        });
        
        browse1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		        JFileChooser jfc = new JFileChooser();
		        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		        jfc.showDialog(new JLabel(), "选择");
		        if(null != jfc.getSelectedFile()){
		        	File file=jfc.getSelectedFile();
		        	tfImage.setText(file.getAbsolutePath());
		        }
			}
		});
        
        browse2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		        JFileChooser jfc = new JFileChooser();
		        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		        jfc.showDialog(new JLabel(), "选择");
		        if(null != jfc.getSelectedFile()){
			        File file=jfc.getSelectedFile();
					tfStegaImage.setText(file.getAbsolutePath());
		        }
			}
		});
    }
	
}
