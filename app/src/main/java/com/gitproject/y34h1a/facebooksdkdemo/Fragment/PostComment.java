package com.gitproject.y34h1a.facebooksdkdemo.Fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.gitproject.y34h1a.facebooksdkdemo.Helper.BaseFragment;
import com.gitproject.y34h1a.facebooksdkdemo.R;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.actions.Cursor;
import com.sromku.simple.fb.entities.Comment;
import com.sromku.simple.fb.listeners.OnCommentsListener;
import com.sromku.simple.fb.listeners.OnPublishListener;
import com.sromku.simple.fb.utils.Utils;

import java.util.List;

public class PostComment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    EditText etComment,etPostId;
    TextView tvLog;
    Button btPostComment,btGetComments,btLoadMore;
    private String mAllPages = "";


    public PostComment() {
        // Required empty public constructor
    }

    public static PostComment newInstance(String param1, String param2) {
        PostComment fragment = new PostComment();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_post_comment, container, false);
        etComment = (EditText) view.findViewById(R.id.etComment);
        btPostComment = (Button) view.findViewById(R.id.btPostComment);
        btGetComments = (Button) view.findViewById(R.id.btGetComment);
        btLoadMore = (Button) view.findViewById(R.id.btLoadMore);
        tvLog = (TextView) view.findViewById(R.id.tvPostLog);
        tvLog.setText("Before add comment please add post id inside source code. Use Graphical Layout just for testing");

        btPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment comment = new Comment.Builder()
                        .setMessage(etComment.getText().toString())
                        .build();


                // You can add image with comment -> comment.setAttachImageUrl(url)
                //You can get your facebook post id by clicking on post creation time.(form pc version)
                // Post id pattern will be: yourFacebookID_yourPostId like 100000286783885_1114772988542250


                SimpleFacebook.getInstance().publish("100000286783885_1103179293034953", comment, new OnPublishListener() {
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
                        showDialog("Please Wait", "Posting Comment...");
                    }

                    @Override
                    public void onComplete(String response) {
                        hideDialog();
                        tvLog.setText("Comment Posted");
                    }
                });

            }
        });

        btGetComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entityId = "100000286783885_1103179293034953";
                SimpleFacebook.getInstance().getComments(entityId, new OnCommentsListener() {

                    @Override
                    public void onThinking() {
                        showDialog("Please Wait","Getting Comments");
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
                    public void onComplete(List<Comment> response) {
                        hideDialog();
                        // make the result more readable.
                        mAllPages += "<u>\u25B7\u25B7\u25B7 (paging) #" + getPageNum() + " \u25C1\u25C1\u25C1</u><br>";
                        mAllPages += Utils.join(response.iterator(), "<br>", new Utils.Process<Comment>() {
                            @Override
                            public String process(Comment comment) {
                                return "\u25CF " + comment.getMessage() + " \u25CF";
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

    private void enableLoadMore(final Cursor<List<Comment>> cursor) {
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
