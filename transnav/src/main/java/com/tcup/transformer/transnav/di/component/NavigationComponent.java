package com.tcup.transformer.transnav.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.tcup.transformer.transnav.di.module.NavigationModule;
import com.tcup.transformer.transnav.mvp.contract.NavigationContract;

import com.jess.arms.di.scope.ActivityScope;
import com.tcup.transformer.transnav.mvp.ui.activity.NavigationActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/17/2019 09:17
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = NavigationModule.class, dependencies = AppComponent.class)
public interface NavigationComponent {
    void inject(NavigationActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        NavigationComponent.Builder view(NavigationContract.View view);

        NavigationComponent.Builder appComponent(AppComponent appComponent);

        NavigationComponent build();
    }
}