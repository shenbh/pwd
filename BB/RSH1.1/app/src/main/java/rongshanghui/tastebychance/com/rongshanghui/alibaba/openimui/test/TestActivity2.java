package rongshanghui.tastebychance.com.rongshanghui.alibaba.openimui.test;

import android.os.Bundle;

import com.alibaba.mobileim.kit.track.TrackBaseActivity;

import rongshanghui.tastebychance.com.rongshanghui.R;

public class TestActivity2 extends TrackBaseActivity {

    private static final String TAG = "TestActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_2);
        setYWTrackTitleAndUrl(TAG, null);
    }
}
