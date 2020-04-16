package com.example.test.watermark;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Menu {
	
	static{
        //����opencv��
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
        
        JLabel total = new JLabel("����ˮӡϵͳ");
        total.setBounds(135, 20, 300, 50);
        total.setFont(new Font("����", Font.BOLD, 40));
        f.add(total);
        
        int gap = 10;
        JPanel pEmbed = new JPanel();
        pEmbed.setLayout(new GridLayout(3, 2, gap, gap));
        pEmbed.setBounds(gap, 100, 500, 90);
         
        JLabel lImage =new JLabel("ͼƬ��ַ��");
        JLabel lWatermark =new JLabel("ˮӡ���ݣ�");
        JLabel lP =new JLabel("Ƕ��ǿ�ȣ�");
         
        JTextField tfImage = new JTextField("");
        JTextField tfWatermark = new JTextField("������������ ");
        JTextField tfP = new JTextField("40");
        
        pEmbed.add(lImage);
        pEmbed.add(tfImage);
        
        pEmbed.add(lWatermark);
        pEmbed.add(tfWatermark);
        
        pEmbed.add(lP);
        pEmbed.add(tfP);
        
        
        JPanel pExtract = new JPanel();
        pExtract.setLayout(new GridLayout(1, 2, gap, gap));
         
        JLabel lStegaImage =new JLabel("Ƕ���ͼƬ�ĵ�ַ��");
        lStegaImage.setBounds(gap, 280, 250, 23);
         
        JTextField tfStegaImage = new JTextField();
        tfStegaImage.setBounds(gap + 250, 280, 250, 23);
        
        JButton browse1 = new JButton("...");
        browse1.setBounds(510, 100, 20, 22);
        JButton browse2 = new JButton("...");
        browse2.setBounds(510, 280, 20, 22);
     
        JButton b = new JButton("Ƕ��");
        b.setBounds(215, 210, 100, 30);
        pExtract.setBounds(gap, 275, 500, 30);
        
        JButton c = new JButton("��ȡ");
        c.setBounds(285, 320, 100, 30);
        
        JButton d = new JButton("�ü�����");
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
	            	JOptionPane.showMessageDialog(f, "Ƕ���ĵ�ַ��" + output);
            	}else{
            		JOptionPane.showMessageDialog(f, "����ͼƬ�ĵ�ַ");	
            	}
            }
        });
        
        c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String image = tfStegaImage.getText();
            	File file = new File(image);
            	if(file.exists()){
            		System.out.println("��ȡǰ�ѷ����ڴ��С��" + Runtime.getRuntime().totalMemory()/1024/1024 + "M");
            		String output = Start.extract(image, 0.9);
	            	if(output == null)
	            		JOptionPane.showMessageDialog(f, "δ��ȡ��ˮӡ");
	            	else
	            		JOptionPane.showMessageDialog(f, "ˮӡ��ַ��"+output);
	            	System.gc();
	            	System.out.println("�ѷ����ڴ��С��" + Runtime.getRuntime().totalMemory()/1024/1024 + "M");
            	}else{
            		JOptionPane.showMessageDialog(f, "����Ƕ���ͼƬ�ĵ�ַ");
            	}
            }
        });
        
        d.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String image = tfStegaImage.getText();
            	Attack.fixedPositionCutting(image, 0.9);
            	JOptionPane.showMessageDialog(f, "�ü��ɹ�");
            }
        });
        
        browse1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		        JFileChooser jfc = new JFileChooser();
		        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		        jfc.showDialog(new JLabel(), "ѡ��");
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
		        jfc.showDialog(new JLabel(), "ѡ��");
		        if(null != jfc.getSelectedFile()){
			        File file=jfc.getSelectedFile();
					tfStegaImage.setText(file.getAbsolutePath());
		        }
			}
		});
    }
	
}
