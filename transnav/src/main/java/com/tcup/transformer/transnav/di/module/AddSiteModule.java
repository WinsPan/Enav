package com.tcup.transformer.transnav.di.module;

import android.support.v4.app.FragmentActivity;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tcup.transformer.transnav.mvp.contract.AddSiteContract;
import com.tcup.transformer.transnav.mvp.contract.MainContract;
import com.tcup.transformer.transnav.mvp.model.AddSiteModel;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/28/2019 09:04
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class AddSiteModule {

    @Binds
    abstract AddSiteContract.Model bindAddSiteModel(AddSiteModel model);

    @ActivityScope
    @Provides
    static RxPermissions provideRxPermissions(AddSiteContract.View view) {
        return new RxPermissions((FragmentActivity) view.getActivity());
    }
}