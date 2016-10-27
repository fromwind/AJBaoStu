package com.udit.aijiabao.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udit.aijiabao.R;
import com.udit.aijiabao.dialog.PromptDialog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipExtractorTask extends AsyncTask<Void, Integer, Long> {
	private final String TAG = "ZipExtractorTask";
	private final File mInput;
	private final File mOutput;
	private PromptDialog mDialog;
	PromptDialog.Builder builder;
	private ProgressBar mProgress;
	TextView speed;
	private final Context mContext;
	private boolean mReplaceAll;
	public ZipExtractorTask(String in, String out, Context context, boolean replaceAll){
		super();
		mInput = new File(in);
		mOutput = new File(out);
		if(!mOutput.exists()){
			if(!mOutput.mkdirs()){
				Log.e(TAG, "Failed to make directories:"+mOutput.getAbsolutePath());
			}
		}
		if(context!=null){
			// 构造软件下载对话框
			builder = new PromptDialog.Builder(context);
			builder.setTitle("正在解压");
			// 给下载对话框增加进度条
			final LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.softupdate_progress, null);
			mProgress = (ProgressBar) v.findViewById(R.id.update_progress1);
			speed = (TextView) v.findViewById(R.id.networkspeed1);
			speed.setVisibility(View.GONE);
			builder.setView(v);
			builder.setButton1("隐藏", new PromptDialog.OnClickListener() {
				@Override
				public void onClick(Dialog dialog, int which) {
					dialog.dismiss();
				}
			});
			mDialog = builder.create();
		}
		else{
			mDialog = null;
		}
		mContext = context;
		mReplaceAll = replaceAll;
	}
	@Override
	protected Long doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return unzip();
	}
	
	@Override
	protected void onPostExecute(Long result) {
		// TODO Auto-generated method stub
		//super.onPostExecute(result);
		if(mDialog!=null&&mDialog.isShowing()){
			mDialog.dismiss();
		}
		if(isCancelled())
			return;

	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		//super.onPreExecute();
		if(mDialog!=null){
			mDialog.show();
		}
	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		//super.onProgressUpdate(values);
		if(mProgress==null)
			return;
		if(values.length>1){
			int max=values[1];
			mProgress.setMax(max);
		}
		else
			mProgress.setProgress(values[0].intValue());
	}
	private long unzip(){
		long extractedSize = 0L;
		Enumeration<ZipEntry> entries;
		ZipFile zip = null;
		try {
			zip = new ZipFile(mInput);
			long uncompressedSize = getOriginalSize(zip);
			publishProgress(0, (int) uncompressedSize);
			
			entries = (Enumeration<ZipEntry>) zip.entries();
			while(entries.hasMoreElements()){
				ZipEntry entry = entries.nextElement();
				if(entry.isDirectory()){
					continue;
				}
				File destination = new File(mOutput, entry.getName());
				if(!destination.getParentFile().exists()){
					Log.e(TAG, "make="+destination.getParentFile().getAbsolutePath());
					destination.getParentFile().mkdirs();
				}
				if(destination.exists()&&mContext!=null&&!mReplaceAll){
					
				}
				ProgressReportingOutputStream outStream = new ProgressReportingOutputStream(destination);
				extractedSize+=copy(zip.getInputStream(entry),outStream);
				outStream.close();
			}
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				zip.close();
				mInput.delete();
				if (!mInput.exists()){
					Log.e("bum_delete_zip",mInput+"success!");
				}else Log.e("bum_delete_zip",mInput+"false!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return extractedSize;
	}

	private long getOriginalSize(ZipFile file){
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) file.entries();
		long originalSize = 0l;
		while(entries.hasMoreElements()){
			ZipEntry entry = entries.nextElement();
			if(entry.getSize()>=0){
				originalSize+=entry.getSize();
			}
		}
		return originalSize;
	}
	
	private int copy(InputStream input, OutputStream output){
		byte[] buffer = new byte[1024*64];
		BufferedInputStream in = new BufferedInputStream(input, 1024*64);
		BufferedOutputStream out  = new BufferedOutputStream(output, 1024*64);
		int count =0,n=0;
		try {
			while((n=in.read(buffer, 0, 1024*64))!=-1){
				out.write(buffer, 0, n);
				count+=n;
			}
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}
	int i;
	private final class ProgressReportingOutputStream extends FileOutputStream{

		public ProgressReportingOutputStream(File file)
				throws FileNotFoundException {
			super(file);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void write(byte[] buffer, int byteOffset, int byteCount)
				throws IOException {
			// TODO Auto-generated method stub
			super.write(buffer, byteOffset, byteCount);
		    i += byteCount;
		    publishProgress(i);
		}
		
	}
}