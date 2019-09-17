package com.tcup.transformer.transnav.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.tcup.transformer.transnav.di.module.SiteDetailModule;
import com.tcup.transformer.transnav.mvp.contract.SiteDetailContract;

import com.jess.arms.di.scope.ActivityScope;
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
@ActivityScope
@Component(modules = SiteDetailModule.class, dependencies = AppComponent.class)
public interface SiteDetailComponent {
    void inject(SiteDetailActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SiteDetailComponent.Builder view(SiteDetailContract.View view);

        SiteDetailComponent.Builder appComponent(AppComponent appComponent);

        SiteDetailComponent build();
    }
}