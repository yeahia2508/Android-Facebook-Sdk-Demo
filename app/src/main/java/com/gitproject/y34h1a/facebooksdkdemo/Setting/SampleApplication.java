package com.gitproject.y34h1a.facebooksdkdemo.Setting;

import android.app.Application;

import com.facebook.login.DefaultAudience;
import com.gitproject.y34h1a.facebooksdkdemo.Helper.SharedObjects;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.utils.Logger;

/**
 * Created by y34h1a on 1/22/16.
 */
public class SampleApplication extends Application {
    private static final String APP_ID = "1531326493826315";
    private static final String APP_NAMESPACE = "sromkuapp_vtwo";

    @Override
    public void onCreate() {
        super.onCreate();
        SharedObjects.context = this;

        // set log to true
        Logger.DEBUG_WITH_STACKTRACE = true;

        // initialize facebook configuration
        Permission[] permissions = new Permission[] {
                // Permission.PUBLIC_PROFILE,
                Permission.EMAIL,
                Permission.USER_EVENTS,
                Permission.USER_ACTIONS_MUSIC,
                Permission.USER_FRIENDS,
                Permission.USER_GAMES_ACTIVITY,
                Permission.USER_BIRTHDAY,
                Permission.USER_TAGGED_PLACES,
                Permission.USER_MANAGED_GROUPS,
                Permission.PUBLISH_ACTION,
                Permission.USER_POSTS,
                Permission.MANAGE_PAGES,
                Permission.USER_MANAGED_GROUPS,
                Permission.USER_PHOTOS,
                Permission.USER_RELATIONSHIPS,
                Permission.USER_EDUCATION_HISTORY,
                Permission.USER_RELIGION_POLITICS,
                Permission.USER_LOCATION,
                Permission.USER_ABOUT_ME};

        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(APP_ID)
                .setNamespace(APP_NAMESPACE)
                .setPermissions(permissions)
                .setDefaultAudience(DefaultAudience.EVERYONE)
                .setAskForAllPermissionsAtOnce(false)
                        // .setGraphVersion("v2.3")
                .build();

        SimpleFacebook.setConfiguration(configuration);
    }
}
