package com.mlz.shuiguang.fragment.message;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mlz.shuiguang.R;
import com.mlz.shuiguang.model.ThemeColor;
import com.mlz.shuiguang.util.Util;
import com.mlz.shuiguang.util.fragmentTransation.FragmentUtil;
import com.mlz.shuiguang.util.fragmentTransation.IMainAppHandler;

/**
 * Created by dehualai on 2/14/17.
 */

public class MessageDetailsFragment extends Fragment {

    String TAG = "MessageDetailsFragment";
    Context mContext;
    FragmentUtil fragmentUtil;
    IMainAppHandler mainAppHandler;

    public final static String CONTENT_URL = "cURL";
    String contentUrl;
    View view;
    Button backButton;
    WebView webView;

    LinearLayout bgLayout;
    LinearLayout headerLayout;

    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "onAttach");
        super.onAttach(activity);
        mContext = activity;

        fragmentUtil = new FragmentUtil(activity);
        if(activity instanceof IMainAppHandler){
            this.mainAppHandler = (IMainAppHandler) activity;
        } else {
            Log.e(TAG, "The input activity must implement interface IMainAppHandler");
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle args = getArguments();
        contentUrl = args.getString(CONTENT_URL);
        view = inflater.inflate(R.layout.fragment_message_detail, container, false);
        bgLayout = (LinearLayout)view.findViewById(R.id.bg_layout);
        headerLayout = (LinearLayout)view.findViewById(R.id.header_layout);
        assignViews();
        addActionToViews();

        return view;
    }

    private void refreshViews() {
        ThemeColor theme = Util.getActiveTheme();
        bgLayout.setBackgroundColor(theme.getMainColor(mContext));
        headerLayout.setBackgroundColor(theme.getMinorColor(mContext));
    }

    private void assignViews(){
        backButton = (Button)view.findViewById(R.id.backButton);
        webView = (WebView)view.findViewById(R.id.webview);
    }

    private void addActionToViews(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentUtil!=null) fragmentUtil.back();
            }
        });
        webView.loadUrl(contentUrl);
    }

    public void onResume(){
        super.onResume();
        refreshViews();
    }
}
