package com.tcup.transformer.transnav.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.tcup.transformer.transnav.di.module.PickLocationModule;
import com.tcup.transformer.transnav.mvp.contract.PickLocationContract;

import com.jess.arms.di.scope.ActivityScope;
import com.tcup.transformer.transnav.mvp.ui.activity.PickLocationActivity;


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
@ActivityScope
@Component(modules = PickLocationModule.class, dependencies = AppComponent.class)
public interface PickLocationComponent {
    void inject(PickLocationActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PickLocationComponent.Builder view(PickLocationContract.View view);

        PickLocationComponent.Builder appComponent(AppComponent appComponent);

        PickLocationComponent build();
    }
}