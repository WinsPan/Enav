package com.tcup.transformer.transnav.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.LogUtils;
import com.paginate.Paginate;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tcup.transformer.transnav.R;
import com.tcup.transformer.transnav.bean.MessageEvent;
import com.tcup.transformer.transnav.di.component.DaggerLocationListComponent;
import com.tcup.transformer.transnav.mvp.contract.LocationListContract;
import com.tcup.transformer.transnav.mvp.model.entity.SiteListBean;
import com.tcup.transformer.transnav.mvp.presenter.LocationListPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/13/2019 09:26
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class LocationListActivity extends BaseActivity<LocationListPresenter> implements LocationListContract.View, OnRefreshListener, OnLoadmoreListener, View.OnClickListener ,SearchView.OnQueryTextListener ,CheckBox.OnCheckedChangeListener{
    @BindView(R.id.toolbar_title_head)
    TextView mTitle;
    @BindView(R.id.toolbar_back_head)
    RelativeLayout mBack;
    @BindView(R.id.checkbox_gz)
    CheckBox mCheckBox;
    @BindView(R.id.serachViewPick)
    SearchView mSearchView;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mSwipeRefreshLayout;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RxPermissions mRxPermissions;
    @Inject
    RecyclerView.Adapter mAdapter;
    private Paginate mPaginate;
    private boolean isLoadingMore;
    private String siteName = "";


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLocationListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_location_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mTitle.setText("变电站列表");
        mSearchView.setOnQueryTextListener(this);
        mCheckBox.setOnCheckedChangeListener(this);
        mBack.setOnClickListener(this::onClick);
        initRecyclerView();
        mRecyclerView.setAdapter(mAdapter);
        initPaginate();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            siteName = newText;
            mPresenter.requestMarks(true, newText);
        } else {
            siteName = "";
            mSwipeRefreshLayout.resetNoMoreData();
            mPresenter.requestMarks(true,siteName);
        }
        return false;
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setOnLoadmoreListener(this);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
    }


    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public boolean getChecked(){
        return mCheckBox.isChecked();
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(mRecyclerView);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
        this.mRxPermissions = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_head:
                EventBus.getDefault().post(new MessageEvent(false));
                killMyself();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onSiteListBean(SiteListBean siteListBean) {
        if (siteListBean.getSiteLat() != null && siteListBean.getSiteLng() != null) {
            ArmsUtils.startActivity(new Intent(LocationListActivity.this,SiteDetailActivity.class));
            killMyself();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mPresenter.requestMarks(true,siteName);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        refreshlayout.finishLoadmore(1000);
        mPresenter.requestMarks(false,siteName);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        refreshlayout.finishRefresh(1000);
        mSwipeRefreshLayout.resetNoMoreData();
        mPresenter.requestMarks(true,siteName);
    }

    @Override
    public void finishNoMore() {
        mSwipeRefreshLayout.finishLoadmoreWithNoMoreData();//将不会再次触发加载更多事件
    }
}
