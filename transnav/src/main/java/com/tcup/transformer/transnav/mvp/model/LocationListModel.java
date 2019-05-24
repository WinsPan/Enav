package com.tcup.transformer.transnav.mvp.model;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.tcup.transformer.transnav.bean.MarketBean;
import com.tcup.transformer.transnav.map.util.ORMUtil;
import com.tcup.transformer.transnav.mvp.contract.LocationListContract;
import com.tcup.transformer.transnav.mvp.model.api.service.SiteService;
import com.tcup.transformer.transnav.mvp.model.entity.BaseResponse;
import com.tcup.transformer.transnav.mvp.model.entity.ListPageBean;
import com.tcup.transformer.transnav.mvp.model.entity.SiteParamBean;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import timber.log.Timber;


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
public class LocationListModel extends BaseModel implements LocationListContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public LocationListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<List<MarketBean>> getMarks(long lastIdQueried, boolean update) {
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return Observable.just(ORMUtil.getLiteOrm(mApplication).query(new QueryBuilder<MarketBean>(MarketBean.class)
                .appendOrderDescBy("createTime")));

    }

    @Override
    public Observable<BaseResponse<String>> addSite(SiteParamBean siteParamBean) {
        return mRepositoryManager.obtainRetrofitService(SiteService.class)
                .addSite(siteParamBean);
    }

    @Override
    public Observable<BaseResponse<ListPageBean>> searchSite(Map<String, Object> map) {
        return mRepositoryManager.obtainRetrofitService(SiteService.class).searchSite(map);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        Timber.d("Release Resource");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}