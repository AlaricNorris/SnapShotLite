package com.alaric.norris.snapshotdemo ;

import java.io.File ;
import java.io.FileOutputStream ;
import java.text.SimpleDateFormat ;
import java.util.Date ;
import android.app.Activity ;
import android.graphics.Bitmap ;
import android.graphics.Canvas ;
import android.graphics.Color ;
import android.graphics.Paint ;
import android.os.Bundle ;
import android.os.Environment ;
import android.util.Log ;
import android.view.View ;
import android.view.View.OnClickListener ;
import android.widget.Button ;
import android.widget.ImageView ;
import android.widget.LinearLayout ;

public class MainActivity extends Activity {

	private Button mButton_SnapView ;

	private Button mButton_SnapScreen ;

	private ImageView mImageView ;

	private LinearLayout mLayout ;

	private String mAppRootPath ;

	/**
	 * Called with the activity is first created.
	 */
	@ Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState) ;
		// Set the layout for this activity. You can find it
		// in res/layout/hello_activity.xml
		setContentView(R.layout.activity_main) ;
		mAppRootPath = getSDPath() ;
		mLayout = (LinearLayout) findViewById(R.id.layout) ;
		mButton_SnapView = (Button) findViewById(R.id.btn_view) ;
		mButton_SnapView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					takeScreenShot(mLayout , mAppRootPath) ;
				}
				catch(Exception e) {
				}
			}
		}) ;
		mButton_SnapScreen = (Button) findViewById(R.id.btn_screen) ;
		mButton_SnapScreen.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					takeScreenShot(getWindow().getDecorView()/*getWindow().peekDecorView()*/,
							mAppRootPath) ;
				}
				catch(Exception e) {
				}
			}
		}) ;
		// 将mImageView 加入到LinearLayout 中
		mImageView = new ImageView(this) ;
		LinearLayout layout = (LinearLayout) findViewById(R.id.preview) ;
		layout.addView(mImageView) ;
	}

	public void takeScreenShot(View view , String path) throws Exception {
		view.setDrawingCacheEnabled(true) ;
		view.buildDrawingCache() ;
		Bitmap bitmap = view.getDrawingCache() ;
		Canvas canvas = new Canvas(bitmap) ;
		int w = bitmap.getWidth() ;
		int h = bitmap.getHeight() ;
		Paint paint = new Paint() ;
		paint.setColor(Color.YELLOW) ;
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss") ;
		String time = simple.format(new Date()) ;
		canvas.drawText(time , w / 10 , h - 16 , paint) ;
		//canvas.save();
		//canvas.restore();
		FileOutputStream fos = null ;
		try {
			File sddir = new File(path) ;
			if( ! sddir.exists()) {
				sddir.mkdirs() ;
			}
			mImageView.setImageBitmap(bitmap) ;
			File file = new File(path + File.separator + "viewshot.png") ;
			fos = new FileOutputStream(file) ;
			Log.i("tag" , file.getPath() + file.getName()) ;
			if(fos != null) {
				bitmap.compress(Bitmap.CompressFormat.PNG , 90 , fos) ;
				fos.close() ;
				Log.i("tag" , "success") ;
			}
		}
		catch(Exception e) {
			e.printStackTrace() ;
			Log.i("tag" , e.getCause().toString()) ;
		}
	}

	public void takeScreenShot2(View view , String path) throws Exception {
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth() , view.getHeight() ,
				Bitmap.Config.ARGB_8888) ;
		Canvas canvas = new Canvas(bitmap) ;
		view.draw(canvas) ; //dispatchDraw
		int w = bitmap.getWidth() ;
		int h = bitmap.getHeight() ;
		Paint paint = new Paint() ;
		paint.setColor(Color.YELLOW) ;
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss") ;
		String time = simple.format(new Date()) ;
		canvas.drawText(time , w / 10 , h - 16 , paint) ;
		//canvas.save();
		//canvas.restore();
		FileOutputStream fos = null ;
		try {
			File sddir = new File(path) ;
			if( ! sddir.exists()) {
				sddir.mkdirs() ;
			}
			mImageView.setImageBitmap(bitmap) ;
			File file = new File(path + File.separator + "viewshot.png") ;
			fos = new FileOutputStream(file) ;
			if(fos != null) {
				bitmap.compress(Bitmap.CompressFormat.PNG , 90 , fos) ;
				fos.close() ;
			}
		}
		catch(Exception e) {
			Log.e("TakeViewShot" , e.getCause().toString()) ;
			e.printStackTrace() ;
		}
	}

	public String getSDPath() {
		File sdDir = null ;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) ; //判断sd卡是否存在 
		if(sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory() ;//获取跟目录 
		}
		return sdDir.toString() ;
	}
}
