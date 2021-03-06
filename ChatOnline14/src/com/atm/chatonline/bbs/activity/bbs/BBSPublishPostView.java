package com.atm.chatonline.bbs.activity.bbs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.charonline.bbs.util.Bimp;
import com.atm.charonline.bbs.util.LogUtil;
import com.atm.charonline.bbs.util.SendDataToServer;
import com.atm.chatonline.bbs.adapter.ExpressionPagerAdapter;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.chat.ui.AttentionActivity;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

/**
 * @类 com.atm.chatonline.activity.bbs ---BBSPublishPostView
 * @作用 该类用于显示发贴界面
 * @作者 钟YD
 * @时间 2015-8-24
 * 
 */
public class BBSPublishPostView extends BaseActivity implements
		OnClickListener {

	private LinearLayout ll_function, ll_exp, ll_photo;
	private ImageView album, department, expression, photo_one, aite,
			iv_return;
	private TextView sendPost;
	private EditText title, content;
	private Uri imageUri;
	private Spinner spinner;
	private static String str_title, str_department = "", str_type, str_label = "",
			str_content;
	private String cookie, tag = "BBSPublishPostView",picturePath = "",
			userID = BaseActivity.getSelf().getUserID();
	private static String response;
	private InputMethodManager mInputMethodManager;
	private ViewPager exp_pager;
	private ExpressionPagerAdapter pagerAdapter;
	private List<View> view;
	private List<Map<String, Object>> listItems1, listItems2;
	private SimpleAdapter adapter1, adapter2;
	private GridView grid1, grid2;
	private View viewPager1, viewPager2;
	private boolean isFaceShow = false;
	private Resources res;
	private JSONArray aiteID = new JSONArray();
	private static int i = 0;// 记录@次数
	private Bitmap myBitmap;
	private Uri uri;
	private static final String CHARSET = "utf-8"; // 设置编码
	private byte[] mContent;
	private String subURL = UriAPI.SUB_URL;
	private String[] description1, description2, type;
	private SendDataToServer send = new SendDataToServer();
	private int contentCursor;
	private int[] imageIds1 = { R.drawable.exp01, R.drawable.exp2,
			R.drawable.exp3, R.drawable.exp4, R.drawable.exp5, R.drawable.exp6,
			R.drawable.exp7, R.drawable.exp8, R.drawable.exp9,
			R.drawable.exp10, R.drawable.exp11, R.drawable.exp12,
			R.drawable.exp13, R.drawable.exp14, R.drawable.exp15,
			R.drawable.exp16, R.drawable.exp17, R.drawable.exp19,
			R.drawable.exp20, R.drawable.exp21, R.drawable.delete, };
	private int[] imageIds2 = { R.drawable.exp22, R.drawable.exp23,
			R.drawable.exp24, R.drawable.exp25, R.drawable.exp26,
			R.drawable.exp27, R.drawable.exp28, R.drawable.exp29,
			R.drawable.exp30, R.drawable.exp31, R.drawable.exp32,
			R.drawable.exp33, R.drawable.delete, };

	// public static final int CROP_PHOTO = 2;
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_publish_post);

		initView();
		getArray();// 从资源文件夹下获取已定义好的数组

		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		accomplishExpBoard();// 实现表情面板

		initEvent();

		title.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					Log.d(tag, "title");
				}
				if (isFaceShow) {
					ll_exp.setVisibility(View.GONE);
					isFaceShow = false;
				}
			}
		});
		content.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (isFaceShow) {
					ll_exp.setVisibility(View.GONE);
					isFaceShow = false;
				}
			}
		});

		// 获取cookie
		SharedPreferences pref = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());

		// 类型的监听事件
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				str_type = type[position];
			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	// 设置监听事件
	private void initEvent() {
		// TODO Auto-generated method stub
		album.setOnClickListener(this);
		expression.setOnClickListener(this);
		department.setOnClickListener(this);
		sendPost.setOnClickListener(this);
		aite.setOnClickListener(this);
		iv_return.setOnClickListener(this);
		photo_one.setOnClickListener(this);

	}

	// 实现表情面板
	private void accomplishExpBoard() {
		// TODO Auto-generated method stub
		listItems1 = new ArrayList<Map<String, Object>>();
		listItems2 = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < imageIds1.length; i++) {
			Map<String, Object> listItem1 = new HashMap<String, Object>();
			listItem1.put("image1", imageIds1[i]);
			listItems1.add(listItem1);
		}
		for (int i = 0; i < imageIds2.length; i++) {
			Map<String, Object> listItem2 = new HashMap<String, Object>();
			listItem2.put("image2", imageIds2[i]);
			listItems2.add(listItem2);
		}
		adapter1 = new SimpleAdapter(this, listItems1, R.layout.simple_item,
				new String[] { "image1" }, new int[] { R.id.image });
		adapter2 = new SimpleAdapter(this, listItems2, R.layout.simple_item,
				new String[] { "image2" }, new int[] { R.id.image });
		viewPager1 = View.inflate(this, R.layout.viewpager1, null);
		viewPager2 = View.inflate(this, R.layout.viewpager2, null);
		grid1 = (GridView) viewPager1.findViewById(R.id.grid1);
		grid2 = (GridView) viewPager2.findViewById(R.id.grid2);
		grid1.setAdapter(adapter1);//如果注释，有第一页，但没有表情
		grid2.setAdapter(adapter2);

		view = new ArrayList<View>();
		view.add(viewPager1);//如果注释，就没有第一页，表情就不可能有
		view.add(viewPager2);
		pagerAdapter = new ExpressionPagerAdapter(view);
		exp_pager.setAdapter(pagerAdapter);

		//点了面板中的表情
		grid1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LogUtil.p(tag,"点grid1");
				// TODO Auto-generated method stub
				int index1 = Math.max(content.getSelectionStart(), 0);
				LogUtil.p(tag,"index1："+index1);
				String oriContent1 = content.getText().toString();
				LogUtil.p(tag,"oriContent1："+oriContent1);
				StringBuilder sBuilder1 = new StringBuilder(oriContent1);//类似StringBuffer,但速度更快
				LogUtil.p(tag, "arg1:"+arg1+".arg2:"+arg2);//arg2索引的位置
				if (arg2 == 20) {//删除图标
					if (content.getSelectionStart() > 0) {
						int selection = content.getSelectionStart();
						String text2 = oriContent1.substring(selection - 1);
						if (")".equals(text2)) {//当删的是表情的时候，整块删掉
							int start = oriContent1.lastIndexOf("#");
							int end = selection;
							content.getText().delete(start, end);
						}
						// input.getText().delete(selection - 1, selection);
					}
				} else {
					sBuilder1.insert(index1, description1[arg2]);
					content.setText(sBuilder1.toString());
					content.setSelection(index1 + description1[arg2].length());
				}
			}

			private void LogUtil(String tag, String string) {
				// TODO Auto-generated method stub
				
			}
		});

		grid2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 LogUtil.p(tag,"点grid2");
				// TODO Auto-generated method stub
				int index2 = Math.max(content.getSelectionStart(), 0);
				String oriContent2 = content.getText().toString();
				StringBuilder sBuilder2 = new StringBuilder(oriContent2);
				if (arg2 == 12) {
					if (content.getSelectionStart() > 0) {
						int selection = content.getSelectionStart();
						String text2 = oriContent2.substring(selection - 1);
						if (")".equals(text2)) {
							int start = oriContent2.lastIndexOf("#");
							int end = selection;
							content.getText().delete(start, end);
						}
						// input.getText().delete(selection - 1, selection);
					}
				} else {
					sBuilder2.insert(index2, description2[arg2]);
					content.setText(sBuilder2.toString());
					content.setSelection(index2 + description2[arg2].length());
				}

			}

			private void LogUtil(String tag, String string) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	// 从资源文件夹下获取已定义好的数组
	private void getArray() {
		// TODO Auto-generated method stub
		res = BBSPublishPostView.this.getResources();
		type = res.getStringArray(R.array.type);
		description1 = res.getStringArray(R.array.description1);
		description2 = res.getStringArray(R.array.description2);
	}

	// arg0 = requestCode请求码,arg1 = resultCode结果码
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		// ContentResolver contentResolver = getContentResolver();
		switch (arg0) {
		case 0:
			if (arg1 == 0) {// 获得从系部界面返回的数据
				str_department = arg2.getStringExtra("department");
			}
			// 新增加 2015.10.30钟
			if (arg1 == RESULT_OK) {// 从关注界面获得数据
				try {
					aiteID.put(i, arg2.getStringExtra("friendID"));
					i++;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				contentCursor = content.getSelectionStart();// 获取文本当前所在光标
				content.getText().insert(contentCursor,
						"@" + arg2.getStringExtra("nickName") + " ");// 在当前光标处插入文本
			}
			break;
		case PHOTO_REQUEST_GALLERY:
			// 从相册返回的数据
			if (arg2 != null) { // 修改 2015.10.30钟
				// 得到图片的全路径
				// uri = arg2.getData();
				try {
					uri = arg2.getData(); // 获取系统返回的照片的Uri
					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = getContentResolver().query(uri,
							filePathColumn, null, null, null);// 从系统表中查询指定Uri对应的照片
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					picturePath = cursor.getString(columnIndex); // 获取照片路径
					cursor.close();
					Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
					// myBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100,
					// true);
					// myBitmap = Bimp.resizePhoto(bitmap);
					myBitmap = Bimp.revitionImageSize(picturePath);
					photo_one.setImageBitmap(myBitmap);
					ll_photo.setVisibility(View.VISIBLE);
					contentCursor = content.getSelectionStart();// 获取文本当前所在光标
					content.getText().insert(contentCursor,
							"[#图片]");// 在当前光标处插入文本
				} catch (Exception e) {
					// TODO Auto-generatedcatch block
					e.printStackTrace();
				}
				// crop(uri);//剪切图片
			}
			break;
		}
	}

	// 初始化控件
	private void initView() {
		// TODO Auto-generated method stub
		sendPost = (TextView) findViewById(R.id.sendPost);
		album = (ImageView) findViewById(R.id.album);
		aite = (ImageView) findViewById(R.id.aite);
		department = (ImageView) findViewById(R.id.department);
		expression = (ImageView) findViewById(R.id.expression);
		ll_function = (LinearLayout) findViewById(R.id.ll_function);
		ll_exp = (LinearLayout) findViewById(R.id.ll_expression);
		spinner = (Spinner) findViewById(R.id.spinner);
		title = (EditText) findViewById(R.id.title);
		content = (EditText) findViewById(R.id.content);
		exp_pager = (ViewPager) findViewById(R.id.exp_pager);
		photo_one = (ImageView) findViewById(R.id.photo_one);
		ll_photo = (LinearLayout) findViewById(R.id.ll_photo);
		iv_return = (ImageView) findViewById(R.id.iv_return);
	}

	/*
	 * 从相册获取
	 */
	public void gallery(View view) {
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.album:
			gallery(arg0);
			break;
		case R.id.expression:
			if (!isFaceShow) {
				mInputMethodManager.hideSoftInputFromWindow(
						content.getWindowToken(), 0);
				try {
					Thread.sleep(80);// 解决此时会黑一下屏幕的问题
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ll_exp.setVisibility(View.VISIBLE);
				isFaceShow = true;
			} else {
				ll_exp.setVisibility(View.GONE);
				isFaceShow = false;
			}
			break;
		// 新添加 (2015.10.30钟)
		case R.id.aite:
			Intent aiteIntent = new Intent(BBSPublishPostView.this,
					AttentionActivity.class);
			aiteIntent.putExtra("userID", userID);
			aiteIntent.putExtra("fromActivity", Config.BBSPOSTDETAILVIEW);
			startActivityForResult(aiteIntent, 0);
			break;
		case R.id.department:
			Intent departmentIntent = new Intent(BBSPublishPostView.this,
					BBSChooseDepartmentView.class);
			startActivityForResult(departmentIntent, 0);
			break;
		// 新添加（2015.11.01钟）
		case R.id.iv_return:
			AlertDialog.Builder back = new AlertDialog.Builder(this);
			back.setTitle("提示框")
					.setMessage("退出当前编辑？")
					.setPositiveButton("退出",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									BBSPublishPostView.this.finish();
								}

							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
								}
							});

			back.create().show();
			break;
		case R.id.sendPost:
			if (!title.getText().toString().equals("")
					&& !content.getText().toString().equals("")) {
				if (!str_department.equals("")) {
					str_title = title.getText().toString();
					str_content = content.getText().toString();
					sendDataToServer();// 将数据传给服务器
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}// 让主线程睡眠1秒，等待参数response

					Log.i(tag, "response:"+response);
					try{
						if (response.equals("success")) {
							Toast.makeText(BBSPublishPostView.this, "发帖成功",
									Toast.LENGTH_SHORT).show();
							finish();
						} else {
							Toast.makeText(BBSPublishPostView.this, response,
									Toast.LENGTH_SHORT).show();
						}
					}catch(NullPointerException e){
						return ;
					}
					
				}else{
					AlertDialog.Builder depart = new AlertDialog.Builder(this);
					depart.setMessage("请选择系别").setNeutralButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					depart.create().show();
				}
			} else {
				// 标题或者内容为空时，弹出提示框
				AlertDialog.Builder build = new AlertDialog.Builder(this);
				build.setMessage("请完善所有内容再点击发贴").setNeutralButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				build.create().show();
			}
			i = 0;
			break;
		case R.id.photo_one:
			Intent photoIntent = new Intent(BBSPublishPostView.this,
					BBSPreviewPicture.class);
			photoIntent.putExtra("picturePath", picturePath);
			startActivityForResult(photoIntent, 0);
			break;
		}
	}

	private void sendDataToServer() {
		// TODO Auto-generated method stub

		new Thread(new Runnable() {
			String getResponse;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 传递帖子内容
				Map<Object, Object> params = new HashMap<Object, Object>();
				params.put("type", str_type);
				params.put("label", str_label);
				params.put("title", str_title);
				params.put("department", str_department);
				params.put("content", str_content);
				params.put("aiteID", aiteID);
				// 传图片
				ArrayList<Bitmap> list = new ArrayList<Bitmap>();

				if (myBitmap != null) {

					list.add(myBitmap);
					Bitmap[] files = (Bitmap[]) list.toArray(new Bitmap[list
							.size()]);
					getResponse = send.post(subURL + "essay/publish.do",
							params, files, cookie);
				} else {
					getResponse = send.post(subURL + "essay/publish.do",
							params, null, cookie);
				}
				try {
					JSONObject object = new JSONObject(getResponse);
					response = object.getString("tip");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}

	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根

	}
}
