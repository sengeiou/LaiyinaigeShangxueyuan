package com.baselib.util;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * 应用程序异常类：用于捕获异常和提示错误信息
 * @author huguoyin
 * 2014-1-24 上午11:47:23
 */
public class AppExceptionUtils extends Exception{

	private static final long serialVersionUID = 1L;

	private final static boolean Debug = true;//是否保存错误日志
	
	/** 定义异常类型 */
	public final static byte TYPE_NETWORK 	= 0x01;
	public final static byte TYPE_SOCKET	= 0x02;
	public final static byte TYPE_HTTP_CODE	= 0x03;
	public final static byte TYPE_HTTP_ERROR= 0x04;
	public final static byte TYPE_XML	 	= 0x05;
	public final static byte TYPE_IO	 	= 0x06;
	public final static byte TYPE_RUN	 	= 0x07;
	
	private byte type;
	private int code;

	private AppExceptionUtils(){
	}
	
	private AppExceptionUtils(byte type, int code, Exception excp) {
		super(excp);
		this.type = type;
		this.code = code;		
		if(Debug){
			this.saveErrorLog(excp);
		}
	}
	public int getCode() {
		return this.code;
	}
	public int getType() {
		return this.type;
	}
	
	
	/**
	 * 保存异常日志
	 * @param excp
	 */
	public void saveErrorLog(Exception excp) {
		String errorlog = "ysLog.txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			//判断是否挂载了SD卡
			String storageState = Environment.getExternalStorageState();		
			if(storageState.equals(Environment.MEDIA_MOUNTED)){
				savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ys/httpLog/";
				File file = new File(savePath);
				if(!file.exists()){
					file.mkdirs();
				}
				logFilePath = savePath + errorlog;
			}
			//没有挂载SD卡，无法写文件
			if(logFilePath == ""){
				return;
			}
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fw = new FileWriter(logFile,true);
			pw = new PrintWriter(fw);
			pw.println("--------------------"+(new Date().toString())+"---------------------");	
			excp.printStackTrace(pw);
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();		
		}finally{ 
			if(pw != null){ pw.close(); } 
			if(fw != null){ try { fw.close(); } catch (IOException e) { }}
		}
	}

	/**
	 * 保存异常日志
	 * @param excp
	 */
	public void saveErrorLog(String excp) {
		String errorlog = "ysLog.txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			//判断是否挂载了SD卡
			String storageState = Environment.getExternalStorageState();
			if(storageState.equals(Environment.MEDIA_MOUNTED)){
				savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ys/httpLog/";
				File file = new File(savePath);
				if(!file.exists()){
					file.mkdirs();
				}
				logFilePath = savePath + errorlog;
			}
			//没有挂载SD卡，无法写文件
			if(logFilePath == ""){
				return;
			}
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fw = new FileWriter(logFile,true);
			pw = new PrintWriter(fw);
			pw.println("--------------------"+(new Date().toString())+"---------------------");
			pw.println(excp);
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(pw != null){ pw.close(); }
			if(fw != null){ try { fw.close(); } catch (IOException e) { }}
		}
	}
	
	public static AppExceptionUtils http(int code) {
		return new AppExceptionUtils(TYPE_HTTP_CODE, code, null);
	}
	
	public static AppExceptionUtils http(Exception e) {
		return new AppExceptionUtils(TYPE_HTTP_ERROR, 0 ,e);
	}

	public static AppExceptionUtils socket(Exception e) {
		return new AppExceptionUtils(TYPE_SOCKET, 0 ,e);
	}
	
	public static AppExceptionUtils io(Exception e) {
		if(e instanceof UnknownHostException || e instanceof ConnectException){
			return new AppExceptionUtils(TYPE_NETWORK, 0, e);
		}
		else if(e instanceof IOException){
			return new AppExceptionUtils(TYPE_IO, 0 ,e);
		}
		return run(e);
	}
	
	public static AppExceptionUtils xml(Exception e) {
		return new AppExceptionUtils(TYPE_XML, 0, e);
	}
	
	public static AppExceptionUtils network(Exception e) {
		if(e instanceof UnknownHostException || e instanceof ConnectException){
			return new AppExceptionUtils(TYPE_NETWORK, 0, e);
		}
		else if(e instanceof SocketException){
			return socket(e);
		}
		return http(e);
	}
	
	public static AppExceptionUtils run(Exception e) {
		return new AppExceptionUtils(TYPE_RUN, 0, e);
	}

	/**
	 * 获取APP异常崩溃处理对象
	 * @return
	 */
	public static AppExceptionUtils getAppExceptionHandler(){
		return new AppExceptionUtils();
	}

}
