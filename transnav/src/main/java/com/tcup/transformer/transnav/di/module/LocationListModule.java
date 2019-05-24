package com.tcup.transformer.transnav.di.module;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tcup.transformer.transnav.bean.MarketBean;
import com.tcup.transformer.transnav.mvp.contract.LocationListContract;
import com.tcup.transformer.transnav.mvp.contract.MainContract;
import com.tcup.transformer.transnav.mvp.model.LocationListModel;
import com.tcup.transformer.transnav.mvp.model.entity.SiteListBean;
import com.tcup.transformer.transnav.mvp.ui.adapter.MarkBeanAdapter;

import java.util.ArrayList;
import java.util.List;


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
@Module
public abstract class LocationListModule {

    @Binds
    abstract LocationListContract.Model bindLocationListModel(LocationListModel model);

    @ActivityScope
    @Provides
    static RxPermissions provideRxPermissions(LocationListContract.View view) {
        return new RxPermissions((FragmentActivity) view.getActivity());
    }

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(LocationListContract.View view) {
        return new GridLayoutManager(view.getActivity(), 1);
    }

    @ActivityScope
    @Provides
    static List<SiteListBean> provideMarkBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static RecyclerView.Adapter provideMarkBeanAdapter(List<SiteListBean> list) {
        return new MarkBeanAdapter(list);
    }
}