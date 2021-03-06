/**
 * 
 */
package com.atm.charonline.bbs.util;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atm.charonline.recuit.ui.RecuitMainView;
import com.atm.charonline.recuit.ui.RecuitNavigationPublishPost;
import com.atm.charonline.recuit.ui.RecuitNavigationSearch;
import com.atm.chatonline.bbs.activity.bbs.BBSMainView;
import com.atm.chatonline.bbs.activity.bbs.BBSPublishPostView;
import com.atm.chatonline.bbs.activity.bbs.NavigationSearch;
import com.atm.chatonline.bbs.activity.login.LoginView;
import com.atm.chatonline.bbs.commom.CircleImageView;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.ui.ChatMainActivity;
import com.atm.chatonline.chat.ui.CreateGroupActivity;
import com.atm.chatonline.chat.ui.SearchFriendListActivity;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.FileUtil;
import com.atm.chatonline.schoolnews.ui.RecommendActivity;
import com.atm.chatonline.schoolnews.ui.SchoolNewsActivity;
import com.atm.chatonline.setting.ui.SettingView;
import com.atm.chatonline.usercenter.activity.usercenter.UserCenter;
import com.example.studentsystem01.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * @类 com.atm.charonline.bbs.util ---SlidingTitleLayout
 * @作用
 * @作者 陈小二
 * @时间 2015-10-25
 * 
 */
public class SlidingTitleLayout extends LinearLayout implements OnClickListener{

	private SlidingMenu slidingMenu;
	private ImageView imgViewPerCenter, imgViewEdit, imgViewSearch;
	private ImageView imgViewChatSearch,imgViewMore;
	private ImageView imgViewInvite;
	private LinearLayout l1,l2,l3,l4,l5,l6;
	private Activity activity;
	private String ACTIVITY;
	private String BBS="com.atm.chatonline.bbs.activity.bbs.BBSMainView";
	private String RECUIT="com.atm.charonline.recuit.ui.RecuitMainView";
	private String CHAT="com.atm.chatonline.chat.ui.ChatMainActivity";
	private String SCHOOLNEWS="com.atm.chatonline.schoolnews.ui.SchoolNewsActivity";
	Intent intent1,intent2,intent3,intent0,intent4,intent5,intent6,intentRecommend;
	Intent intentCreateGroup,intentSearch;//对应于聊天的小窗口
	private int i=0;
	private Button btnexit;
	private String tag ="SlidingTitleLayout";
	private CircleImageView headImage;
	private String subPath="atm_getUserHead.action";
	private String headSubPath;
	private TextView txt_bbs,txt_chat,txt_recuit,txt_news,txt_personal,txt_msg,txt_setting;
	
//	private MtitlePopupWindow popupWindow1;
	/**
	 * @param context
	 * @param attrs
	 */
	public SlidingTitleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LogUtil.p(tag, "侧拉框被点击！！！！");
		// 初始化标题栏
		activity = (Activity) getContext();
//		BaseActivity.
		ACTIVITY=activity.getLocalClassName().toString();
		Log.i(tag, "ACTIVITY"+ACTIVITY);
		
		if(ACTIVITY.equals(BBS)||ACTIVITY.equals(RECUIT)){
			LogUtil.p(tag, "BBSMainView是这个");
			LayoutInflater.from(context).inflate(R.layout.slidingtitle, this);
			
			imgViewEdit = (ImageView) findViewById(R.id.imgView_edit);
			imgViewEdit.setOnClickListener(this);
			imgViewSearch = (ImageView) findViewById(R.id.imgView_search);
			imgViewSearch.setOnClickListener(this);
			
		}else if(ACTIVITY.equals(CHAT)){
			LogUtil.p(tag, "ChatMainActivity是这个");
			LayoutInflater.from(context).inflate(R.layout.chat_main_head, this);
//			imgViewMore = (ImageView) findViewById(R.id.imgView_more);
//			imgViewMore.setOnClickListener(this);
			imgViewChatSearch = (ImageView) findViewById(R.id.imgView__chat_search);
			imgViewChatSearch.setOnClickListener(this);
//			initPopupWindow();
			
		}else if(ACTIVITY.equals(SCHOOLNEWS)){
			LogUtil.p(tag, "SchoolNewsActivity是这个");
			LayoutInflater.from(context).inflate(R.layout.school_news_head, this);
			imgViewInvite = (ImageView) findViewById(R.id.imgView_invite);
			imgViewInvite.setOnClickListener(this);
		}
		imgViewPerCenter = (ImageView) findViewById(R.id.imgView_person_center);
		imgViewPerCenter.setOnClickListener(this);
		
		
		
