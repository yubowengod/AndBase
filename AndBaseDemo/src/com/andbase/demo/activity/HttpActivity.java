package com.andbase.demo.activity;

import java.io.File;
import java.net.URLEncoder;

import org.apache.http.protocol.HTTP;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import com.ab.http.AbBinaryHttpResponseListener;
import com.ab.http.AbFileHttpResponseListener;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbImageUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.progress.AbHorizontalProgressBar;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;
/**
 * 名称：HttpActivity
 * 描述：Http框架
 * @author 还如一梦中
 * @date 2011-12-13
 * @version
 */
public class HttpActivity extends AbActivity {
	
	/** The Constant TAG. */
    private static final String TAG = "HttpActivity";
	
	private MyApplication application;
	
	private AbHttpUtil mAbHttpUtil = null;
	
	// ProgressBar进度控制
	private AbHorizontalProgressBar mAbProgressBar;
	// 最大100
	private int max = 100;	
	private int progress = 0;
	private TextView numberText, maxText;
	private DialogFragment  mAlertDialog  = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setAbContentView(R.layout.http_main);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.http_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
	    
        application = (MyApplication)abApplication;
        Button getBtn  = (Button)this.findViewById(R.id.getBtn);
        Button postBtn  = (Button)this.findViewById(R.id.postBtn);
        Button byteBtn  = (Button)this.findViewById(R.id.byteBtn);
        Button fileDownBtn  = (Button)this.findViewById(R.id.fileBtn);
        Button fileUploadBtn  = (Button)this.findViewById(R.id.fileUploadBtn);
        
