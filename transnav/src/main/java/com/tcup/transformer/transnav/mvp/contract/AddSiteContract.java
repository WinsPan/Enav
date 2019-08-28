package com.tcup.transformer.transnav.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tcup.transformer.transnav.mvp.model.entity.AreaBean;
import com.tcup.transformer.transnav.mvp.model.entity.BaseResponse;
import com.tcup.transformer.transnav.mvp.model.entity.RangeSearchBean;
import com.tcup.transformer.transnav.mvp.model.entity.SiteListBean;
import com.tcup.transformer.transnav.mvp.model.entity.SiteParamBean;
import com.tcup.transformer.transnav.mvp.model.entity.TypeBean;
import com.tcup.transformer.transnav.mvp.model.entity.TypeResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
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
public interface AddSiteContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Activity getActivity();

        //申请权限
        RxPermissions getRxPermissions();

        void initType(List<TypeBean> typeBeans);

        void initArea(List<AreaBean> areaBeans);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<TypeResponse> typeSearch(@QueryMap Map<String, Object> map);
        Observable<RangeSearchBean> areaSearch(@QueryMap Map<String, Object> map);
        Observable<BaseResponse<String>> addSite(@Body SiteParamBean siteParamBean);
    }
}
