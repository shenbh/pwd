package rongshanghui.tastebychance.com.rongshanghui.mime.jgmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.dyhdyh.widget.loading.bar.LoadingBar;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rongshanghui.tastebychance.com.rongshanghui.MyAppCompatActivity;
import rongshanghui.tastebychance.com.rongshanghui.R;
import rongshanghui.tastebychance.com.rongshanghui.bean.ResInfo;
import rongshanghui.tastebychance.com.rongshanghui.home.sht.detail.bean.SubjectCount;
import rongshanghui.tastebychance.com.rongshanghui.home.sht.detail.bean.ToDetailBean;
import rongshanghui.tastebychance.com.rongshanghui.mime.managercommon.jjedt.ManagerJJActivity;
import rongshanghui.tastebychance.com.rongshanghui.mime.jgmanager.xinyong.GRXYDActivity;
import rongshanghui.tastebychance.com.rongshanghui.mime.managercommon.sbmanager.SBManagerActivity;
import rongshanghui.tastebychance.com.rongshanghui.mime.managercommon.xzmanager.DownloadManagerActivity;
import rongshanghui.tastebychance.com.rongshanghui.mime.managercommon.zhinan.ZNSCActivity;
import rongshanghui.tastebychance.com.rongshanghui.util.Constants;
import rongshanghui.tastebychance.com.rongshanghui.util.CrashHandler;
import rongshanghui.tastebychance.com.rongshanghui.util.PicassoUtils;
import rongshanghui.tastebychance.com.rongshanghui.util.StringUtil;
import rongshanghui.tastebychance.com.rongshanghui.util.SystemUtil;
import rongshanghui.tastebychance.com.rongshanghui.util.ToastUtils;
import rongshanghui.tastebychance.com.rongshanghui.util.UrlManager;
import rongshanghui.tastebychance.com.rongshanghui.view.CustomLoadingFactory;

/**
 * 类描述：JGManagerActivity 我的-机构管理界面
 * 创建人: shenbh Email:shenbh@qq.com
 * 创建时间： 2017/12/1 9:39
 * 修改人：
 * 修改时间：2017/12/1 9:39
 * 修改备注：
 *
 * @version 1.0
 */
public class JGManagerActivity extends MyAppCompatActivity {