        //获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(10000);
        //get请求
        getBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// 一个url地址
				String urlString = "http://www.amsoft.cn/rss.php"; 
				mAbHttpUtil.get(urlString, new AbStringHttpResponseListener() {
					
					//获取数据成功会调用这里
		        	@Override
					public void onSuccess(int statusCode, String content) {
		        		Log.d(TAG, "onSuccess");
		        		
		        		AbDialogUtil.showAlertDialog(HttpActivity.this,"返回结果",content.trim(),new AbDialogOnClickListener(){

							@Override
							public void onNegativeClick() {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onPositiveClick() {
								// TODO Auto-generated method stub
								
							}
							
		            		
		            	});
		        	
		        	}
		        	
		        	
		        	// 失败，调用
		            @Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
		            	
		            	Log.d(TAG, "onFailure");
		            	AbToastUtil.showToast(HttpActivity.this,error.getMessage());
					}

		            // 开始执行前
		            @Override
					public void onStart() {
		            	Log.d(TAG, "onStart");
		            	//显示进度框
		            	AbDialogUtil.showProgressDialog(HttpActivity.this,0,"正在查询...");
					}


					// 完成后调用，失败，成功
		            @Override
		            public void onFinish() { 
		            	Log.d(TAG, "onFinish");
		            	//移除进度框
		            	AbDialogUtil.removeDialog(HttpActivity.this);
		            };
		            
		        });
				
			}
		});
        
        //post请求
        postBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String url = "http://www.amsoft.cn/sort/10";
				// 绑定参数
		        AbRequestParams params = new AbRequestParams(); 
		        params.put("param1", "1");
		        params.put("param2", "2");
		        params.put("param3", "10");
		        mAbHttpUtil.post(url,params, new AbStringHttpResponseListener() {
		        	
		        	// 获取数据成功会调用这里
		        	@Override
		        	public void onSuccess(int statusCode, String content) {
		        		Log.d(TAG, "onSuccess");
		        		AbDialogUtil.showAlertDialog(HttpActivity.this,"返回结果",content,new AbDialogOnClickListener(){

							@Override
							public void onPositiveClick() {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onNegativeClick() {
								// TODO Auto-generated method stub
								
							}
		            		
		            	});
		            };
		            
		            // 开始执行前
		            @Override
					public void onStart() {
		            	Log.d(TAG, "onStart");
		            	//显示进度框
		            	AbDialogUtil.showProgressDialog(HttpActivity.this,0,"正在查询...");
					}
		            
		            // 失败，调用
		            @Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
		            	AbToastUtil.showToast(HttpActivity.this,error.getMessage());
					}

					// 完成后调用，失败，成功
		            @Override
		            public void onFinish() { 
		            	Log.d(TAG, "onFinish");
		            	//移除进度框
		            	AbDialogUtil.removeDialog(HttpActivity.this);
		            };
		            
		        });
			}
		});
        
        //字节数组下载
        byteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String url = "http://www.amsoft.cn/content/templates/amsoft/images/rand/8.jpg";
				mAbHttpUtil.get(url, new AbBinaryHttpResponseListener() {
		        	
					// 获取数据成功会调用这里
		        	@Override
					public void onSuccess(int statusCode, byte[] content) {
		        		Log.d(TAG, "onSuccess");
		        		Bitmap bitmap = AbImageUtil.bytes2Bimap(content);
		            	ImageView view = new ImageView(HttpActivity.this);
		            	view.setImageBitmap(bitmap);
		            	
		            	AbDialogUtil.showAlertDialog("返回结果",view,new AbDialogOnClickListener(){

							@Override
							public void onPositiveClick() {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onNegativeClick() {
								// TODO Auto-generated method stub
								
							}
		            		
		            	});
					}
		        	
		        	// 开始执行前
		            @Override
					public void onStart() {
		            	Log.d(TAG, "onStart");
		            	//显示进度框
		            	AbDialogUtil.showProgressDialog(HttpActivity.this,0,"正在查询...");
					}

		            // 失败，调用
		            @Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
		            	AbToastUtil.showToast(HttpActivity.this,error.getMessage());
					}

					// 完成后调用，失败，成功
		            @Override
		            public void onFinish() { 
		            	Log.d(TAG, "onFinish");
		            	//移除进度框
		            	AbDialogUtil.removeDialog(HttpActivity.this);
		            };
		            
		        });
			}
		});
        
        //文件下载
        fileDownBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String url = "http://www.amsoft.cn/content/uploadfile/201311/38ed1385599018.jpg";
				
				mAbHttpUtil.get(url, new AbFileHttpResponseListener(url) {
		        	
					
					// 获取数据成功会调用这里
		        	@Override
					public void onSuccess(int statusCode, File file) {
		        		Log.d(TAG, "onSuccess");
		        		Bitmap bitmap = AbFileUtil.getBitmapFromSD(file);
		            	ImageView view = new ImageView(HttpActivity.this);
		            	view.setImageBitmap(bitmap);
		            	
		            	AbDialogUtil.showAlertDialog("返回结果",view,new AbDialogOnClickListener(){

							@Override
							public void onPositiveClick() {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onNegativeClick() {
								// TODO Auto-generated method stub
								
							}
							
		            		
		            	});
					}
		        	
		        	// 开始执行前
		            @Override
					public void onStart() {
		            	Log.d(TAG, "onStart");
		            	//打开进度框
		            	View v = LayoutInflater.from(HttpActivity.this).inflate(R.layout.progress_bar_horizontal, null, false);
		            	mAbProgressBar = (AbHorizontalProgressBar) v.findViewById(R.id.horizontalProgressBar);
		            	numberText = (TextView) v.findViewById(R.id.numberText);
		        		maxText = (TextView) v.findViewById(R.id.maxText);
		        		
		        		maxText.setText(progress+"/"+String.valueOf(max));
		        		mAbProgressBar.setMax(max);
		        		mAbProgressBar.setProgress(progress);
		            	
		        		mAlertDialog = AbDialogUtil.showAlertDialog("正在下载",v);
					}

		        	// 失败，调用
		            @Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						Log.d(TAG, "onFailure");
						AbToastUtil.showToast(HttpActivity.this,error.getMessage());
					}
					
					// 下载进度
					@Override
					public void onProgress(int bytesWritten, int totalSize) {
						maxText.setText(bytesWritten/(totalSize/max)+"/"+max);
						mAbProgressBar.setProgress(bytesWritten/(totalSize/max));
					}

					// 完成后调用，失败，成功
		            public void onFinish() { 
		            	//下载完成取消进度框
		            	if(mAlertDialog!=null){
		            		mAlertDialog.dismiss();
			            	mAlertDialog  = null;
		            	}
		            	
		            	Log.d(TAG, "onFinish");
		            };
		            
		        });
			}
		});
        
        //文件上传
        fileUploadBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//已经在后台上传
				if(mAlertDialog!=null){
					mAlertDialog.show(getFragmentManager(), "dialog");
					return;
				}
				String url = "http://192.168.1.124:8080/demo/upload.do";
				
				AbRequestParams params = new AbRequestParams(); 
				
				try {
					//多文件上传添加多个即可
					File pathRoot = Environment.getExternalStorageDirectory();
					String path = pathRoot.getAbsolutePath();
					params.put("data1",URLEncoder.encode("中文的处理",HTTP.UTF_8));
					params.put("data2","100");
					//参数随便加，在sd卡根目录放图片
					File file1 = new File(path+"/1.jpg");
					File file2 = new File(path+"/1.txt");
					params.put(file1.getName(),file1);
					params.put(file2.getName(),file2);
					
					//只支持最多2个文件域，因为会产生流中断的异常，所以你需要传递更多，请分次数上传
					//File file3 = new File(path+"/3.log");
					//File file4 = new File(path+"/1.jpg");
					//params.put(file3.getName(),file3);
					//params.put(file4.getName(),file4);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {

					
					@Override
					public void onSuccess(int statusCode, String content) {
						AbToastUtil.showToast(HttpActivity.this,"onSuccess");
					}

					// 开始执行前
		            @Override
					public void onStart() {
		            	Log.d(TAG, "onStart");
		            	//打开进度框
		            	View v = LayoutInflater.from(HttpActivity.this).inflate(R.layout.progress_bar_horizontal, null, false);
		            	mAbProgressBar = (AbHorizontalProgressBar) v.findViewById(R.id.horizontalProgressBar);
		            	numberText = (TextView) v.findViewById(R.id.numberText);
		        		maxText = (TextView) v.findViewById(R.id.maxText);
		        		
		        		maxText.setText(progress+"/"+String.valueOf(max));
		        		mAbProgressBar.setMax(max);
		        		mAbProgressBar.setProgress(progress);
		            	
		        		mAlertDialog = AbDialogUtil.showAlertDialog("正在上传",v);
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						AbToastUtil.showToast(HttpActivity.this,error.getMessage());
					}

					// 进度
					@Override
					public void onProgress(int bytesWritten, int totalSize) {
						maxText.setText(bytesWritten/(totalSize/max)+"/"+max);
						mAbProgressBar.setProgress(bytesWritten/(totalSize/max));
					}

					// 完成后调用，失败，成功，都要调用
		            public void onFinish() { 
		            	Log.d(TAG, "onFinish");
		            	//下载完成取消进度框
		            	if(mAlertDialog!=null){
		            	    mAlertDialog.dismiss();
		            	    mAlertDialog  = null;
		            	}
		            };
					
		            
		        });
			}
		});
        
      } 

    
}
