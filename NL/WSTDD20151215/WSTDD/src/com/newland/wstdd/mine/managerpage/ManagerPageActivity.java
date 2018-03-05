/**
 * 
 */
package com.newland.wstdd.mine.managerpage;


import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.bean.TddActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.widget.PengTextView;
import com.newland.wstdd.find.categorylist.detail.FindChairDetailActivity;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.mine.applyList.ManagerApplyListActivity;
import com.newland.wstdd.mine.beanresponse.TddUserCertificate;
import com.newland.wstdd.mine.managerpage.activitycode.ActivityCodeActivity;
import com.newland.wstdd.mine.managerpage.beanrequest.OnTddRecommendReq;
import com.newland.wstdd.mine.managerpage.beanrequest.OpenActivityPeoplesReq;
import com.newland.wstdd.mine.managerpage.beanrequest.RegistrationCheckReq;
import com.newland.wstdd.mine.managerpage.beanrequest.RegistrationStateReq;
import com.newland.wstdd.mine.managerpage.beanresponse.OnTddRecommendRes;
import com.newland.wstdd.mine.managerpage.beanresponse.OpenActivityPeoplesRes;
import com.newland.wstdd.mine.managerpage.beanresponse.RegistrationCheckRes;
import com.newland.wstdd.mine.managerpage.beanresponse.RegistrationStateRes;
import com.newland.wstdd.mine.managerpage.handle.ManagerPagerSingleActivityHandle;
import com.newland.wstdd.mine.managerpage.handle.ManagerpageHandle;
import com.newland.wstdd.mine.managerpage.ilike.LikeListActivity;
import com.newland.wstdd.mine.managerpage.multitext.MultiTextActivity;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.newland.wstdd.originate.beanrequest.SingleActivityReq;
import com.newland.wstdd.originate.beanresponse.SingleActivityRes;
import com.newland.wstdd.originate.origateactivity.OriginateChairActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 管理页面
 * 
 * @author H81 2015-11-10
 * 
 */
/**
 * @author H81 2015-11-12
 * 
 */

public class ManagerPageActivity extends BaseFragmentActivity implements OnPostListenerInterface, OnCheckedChangeListener, IWXAPIEventHandler {
	private AppContext appContext = AppContext.getAppContext();

	/**
	 * 分享
	 */
	private PopupWindow popupWindow;// 分享窗口
	/**
	 * 分享相关的
	 */
	// 微信
	private static final String appid = "wx1b84c30d9f380c89";// 微信的appid
	private IWXAPI wxApi;// 微信的API
	// QQ
	private Tencent mTencent;
	private static final String APP_ID = "1104957952";


	//注册广播
	private IntentFilter filter;// 定一个广播接收过滤器
	private IntentFilter filter2;//用于接收处理“活动详情”界面的广播
	private TextView activity_managerpage_activityType_tv;//活动类型
	private TextView activity_managerpage_activityTitle_tv;//活动标题

	private com.newland.wstdd.common.widget.PengTextView mActivity_managerpage_edit;// 活动编辑
	private com.newland.wstdd.common.widget.PengTextView mActivity_managerpage_detail;// 活动预览
	private com.newland.wstdd.common.widget.PengTextView mActivity_managerpage_signlist;// 报名名单
	private com.newland.wstdd.common.widget.PengTextView mActivity_managerpage_share;// 分享
	private com.newland.wstdd.common.widget.PengTextView mActivity_managerpage_groupsend;// 群发消息
	private com.newland.wstdd.common.widget.PengTextView mActivity_managerpage_activitycode;// 活动二维码
	private LinearLayout fouritem_ll;//四个子项的LinearLayout
	
	// 报名
	private LinearLayout mActivity_managerpage_signup_ll;
	private TextView mActivity_managerpage_signup_content_tv;
	private TextView mActivity_managerpage_signup_tv;
	// 评论
	private LinearLayout mActivity_managerpage_comment_ll;
	private TextView mActivity_managerpage_comment_content_tv;
	private TextView mActivity_managerpage_comment_tv;
	// 点赞
	private LinearLayout mActivity_managerpage_like_ll;
	private TextView mActivity_managerpage_like_content_tv;
	private TextView mActivity_managerpage_like_tv;
	// 阅读
	private TextView mActivity_managerpage_read_content_tv;
	// // 支付方式
	// private RelativeLayout mActivity_managerpage_payment_rl;
	// private LinearLayout mActivity_managerpage_payment_content_ll;
	// private TextView mActivity_managerpage_payment_cash_tv;// 货到付款
	// private TextView mActivity_managerpage_payment_online_tv;// 在线支付
	// private ImageView mActivity_managerpage_payment_line_iv;// 下划线
	// private ImageView mActivity_managerpage_payment_extendable_iv;// 下拉箭头
	// // 配送方式
	// private RelativeLayout mActivity_managerpage_distributionmethod_rl;
	// private LinearLayout mActivity_managerpage_distributionmethod_content_ll;
	// private TextView mActivity_managerpage_distributionmethod_self_tv;// 自提
	// private TextView mActivity_managerpage_distributionmethod_safe_tv;// 当面交易
	// private TextView mActivity_managerpage_distributionmethod_mail_tv;// 邮寄
	// private ImageView mActivity_managerpage_distributionmethod_line_iv;// 下划线
	// private ImageView mActivity_managerpage_distributionmethod_extendable_iv;

	// private com.newland.wstdd.common.widget.PengTextView
	// mActivity_managerpage_share_penttv;// 分享
	// private com.newland.wstdd.common.widget.PengTextView
	// mActivity_managerpage_invite_penttv;// 邀请参与
	// private com.newland.wstdd.common.widget.PengTextView
	// mActivity_managerpage_cover_penttv;// 设置封面
	// private com.newland.wstdd.common.widget.PengTextView
	// mActivity_managerpage_secretary_penttv;// 设置团秘
	// private com.newland.wstdd.common.widget.PengTextView
	// mActivity_managerpage_originateagain_penttv;// 再次发起
	// private com.newland.wstdd.common.widget.PengTextView
	// mActivity_managerpage_exportlist_penttv;// 导出名单
	// private com.newland.wstdd.common.widget.PengTextView
	// mActivity_managerpage_mass_penttv;// 群发消息
	// private com.newland.wstdd.common.widget.PengTextView
	// mActivity_managerpage_delete_penttv;// 删除活动

	private CheckBox mActivity_managerpage_isneedcheck_cb;// 报名需要审核
	private CheckBox mActivity_managerpage_ispublishsignnum_cb;// 公开报名人数
	private CheckBox mActivity_managerpage_isnotification_cb;// 团员报名通知
	private CheckBox mActivity_managerpage_isrecommend_cb;// 想不想上团大大热点推荐
	/** 活动状态（报名中）*/
	private LinearLayout mActivity_managerpage_activitystate_ll;
	private TextView mActivity_managerpage_activitystate_tv;
	private ImageView mActivity_managerpage_activitystate_iv;
	private LinearLayout isTD_ll;//是团大or团秘
	private LinearLayout botttom_ll;//底部（复制活动、删除活动）
	
