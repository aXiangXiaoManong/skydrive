package com.ax.springmvc.util.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

public class FileOption {

	// �����ٶȿ�-�ļ������ַ���--�ļ�ֱ����
	public static void copy1(String oldPath, String newPath) {
		BufferedWriter bfw = null;
		BufferedReader bfr = null;
		try {
			bfr = new BufferedReader(new FileReader(oldPath));
			bfw = new BufferedWriter(new FileWriter(newPath));
			String str = null;
			while ((str = bfr.readLine()) != null) {
				bfw.write(str);
				bfw.newLine();
				bfw.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bfw != null) {
				try {
					bfw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bfr != null) {
				try {
					bfr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * �����ļ�--��ȫ
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public void copy(String oldPath, String newPath) {
		try {
			FileInputStream in = new FileInputStream(oldPath);
			File file = new File(newPath);
			if (!file.exists())
				file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			int c;
			byte buffer[] = new byte[1024];
			while ((c = in.read(buffer)) != -1) {
				for (int i = 0; i < c; i++)
					out.write(buffer[i]);
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// �ֽ������쳣
	public void a(String oldPath, String newPath) {
		try {
			// ��װ����Դ
			InputStreamReader isr = new InputStreamReader(new FileInputStream(oldPath));
			// ��װĿ�ĵ�
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(newPath));

			// ��д����
			char[] chs = new char[1024];
			int len = 0;
			while ((len = isr.read(chs)) != -1) {
				osw.write(chs, 0, len);
			}

			// �ͷ���Դ
			osw.close();
			isr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param s Դ�ļ�
	 * @param t	���Ƶ������ļ�
	 */
	public void fileChannelCopy(File s, File t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();
			out = fo.getChannel();
			in.transferTo(0, in.size(), out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				fo.close();
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
