package com.gitproject.y34h1a.facebooksdkdemo.Helper;

/**
 * Created by y34h1a on 1/22/16.
 */
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    private ProgressDialog mProgressDialog;

    protected void showDialog(String title, String dis) {
        if (mProgressDialog == null) {
            setProgressDialog(title,dis);
        }
        mProgressDialog.show();
    }

    protected void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void setProgressDialog(String title, String dis) {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(dis);
    }

}