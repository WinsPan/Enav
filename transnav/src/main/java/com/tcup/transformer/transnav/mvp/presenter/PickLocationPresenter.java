package com.tcup.transformer.transnav.mvp.presenter;

import android.Manifest;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.jess.arms.utils.PermissionUtil;
import com.tcup.transformer.transnav.mvp.contract.MainContract;
import com.tcup.transformer.transnav.mvp.contract.PickLocationContract;

import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/24/2019 15:50
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class PickLocationPresenter extends BasePresenter<PickLocationContract.Model, PickLocationContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    private final String[] pemissions = new String[]

            {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };


    @Inject
    public PickLocationPresenter(PickLocationContract.Model model, PickLocationContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        requestUserPermission();
    }

    public void requestUserPermission() {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.requestPermission(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //request permission success, do something.
                mRootView.initMap();
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                mRootView.showMessage("请进行授权后使用");
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                mRootView.showMessage("需要到设置界面进行授权");
            }
        }, mRootView.getRxPermissions(), mErrorHandler, pemissions);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
