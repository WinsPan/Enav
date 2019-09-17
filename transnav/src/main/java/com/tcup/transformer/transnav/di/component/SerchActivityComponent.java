package com.tcup.transformer.transnav.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.tcup.transformer.transnav.di.module.SerchActivityModule;
import com.tcup.transformer.transnav.mvp.contract.SerchActivityContract;

import com.jess.arms.di.scope.ActivityScope;
import com.tcup.transformer.transnav.mvp.ui.activity.SerchActivityActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/17/2019 09:10
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = SerchActivityModule.class, dependencies = AppComponent.class)
public interface SerchActivityComponent {
    void inject(SerchActivityActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SerchActivityComponent.Builder view(SerchActivityContract.View view);

        SerchActivityComponent.Builder appComponent(AppComponent appComponent);

        SerchActivityComponent build();
    }
}