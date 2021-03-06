package com.atm.chatonline.usercenter.activity.usercenter;



import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.atm.charonline.bbs.bean.BBS;
import com.atm.charonline.bbs.bean.Data;
import com.atm.charonline.bbs.util.BBSConnectNet;
import com.atm.charonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.activity.bbs.BBSPostDetailView;
import com.atm.chatonline.bbs.adapter.MyBbsAdapter;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MyBbs extends BaseActivity implements OnClickListener{
	private View v;
	private String tag="MyBbs";
	private int bbsNums = 0;
	private String response;
	private String cookie;
	private PullToRefreshListView plv;
	private List<BBS> bbsList= new ArrayList<BBS>();
	private MyBbsAdapter bbsAdapter;
	private BBSConnectNet bBSConnectNet;
	private Handler handler;
	private Button btnBack=null;
	private int count =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_bbs_bg_normal);
		final ProgressBar pro=(ProgressBar)findViewById(R.id.myprogressbar);
		btnBack=(Button)findViewById(R.id.btn_back);
		Log.i(tag, "我的帖子。。。。");
		initParams();
		new GetDataTask().execute();
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO 自动生成的方法存根
				
				
				pro.setVisibility(View.GONE);
				switch(msg.what){
				case 1:
					initView();
					break;
				case 2:
					Toast.makeText(getApplicationContext(), "没有帖子了，别再刷新哦！", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					Toast.makeText(getApplicationContext(), "你还没有发过帖子，赶紧去发帖子吧！！", Toast.LENGTH_SHORT).show();
					break;
				}
			
			}
			
		};
	}

	
	
	/**
	 * 初始化参数：
	 * 获取由Bundle传入的参数。
	 * 该参数是用于传递给服务器端所需的参数。
	 * 四个参数：id;tip;relativePath
	 */
	private void initParams() {
		
		Log.i(tag,"initParams");
		
		//Bundle bundle = this.getIntent().getExtras();
		/*this.relativePath = bundle.getString("relativePath");
		this.id = bundle.getString("id");
		this.tip = bundle.getString("tip");*/

		SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
		Log.i(tag,"cookie:"+cookie);
	}
	private void initView(){
		LayoutInflater lf=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v=lf.inflate(R.layout.my_bbs_main_view, null);
		Button btnBackOnMainView=(Button) v.findViewById(R.id.btn_back);
		btnBackOnMainView.setOnClickListener(this);
		plv=(PullToRefreshListView)v.findViewById(R.id.lv_mybbs);
		bbsAdapter=new MyBbsAdapter(this,R.layout.my_bbs_view_item,bbsList);
		plv.setAdapter(bbsAdapter);
		
		
		
		//为该列表设置上拉下拉监听
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				bbsNums = 0;
				new GetDataTask().execute();
			}
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
			}
		});
		
		//设置PullRefreshListView上提加载时的加载提示
        plv.getLoadingLayoutProxy(false, true).setPullLabel("正在加载...");
        plv.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载ing");
        plv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开啦，我就加载，(*^__^*) 嘻嘻……");
        // 设置PullRefreshListView下拉加载时的加载提示
        plv.getLoadingLayoutProxy(true, false).setPullLabel("下拉，下拉，下拉，O(∩_∩)O哈哈~");
        plv.getLoadingLayoutProxy(true, false).setRefreshingLabel("伦家正在努力刷新ing。↖(^ω^)↗");
        plv.getLoadingLayoutProxy(true, false).setReleaseLabel("松开啦，我就刷新，(*^__^*) 嘻嘻……");
	
        plv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//传递essayid给帖子。
				Log.i(tag, "帖子被点击");
				ExtendsIntent intent = new ExtendsIntent( MyBbs.this , BBSPostDetailView.class ,
						bbsList.get(position - 1).getEssayId(), null , null , 1);
				Log.i(tag, "帖子被点击111");
				Log.i(tag, bbsList.get(position - 1).getEssayId());
				startActivity(intent);
				//Log.i(tag, "queue.size:"+WoliaoBaseActivity.queue.size());
				Log.i(tag, "帖子被点击2");
			}
        	
		});
        Log.i(tag,"走完initView");
        //如果是第一次加载则替换view，如果不是，则更新view
       /*if(count==0){
    	   count++;
    	   setContentView(v);
       }*/
        setContentView(v);
	}
	
	
	
	/**
	 * 异步加载数据
	 */
	private class GetDataTask extends AsyncTask<Void , Void , String>{
		
		
		public GetDataTask(){
			Log.i(tag, "+++++++first");
		}
		
		protected String doInBackground(Void... params) {
		try{
				//下拉或上拉停住，等待2秒
			Log.i(tag, "+++++++second");
			//2015.10.9这个我改了一下，doInBackground是处理耗时问题，所以把loadData放在这里，避免出现ANR问题---郑
				Thread.sleep(2000);
				try {
					Log.i(tag, "+++++++third");
					loadData();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}catch(InterruptedException e){
			}
			return null;
		}
	
		protected void onPostExecute(String result){
			Log.i(tag, "update bbslistview !");
			bbsAdapter.notifyDataSetChanged();
			plv.onRefreshComplete();
		}
		
		
		

		/**
		 * 从服务器端获取数据
		 * @return response：json的数据
		 */
		private String getResponseFromNet() {
			Log.i(tag,"getResponseFromNet");
			Thread thread = new Thread(new Runnable(){
				public void run(){
					bBSConnectNet = new BBSConnectNet("","",bbsNums,"essay_publishedEssay.action",cookie);
					
					Log.i(tag,"getResponseFromNet+BBSConnectNet结束");
					response = bBSConnectNet.getResponse();
					Log.i(tag,"Gson:"+response);
					Log.i(tag,"bBSConnectNet.getResponse()结束");
				}
			});
			thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				Log.d(tag,"getResponseFromNet-线程被打断");
				e.printStackTrace();
			}
			Log.i(tag,"555");
			return response;
		}
		
		/**
		 * 异步更新的加载数据
		 * @param page2
		 * @throws JSONException 
		 */
		public void loadData() throws JSONException {
			
			Log.d(tag,"loadData");
			
			if(bbsNums == 0){
				//先把列表清空
				Log.d(tag,"bbsList.clear()");
				bbsList.clear();
			}
			response = getResponseFromNet();
			Log.d(tag,"loadData+1");
			
			Data data = new Gson().fromJson(response,Data.class);
			
			Log.d(tag,"loadData+3");
	
				addData(data);
				//将BBSFirst中的BBS列表项加到BBS bean类中
				for(BBS bbs :bbsList ){
					Log.i(tag,"img path"+ bbs.getHeadImagePath().toLowerCase());
				}
				Log.d(tag,"loadData走完");
				if(count==0){
				handler.sendEmptyMessage(1);
				count++;
				}
		}
	}
	
	
	/**
	 * 加载数据
	 * 将BBSFirst中的BBS列表项加到BBS bean类中，并获取图片
	 * @param dataFromJson : BBSFirst的实例化
	 */
	private void addData(Data data) {

		Log.d(tag,"addData");
		
		//将BBSFirst中的BBS列表项加到BBS bean类中
		for(BBS bbs :data.getBbs()){
			if(!bbsList.contains(bbs)){
				bbsList.add(bbs);
				bbsNums++;
			}
		}

		Log.i(tag, "bbsList.size()"+bbsList.size());
		int count=1;
		//获取照片。
		for(BBS bbs: bbsList){
			Log.i(tag, "第"+count+++"条记录");
			//bbs.setHeadImage(new ReceivePhoto(bbs.getHeadImagePath()).getPhoto());
			//获取标签名和标签颜色
			Log.i(tag, "获取标签名和标签颜色---111");
			bbs.setLabName0(bbs.getLabName().split("\\*#"));
			Log.i(tag, "获取标签名和标签颜色---222");
			String[] colors = bbs.getLabColor().split("\\*");
			int[] color = new int[colors.length];
			for(int i = 0 ; i < colors.length; i++){
				color[i] = Color.parseColor(colors[i]);
			}
			bbs.setLabColor0(color);
			Log.i(tag, "获取标签名和标签颜色---333");
		}
	}



	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id){
		case R.id.btn_back:
			this.onBackPressed();
			break;
		}
		
	}



	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根
		
	}
	
}
