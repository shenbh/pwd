/**
 * @fields ManagerApplyListActivity.java
 */
package com.newland.wstdd.mine.applyList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.adapter.BaseFragmentPagerAdapter;
import com.newland.wstdd.common.base.BaseFragment;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.EditTextUtil;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.mine.applyList.bean.RegistrationListReq;
import com.newland.wstdd.mine.applyList.bean.RegistrationListRes;
import com.newland.wstdd.mine.applyList.bean.TddActivitySignVo;
import com.newland.wstdd.mine.applyList.bean.TddActivitySignVoInfo;
import com.newland.wstdd.mine.applyList.bean.UpdateRegistrationListReq;
import com.newland.wstdd.mine.applyList.bean.UpdateRegistrationListRes;
import com.newland.wstdd.mine.applyList.beanreq.MailUrlReq;
import com.newland.wstdd.mine.applyList.beanres.MailUrlRes;
import com.newland.wstdd.mine.applyList.dialog.MailDialog;
import com.newland.wstdd.mine.applyList.handle.RegistrationHandle;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;



/**
 * 报名名单
 * 
 * @author H81 2015-11-28
 * 
 */
public class ManagerApplyListActivity extends BaseFragmentActivity implements OnClickListener ,OnPostListenerInterface{
	final String FLAG = "right_tv_change";
	
	private String activityId;//接收到的数据
	public int signRole = 9 ;//报名用户角色 1.团长 2.团秘 9.团员
	private String isLeader;//是否是团大
	
	private LinearLayout applylist_bottom_ll;//导出名单、在线查看布局
	private TextView exportListTv;//导出名单
	private TextView onlineListTv;//在线查看
	public MailDialog mailDialog;//发送邮件对话框对话框
	private List<BaseFragment> listFragments;
	private BaseFragment currentFragment;// 当前选中的fragment
	private PassListFragment passListFragment;// 通过
	private NoPassListFragment noPassListFragment;// 未通过

	private LinearLayout mPassLayout;
	private LinearLayout mNoPassLayout;
	private TextView mPassTextView;
	private ImageView mPassImageView;
	private TextView mNoPassTextView;
	private ImageView mNoPassImageView;
	private android.support.v4.view.ViewPager mViewPager;

	TextView right_tv;//编辑、完成
	
	// 服务器返回的数据
	RegistrationListRes registrationListRes;
	UpdateRegistrationListRes updateRegistrationListRes;
	MailUrlRes mailUrlRes;//导出名单发送的返回信息
	RegistrationHandle handler = new RegistrationHandle(this);
	RegistrationHandle handlerUpdate = new RegistrationHandle(this);
	List<TddActivitySignVoInfo> tddActivitySigns = new ArrayList<TddActivitySignVoInfo>();
	static List<TddActivitySignVoInfo> allList = new ArrayList<TddActivitySignVoInfo>();
	private List<TddActivitySignVo> allTddActivitySignVos = new ArrayList<TddActivitySignVo>();//全部名单（用于提交）
	
	
	private List<TddActivitySignVoInfo> signNoPassList = new ArrayList<TddActivitySignVoInfo>();// 未通过
	private List<TddActivitySignVoInfo> passAllList = new ArrayList<TddActivitySignVoInfo>();//通过的所有名单
	TddActivitySignVo tdTddActivitySignVo ;// 团大
	
	public boolean hasTD = false;//是否有团大
	public int passNum = 0 ;//已通过人数
	public int noPassNum = 0;//未通过人数
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityId = getIntent().getStringExtra("activityId");
		signRole = getIntent().getIntExtra("signRole", 0);
		isLeader = getIntent().getStringExtra("isLeader");
		setContentView(R.layout.activity_applylist);
		setTitle();
		bindViews();
//		initFragment();
		getRegistrationListInfos();
		