	private PopupWindow statePopupWindow;
	/** 活动状态（报名中）*/
	// // 公开范围
	// private LinearLayout mActivity_managerpage_publicrange_ll;
	// private TextView mActivity_managerpage_publicrange_tv;
	// private ImageView mActivity_managerpage_publicrange_iv;

	// 服务器返回的相关操作
	OpenActivityPeoplesRes openActivityPeoplesRes;// 设置是否公开活动人员
	OnTddRecommendRes onTddRecommendRes;// 设置是否上团大大热搜
	RegistrationStateRes registrationStateRes;// 设置报名状态
	RegistrationCheckRes registrationCheckRes;// 报名是否需要通过审核
	ManagerpageHandle handler = new ManagerpageHandle(this);
	//请求角色的网络请求，用于显示不同的界面布局
	SingleActivityRes singleActivityRes = new SingleActivityRes();//是团大、团秘、团员
	ManagerPagerSingleActivityHandle singleActivityHandle = new ManagerPagerSingleActivityHandle(this);
	private int signRole = 9 ;//报名用户角色 1.团长 2.团秘 9.团员
	private String isLeader = "false";//是否是团大
	private TddActivity tddActivity;// 活动对象
	

	@Override
	protected void processMessage(Message msg) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_managerpage);
		// 分享
		// QQ
		final Context ctxContext = this.getApplicationContext();
		mTencent = Tencent.createInstance(APP_ID, ctxContext);
		mHandler = new Handler();

		// weixin
		wxApi = WXAPIFactory.createWXAPI(this, appid);
		wxApi.registerApp(appid);
		getIntentData();

		setTitle();// 设置标题栏
		initView();// 初始化控件
		
		//
		// 注册广播
		filter = new IntentFilter("RECEIVE_TDDACTIVITY");// 用来接收从这个界面出去之后回来之后的tddactivity
		registerReceiver(broadcastReceiver, filter);// 在对象broadcastReceiver中进行接收的相应处理
		filter2 = new IntentFilter("ManagerPageActivityRefresh");
		registerReceiver(broadcastReceiver2, filter2);
		// 获取服务器的相关测试
		// openActivityPeoples();// 是否公开活动的报名人数
		// onTddRecommend();// 是否上团大大热搜
		// setRegistrationState();// 设置报名的状态
		// setRegistrationCheck();// 是否需要审核
	}

	/**
	 * 接收传递过来的tddActivity数据
	 * 
	 */
	private void getIntentData() {
		singleActivityRes = (SingleActivityRes) getIntent().getSerializableExtra("singleActivityRes");
		tddActivity = singleActivityRes.getTddActivity();
		
		/*tddActivity = (TddActivity) getIntent().getSerializableExtra("tddActivity");*/
//		judgeRole(tddActivity.getActivityId());
	}

	/**
	 * 单个活动查询(判断角色、更新阅读数等)
	 * @param activityId
	 * @return signRole	报名用户角色 1.团长 2.团秘 9.团员	Int,若userSignstate显示不参加，则null
	 */
	/*private void judgeRole(final String activityId){
		super.refresh();
		try {
			new Thread(){
				public void run() {
					//需要发送一个request对象进行请求
					SingleActivityReq singleActivityReq = new SingleActivityReq();
					singleActivityReq.setActivityId(activityId);
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<SingleActivityRes> retMsg = mgr.getSingleActivityInfo(singleActivityReq);
					Message message = new Message();
					message.what  = ManagerPagerSingleActivityHandle.SINGLE_ACTIVITY;
					//访问服务器成功1，否则访问服务器失败
					if (retMsg.getCode() == 1) {
						singleActivityRes = (SingleActivityRes) retMsg.getObj();
						message.obj = singleActivityRes;
					}else {
						message.obj = retMsg.getMsg();
					}
					singleActivityHandle.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}*/
	private void setTitle() {
		ImageView left_btn = (ImageView) findViewById(R.id.head_left_iv);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		TextView right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText("我的活动");
		if (left_btn != null) {
			left_btn.setVisibility(View.VISIBLE);
			left_btn.setOnClickListener(this);
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {
			right_tv.setVisibility(View.VISIBLE);
			right_tv.setText("签到");
			right_tv.setTextColor(getResources().getColor(R.color.red));
			right_tv.setOnClickListener(this);
		}
	}

	public void initView() {
		activity_managerpage_activityType_tv = (TextView) findViewById(R.id.activity_managerpage_activityType_tv);
		activity_managerpage_activityTitle_tv = (TextView) findViewById(R.id.activity_managerpage_activityTitle_tv);
		mActivity_managerpage_edit = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_managerpage_edit);
		mActivity_managerpage_detail = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_managerpage_detail);
		mActivity_managerpage_signlist = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_managerpage_signlist);
		mActivity_managerpage_share = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_managerpage_share);
		mActivity_managerpage_groupsend = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_managerpage_groupsend);
		mActivity_managerpage_activitycode = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_managerpage_activitycode);
		mActivity_managerpage_signup_ll = (LinearLayout) findViewById(R.id.activity_managerpage_signup_ll);
		mActivity_managerpage_signup_content_tv = (TextView) findViewById(R.id.activity_managerpage_signup_content_tv);
		mActivity_managerpage_signup_tv = (TextView) findViewById(R.id.activity_managerpage_signup_tv);
		mActivity_managerpage_comment_ll = (LinearLayout) findViewById(R.id.activity_managerpage_comment_ll);
		mActivity_managerpage_comment_content_tv = (TextView) findViewById(R.id.activity_managerpage_comment_content_tv);
		mActivity_managerpage_comment_tv = (TextView) findViewById(R.id.activity_managerpage_comment_tv);
		mActivity_managerpage_like_ll = (LinearLayout) findViewById(R.id.activity_managerpage_like_ll);
		mActivity_managerpage_like_content_tv = (TextView) findViewById(R.id.activity_managerpage_like_content_tv);
		mActivity_managerpage_like_tv = (TextView) findViewById(R.id.activity_managerpage_like_tv);
		mActivity_managerpage_read_content_tv = (TextView) findViewById(R.id.activity_managerpage_read_content_tv);
		mActivity_managerpage_activitystate_ll = (LinearLayout) findViewById(R.id.activity_managerpage_activitystate_ll);
		mActivity_managerpage_activitystate_tv = (TextView) findViewById(R.id.activity_managerpage_activitystate_tv);
		mActivity_managerpage_activitystate_iv = (ImageView) findViewById(R.id.activity_managerpage_activitystate_iv);
		mActivity_managerpage_isneedcheck_cb = (CheckBox) findViewById(R.id.activity_managerpage_isneedcheck_cb);
		mActivity_managerpage_ispublishsignnum_cb = (CheckBox) findViewById(R.id.activity_managerpage_ispublishsignnum_cb);
		mActivity_managerpage_isnotification_cb = (CheckBox) findViewById(R.id.activity_managerpage_isnotification_cb);
		mActivity_managerpage_isrecommend_cb = (CheckBox) findViewById(R.id.activity_managerpage_isrecommend_cb);
		fouritem_ll = (LinearLayout) findViewById(R.id.fouritem_ll);
		isTD_ll = (LinearLayout) findViewById(R.id.isTD_ll);
		botttom_ll = (LinearLayout) findViewById(R.id.botttom_ll);
		
		fouritem_ll.getLayoutParams().height = AppContext.getAppContext().getScreenWidth()/4;
		
		initData();
		setClickListener();

		mActivity_managerpage_isneedcheck_cb.setOnCheckedChangeListener(this);
		mActivity_managerpage_ispublishsignnum_cb.setOnCheckedChangeListener(this);
		mActivity_managerpage_isnotification_cb.setOnCheckedChangeListener(this);
		mActivity_managerpage_isrecommend_cb.setOnCheckedChangeListener(this);
		
		initViewData();
	}

	/**
	 * 初始化界面数据
	 */
	private void initData() {
		activity_managerpage_activityType_tv.setText(StringUtil.intType2Str(tddActivity.getActivityType()));
		activity_managerpage_activityTitle_tv.setText(tddActivity.getActivityTitle());
		mActivity_managerpage_signup_content_tv.setText(tddActivity.getSignCount() + "");// 报名人数
		mActivity_managerpage_comment_content_tv.setText(tddActivity.getCommentCount() + "");// 评论
		mActivity_managerpage_like_content_tv.setText(tddActivity.getLikeCount() + "");// 点赞数量
		mActivity_managerpage_read_content_tv.setText(tddActivity.getViewCount() + "");// 阅读人数

		if (tddActivity.getStatus()==1) {//status： 2 关闭报名，1开启报名
			mActivity_managerpage_activitystate_tv.setText("开启报名");
		}else if (tddActivity.getStatus() == 2) {
			mActivity_managerpage_activitystate_tv.setText("关闭报名");
		}
		if (tddActivity.getNeedAudit() == 1) {// 报名需要审核1.需要 2.不需要
			mActivity_managerpage_isneedcheck_cb.setSelected(true);
		} else if (tddActivity.getNeedAudit() == 2) {
			mActivity_managerpage_isneedcheck_cb.setSelected(false);
		} else if (tddActivity.getNeedPublicSigninfo() == 1) {// 是否公开报名情况
																// 1.公开2不公开
			mActivity_managerpage_ispublishsignnum_cb.setSelected(true);
		} else if (tddActivity.getNeedPublicSigninfo() == 2) {
			mActivity_managerpage_ispublishsignnum_cb.setSelected(false);
		}
		// TODO 缺少团员报名通知接口

		else if (StringUtil.isNotEmpty(tddActivity.getActivityId()) && StringUtil.isNotEmpty(tddActivity.getShareScope() + "") && StringUtil.isNotEmpty(tddActivity.getPublicWeight() + "")) {// 想不想上团大大热点推荐
			// 要上
			mActivity_managerpage_isrecommend_cb.setSelected(true);
		} else {
			mActivity_managerpage_isrecommend_cb.setSelected(false);
		}
	}

	private void setClickListener() {
		mActivity_managerpage_edit.setOnClickListener(this);
		mActivity_managerpage_detail.setOnClickListener(this);
		mActivity_managerpage_signlist.setOnClickListener(this);
		mActivity_managerpage_share.setOnClickListener(this);
		mActivity_managerpage_groupsend.setOnClickListener(this);
		mActivity_managerpage_activitycode.setOnClickListener(this);
		// mActivity_managerpage_signup_ll.setOnClickListener(this);
		// mActivity_managerpage_comment_ll.setOnClickListener(this);
		 mActivity_managerpage_like_ll.setOnClickListener(this);
		mActivity_managerpage_activitystate_ll.setOnClickListener(this);
		mActivity_managerpage_activitystate_tv.setOnClickListener(this);
		mActivity_managerpage_activitystate_iv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.activity_managerpage_edit:// 活动编辑

			intent = new Intent(this, OriginateChairActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("tddActivity", tddActivity);
			bundle1.putString("activity_action", "edit");
			intent.putExtras(bundle1);
			startActivity(intent);
			break;
		case R.id.activity_managerpage_detail:// 活动详情
			intent = new Intent(ManagerPageActivity.this, FindChairDetailActivity.class);// 跳转到详情
			Bundle bundle2 = new Bundle();
			if (singleActivityRes != null) {
				bundle2.putSerializable("tddActivity", singleActivityRes.getTddActivity());
			}
			intent.putExtras(bundle2);
			startActivity(intent);
			break;
		// TODO 报名名单
		case R.id.activity_managerpage_signlist:// 报名名单
			// intent = new Intent(this, MineRegistrationListActivity.class);
			// startActivity(intent);
			intent = new Intent(this, ManagerApplyListActivity.class);
			intent.putExtra("activityId", tddActivity.getActivityId());
			intent.putExtra("signRole", signRole);
			intent.putExtra("isLeader", isLeader);
			startActivity(intent);
			break;
		case R.id.activity_managerpage_share:// 分享
			UiHelper.ShowOneToast(this, "未开发");
			getPopWindow();

			break;
		case R.id.activity_managerpage_groupsend:// 群发消息
			intent = new Intent(this, MultiTextActivity.class);
			intent.putExtra("activityId", tddActivity.getActivityId());
			intent.putExtra("nickName", tddActivity.getNickName());
			startActivity(intent);
			break;
		case R.id.activity_managerpage_activitycode:// 活动二维码
			intent = new Intent(ManagerPageActivity.this, ActivityCodeActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("tddActivity", tddActivity);
			intent.putExtras(bundle);
			startActivity(intent);
			break;

		// /**
		// * 报名
		// */
		// case R.id.activity_managerpage_signup_ll:
		//
		// break;
		// /**
		// * 评论
		// */
		// case R.id.activity_managerpage_comment_ll:
		// intent = new Intent(this, MineRegistrationListActivity.class);
		// startActivity(intent);
		// break;
		// /**
		// * 点赞
		// */
		 case R.id.activity_managerpage_like_ll:
		 intent = new Intent(this, LikeListActivity.class);
		 Bundle bundle3=new Bundle();
		 bundle3.putSerializable("tddactivity", tddActivity);
		 intent.putExtras(bundle3);
		 startActivity(intent);
		 break;
		/**
		 * 支付方式
		 */
		// case R.id.activity_managerpage_payment_rl:
		// UiHelper.ShowOneToast(this, "未开发");
		// if (mActivity_managerpage_payment_content_ll.getVisibility() ==
		// View.VISIBLE) {
		// mActivity_managerpage_payment_line_iv.setVisibility(View.GONE);
		// mActivity_managerpage_payment_content_ll.setVisibility(View.GONE);
		// mActivity_managerpage_payment_extendable_iv.setImageDrawable(getResources().getDrawable(R.drawable.right_expansion));
		// } else if (mActivity_managerpage_payment_content_ll.getVisibility()
		// == View.GONE) {
		// mActivity_managerpage_payment_line_iv.setVisibility(View.VISIBLE);
		// mActivity_managerpage_payment_content_ll.setVisibility(View.VISIBLE);
		// mActivity_managerpage_payment_extendable_iv.setImageDrawable(getResources().getDrawable(R.drawable.down_expansion));
		// }
		// break;
		// case R.id.activity_managerpage_payment_cash_tv:
		// setPaymentColor(R.id.activity_managerpage_payment_cash_tv);
		// break;
		// case R.id.activity_managerpage_payment_online_tv:
		// setPaymentColor(R.id.activity_managerpage_payment_online_tv);
		// break;
		/**
		 * 配送方式
		 */
		// case R.id.activity_managerpage_distributionmethod_rl:
		// if
		// (mActivity_managerpage_distributionmethod_content_ll.getVisibility()
		// == View.GONE) {
		// mActivity_managerpage_distributionmethod_content_ll.setVisibility(View.VISIBLE);
		// mActivity_managerpage_distributionmethod_line_iv.setVisibility(View.VISIBLE);
		// mActivity_managerpage_distributionmethod_extendable_iv.setImageDrawable(getResources().getDrawable(R.drawable.down_expansion));
		// } else if
		// (mActivity_managerpage_distributionmethod_content_ll.getVisibility()
		// == View.VISIBLE) {
		// mActivity_managerpage_distributionmethod_content_ll.setVisibility(View.GONE);
		// mActivity_managerpage_distributionmethod_line_iv.setVisibility(View.GONE);
		// mActivity_managerpage_distributionmethod_extendable_iv.setImageDrawable(getResources().getDrawable(R.drawable.right_expansion));
		// }
		// break;
		// case R.id.activity_managerpage_distributionmethod_self_tv:
		// setDistributionMethodColor(R.id.activity_managerpage_distributionmethod_self_tv);
		// break;
		// case R.id.activity_managerpage_distributionmethod_safe_tv:
		// setDistributionMethodColor(R.id.activity_managerpage_distributionmethod_safe_tv);
		// break;
		// case R.id.activity_managerpage_distributionmethod_mail_tv:
		// setDistributionMethodColor(R.id.activity_managerpage_distributionmethod_mail_tv);
		// break;
		// case R.id.activity_managerpage_share_penttv:// 分享
		// UiHelper.ShowOneToast(this, "未开发");
		// break;
		// case R.id.activity_managerpage_invite_penttv:// 邀请参与
		// UiHelper.ShowOneToast(this, "未开发");
		// break;
		// case R.id.activity_managerpage_cover_penttv:// 设置封面
		// UiHelper.ShowOneToast(this, "未开发");
		// break;
		// case R.id.activity_managerpage_secretary_penttv:// 设置团秘
		// UiHelper.ShowOneToast(this, "未开发");
		// break;
		// case R.id.activity_managerpage_originateagain_penttv:// 再次发起
		// UiHelper.ShowOneToast(this, "未开发");
		// break;
		// case R.id.activity_managerpage_exportlist_penttv:// 导出名单
		// UiHelper.ShowOneToast(this, "未开发");
		// break;
		// case R.id.activity_managerpage_mass_penttv:// 群发消息
		// UiHelper.ShowOneToast(this, "未开发");
		// break;
		// case R.id.activity_managerpage_delete_penttv:// 删除活动
		// UiHelper.ShowOneToast(this, "未开发");
		// break;
		/**
		 * 活动状态
		 */
		case R.id.activity_managerpage_activitystate_ll:
		case R.id.activity_managerpage_activitystate_tv:
		case R.id.activity_managerpage_activitystate_iv:
			getStatePopWind();//活动状态的popwindow
			statePopupWindow.showAsDropDown(v);
			break;
		/**
		 * 公开范围
		 */
		// case R.id.activity_managerpage_publicrange_ll:
		// UiHelper.ShowOneToast(this, "未开发");
		// break;
		// case R.id.activity_managerpage_publicrange_tv:
		// UiHelper.ShowOneToast(this, "未开发");
		// break;
		// case R.id.activity_managerpage_publicrange_iv:
		// UiHelper.ShowOneToast(this, "未开发");
		// break;
		case R.id.head_left_iv:// 返回
			finish();
			break;
		case R.id.head_right_tv:// 更多
			UiHelper.ShowOneToast(this, "签到");
			break;
		default:
			break;
		}
	}

	/**
	 * 获取活动状态popwindow
	 */
	private void getStatePopWind() {
		if (statePopupWindow != null) {
			statePopupWindow.dismiss();
			return;
		} else {
			initStatePopupWindow();
		}
	}

	/**
	 * 初始化活动状态的popwindow
	 */
	TextView closeApplyTextView,openApplyTextView;//开启、关闭报名
	private void initStatePopupWindow() {
		//TODO
		View popupWindow_view1 = this.getLayoutInflater().inflate(R.layout.activity_manager_activitystate_popwindow, null, false);
		statePopupWindow = new PopupWindow(popupWindow_view1, AppContext.getAppContext().getScreenWidth() , LayoutParams.WRAP_CONTENT, true);
		statePopupWindow.setOutsideTouchable(true);// 点击区域外，关闭
		statePopupWindow.setTouchable(true);
		popupWindow_view1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (statePopupWindow != null && statePopupWindow.isShowing()) {
					statePopupWindow.dismiss();
					statePopupWindow = null;
				}
				return false;
			}
		});
		closeApplyTextView=(TextView) popupWindow_view1.findViewById(R.id.close_apply_tv);
		openApplyTextView = (TextView) popupWindow_view1.findViewById(R.id.open_apply_tv);
		closeApplyTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openApplyTextView.setTextColor(getResources().getColor(R.color.grey_text));
				closeApplyTextView.setTextColor(getResources().getColor(R.color.black));
				closeApplyTextView.setTextSize(18);
				openApplyTextView.setTextSize(14);
				//(activityId 和 status不能为空，status： 2 关闭报名，1开启报名)
				tddActivity.setStatus(1);
				mActivity_managerpage_activitystate_tv.setText("关闭报名");
			}
		});
		openApplyTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openApplyTextView.setTextColor(getResources().getColor(R.color.black));
				closeApplyTextView.setTextColor(getResources().getColor(R.color.grey_text));
				closeApplyTextView.setTextSize(14);
				openApplyTextView.setTextSize(18);
				tddActivity.setStatus(2);
				mActivity_managerpage_activitystate_tv.setText("开启报名");
			}
		});
		// 设置透明度
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.3f;
		getWindow().setAttributes(lp);
		statePopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
				setRegistrationState();
			}
		});
