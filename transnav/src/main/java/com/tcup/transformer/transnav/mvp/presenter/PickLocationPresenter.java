package com.tcup.transformer.transnav.mvp.presenter;

import android.Manifest;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.RxLifecycleUtils;
import com.tcup.transformer.transnav.mvp.contract.MainContract;
import com.tcup.transformer.transnav.mvp.contract.PickLocationContract;
import com.tcup.transformer.transnav.mvp.model.entity.BaseResponse;
import com.tcup.transformer.transnav.mvp.model.entity.ListPageBean;
import com.tcup.transformer.transnav.mvp.model.entity.SiteListBean;
import com.tcup.transformer.transnav.mvp.model.entity.SiteParamBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    public void editSite(SiteListBean siteListBean) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
//        SiteParamBean siteParamBean = new SiteParamBean();
//        siteParamBean.setId(siteListBean.getId());
        paramMap.put("id", siteListBean.getId());
//        siteParamBean.setAreaId(siteListBean.getEqpAreaDomain().getId());
//        siteParamBean.setCreateUserId(siteListBean.getCreateUser().getId());
//        siteParamBean.setOperateUserId(siteListBean.getOperateUser().getId());
//        siteParamBean.setSiteAddr(siteListBean.getSiteAddr());
//        siteParamBean.setSiteDate(siteListBean.getSiteDate());
//        siteParamBean.setSiteLat(siteListBean.getSiteLat());
//        siteParamBean.setSiteLng(siteListBean.getSiteLng());
        paramMap.put("siteLat", siteListBean.getSiteLat());
        paramMap.put("siteLng", siteListBean.getSiteLng());
        paramMap.put("userAccount", "appAccount");
        paramMap.put("token", "df6bc488b3c6f3a2140f316c923462e5");
//        siteParamBean.setSiteName(siteListBean.getSiteName());
//        siteParamBean.setSiteNo(siteListBean.getSiteNo());
//        siteParamBean.setSiteRemark(siteListBean.getSiteRemark());
//        siteParamBean.setSiteStatus(siteListBean.getSiteStatus());
//        siteParamBean.setTypeId(siteListBean.getEqpTypeDomain().getId());
        mModel.editSite(paramMap).subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse<String>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse<String> baseResponse) {
                        if (baseResponse.getStatus() > -1) {
                            ArmsUtils.snackbarText("位置信息更新成功");
                        } else {
                            ArmsUtils.snackbarText(baseResponse.getMessage());
                        }
                    }
                });
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
