package com.tcup.transformer.transnav.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.tcup.transformer.transnav.mvp.contract.AddSiteContract;
import com.tcup.transformer.transnav.mvp.model.api.service.SiteService;
import com.tcup.transformer.transnav.mvp.model.entity.BaseResponse;
import com.tcup.transformer.transnav.mvp.model.entity.RangeSearchBean;
import com.tcup.transformer.transnav.mvp.model.entity.SiteParamBean;
import com.tcup.transformer.transnav.mvp.model.entity.TypeResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.QueryMap;


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
public class AddSiteModel extends BaseModel implements AddSiteContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public AddSiteModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<TypeResponse> typeSearch(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(SiteService.class).typeSearch(map);
    }

    @Override
    public Observable<RangeSearchBean> areaSearch(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(SiteService.class).areaSearch(map);
    }

    @Override
    public Observable<BaseResponse<String>> addSite(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(SiteService.class).addSite(map);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}