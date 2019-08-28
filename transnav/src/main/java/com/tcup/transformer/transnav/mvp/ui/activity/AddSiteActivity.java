package com.tcup.transformer.transnav.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.navi.model.NaviLatLng;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tcup.transformer.transnav.di.component.DaggerAddSiteComponent;
import com.tcup.transformer.transnav.map.util.ToastUtil;
import com.tcup.transformer.transnav.mvp.contract.AddSiteContract;
import com.tcup.transformer.transnav.mvp.model.entity.AreaBean;
import com.tcup.transformer.transnav.mvp.model.entity.SiteListBean;
import com.tcup.transformer.transnav.mvp.model.entity.TypeBean;
import com.tcup.transformer.transnav.mvp.presenter.AddSitePresenter;

import com.tcup.transformer.transnav.R;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.amap.api.maps.model.BitmapDescriptorFactory.getContext;
import static com.jess.arms.utils.Preconditions.checkNotNull;


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
public class AddSiteActivity extends BaseActivity<AddSitePresenter> implements AddSiteContract.View, View.OnClickListener {
    @BindView(R.id.toolbar_title_head)
    TextView mTitle;
    @BindView(R.id.toolbar_back_head)
    RelativeLayout mBack;
    @BindView(R.id.edt_site_no)
    EditText siteNoEdt;
    @BindView(R.id.edt_site_name)
    EditText siteNameEdt;
    @BindView(R.id.edt_site_addr)
    EditText siteAddrEdt;
    @BindView(R.id.edt_site_lan)
    EditText siteLanEdt;
    @BindView(R.id.edt_site_type)
    TextView siteTypeText;
    @BindView(R.id.edt_site_date)
    TextView siteDateText;
    //    @BindView(R.id.edt_site_area)
//    TextView siteAreaText;
    @BindView(R.id.pick_img)
    ImageButton pickImgBtn;

    private int typeIndex = 0;
    private int areaIndex = 0;


    private List<TypeBean> typeBeanList = new ArrayList<TypeBean>();
    private String[] typeString;
    private List<AreaBean> areaBeanList = new ArrayList<AreaBean>();
    private String[] areaString;
    @Inject
    RxPermissions mRxPermissions;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAddSiteComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_add_site; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        mTitle.setText("新增站点");
        siteTypeText.setOnClickListener(this::onClick);
//        siteAreaText.setOnClickListener(this::onClick);
        siteDateText.setOnClickListener(this::onClick);
        pickImgBtn.setOnClickListener(this::onClick);
        siteLanEdt.setText(getIntent().getStringExtra("lonlat"));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_head:
                killMyself();
                break;
            case R.id.edt_site_type:
                new XPopup.Builder(getActivity())
                        .asCenterList("请选择类型", typeString,
                                null, typeIndex,
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        typeIndex = position;
                                        siteTypeText.setText(text);
                                    }
                                })
                        .show();
                break;
            case R.id.pick_img:
                ArmsUtils.startActivity(new Intent(AddSiteActivity.this, PickLocationActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void initType(List<TypeBean> typeBeans) {
        typeBeanList.clear();
        typeBeanList.addAll(typeBeans);
        typeString = new String[typeBeanList.size()];
        for (int i = 0; i < typeBeanList.size(); i++) {
            typeString[i] = typeBeanList.get(i).getTypeName();
        }
    }

    @Override
    public void initArea(List<AreaBean> areaBeans) {
        areaBeanList.clear();
        areaBeanList.addAll(areaBeans);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSiteListBean(SiteListBean siteListBean) {
        siteLanEdt.setText(siteListBean.getSiteLng() + "," + siteListBean.getSiteLat());
    }
}