//		statePopupWindow.setFocusable(true);
		
		statePopupWindow.update();
		// 显示窗口 位置
		statePopupWindow.showAtLocation(this.findViewById(R.id.layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	@Override
	public void refresh() {

	}

	/**
	 * 设置支付方式字体和边框颜色
	 * 
	 * @param id
	 *            控件id
	 */
	// private void setPaymentColor(int id) {
	// switch (id) {
	// case R.id.activity_managerpage_payment_cash_tv:// 货到付款
	// mActivity_managerpage_payment_cash_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_rectangleredboderstyle));
	// mActivity_managerpage_payment_cash_tv.setTextColor(getResources().getColor(R.color.red));
	// mActivity_managerpage_payment_online_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_rectanglegrayboderstyle));
	// mActivity_managerpage_payment_online_tv.setTextColor(getResources().getColor(R.color.black));
	// break;
	// case R.id.activity_managerpage_payment_online_tv:// 在线支付
	// mActivity_managerpage_payment_cash_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_rectanglegrayboderstyle));
	// mActivity_managerpage_payment_cash_tv.setTextColor(getResources().getColor(R.color.black));
	// mActivity_managerpage_payment_online_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_rectangleredboderstyle));
	// mActivity_managerpage_payment_online_tv.setTextColor(getResources().getColor(R.color.red));
	// break;
	// default:
	// break;
	// }
	// }

	/**
	 * 设置配送方式字体和边框的颜色
	 * 
	 * @param id
	 */
	// private void setDistributionMethodColor(int id) {
	// switch (id) {
	// case R.id.activity_managerpage_distributionmethod_self_tv:// 货到付款
	// mActivity_managerpage_distributionmethod_self_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_rectangleredboderstyle));
	// mActivity_managerpage_distributionmethod_self_tv.setTextColor(getResources().getColor(R.color.red));
	// mActivity_managerpage_distributionmethod_safe_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_rectanglegrayboderstyle));
	// mActivity_managerpage_distributionmethod_safe_tv.setTextColor(getResources().getColor(R.color.black));
	// mActivity_managerpage_distributionmethod_mail_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_rectanglegrayboderstyle));
	// mActivity_managerpage_distributionmethod_mail_tv.setTextColor(getResources().getColor(R.color.black));
	// break;
	// case R.id.activity_managerpage_distributionmethod_safe_tv:// 在线支付
	// mActivity_managerpage_distributionmethod_self_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_rectanglegrayboderstyle));
	// mActivity_managerpage_distributionmethod_self_tv.setTextColor(getResources().getColor(R.color.black));
	// mActivity_managerpage_distributionmethod_safe_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_rectangleredboderstyle));
	// mActivity_managerpage_distributionmethod_safe_tv.setTextColor(getResources().getColor(R.color.red));
	// mActivity_managerpage_distributionmethod_mail_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_rectanglegrayboderstyle));
	// mActivity_managerpage_distributionmethod_mail_tv.setTextColor(getResources().getColor(R.color.black));
	// break;
	// case R.id.activity_managerpage_distributionmethod_mail_tv:
	// mActivity_managerpage_distributionmethod_self_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_rectanglegrayboderstyle));
	// mActivity_managerpage_distributionmethod_self_tv.setTextColor(getResources().getColor(R.color.black));
	// mActivity_managerpage_distributionmethod_safe_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_rectanglegrayboderstyle));
	// mActivity_managerpage_distributionmethod_safe_tv.setTextColor(getResources().getColor(R.color.black));
	// mActivity_managerpage_distributionmethod_mail_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_rectangleredboderstyle));
	// mActivity_managerpage_distributionmethod_mail_tv.setTextColor(getResources().getColor(R.color.red));
	// break;
	// default:
	// break;
	// }
	// }

	/***
	 * 跟服务器的相关操作
	 */
	// 是否公开后动的报名人数
	private void openActivityPeoples() {
		super.refresh();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					OpenActivityPeoplesReq reqInfo = new OpenActivityPeoplesReq();
					TddUserCertificate tddUserCertificate = new TddUserCertificate();

					if (mActivity_managerpage_ispublishsignnum_cb.isChecked()) {
						tddActivity.setNeedPublicSigninfo(1);// 公开
					} else {
						tddActivity.setNeedPublicSigninfo(2);// 关闭
					}
					tddUserCertificate.setUserId(tddActivity.getActivityId());
					tddUserCertificate.setUserName(tddActivity.getUserName());
					tddUserCertificate.setHeadimgurl(tddActivity.getHeadimgurl());
					tddUserCertificate.setNickName(tddActivity.getNickName());
					tddUserCertificate.setMobilePhone(tddActivity.getUserMobilePhone());
					// tddUserCertificate.setSex(tddActivity.gets);
					// tddUserCertificate.setEmail(tddActivity.get);
					// tddUserCertificate.setIdentity(identity);
					// tddUserCertificate.setCerStatus(cerStatus);

					reqInfo.setTddActivity(tddActivity);
					reqInfo.setTddUserCertificate(tddUserCertificate);
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<OpenActivityPeoplesRes> ret = mgr.getOpenActivityPeoplesInfo(reqInfo, ManagerPageActivity.this.tddActivity.getActivityId());// 泛型类，
					Message message = new Message();
					message.what = ManagerpageHandle.OPENACTIVITY_PEOPLES;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						openActivityPeoplesRes = (OpenActivityPeoplesRes) ret.getObj();
						message.obj = openActivityPeoplesRes;
					} else {
						message.obj = ret.getMsg();
					}
					handler.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 设置是否上团大大热搜
	private void onTddRecommend() {
		super.refresh();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					OnTddRecommendReq reqInfo = new OnTddRecommendReq();
					TddUserCertificate tddUserCertificate = new TddUserCertificate();

					if (mActivity_managerpage_isrecommend_cb.isChecked()) {
						tddActivity.setNeedAudit(1);
					} else {
						tddActivity.setNeedAudit(2);
					}
					tddUserCertificate.setUserId(tddActivity.getActivityId());
					tddUserCertificate.setUserName(tddActivity.getUserName());
					tddUserCertificate.setHeadimgurl(tddActivity.getHeadimgurl());
					tddUserCertificate.setNickName(tddActivity.getNickName());
					tddUserCertificate.setMobilePhone(tddActivity.getUserMobilePhone());
					// tddUserCertificate.setSex(tddActivity.gets);
					// tddUserCertificate.setEmail(tddActivity.get);
					// tddUserCertificate.setIdentity(identity);
					// tddUserCertificate.setCerStatus(cerStatus);
					reqInfo.setTddActivity(tddActivity);
					reqInfo.setTddUserCertificate(tddUserCertificate);
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<OnTddRecommendRes> ret = mgr.getOnTddRecommendInfo(reqInfo, tddActivity.getActivityId());// 泛型类，
					Message message = new Message();
					message.what = ManagerpageHandle.ONTDD_RECOMMENT;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						onTddRecommendRes = (OnTddRecommendRes) ret.getObj();
						message.obj = onTddRecommendRes;
					} else {
						message.obj = ret.getMsg();
					}
					handler.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 设置报名的状态
	private void setRegistrationState() {
		super.refresh();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					RegistrationStateReq reqInfo = new RegistrationStateReq();
					TddUserCertificate tddUserCertificate = new TddUserCertificate();
					
					tddUserCertificate.setUserId(tddActivity.getActivityId());
					tddUserCertificate.setUserName(tddActivity.getUserName());
					tddUserCertificate.setHeadimgurl(tddActivity.getHeadimgurl());
					tddUserCertificate.setNickName(tddActivity.getNickName());
					tddUserCertificate.setMobilePhone(tddActivity.getUserMobilePhone());
					// tddUserCertificate.setSex(tddActivity.gets);
					// tddUserCertificate.setEmail(tddActivity.get);
					// tddUserCertificate.setIdentity(identity);
					// tddUserCertificate.setCerStatus(cerStatus);
					
					reqInfo.setTddActivity(tddActivity);
					reqInfo.setTddUserCertificate(tddUserCertificate);
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<RegistrationStateRes> ret = mgr.getRegistrationStateInfo(reqInfo,tddActivity.getActivityId());// 泛型类，
					Message message = new Message();
					message.what = ManagerpageHandle.REGISTRATION_STATE;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						registrationStateRes = (RegistrationStateRes) ret.getObj();
						message.obj = registrationStateRes;
					} else {
						message.obj = ret.getMsg();
					}
					handler.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 是否需要审核
	private void setRegistrationCheck() {
		super.refresh();
		// try {
		new Thread() {
			public void run() {
				// 需要发送一个request的对象进行请求
				RegistrationCheckReq reqInfo = new RegistrationCheckReq();
				TddUserCertificate tddUserCertificate = new TddUserCertificate();
				// 报名需要审核1.需要 2.不需要
				if (mActivity_managerpage_isneedcheck_cb.isChecked()) {
					tddActivity.setNeedAudit(1);
				} else {
					tddActivity.setNeedAudit(2);
				}
				tddUserCertificate.setUserId(tddActivity.getActivityId());
				tddUserCertificate.setUserName(tddActivity.getUserName());
				tddUserCertificate.setHeadimgurl(tddActivity.getHeadimgurl());
				tddUserCertificate.setNickName(tddActivity.getNickName());
				tddUserCertificate.setMobilePhone(tddActivity.getUserMobilePhone());
				// tddUserCertificate.setSex(tddActivity.gets);
				// tddUserCertificate.setEmail(tddActivity.get);
				// tddUserCertificate.setIdentity(identity);
				// tddUserCertificate.setCerStatus(cerStatus);

				reqInfo.setTddActivity(tddActivity);
				reqInfo.setTddUserCertificate(tddUserCertificate);
				BaseMessageMgr mgr = new HandleNetMessageMgr();
				RetMsg<RegistrationCheckRes> ret = mgr.getRegistrationCheckInfo(reqInfo, tddActivity.getActivityId());// 泛型类，
				Message message = new Message();
				message.what = ManagerpageHandle.REGISTRATION_CHECK;// 设置死
				// 访问服务器成功 1 否则访问服务器失败
				if (ret.getCode() == 1) {
					registrationCheckRes = (RegistrationCheckRes) ret.getObj();
					message.obj = registrationCheckRes;
				} else {
					message.obj = ret.getMsg();
				}
				handler.sendMessage(message);
			};
		}.start();
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.activity_managerpage_isneedcheck_cb:
			System.out.println("报名需要审核---" + isChecked);
			setRegistrationCheck();
			break;
		case R.id.activity_managerpage_ispublishsignnum_cb:
			System.out.println("公开报名人数---" + isChecked);
			openActivityPeoples();
			break;
		case R.id.activity_managerpage_isnotification_cb:
			System.out.println("团员报名通知----" + isChecked);
			break;
		case R.id.activity_managerpage_isrecommend_cb:
			System.out.println("想不想上团大大热点推荐----" + isChecked);
			onTddRecommend();
			break;
		default:
			break;
		}
	}

	@Override
	public void OnHandleResultListener(Object obj, int responseId) {
		try {

			switch (responseId) {
			// 是否公开活动报名人数
			case ManagerpageHandle.OPENACTIVITY_PEOPLES:
				if (dialog != null) {
					dialog.dismiss();
				}
				openActivityPeoplesRes = (OpenActivityPeoplesRes) obj;
				if (openActivityPeoplesRes != null) {
//					UiHelper.ShowOneToast(this, "公开报名请求成功");
					tddActivity = openActivityPeoplesRes.getTddActivity();
				}

				break;
			// 是否上团大大热搜
			case ManagerpageHandle.ONTDD_RECOMMENT:
				if (dialog != null) {
					dialog.dismiss();
				}
				onTddRecommendRes = (OnTddRecommendRes) obj;
				if (onTddRecommendRes != null) {
//					UiHelper.ShowOneToast(this, "是否上团大大热搜请求成功");
					tddActivity = onTddRecommendRes.getTddActivity();
				}
				break;
			// 是否需要审核
			case ManagerpageHandle.REGISTRATION_CHECK:
				if (dialog != null) {
					dialog.dismiss();
				}
				registrationCheckRes = (RegistrationCheckRes) obj;
				if (registrationCheckRes != null) {
//					UiHelper.ShowOneToast(this, "是否需要审核请求成功");
					tddActivity = registrationCheckRes.getTddActivity();
				}
				break;
			case ManagerpageHandle.REGISTRATION_STATE://当前报名状态
				if (dialog != null) {
					dialog.dismiss();
				}
				registrationStateRes = (RegistrationStateRes) obj;
				if (registrationStateRes != null) {
//					UiHelper.ShowOneToast(this, "当前报名状态请求成功");
					tddActivity = registrationStateRes.getTddActivity();
				}
				break;
			/*case ManagerPagerSingleActivityHandle.SINGLE_ACTIVITY://单个活动接口获取SignRole
				if (dialog != null) {
					dialog.dismiss();
				}
//				UiHelper.ShowOneToast(this, "单个活动接口请求成功");
				singleActivityRes = (SingleActivityRes) obj;
				if (singleActivityRes != null) {
					signRole = singleActivityRes.getSignRole();
					isLeader = singleActivityRes.getIsLeader();
					if(signRole == 2){
						isTD_ll.setVisibility(View.GONE);
						botttom_ll.setVisibility(View.GONE);
						mActivity_managerpage_detail.setDrawableTop(getResources().getDrawable(R.drawable.manager_activitydetail_gray));
						mActivity_managerpage_detail.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_detail.getDrawableTop(), null, null);
						mActivity_managerpage_detail.setTextColor(getResources().getColor(R.color.textgray));
						mActivity_managerpage_detail.setCompoundDrawablePadding(3);
						
						mActivity_managerpage_detail.setClickable(false);
						
						mActivity_managerpage_edit.setDrawableTop(getResources().getDrawable(R.drawable.manager_activityedit_gray));
						mActivity_managerpage_edit.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_edit.getDrawableTop(), null, null);
						mActivity_managerpage_edit.setTextColor(getResources().getColor(R.color.textgray));
						mActivity_managerpage_edit.setCompoundDrawablePadding(3);
						mActivity_managerpage_edit.setClickable(false);
						
						setData();
					}else
					if (isLeader.equals("true")) {
						isTD_ll.setVisibility(View.VISIBLE);
						botttom_ll.setVisibility(View.VISIBLE);
						
						mActivity_managerpage_detail.setDrawableTop(getResources().getDrawable(R.drawable.manager_activitydetail));
						mActivity_managerpage_detail.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_detail.getDrawableTop(), null, null);
						mActivity_managerpage_detail.setClickable(true);
						mActivity_managerpage_detail.setCompoundDrawablePadding(3);
						
						mActivity_managerpage_edit.setDrawableTop(getResources().getDrawable(R.drawable.manager_activityedit));
						mActivity_managerpage_edit.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_edit.getDrawableTop(), null, null);
						mActivity_managerpage_edit.setClickable(true);
						mActivity_managerpage_edit.setCompoundDrawablePadding(3);
						
						setData();
					}
					
					//更新阅读数、报名数等数据
					updateData(singleActivityRes);
				break;
				}*/
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	/**
	 * 初始化布局、数值等数据
	 */
	private void initViewData(){
		if (singleActivityRes != null) {
			signRole = singleActivityRes.getSignRole();
			isLeader = singleActivityRes.getIsLeader();
			if(signRole == 2){
				isTD_ll.setVisibility(View.GONE);
				botttom_ll.setVisibility(View.GONE);
				mActivity_managerpage_detail.setDrawableTop(getResources().getDrawable(R.drawable.manager_activitydetail_gray));
				mActivity_managerpage_detail.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_detail.getDrawableTop(), null, null);
				mActivity_managerpage_detail.setTextColor(getResources().getColor(R.color.textgray));
				mActivity_managerpage_detail.setCompoundDrawablePadding(3);
				
				mActivity_managerpage_detail.setClickable(false);
				
				mActivity_managerpage_edit.setDrawableTop(getResources().getDrawable(R.drawable.manager_activityedit_gray));
				mActivity_managerpage_edit.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_edit.getDrawableTop(), null, null);
				mActivity_managerpage_edit.setTextColor(getResources().getColor(R.color.textgray));
				mActivity_managerpage_edit.setCompoundDrawablePadding(3);
				mActivity_managerpage_edit.setClickable(false);
				
				setData();
			}else
			if (isLeader.equals("true")) {
				isTD_ll.setVisibility(View.VISIBLE);
				botttom_ll.setVisibility(View.VISIBLE);
				
				mActivity_managerpage_detail.setDrawableTop(getResources().getDrawable(R.drawable.manager_activitydetail));
				mActivity_managerpage_detail.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_detail.getDrawableTop(), null, null);
				mActivity_managerpage_detail.setClickable(true);
				mActivity_managerpage_detail.setCompoundDrawablePadding(3);
				
				mActivity_managerpage_edit.setDrawableTop(getResources().getDrawable(R.drawable.manager_activityedit));
				mActivity_managerpage_edit.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_edit.getDrawableTop(), null, null);
				mActivity_managerpage_edit.setClickable(true);
				mActivity_managerpage_edit.setCompoundDrawablePadding(3);
				
				setData();
			}
			
			//更新阅读数、报名数等数据
			updateData(singleActivityRes);
		}
	}
	private void setData(){
		mActivity_managerpage_signlist.setDrawableTop(getResources().getDrawable(R.drawable.manager_signlist));
		mActivity_managerpage_signlist.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_signlist.getDrawableTop(), null, null);
		mActivity_managerpage_signlist.setClickable(true);
		mActivity_managerpage_signlist.setCompoundDrawablePadding(3);
		
		mActivity_managerpage_share.setDrawableTop(getResources().getDrawable(R.drawable.manager_share));
		mActivity_managerpage_share.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_share.getDrawableTop(), null, null);
		mActivity_managerpage_share.setClickable(true);
		mActivity_managerpage_share.setCompoundDrawablePadding(3);
		
		mActivity_managerpage_groupsend.setDrawableTop(getResources().getDrawable(R.drawable.manager_groupsend));
		mActivity_managerpage_groupsend.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_groupsend.getDrawableTop(), null, null);
		mActivity_managerpage_groupsend.setClickable(true);
		mActivity_managerpage_groupsend.setCompoundDrawablePadding(3);
		
		mActivity_managerpage_activitycode.setDrawableTop(getResources().getDrawable(R.drawable.manager_activitycode));
		mActivity_managerpage_activitycode.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_activitycode.getDrawableTop(), null, null);
		mActivity_managerpage_activitycode.setClickable(true);
		mActivity_managerpage_activitycode.setCompoundDrawablePadding(3);
	}
	
	/**更新报名数、阅读数、评论数、点赞数
	 * @param singleActivityRes2 
	 * 
	 */
	private void updateData(SingleActivityRes singleActivityRes2){
		if(singleActivityRes2.getTddActivity().getSignCount()!=0){
			mActivity_managerpage_signup_content_tv.setText(singleActivityRes2.getTddActivity().getSignCount()+"");
		}else{
			mActivity_managerpage_signup_content_tv.setText("0");
		}
		if(singleActivityRes2.getTddActivity().getCommentCount()!=0){
			mActivity_managerpage_comment_content_tv.setText(singleActivityRes2.getTddActivity().getCommentCount()+"");
		}else {
			mActivity_managerpage_comment_content_tv.setText("0");
		}
		if(singleActivityRes2.getTddActivity().getLikeCount()!=0){
			mActivity_managerpage_like_content_tv.setText(singleActivityRes2.getTddActivity().getLikeCount()+"");
		}else {
			mActivity_managerpage_like_content_tv.setText("0");
		}
		if(singleActivityRes2.getTddActivity().getViewCount()!=0){
			mActivity_managerpage_read_content_tv.setText(singleActivityRes2.getTddActivity().getViewCount()+"");
		}else {
			mActivity_managerpage_read_content_tv.setText("0");
		}
		
	}
	
	@Override
	public void OnFailResultListener(String mess) {if (dialog != null) {dialog.dismiss();}
		UiHelper.ShowOneToast(this, mess);
	}

	/**
	 * 分享的相关操作
	 */
	public void friend(View v) {
		share(0);
	}

	public void friendline(View v) {
		share(1);
	}

	private void share(int flag) {
		downloadWeiXinImg(flag);

	}

	// 微信分享需要 先去下载封面的图片，然后才会分享出去
	private void downloadWeiXinImg(final int flag) {
		if (tddActivity.getShareContent() != null && tddActivity.getShareImg() != null && tddActivity.getShareUrl() != null) {

			ImageLoader.getInstance().loadImage(tddActivity.getShareImg(), new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String arg0, View arg1) {

				}

				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// 下载失败
					WXWebpageObject webpage = new WXWebpageObject();
					webpage.webpageUrl = tddActivity.getShareUrl();
					WXMediaMessage msg = new WXMediaMessage(webpage);
					msg.title = tddActivity.getActivityTitle();
					msg.description = tddActivity.getActivityDescription();
					// 根据ImgUrl下载下来一张图片，弄出bitmap格式
					// 这里替换一张自己工程里的图片资源
					Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
					msg.setThumbImage(thumb);
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = buildTransaction("webpage");
					req.message = msg;
					req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
					boolean fla = wxApi.sendReq(req);
					System.out.println("fla=" + fla);
				}

				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
					// 表示下载成功了
					WXWebpageObject webpage = new WXWebpageObject();
					webpage.webpageUrl = tddActivity.getShareUrl();
					WXMediaMessage msg = new WXMediaMessage(webpage);
					msg.title = tddActivity.getActivityTitle();
					msg.description = tddActivity.getActivityDescription();
					// 根据ImgUrl下载下来一张图片，弄出bitmap格式
					// 这里替换一张自己工程里的图片资源
					Bitmap thumb = bitmap;
					msg.setThumbImage(thumb);
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = buildTransaction("webpage");
					req.message = msg;
					req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
					boolean fla = wxApi.sendReq(req);
					System.out.println("fla=" + fla);
				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {

				}
			});
		} else {
			UiHelper.ShowOneToast(ManagerPageActivity.this, "第三方分享的内容不能为空！！！");
			finish();
		}
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	@Override
	public void onReq(BaseReq arg0) {

	}

	@Override
	public void onResp(BaseResp arg0) {
		finish();
	}

	private void onClickShareToQQ() {
		Bundle b = getShareBundle();
		if (b != null) {
			shareParams = b;
			Thread thread = new Thread(shareThread);
			thread.start();
		}
	}

	private Bundle getShareBundle() {
		Bundle bundle = new Bundle();
		bundle.putString("title", tddActivity.getActivityTitle());
		bundle.putString("imageUrl", tddActivity.getShareImg());
		bundle.putString("targetUrl", tddActivity.getShareUrl());
		bundle.putString("summary", tddActivity.getActivityDescription());
		bundle.putString("site", "1104957952");
		bundle.putString("appName", "我是TDD");
		return bundle;
	}

	Bundle shareParams = null;

	Handler shareHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
		}
	};

	// 线程类，该类使用匿名内部类的方式进行声明
	Runnable shareThread = new Runnable() {

		public void run() {
			doShareToQQ(shareParams);
			Message msg = shareHandler.obtainMessage();

			// 将Message对象加入到消息队列当中
			shareHandler.sendMessage(msg);

		}
	};

	private void doShareToQQ(Bundle params) {
		mTencent.shareToQQ(ManagerPageActivity.this, params, new BaseUiListener() {
			protected void doComplete(JSONObject values) {
				showResult("shareToQQ:", "onComplete");
			}

			@Override
			public void onError(UiError e) {
				showResult("shareToQQ:", "还未安装QQ");
			}

			@Override
			public void onCancel() {
				showResult("shareToQQ", "onCancel");
			}
		});
	}

	private class BaseUiListener implements IUiListener {

		// @Override
		// public void onComplete(JSONObject response) {
		// // mBaseMessageText.setText("onComplete:");
		// // mMessageText.setText(response.toString());
		// doComplete(response);
		// }

		protected void doComplete(Object values) {

		}

		@Override
		public void onError(UiError e) {
			showResult("onError:", "code:" + e.errorCode + ", msg:" + e.errorMessage + ", detail:" + e.errorDetail);
		}

		@Override
		public void onCancel() {
			showResult("onCancel", "");
		}

		@Override
		public void onComplete(Object arg0) {
			doComplete(arg0);
		}
	}

	private Handler mHandler;

	// qq分享的结果处理
	private void showResult(final String base, final String msg) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// finish();//结束

				UiHelper.ShowOneToast(ManagerPageActivity.this, msg);
				popupWindow.dismiss();
				finish();
			}
		});
	}

	/**
	 * 设置窗口
	 */
	private void getPopWindow() {
		if (popupWindow != null) {
			popupWindow.dismiss();
			return;
		} else {
			initPopupWindow();
		}
	}

	/**
	 * 分享窗口初始化
	 */
	private void initPopupWindow() {
		View popupWindow_view = this.getLayoutInflater().inflate(R.layout.activity_originate_chair_popwindow, null, false);
		popupWindow = new PopupWindow(popupWindow_view, AppContext.getAppContext().getScreenWidth() * 4 / 5, LayoutParams.WRAP_CONTENT, true);
		popupWindow_view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});
		PengTextView weixinFriend, weixinZone, qqFriend;// 弹出窗口三个控件
		weixinFriend = (PengTextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_weixin);
		weixinZone = (PengTextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_weixin_zone);
		qqFriend = (PengTextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_qq);
		// 微信好友分享
		weixinFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				friend(v);
			}
		});
		// 微信朋友圈分享
		weixinZone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				friendline(v);
			}
		});
		// qq好友分享
		qqFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickShareToQQ();
			}
		});
		// 设置透明度
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.3f;
		getWindow().setAttributes(lp);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
		// 显示窗口 位置
		popupWindow.showAtLocation(this.findViewById(R.id.layout), Gravity.CENTER, 0, 0);//
	}
	
	
	/**
	 * 设置一个广播，用来接收activity
	 */
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@SuppressWarnings("unchecked")
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bundle=intent.getExtras();
			if((TddActivity) bundle.getSerializable("tddactivity")!=null)
			tddActivity = (TddActivity) bundle.getSerializable("tddactivity");
			initData();

		}
	};

	
	@Override
	public void onDestroy() {
		unregisterReceiver(broadcastReceiver);
		unregisterReceiver(broadcastReceiver2);
		super.onDestroy();
	}

	/**设置广播，用于接收处理“活动详情”发送的广播
	 * 
	 */
	BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			/*Bundle bundle = intent.getExtras();
			judgeRole((String)bundle.getSerializable("activityId"));*/
//			judgeRole(tddActivity.getActivityId());
		}
	};
	
}