		//模拟数据
//		getData();
	}

	/**
	 * 初始化Fragment
	 * @param passAllList2 通过：团秘&团员
	 * @param signNoPassList2 未通过&未审批
	 * @param tdTddActivitySignVo2 团大
	 */
	private void initFragment(List<TddActivitySignVoInfo> signNoPassList2, List<TddActivitySignVoInfo> passAllList2, TddActivitySignVo tdTddActivitySignVo) {
		listFragments = new ArrayList<BaseFragment>();
		passListFragment = PassListFragment.newInstance(ManagerApplyListActivity.this);
		Bundle bundle2 = new Bundle();
		bundle2.putSerializable("passAllList", (Serializable) passAllList2);
		/*bundle2.putSerializable("tdTddActivitySignVo", (Serializable) tdTddActivitySignVo);*/
		bundle2.putSerializable("signRole", signRole);
		bundle2.putSerializable("isLeader", isLeader);
		passListFragment.setArguments(bundle2);

		noPassListFragment = NoPassListFragment.newInstance(ManagerApplyListActivity.this);
		Bundle bundle = new Bundle();
		bundle.putSerializable("signNoPassList", (Serializable) signNoPassList2);
		noPassListFragment.setArguments(bundle);
		
		listFragments.add(passListFragment);
		listFragments.add(noPassListFragment);

		//这里   用法要注意下
		BaseFragmentPagerAdapter mAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), listFragments);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(pageListener);
		mViewPager.setOffscreenPageLimit(1);
		
		currentFragment = passListFragment;
	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		ImageView left_btn = (ImageView) findViewById(R.id.head_left_iv);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText("报名名单");
		if (left_btn != null) {
			left_btn.setVisibility(View.VISIBLE);
			left_btn.setImageDrawable(getResources().getDrawable(R.drawable.left_expandsion));
			left_btn.setOnClickListener(this);
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {
			right_tv.setText("编辑");
			right_tv.setTextColor(getResources().getColor(R.color.originate_darkred));
			right_tv.setVisibility(View.VISIBLE);
			right_tv.setOnClickListener(this);
		}
	}

	private void bindViews() {
		onlineListTv = (TextView) findViewById(R.id.applist_online_tv);
		onlineListTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UiHelper.ShowOneToast(ManagerApplyListActivity.this, "还在完善中！");
			}
		});
		exportListTv = (TextView) findViewById(R.id.applist_exportlist_tv);
		exportListTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//有邮箱直接发送
				
				
					showMailDialog();
				
				
			}
		});
		mPassLayout = (LinearLayout) findViewById(R.id.pass_ll);
		mNoPassLayout = (LinearLayout) findViewById(R.id.nopass_ll);
		mPassTextView = (TextView) findViewById(R.id.pass_tv);
		mPassImageView = (ImageView) findViewById(R.id.pass_iv);
		mNoPassTextView = (TextView) findViewById(R.id.nopass_tv);
		mNoPassImageView = (ImageView) findViewById(R.id.nopass_iv);
		mViewPager = (android.support.v4.view.ViewPager) findViewById(R.id.viewpager);
		mPassLayout.setOnClickListener(this);
		mNoPassLayout.setOnClickListener(this);
		
		applylist_bottom_ll = (LinearLayout) findViewById(R.id.applylist_bottom_ll);
	}

	/**
	 * ViewPager切换监听方法
	 * */
	public OnPageChangeListener pageListener = new OnPageChangeListener() {

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int position) {
			clearPress();
			mViewPager.setCurrentItem(position);
			currentFragment = listFragments.get(position);
			switch (position) {
			case 0:
				mPassImageView.setVisibility(View.VISIBLE);
				mPassTextView.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
				break;
			case 1:
				mNoPassImageView.setVisibility(View.VISIBLE);
				mNoPassTextView.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
				break;
			default:
				break;
			}
			currentFragment.refresh();
//			right_tv.setText("编辑");
		}

	};

	private void clearPress() {
		mPassImageView.setVisibility(View.INVISIBLE);
		mNoPassImageView.setVisibility(View.INVISIBLE);

		mPassTextView.setTextColor(this.getResources().getColor(R.color.textgray));
		mNoPassTextView.setTextColor(this.getResources().getColor(R.color.textgray));
		
	}

	/**设置已通过viewpager标签上的内容
	 * @param num
	 */
	public void setPassTextViewData(int num){
		mPassTextView.setText("已通过("+num+")");
		
	}
	/**设置未通过viewpager标签上的内容
	 * @param num
	 */
	public void setNoPassTextViewData(int num){
		mNoPassTextView.setText("未通过("+num+")");
	}
	
	@Override
	protected void processMessage(Message msg) {

	}

	@Override
	public void refresh() {

	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_left_iv:// 返回
			finish();
			break;
		case R.id.head_right_tv:// 编辑
			if (right_tv.getText().toString().equals("编辑")) {
				right_tv.setText("完成");
				applylist_bottom_ll.setVisibility(View.GONE);
			}else if (right_tv.getText().toString().equals("完成")) {
				right_tv.setText("编辑");
				applylist_bottom_ll.setVisibility(View.VISIBLE);
				getUpdateRegistrationListInfos();
			}
			Intent intent = new Intent();
			intent.setAction(FLAG);
			sendBroadcast(intent);
			break;
		case R.id.pass_ll:
			clearPress();
			mPassImageView.setVisibility(View.VISIBLE);
			mPassTextView.setTextColor(this.getResources().getColor(R.color.red));
			mViewPager.setCurrentItem(0);
			break;
		case R.id.nopass_ll:
			clearPress();
			mNoPassImageView.setVisibility(View.VISIBLE);
			mNoPassTextView.setTextColor(this.getResources().getColor(R.color.red));
			mViewPager.setCurrentItem(1);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 35.	活动报名人员列表
	 */
	private void getRegistrationListInfos() {
		super.refresh();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					RegistrationListReq reqInfo = new RegistrationListReq();
					reqInfo.setActivityId(activityId);
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<RegistrationListRes> ret = mgr.getRegistrationListInfo(reqInfo);// 泛型类，
					Message message = new Message();
					message.what = RegistrationHandle.REGISTRATION_LIST;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						registrationListRes = (RegistrationListRes) ret.getObj();
						message.obj = registrationListRes;
					} else {
						message.obj = ret.getMsg();
					}
					handlerUpdate.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 *  36.	更新报名列表
	 */
	private void getUpdateRegistrationListInfos() {
		super.refresh();
		try {
			new Thread() {
				public void run() {
					UpdateRegistrationListReq reqInfo = new UpdateRegistrationListReq();
					if (tdTddActivitySignVo != null) {//团大不为空，则添加上团大
						allTddActivitySignVos.add(tdTddActivitySignVo);
					}
					
					for (int i = 0; i < allList.size(); i++) {
						TddActivitySignVo  tddActivitySignVo=new TddActivitySignVo();
						tddActivitySignVo=allList.get(i).getTddActivitySignVo();
						allTddActivitySignVos.add(tddActivitySignVo);
					}
					reqInfo.setTddActivitySigns(allTddActivitySignVos);
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<UpdateRegistrationListRes> ret = mgr.getUpdateRegistrationListInfo(reqInfo);// 泛型类，
					Message message = new Message();
					message.what = RegistrationHandle.UPDATE_REGISTRATION_LIST;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						updateRegistrationListRes = new UpdateRegistrationListRes();
						updateRegistrationListRes.setGetResMess(StringUtil.noNull(ret.getMsg()));
						message.obj = updateRegistrationListRes;
					} else {
						message.obj = ret.getMsg();
					}
					handlerUpdate.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/***
	 * 服务器返回的信息
	 */
	@Override
	public void OnHandleResultListener(Object obj, int responseId) {
		try {
			switch (responseId) {
			case  RegistrationHandle.REGISTRATION_LIST://活动报名人员列表
				if (dialog !=null) {
					dialog.dismiss();
					
				}
				registrationListRes= (RegistrationListRes) obj;
				if (registrationListRes != null) {
					//未通过名单
					/*List<TddActivitySignVo> tempSignNoPassList=registrationListRes.getSignNoPassList();
					for (int i = 0; i < tempSignNoPassList.size(); i++) {
						TddActivitySignVoInfo inActivitySignVoInfo = new TddActivitySignVoInfo();
						inActivitySignVoInfo.setTddActivitySignVo(tempSignNoPassList.get(i));
						inActivitySignVoInfo.setSelected(false);
						signNoPassList.add(inActivitySignVoInfo);
					}
					
					//通过名单
					List<TddActivitySignVo> tempSignPassList=registrationListRes.getSignPassList();
					
					for (int i = 0; i < tempSignPassList.size(); i++) {
						TddActivitySignVoInfo inActivitySignVoInfo = new TddActivitySignVoInfo();
						inActivitySignVoInfo.setTddActivitySignVo(tempSignPassList.get(i));
						inActivitySignVoInfo.setSelected(false);
						passAllList.add(inActivitySignVoInfo);
					}*/
					
					for (int i = 0,size = registrationListRes.getAllSign().size(); i <size; i++) {
						if (registrationListRes.getAllSign().get(i).getAuditStatus() == 2) {
							TddActivitySignVoInfo inActivitySignVoInfo = new TddActivitySignVoInfo();
							inActivitySignVoInfo.setTddActivitySignVo(registrationListRes.getAllSign().get(i));
							inActivitySignVoInfo.setSelected(false);
							passAllList.add(inActivitySignVoInfo);//通过名单
							if (registrationListRes.getAllSign().get(i).getSignRole()==1){
								tdTddActivitySignVo = registrationListRes.getAllSign().get(i);
							}
						}else {
							TddActivitySignVoInfo inActivitySignVoInfo = new TddActivitySignVoInfo();
							inActivitySignVoInfo.setTddActivitySignVo(registrationListRes.getAllSign().get(i));
							inActivitySignVoInfo.setSelected(false);
							signNoPassList.add(inActivitySignVoInfo);//未通过名单
						}
						
						/*if (registrationListRes.getAllSign().get(i).getSignRole()==1){
							tdTddActivitySignVo = registrationListRes.getAllSign().get(i);
						}else if (registrationListRes.getAllSign().get(i).getSignRole()==2 ||
							registrationListRes.getAllSign().get(i).getSignRole()==9) {//signRole	报名用户角色 1.团大 2.团秘 9.团员	int
							TddActivitySignVoInfo inActivitySignVoInfo = new TddActivitySignVoInfo();
							inActivitySignVoInfo.setTddActivitySignVo(registrationListRes.getAllSign().get(i));
							inActivitySignVoInfo.setSelected(false);
							passAllList.add(inActivitySignVoInfo);//通过名单
						}else  {
							TddActivitySignVoInfo inActivitySignVoInfo = new TddActivitySignVoInfo();
							inActivitySignVoInfo.setTddActivitySignVo(registrationListRes.getAllSign().get(i));
							inActivitySignVoInfo.setSelected(false);
							signNoPassList.add(inActivitySignVoInfo);//未通过名单
						}*/
					}
					
					allList = new ArrayList<TddActivitySignVoInfo>();
					for (int i = 0; i <registrationListRes.getAllSign().size(); i++) {
						TddActivitySignVoInfo tddActivitySignVoInfo = new TddActivitySignVoInfo();
						tddActivitySignVoInfo.setTddActivitySignVo(registrationListRes.getAllSign().get(i));
						tddActivitySignVoInfo.setSelected(false);
						allList.add(tddActivitySignVoInfo);
					}
					for (int i = 0; i <allList.size(); i++) {
				
						if (allList.get(i).getTddActivitySignVo().getAuditStatus()==2&&allList.get(i).getTddActivitySignVo().getSignRole()==1) {
//							tdTddActivitySignVo = allList.get(i).getTddActivitySignVo();
							allList.remove(i);
						}
					}
					initFragment(signNoPassList,passAllList,tdTddActivitySignVo);
					
					setPassTextViewData(registrationListRes.getSignPassCount());
					setNoPassTextViewData(registrationListRes.getSignNoPassCount());
				}
				break;
			case RegistrationHandle.UPDATE_REGISTRATION_LIST:// 更新报名列表
				if (dialog != null) {
					dialog.dismiss();
				}
				updateRegistrationListRes = (UpdateRegistrationListRes) obj;
				if (updateRegistrationListRes != null) {
					UiHelper.ShowOneToast(this, "提交成功");
				}
				break;
				
				
			case RegistrationHandle.MAIL_URL:// 更新报名列表
				if (dialog != null) {
					dialog.dismiss();
				}
				mailUrlRes = (MailUrlRes) obj;
				if (mailUrlRes != null) {
					mailDialog.dismiss();
					UiHelper.ShowOneToast(this, "提交成功");
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void OnFailResultListener(String mess) {if (dialog != null) {dialog.dismiss();}
		UiHelper.ShowOneToast(this, mess);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}
	
	// 弹窗
	private void showMailDialog() {
		mailDialog = new MailDialog(ManagerApplyListActivity.this);
	    final EditText editText = (EditText) mailDialog.getEditText();//方法在CustomDialog中实现
	    if(AppContext.getAppContext().getEmail()!=null&&"".equals(AppContext.getAppContext().getEmail())){
	    	editText.setText(AppContext.getAppContext().getEmail());
    	}
	    //提交邮箱地址
	    mailDialog.setOnPositiveListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            //dosomething youself
	        	
	        	if(EditTextUtil.checkEmail(editText.getText().toString())){
	        	commitMailUrl(editText.getText().toString());//提交到服务器上去
	        	}else {
					UiHelper.ShowOneToast(ManagerApplyListActivity.this, "邮箱格式不正确!");
				}
	        }

		
	    });
	    //取消
	    mailDialog.setOnNegativeListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	mailDialog.dismiss();
	        }
	    });
	    Window dialogWindow = mailDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

        /*
         * lp.x与lp.y表示相对于原始位置的偏移.
         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时
         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时
         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         * 
         * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
         * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了,
         * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
         */
//        lp.x = 100; // 新位置X坐标
//        lp.y = 100; // 新位置Y坐标
        lp.width = 500; // 宽度
        lp.height = 300; // 高度
//        lp.alpha = 0.7f; // 透明度

        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
        dialogWindow.setAttributes(lp);
        mailDialog.setCanceledOnTouchOutside(false);
        /*
         * 将对话框的大小按屏幕大小的百分比设置
         */
//        WindowManager m = getWindowManager();
//        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
//        p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.95
//        dialogWindow.setAttributes(p);
	    mailDialog.show();
	}
	
	/**
	 * 跟服务器进行交互
	 */
	private void commitMailUrl(final String mailString) {
		// TODO Auto-generated method stub
		try {
			new Thread() {
				public void run() {
					
					// 需要发送一个request的对象进行请求
					MailUrlReq reqInfo = new MailUrlReq();
					reqInfo.setMailto(mailString);
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<MailUrlRes> ret = mgr.getMailUrlInfo(reqInfo, activityId);// 泛型类，																
					Message message = new Message();
					message.what = RegistrationHandle.MAIL_URL;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						if(ret.getObj()!=null){
							mailUrlRes= (MailUrlRes) ret.getObj();
						    message.obj = mailUrlRes;
						}else {
							mailUrlRes =new MailUrlRes();
							message.obj = mailUrlRes;
						}
						
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
}
