package com.tcup.transformer.transnav.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.tcup.transformer.transnav.di.module.LocationListModule;
import com.tcup.transformer.transnav.mvp.contract.LocationListContract;

import com.jess.arms.di.scope.ActivityScope;
import com.tcup.transformer.transnav.mvp.ui.activity.LocationListActivity;


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
@ActivityScope
@Component(modules = LocationListModule.class, dependencies = AppComponent.class)
public interface LocationListComponent {
    void inject(LocationListActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LocationListComponent.Builder view(LocationListContract.View view);

        LocationListComponent.Builder appComponent(AppComponent appComponent);

        LocationListComponent build();
    }
}