    @BindView(R.id.head_left_iv)
    ImageView headLeftIv;
    @BindView(R.id.head_center_title_tv)
    TextView headCenterTitleTv;
    @BindView(R.id.head_right_iv)
    ImageView headRightIv;
    @BindView(R.id.head_right_tv)
    TextView headRightTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_mine_manager_head_iv)
    ImageView activityMineManagerHeadIv;
    @BindView(R.id.activity_mine_manager_fsnum_tv)
    TextView activityMineManagerFsnumTv;
    @BindView(R.id.activity_mine_manager_fs_tv)
    TextView activityMineManagerFsTv;
    @BindView(R.id.activity_mine_manager_fsnum_ll)
    LinearLayout activityMineManagerFsnumLl;
    @BindView(R.id.activity_mine_manager_fangdaiinfonum_tv)
    TextView activityMineManagerFangdaiinfonumTv;
    @BindView(R.id.activity_mine_manager_fangdaiinfo_tv)
    TextView activityMineManagerFangdaiinfoTv;
    @BindView(R.id.activity_mine_manager_shxnum_ll)
    LinearLayout activityMineManagerShxnumLl;
    @BindView(R.id.activity_mine_manager_llshownum_tv)
    TextView activityMineManagerLlshownumTv;
    @BindView(R.id.activity_mine_manager_llnum_tv)
    TextView activityMineManagerLlnumTv;
    @BindView(R.id.activity_mine_manager_llnum_ll)
    LinearLayout activityMineManagerLlnumLl;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.activity_mine_manager_jj_iv)
    ImageView activityMineManagerJjIv;
    @BindView(R.id.activity_mine_manager_fdfb_iv)
    ImageView activityMineManagerFdfbIv;
    @BindView(R.id.activity_mine_manager_zxfb_iv)
    ImageView activityMineManagerZxfbIv;
    @BindView(R.id.activity_mine_manager_znsc_iv)
    ImageView activityMineManagerZnscIv;
    @BindView(R.id.relativelalyout1)
    RelativeLayout relativelalyout1;
    @BindView(R.id.activity_mine_manager_xzsc_iv)
    ImageView activityMineManagerXzscIv;
    @BindView(R.id.activity_mine_manager_sbgl_iv)
    ImageView activityMineManagerSbglIv;
    @BindView(R.id.activity_mine_manager_xyklj_iv)
    ImageView activityMineManagerXykljIv;
    @BindView(R.id.activity_mine_manager_grxydlj_iv)
    ImageView activityMineManagerGrxydljIv;
    @BindView(R.id.content_jgmanager)
    RelativeLayout contentJgmanager;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.activity_mine_jgmanager_rootlayout)
    CoordinatorLayout activityMineJgmanagerRootlayout;

    /**
     * Instances of static inner classes do not hold an implicit
     * reference to their outer class.
     */
    private static class MyHandler<T extends Activity> extends Handler {
        private final WeakReference<T> mT;

        public MyHandler(T t) {
            this.mT = new WeakReference<T>(t);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            final T t = mT.get();
            if (null != t) {
                try {
                    switch (msg.what) {
                        case Constants.WHAT_GETDATA:
                            ResInfo resInfo = (ResInfo) msg.obj;
                            SubjectCount subjectCount = resInfo.getClass(SubjectCount.class);
                            PicassoUtils.getinstance().loadNormalByPath(t, subjectCount.getImage(), ((JGManagerActivity) t).activityMineManagerHeadIv);
                            ((JGManagerActivity) t).activityMineManagerFsnumTv.setText(subjectCount.getFans_count() + "");
                            ((JGManagerActivity) t).activityMineManagerFangdaiinfonumTv.setText(subjectCount.getFdxx() + "");
                            ((JGManagerActivity) t).activityMineManagerLlshownumTv.setText(subjectCount.getHits_count() + "");
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_jgmanager);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setTitle();
        initView();
    }

    private void initView() {
        activityMineManagerJjIv.setLayoutParams(SystemUtil.getInstance().getRLLparams(activityMineManagerJjIv, 82, 101));
        activityMineManagerFdfbIv.setLayoutParams(SystemUtil.getInstance().getRLLparams(activityMineManagerFdfbIv, 81, 101));
        activityMineManagerZxfbIv.setLayoutParams(SystemUtil.getInstance().getRLLparams(activityMineManagerZxfbIv, 82, 101));
        activityMineManagerZnscIv.setLayoutParams(SystemUtil.getInstance().getRLLparams(activityMineManagerZnscIv, 82, 101));
        activityMineManagerXzscIv.setLayoutParams(SystemUtil.getInstance().getRLLparams(activityMineManagerXzscIv, 82, 101));
        activityMineManagerSbglIv.setLayoutParams(SystemUtil.getInstance().getRLLparams(activityMineManagerSbglIv, 81, 101));
        activityMineManagerXykljIv.setLayoutParams(SystemUtil.getInstance().getRLLparams(activityMineManagerXykljIv, 82, 101));
        activityMineManagerGrxydljIv.setLayoutParams(SystemUtil.getInstance().getRLLparams(activityMineManagerGrxydljIv, 82, 101));
    }

    private void setTitle() {
        if (null != headCenterTitleTv) {
            headCenterTitleTv.setVisibility(View.VISIBLE);
            headCenterTitleTv.setText("机构管理");
        }
        if (null != headRightTv) {
            headRightTv.setVisibility(View.GONE);
        }
    }

    /**
     * token 	是 	string 	token值
     * id 	是 	int 	查看主体所对应的用户id
     */
    private void getData() {
        if (null == loadingBar) {
            loadingBar = LoadingBar.make(findViewById(R.id.activity_mine_jgmanager_rootlayout), new CustomLoadingFactory());
        }
        if (null != loadingBar) {
            loadingBar.show();
            loadingBar.setOnClickListener(null);
        }

//采用okhttp3来进行网络请求
        final String url = UrlManager.URL_SUBJECTCOUNT;
        OkHttpClient mOkHttpClient = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        if (StringUtil.isNotEmpty(SystemUtil.getInstance().getToken())) {
            builder.add("token", SystemUtil.getInstance().getToken());
            Log.i(Constants.TAG, SystemUtil.getInstance().getToken());
        }
        builder.add("id", SystemUtil.getInstance().getUserInfo().getId() + "");
        RequestBody formBody = builder.build();
        if (null == formBody) {
            return;
        }

        Request request = new Request.Builder().url(url).post(formBody).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                dialogCancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dialogCancel();
                try {
                    String str = response.body().string();
                    Log.i(Constants.TAG, str);

                    final ResInfo resInfo = JSONObject.parseObject(str, ResInfo.class);
                    if (resInfo.getCode() == Constants.RESULT_SUCCESS) {
                        /**Looper.prepare();
                         UiHelper.ShowOneToast(getApplicationContext(),"数据请求成功");
                         Looper.loop();*/

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = Constants.WHAT_GETDATA;
                                msg.obj = resInfo;
                                handler.sendMessage(msg);
                            }
                        });
                    } else {
                        Message msg = new Message();
                        msg.what = ERROR_WHAT;
                        msg.obj = resInfo.getError_message();
                        myHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CrashHandler.getInstance().handlerError("处理" + url + " 返回的成功数据报错");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @OnClick({R.id.head_left_iv, R.id.activity_mine_manager_jj_iv, R.id.activity_mine_manager_fdfb_iv, R.id.activity_mine_manager_zxfb_iv, R.id.activity_mine_manager_znsc_iv, R.id.activity_mine_manager_xzsc_iv, R.id.activity_mine_manager_sbgl_iv, R.id.activity_mine_manager_xyklj_iv, R.id.activity_mine_manager_grxydlj_iv})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.head_left_iv:
                JGManagerActivity.this.finish();
                break;
            case R.id.activity_mine_manager_jj_iv:
                intent = new Intent(JGManagerActivity.this, ManagerJJActivity.class);
                intent.putExtra("type", Constants.PUBLISHCATE_JG);
                startActivity(intent);
                break;
            case R.id.activity_mine_manager_fdfb_iv:
                WeakReference<ToDetailBean> wf = new WeakReference<ToDetailBean>(new ToDetailBean());
                wf.get().setTitle("我的放贷");
                wf.get().setPublishCate(Constants.PUBLISHCATE_JG);//0:平台;1:商会;2:企业;3:机构;4:部门;5:个人;6:镇街
                wf.get().setPublishType(Constants.PULISHTYPE_FDXX);////0:平台;1:融资项目;2:放贷信息;3:政策;4:资讯;5:指南;6:招商秀;7:商会秀;8:下载材料;9:企业风采,10:上报
                SystemUtil.getInstance().intentToFabuDeleteableActivity(JGManagerActivity.this, wf.get());
                break;
            case R.id.activity_mine_manager_zxfb_iv:
                WeakReference<ToDetailBean> wf2 = new WeakReference<ToDetailBean>(new ToDetailBean());
                wf2.get().setTitle("我的资讯");
                wf2.get().setPublishCate(Constants.PUBLISHCATE_JG);//0:平台;1:商会;2:企业;3:机构;4:部门;5:个人;6:镇街
                wf2.get().setPublishType(Constants.PULISHTYPE_ZX);////0:平台;1:融资项目;2:放贷信息;3:政策;4:资讯;5:指南;6:招商秀;7:商会秀;8:下载材料;9:企业风采,10:上报
                SystemUtil.getInstance().intentToFabuDeleteableActivity(JGManagerActivity.this, wf2.get());
                break;
            case R.id.activity_mine_manager_znsc_iv:
                if (StringUtil.isEmpty(SystemUtil.getInstance().getToken())) {
                    ToastUtils.showOneToast(getApplicationContext(), Constants.LOGIN_INVALID);
                    return;
                }
                intent = new Intent(JGManagerActivity.this, ZNSCActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_mine_manager_xzsc_iv:
                intent = new Intent(JGManagerActivity.this, DownloadManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_mine_manager_sbgl_iv:
                if (StringUtil.isEmpty(SystemUtil.getInstance().getToken())) {
                    ToastUtils.showOneToast(getApplicationContext(), Constants.LOGIN_INVALID);
                    return;
                }
                intent = new Intent(JGManagerActivity.this, SBManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_mine_manager_xyklj_iv:
                if (StringUtil.isEmpty(SystemUtil.getInstance().getToken())) {
                    ToastUtils.showOneToast(getApplicationContext(), Constants.LOGIN_INVALID);
                    return;
                }
                intent = new Intent(JGManagerActivity.this, GRXYDActivity.class);
                intent.putExtra("toType", Constants.JG_TO_XYK);
                startActivity(intent);
                break;
            case R.id.activity_mine_manager_grxydlj_iv:
                if (StringUtil.isEmpty(SystemUtil.getInstance().getToken())) {
                    ToastUtils.showOneToast(getApplicationContext(), Constants.LOGIN_INVALID);
                    return;
                }
                intent = new Intent(JGManagerActivity.this, GRXYDActivity.class);
                intent.putExtra("toType", Constants.JG_TO_GRXYD);
                startActivity(intent);
                break;
        }
    }
}