/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tcup.transformer.transnav.mvp.model.api.service;

import com.tcup.transformer.transnav.mvp.model.entity.BaseResponse;
import com.tcup.transformer.transnav.mvp.model.entity.ListPageBean;
import com.tcup.transformer.transnav.mvp.model.entity.RangeSearchBean;
import com.tcup.transformer.transnav.mvp.model.entity.SiteParamBean;
import com.tcup.transformer.transnav.mvp.model.entity.TypeResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * ================================================
 * 展示 {@link Retrofit#create(Class)} 中需要传入的 ApiService 的使用方式
 * 存放关于用户的一些 API
 * <p>
 * Created by JessYan on 08/05/2016 12:05
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface SiteService {
    String HEADER_API_VERSION = "Accept: */*";

    @POST("/site/add")
    Observable<BaseResponse<String>> addSite(@QueryMap Map<String, Object> map);

    @POST("/site/search")
    Observable<BaseResponse<ListPageBean>> searchSite(@QueryMap Map<String, Object> map);

    @POST("/site/update")
    Observable<BaseResponse<String>> editSite(@QueryMap Map<String, Object> map);

    @POST("/site/range/search")
    Observable<BaseResponse<RangeSearchBean>> rangeSearchSite(@QueryMap Map<String, Object> map);

    @POST("type/search")
    Observable<TypeResponse> typeSearch(@QueryMap Map<String, Object> map);

    @POST("/area/search")
    Observable<RangeSearchBean> areaSearch(@QueryMap Map<String, Object> map);
}