		InItSlidingMenu();
		
//		l1 = (LinearLayout) findViewById(R.id.bbs);
//		l2 = (LinearLayout) findViewById(R.id.chat);
//		l3 = (LinearLayout) findViewById(R.id.recuit);
//		l4 = (LinearLayout)findViewById(R.id.user_center);
//		l1.setOnClickListener(this);
//		l2.setOnClickListener(this);
//		l3.setOnClickListener(this);
//		l4.setOnClickListener(this); 
		intent2 = new Intent(getContext(), ChatMainActivity.class);
		intent1 = new Intent(getContext(), BBSMainView.class);
		intent3 = new Intent(getContext(), RecuitMainView.class);
		intent0 = new Intent(getContext(),LoginView.class);
		intent4 = new Intent(getContext(),SchoolNewsActivity.class);
		intent5 = new Intent(getContext(),UserCenter.class);
		intent6 = new Intent(getContext(),SettingView.class);
		intentRecommend = new Intent(getContext(),RecommendActivity.class);
		headImage.setOnClickListener(this);
		intentCreateGroup = new Intent(getContext(),CreateGroupActivity.class);
		intentSearch = new Intent(getContext(),SearchFriendListActivity.class);
	}
	
	/**
	 * 初始化滑动菜单
	 * 
	 * */

	void InItSlidingMenu() {
		slidingMenu = new SlidingMenu(getContext());
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setBehindOffset(4 / 5);
		slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
		slidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);// 设置触摸范围
											//(Activity) getContext()对的吗？					// TOUCHMODE_FULLSCREEN（全局）
