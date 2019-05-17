package com.tcup.transformer.transnav.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.tcup.transformer.transnav.di.module.BasicNaviModule;
import com.tcup.transformer.transnav.mvp.contract.BasicNaviContract;

import com.jess.arms.di.scope.ActivityScope;
import com.tcup.transformer.transnav.mvp.ui.activity.BasicNaviActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/17/2019 14:10
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = BasicNaviModule.class, dependencies = AppComponent.class)
public interface BasicNaviComponent {
    void inject(BasicNaviActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BasicNaviComponent.Builder view(BasicNaviContract.View view);

        BasicNaviComponent.Builder appComponent(AppComponent appComponent);

        BasicNaviComponent build();
    }
}