package com.tcup.transformer.transnav.di.module;

import android.support.v4.app.FragmentActivity;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tcup.transformer.transnav.mvp.contract.MainContract;
import com.tcup.transformer.transnav.mvp.contract.PickLocationContract;
import com.tcup.transformer.transnav.mvp.model.PickLocationModel;


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
@Module
public abstract class PickLocationModule {

    @Binds
    abstract PickLocationContract.Model bindPickLocationModel(PickLocationModel model);

    @ActivityScope
    @Provides
    static RxPermissions provideRxPermissions(PickLocationContract.View view) {
        return new RxPermissions((FragmentActivity) view.getActivity());
    }
}