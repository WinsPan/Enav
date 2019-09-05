package com.tcup.transformer.transnav.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.kongzue.dialog.v3.WaitDialog;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tcup.transformer.transnav.R;
import com.tcup.transformer.transnav.di.component.DaggerAddSiteComponent;
import com.tcup.transformer.transnav.map.util.ToastUtil;
import com.tcup.transformer.transnav.mvp.contract.AddSiteContract;
import com.tcup.transformer.transnav.mvp.model.entity.AreaBean;
import com.tcup.transformer.transnav.mvp.model.entity.SiteListBean;
import com.tcup.transformer.transnav.mvp.model.entity.SiteParamBean;
import com.tcup.transformer.transnav.mvp.model.entity.TypeBean;
import com.tcup.transformer.transnav.mvp.model.entity.UserBean;
import com.tcup.transformer.transnav.mvp.presenter.AddSitePresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;

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
public class AddSiteActivity extends BaseActivity<AddSitePresenter> implements AddSiteContract.View, View.OnClickListener,RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.toolbar_edit_title_head)
    TextView mTitle;
    @BindView(R.id.toolbar_edit_back_head)
    RelativeLayout mBack;
    @BindView(R.id.submit_btn)
    Button submitBtn;
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

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    //    @BindView(R.id.edt_site_area)
//    TextView siteAreaText;
    @BindView(R.id.edt_site_remark)
    TextView siteremarkText;
    @BindView(R.id.pick_img)
    ImageButton pickImgBtn;

    private int typeIndex = 0;
    private int areaIndex = 0;
    private String siteStatu = "1";


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
        mBack.setOnClickListener(this::onClick);
        submitBtn.setOnClickListener(this::onClick);
        radioGroup.setOnCheckedChangeListener(this::onCheckedChanged);
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
        WaitDialog.dismiss();
        clearText();
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        clearText();
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
            case R.id.toolbar_edit_back_head:
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
                Intent intent = new Intent(AddSiteActivity.this, PickLocationActivity.class);
                intent.putExtra("lonlat", siteLanEdt.getText().toString());
                ArmsUtils.startActivity(intent);
                break;
            case R.id.submit_btn:
                submitForm();
                break;
            case R.id.edt_site_date:
                onYearMonthDayPicker();
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radbtn = (RadioButton) findViewById(checkedId);
        if ("正常".equals(radbtn.getText().toString())){
            siteStatu = "1";
        }else {
            siteStatu = "2";
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onSiteListBean(SiteListBean siteListBean) {
        if (siteListBean.getSiteLat() != null && siteListBean.getSiteLat() != null) {
            siteLanEdt.setText(siteListBean.getSiteLng() + "," + siteListBean.getSiteLat());
        }
    }

    public void submitForm() {
        WaitDialog.show(AddSiteActivity.this, "请稍候...");
        if (checkForm()) {
            UserBean userBean = new UserBean();
            SiteParamBean siteParamBean = new SiteParamBean();
            siteParamBean.setSiteNo(siteNoEdt.getText().toString());
            siteParamBean.setSiteName(siteNameEdt.getText().toString());
            siteParamBean.setSiteAddr(siteAddrEdt.getText().toString());
            siteParamBean.setSiteLng(siteLanEdt.getText().toString().split(",")[0]);
            siteParamBean.setSiteLat(siteLanEdt.getText().toString().split(",")[1]);
            siteParamBean.setSiteRemark(siteremarkText.getText() == null ? "" : siteremarkText.getText().toString());
            siteParamBean.setCreateUserId(userBean.getUserAccount());
            siteParamBean.setSiteDate(siteDateText.getText().toString());
            siteParamBean.setOperateUserId(userBean.getUserAccount());
            siteParamBean.setSiteStatus(siteStatu);
            mPresenter.addSite(siteParamBean);
        }
    }

    public boolean checkForm() {
        if (siteNoEdt.getText() == null || "".equals(siteNoEdt.getText().toString())) {
            ToastUtil.show(AddSiteActivity.this, "请输入站点编号");
            return false;
        } else if (siteNameEdt.getText() == null || "".equals(siteNameEdt.getText().toString())) {
            ToastUtil.show(AddSiteActivity.this, "请输入站点名称");
            return false;
        } else if (siteAddrEdt.getText() == null || "".equals(siteAddrEdt.getText().toString())) {
            ToastUtil.show(AddSiteActivity.this, "请输入站点地址");
            return false;
        } else if (siteLanEdt.getText() == null || "".equals(siteLanEdt.getText().toString())) {
            ToastUtil.show(AddSiteActivity.this, "请输入站点经纬度信息");
            return false;
        } else if (siteTypeText.getText() == null || "".equals(siteTypeText.getText().toString())) {
            ToastUtil.show(AddSiteActivity.this, "清选择站点类型");
            return false;
        } else if (siteDateText.getText() == null || "".equals(siteDateText.getText().toString())) {
            ToastUtil.show(AddSiteActivity.this, "清选择投运日期");
            return false;
        }
        return true;
    }

    public void clearText() {
        siteNoEdt.setText("");
        siteNameEdt.setText("");
        siteLanEdt.setText("");
        siteAddrEdt.setText("");
        siteTypeText.setText("");
        siteremarkText.setText("");
    }

    public void onYearMonthDayPicker() {
        final DatePicker picker = new DatePicker(this);
        Calendar cal = Calendar.getInstance();
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 10));
        picker.setRangeEnd(2050, 1, 1);
        picker.setRangeStart(1950, 1, 1);
        picker.setSelectedItem(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                siteDateText.setText(year + month + day);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }
}