//		slidingMenu.attachToActivity(activity,SlidingMenu.RIGHT);// SLIDING_CONTENT
		slidingMenu.setMenu(R.layout.sliding_menu_view);

		l1 = (LinearLayout) slidingMenu.findViewById(R.id.bbs);
		l2 = (LinearLayout) slidingMenu.findViewById(R.id.chat);
		l3 = (LinearLayout) slidingMenu.findViewById(R.id.recuit);
		l4 = (LinearLayout) slidingMenu.findViewById(R.id.news);
		l5 = (LinearLayout) slidingMenu.findViewById(R.id.user_center);
		l6 = (LinearLayout) slidingMenu.findViewById(R.id.setting);
		headImage = (CircleImageView) slidingMenu.findViewById(R.id.btn_headimage);
		btnexit = (Button) slidingMenu.findViewById(R.id.btn_slide_exit);
		
		
		txt_bbs = (TextView)slidingMenu.findViewById(R.id.btn_bbs);
		
		txt_chat = (TextView)slidingMenu.findViewById(R.id.btn_chat);
		
		txt_recuit = (TextView)slidingMenu.findViewById(R.id.btn_recuit);
		txt_news = (TextView)slidingMenu.findViewById(R.id.btn_news);
		txt_personal = (TextView)slidingMenu.findViewById(R.id.btn_personal);
		
		txt_msg = (TextView)slidingMenu.findViewById(R.id.btn_msg);
		txt_setting = (TextView)slidingMenu.findViewById(R.id.btn_settings);
		
		btnexit.setTextSize(BaseActivity.fontSize);
		
		LogUtil.p(tag, "111BaseActivity.fontSize:"+BaseActivity.fontSize);
		txt_bbs.setTextSize(BaseActivity.fontSize);
		LogUtil.p(tag, "222");
		txt_chat.setTextSize(BaseActivity.fontSize);
		LogUtil.p(tag, "333");
		txt_recuit.setTextSize(BaseActivity.fontSize);
		txt_news.setTextSize(BaseActivity.fontSize);
		txt_personal.setTextSize(BaseActivity.fontSize);
		LogUtil.p(tag, "44");
		txt_msg.setTextSize(BaseActivity.fontSize);
		txt_setting.setTextSize(BaseActivity.fontSize);
		
		l1.setOnClickListener(this);
		l2.setOnClickListener(this);
		l3.setOnClickListener(this);
		l4.setOnClickListener(this);
		l5.setOnClickListener(this);
		l6.setOnClickListener(this);
		btnexit.setOnClickListener(this);
	}
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imgView_person_center:
			LogUtil.p(tag,"imgView_person_center被点了");
			if(i==0) {
				LogUtil.p(tag, "imgView_person_center进来");
				//每点击之前获取不到activity，所以只能在点击之后在设置改变量
				slidingMenu.attachToActivity(activity,SlidingMenu.RIGHT);
				//设置侧拉框的头像
				if(ConfigUtil.nums == 0) {
				Thread thread = new Thread(new Runnable() {
					public void run() {
						BBSConnectNet bBSConnectNet = new BBSConnectNet(subPath,ConfigUtil.getCookie());
						String response = bBSConnectNet.getResponse();
						try {
							JSONObject obj = new JSONObject(response);
							if(obj.getString("userHead")!=null) {
								headSubPath = obj.getString("userHead");
								LogUtil.p("userHead", headSubPath);
							}else {
								LogUtil.p("userHead", "null");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Environment.getExternalStorageDirectory()+"/ATM/userhead"
//				ConfigUtil.setHead(new ReceivePhoto(headSubPath).getPhoto());
//				headImage.setImageDrawable(ConfigUtil.getHead());
				headImage.setImageBitmap(FileUtil.getBitmap(Environment.getExternalStorageDirectory()+"/ATM/userhead/"+BaseActivity.getSelf().getUserID()+".jpg"));
				ConfigUtil.nums++;}
				else {
//					headImage.setImageDrawable(ConfigUtil.getHead());
					headImage.setImageBitmap(FileUtil.getBitmap(Environment.getExternalStorageDirectory()+"/ATM/userhead/"+BaseActivity.getSelf().getUserID()+".jpg"));
				}
				i++;
			}
			slidingMenu.toggle(true);
			break;
		case R.id.imgView_edit:
			LogUtil.p(tag,"imgView_edit被点了");
			if(ACTIVITY.equals(BBS)) {
			activity.startActivity(new Intent(getContext(), BBSPublishPostView.class));}
			if(ACTIVITY.equals(RECUIT)) {
			activity.startActivity(new Intent(getContext(), RecuitNavigationPublishPost.class));}
			break;
		case R.id.imgView_search:
			LogUtil.p(tag,"imgView_search被点了");
			if(ACTIVITY.equals(BBS)) {
			activity.startActivity(new Intent(getContext(), NavigationSearch.class));}
			if(ACTIVITY.equals(RECUIT))
			activity.startActivity(new Intent(getContext(), RecuitNavigationSearch.class));
			break;
			//对应聊天界面右上角的小窗口，有建群和搜人-----修改了：改为只有搜索（2016-3-18）
		case R.id.imgView__chat_search:
//			popupWindow.showAsDropDown(v);
			activity.startActivity(intentSearch);
			break;
//			
		case R.id.bbs:
			LogUtil.p(tag,"bbs被点了");
			if(ACTIVITY.equals(BBS)) 
				slidingMenu.toggle(false);
			else
			activity.startActivity(intent1);
			activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.chat:
			LogUtil.p(tag,"chat被点了");
			if(ACTIVITY.equals(CHAT)) 
				slidingMenu.toggle(false);
			else
			activity.startActivity(intent2);
			activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.recuit:
			LogUtil.p(tag,"recuit被点了");
			if(ACTIVITY.equals(RECUIT)) 
				slidingMenu.toggle(false);
			else
			activity.startActivity(intent3);
			activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.user_center:
			LogUtil.p(tag,"user_center被点了");
			activity.startActivity(intent5);
			break;
//
		case R.id.news:
			LogUtil.p(tag,"news被点了");
			if(ACTIVITY.equals(SCHOOLNEWS)) 
				slidingMenu.toggle(false);
			else
			activity.startActivity(intent4);
			LogUtil.p(tag, "SCHOOLNEWS跳转");
			break;
		
		case R.id.imgView_invite:
			LogUtil.p(tag,"推荐校友被点了");
			activity.startActivity(intentRecommend);
			break;
			
		case R.id.btn_slide_exit:
			LogUtil.p(tag,"btn_slide_exit被点了");
			new Thread(exitRunnable).start();
			break;
			
		case R.id.btn_headimage:
			LogUtil.p(tag,"btn_headimage头像被点了");
			activity.startActivity(intent5);
			break;
			
		case R.id.setting:
			LogUtil.p(tag,"设置被点击");
			activity.startActivity(intent6);
			break;
		}
		
	}
	
	Runnable exitRunnable = new Runnable() {

		@Override
		public void run() {
			Log.i("---->>>>>>", "userId = " + BaseActivity.getSelf().getUserID());

			if(BaseActivity.con==null){
				LogUtil.p(tag, "con为空");
			}else{
			BaseActivity.con.shutDownSocketChannel();
			}
//			File data=new File("/data/data/"+getPackageName().toString()+"/shared_prefs","data.xml");
//			File count=new File("/data/data/"+getPackageName().toString()+"/shared_prefs","count.xml");
			
			
			File data=new File("/data/data/"+activity.getPackageName()+"/shared_prefs","data.xml");
			File count=new File("/data/data/"+activity.getPackageName()+"/shared_prefs","count.xml");
			if(data.exists()==true){
				
				data.delete();
				Log.i(tag, "data删除成功");
			}
			else{
				Log.i(tag, "data 删除不成功");
			}
			if(count.exists()==true){
				count.delete();
				Log.i(tag, "count删除成功");
			}
			else{
				Log.i(tag, "count 删除不成功");
			}
			
			if(!BaseActivity.queue.isEmpty()){
				BaseActivity.queue.clear();
				LogUtil.p(tag, "exit--queue:"+BaseActivity.queue.toString());
			}
			redirectTo();
		}

	};
	
	//2015-10-24之前注释
		private void redirectTo() {
			intent0.putExtra("login", Config.BE_OFF_LOGIN);
			activity.startActivity(intent0);
			activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			
			Log.i(tag, "main view queue size="+BaseActivity.queue.size());
			
		}
		
		//initPopupWindow()被注释掉的原因是因为我把搜索群和新建群都给弄没了
//		void initPopupWindow(){
//			popupWindow = new MtitlePopupWindow(activity);
//			popupWindow.setOnPopupWindowClickListener(new OnPopupWindowClickListener() {
//				
//				@Override
//				public void onPopupWindowItemClick(int position) {
//					//你要做的事
//					Log.i(tag, "********");
//					if(position==0){
//						Log.i(tag, "position被选中是0");
//						activity.startActivity(intentCreateGroup);
//					}
//					else if(position==1){
//						Log.i(tag, "position被选中是1");
//						activity.startActivity(intentSearch);
//					}
//				}
//			});
		
		
	

		

}
