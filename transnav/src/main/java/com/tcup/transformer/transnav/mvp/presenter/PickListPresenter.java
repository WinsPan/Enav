package com.tcup.transformer.transnav.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.support.v7.widget.RecyclerView;

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
import com.tcup.transformer.transnav.mvp.contract.PickListContract;
import com.tcup.transformer.transnav.mvp.model.entity.BaseResponse;
import com.tcup.transformer.transnav.mvp.model.entity.ListPageBean;
import com.tcup.transformer.transnav.mvp.model.entity.QueryParam;
import com.tcup.transformer.transnav.mvp.model.entity.SiteListBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/27/2019 10:30
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class PickListPresenter extends BasePresenter<PickListContract.Model, PickListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    List<SiteListBean> siteListBeans;
    @Inject
    RecyclerView.Adapter mAdapter;
    private long lastUserId = 1;
    private boolean isFirst = true;
    private int preEndIndex;
    private int mPage = 1;
    private boolean isHasNextPage = true;

    @Inject
    public PickListPresenter(PickListContract.Model model, PickListContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycles 的新特性 (此特性已被加入 Support library)
     * 使 {@code Presenter} 可以与 {@link SupportActivity} 和 {@link Fragment} 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        requestMarks(true, "");//打开 App 时自动加载列表
    }

    public int getmPage() {
        return mPage;
    }

    public boolean isLastPage() {
        return isHasNextPage;
    }

    public void setLastPage(boolean lastPage) {
        isHasNextPage = lastPage;
    }

    public void requestMarks(final boolean pullToRefresh, String siteName) {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                requestFromModel(pullToRefresh, siteName);
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                mRootView.showMessage("Request permissions failure");
                mRootView.hideLoading();//隐藏下拉刷新的进度条
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                mRootView.showMessage("Need to go to the settings");
                mRootView.hideLoading();//隐藏下拉刷新的进度条
            }
        }, mRootView.getRxPermissions(), mErrorHandler);
    }

    private void requestFromModel(boolean pullToRefresh, String siteName) {
        if (pullToRefresh) {
            mPage = 1;
        }//下拉刷新默认只请求第一页
        if (mPage == -1) {
            ArmsUtils.snackbarText("没有更多数据了");
        }
        QueryParam queryParam = new QueryParam();
        queryParam.setPageIndex(mPage);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("siteName", siteName);
        paramMap.put("pageIndex", mPage);
        paramMap.put("userAccount", queryParam.getUserAccount());
        paramMap.put("token", queryParam.getToken());


        mModel.searchSite(paramMap).subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh)
                        mRootView.showLoading();//显示下拉刷新的进度条
                    else
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRefresh)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse<ListPageBean>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse<ListPageBean> baseResponse) {
                        if (baseResponse.getStatus() > -1) {
                            if (pullToRefresh) {
                                siteListBeans.clear();//如果是下拉刷新则清空列表
                            }
                            preEndIndex = siteListBeans.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            siteListBeans.addAll(baseResponse.getData().getList());
                            isHasNextPage = baseResponse.getData().isHasNextPage();
                            if (isHasNextPage) {
                                mPage = baseResponse.getData().getNextPage();
                            } else {
                                mPage = -1;
                            }
                            if (pullToRefresh) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, baseResponse.getData().getList().size());
                            }
                        } else if (mPage != -1) {
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
