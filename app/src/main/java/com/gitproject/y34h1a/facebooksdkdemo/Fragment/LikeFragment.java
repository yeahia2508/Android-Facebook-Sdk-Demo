package com.gitproject.y34h1a.facebooksdkdemo.Fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.gitproject.y34h1a.facebooksdkdemo.Helper.BaseFragment;
import com.gitproject.y34h1a.facebooksdkdemo.R;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.actions.Cursor;
import com.sromku.simple.fb.entities.Like;
import com.sromku.simple.fb.listeners.OnLikesListener;
import com.sromku.simple.fb.listeners.OnPublishListener;
import com.sromku.simple.fb.utils.Utils;

import java.util.List;


public class LikeFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    Button btSendLike;
    Button btGetLikes;
    Button btLoadMore;
    TextView tvLog;
    private String mAllPages = "";


    public LikeFragment() {
        // Required empty public constructor
    }


    public static LikeFragment newInstance(String param1, String param2) {
        LikeFragment fragment = new LikeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_like, container, false);
        btGetLikes = (Button) view.findViewById(R.id.btGetLikes);
        btSendLike = (Button) view.findViewById(R.id.btPostLike);
        btLoadMore = (Button) view.findViewById(R.id.btLoadMore);
        tvLog = (TextView) view.findViewById(R.id.tvPostLog);
        tvLog.setText("Before use this buttons please change post id inside source code. Use Graphical Layout only for testing purpose");
        btGetLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entityId = "100000286783885_1103179293034953";
                SimpleFacebook.getInstance().getLikes(entityId, new OnLikesListener() {

                    @Override
                    public void onThinking() {
                        showDialog("Please Wait","Getting Post Likes....");
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        hideDialog();
                        tvLog.setText(throwable.getMessage());
                    }

                    @Override
                    public void onFail(String reason) {
                        hideDialog();
                        tvLog.setText(reason);
                    }

                    @Override
                    public void onComplete(List<Like> response) {
                        hideDialog();
                        // make the result more readable.
                        mAllPages += "<u>\u25B7\u25B7\u25B7 (paging) #" + getPageNum() + " \u25C1\u25C1\u25C1</u><br>";
                        mAllPages += Utils.join(response.iterator(), "<br>", new Utils.Process<Like>() {
                            @Override
                            public String process(Like like) {
                                return "\u25CF " + like.getUser().getId() + " \u25CF";
                            }
                        });
                        mAllPages += "<br>";
                        tvLog.setText(Html.fromHtml(mAllPages));

                        // check if more pages exist
                        if (hasNext()) {
                            enableLoadMore(getCursor());
                        } else {
                            disableLoadMore();
                        }
                    }
                });
            }
        });

        btSendLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Like like = new Like.Builder().build();

                SimpleFacebook.getInstance().publish("100000286783885_1114772988542250", like, new OnPublishListener() {
                    @Override
                    public void onException(Throwable throwable) {
                        hideDialog();
                        tvLog.setText(throwable.getMessage());
                    }

                    @Override
                    public void onFail(String reason) {
                        hideDialog();
                        tvLog.setText(reason);
                    }

                    @Override
                    public void onThinking() {
                        showDialog("Please Wait","Sending Like...");
                    }

                    @Override
                    public void onComplete(String response) {
                        hideDialog();
                        tvLog.setText("You Like Posted Successfully");
                    }
                });
            }
        });

        return view;
    }

    private void enableLoadMore(final Cursor<List<Like>> cursor) {
        btLoadMore.setVisibility(View.VISIBLE);
        btLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mAllPages += "<br>";
                cursor.next();
            }
        });
    }

    private void disableLoadMore() {
        btLoadMore.setOnClickListener(null);
        btLoadMore.setVisibility(View.INVISIBLE);
    }
}
