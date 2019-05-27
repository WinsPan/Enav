package com.tcup.transformer.transnav.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.tcup.transformer.transnav.di.module.PickListModule;
import com.tcup.transformer.transnav.mvp.contract.PickListContract;

import com.jess.arms.di.scope.ActivityScope;
import com.tcup.transformer.transnav.mvp.ui.activity.PickListActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/27/2019 10:30
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = PickListModule.class, dependencies = AppComponent.class)
public interface PickListComponent {
    void inject(PickListActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PickListComponent.Builder view(PickListContract.View view);

        PickListComponent.Builder appComponent(AppComponent appComponent);

        PickListComponent build();
    }
}