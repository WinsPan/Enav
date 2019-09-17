package com.tcup.transformer.transnav.di.module;

import android.support.v4.app.FragmentActivity;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tcup.transformer.transnav.mvp.contract.SerchActivityContract;
import com.tcup.transformer.transnav.mvp.contract.SiteDetailContract;
import com.tcup.transformer.transnav.mvp.model.SiteDetailModel;
import com.tcup.transformer.transnav.mvp.ui.activity.SiteDetailActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/17/2019 12:34
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class SiteDetailModule {

    @Binds
    abstract SiteDetailContract.Model bindSiteDetailModel(SiteDetailModel model);

    @ActivityScope
    @Provides
    static RxPermissions provideRxPermissions(SiteDetailContract.View view) {
        return new RxPermissions((FragmentActivity) view.getActivity());
    }
}