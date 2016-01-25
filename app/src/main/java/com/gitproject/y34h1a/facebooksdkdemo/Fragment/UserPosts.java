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
import com.sromku.simple.fb.entities.Post;
import com.sromku.simple.fb.listeners.OnPostsListener;
import com.sromku.simple.fb.utils.Utils;

import java.util.List;


public class UserPosts extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button btGetPosts,btLoadMore;
    TextView tvLog;
    private String mAllPages = "";

    public UserPosts() {

    }

    public static UserPosts newInstance(String param1, String param2) {
        UserPosts fragment = new UserPosts();
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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_posts, container, false);
        btGetPosts = (Button) view.findViewById(R.id.btGetPosts);
        btLoadMore = (Button) view.findViewById(R.id.btLoadMore);
        tvLog = (TextView) view.findViewById(R.id.tvPostLog);

        btGetPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAllPages = "";
                tvLog.setText(mAllPages);

                SimpleFacebook.getInstance().getPosts(new OnPostsListener() {

                    @Override
                    public void onThinking() {
                        showDialog("Please Wait","Posts Retriving...");
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
                    public void onComplete(List<Post> response) {
                        hideDialog();
                        // make the result more readable.
                        mAllPages += "<u>\u25B7\u25B7\u25B7 Page #" + getPageNum() + " \u25C1\u25C1\u25C1</u><br>";
                        mAllPages += Utils.join(response.iterator(), "<br>", new Utils.Process<Post>() {
                            @Override
                            public String process(Post post) {
                                // if post message is null then get id , otherwise get post message
                                return "● " + post.getMessage() + " \u25CF";
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
        return view;
    }

    private void enableLoadMore(final Cursor<List<Post>> cursor) {
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
