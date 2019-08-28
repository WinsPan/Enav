package com.tcup.transformer.transnav.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.tcup.transformer.transnav.di.module.AddSiteModule;
import com.tcup.transformer.transnav.mvp.contract.AddSiteContract;

import com.jess.arms.di.scope.ActivityScope;
import com.tcup.transformer.transnav.mvp.ui.activity.AddSiteActivity;


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
@ActivityScope
@Component(modules = AddSiteModule.class, dependencies = AppComponent.class)
public interface AddSiteComponent {
    void inject(AddSiteActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AddSiteComponent.Builder view(AddSiteContract.View view);

        AddSiteComponent.Builder appComponent(AppComponent appComponent);

        AddSiteComponent build();
    }
}