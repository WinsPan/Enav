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
package com.tcup.transformer.transnav.mvp.ui.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tcup.transformer.transnav.R;
import com.tcup.transformer.transnav.app.EventBusTags;
import com.tcup.transformer.transnav.mvp.model.entity.SiteListBean;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class MarkBeanHolder extends BaseHolder<SiteListBean> {

    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.type)
    TextView mType;
    @BindView(R.id.content)
    TextView mContent;
    @BindView(R.id.lanlat)
    TextView mLanlat;
    private AppComponent mAppComponent;

    public MarkBeanHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });
    }

    @Override
    public void setData(@NonNull SiteListBean data, int position) {
        mName.setText(data.getSiteName());
        mType.setText("位置名称：" + data.getSiteAddr());
        mContent.setText("位置描述：" + data.getSiteRemark());
        mLanlat.setText("经纬度信息：" + data.getSiteLng() + "," + data.getSiteLat());

        setOnItemClickListener(new OnViewClickListener() {
            @Override
            public void onViewClick(View view, int position) {
                EventBus.getDefault().postSticky(data);
            }
        });
    }

    /**
     * 在 Activity 的 onDestroy 中使用 {@link DefaultAdapter#releaseAllHolder(RecyclerView)} 方法 (super.onDestroy() 之前)
     * {@link BaseHolder#onRelease()} 才会被调用, 可以在此方法中释放一些资源
     */
    @Override
    protected void onRelease() {
        this.mName = null;
        this.mAppComponent = null;
    }
}
