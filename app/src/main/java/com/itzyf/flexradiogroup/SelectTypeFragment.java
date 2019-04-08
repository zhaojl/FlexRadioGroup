package com.itzyf.flexradiogroup;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

public class SelectTypeFragment extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "SelectTypeFragment";
    private static final int BTN_WIDTH = 220;
    private static final int BTN_HEIGHT = 80;
    private static final int BTN_MARGIN = 31;
    private FlexRadioGroup2 fblFilterFree, fblFilterType, fblFilterBrand;
    private SparseBooleanArray isCollapses; //是否收缩
    private Drawable dropUp, dropDown;

    //private MapViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.select_type_bottom_sheet, container, false);

        fblFilterFree = (FlexRadioGroup2) view.findViewById(R.id.fbl_filter_free);
        fblFilterType = (FlexRadioGroup2) view.findViewById(R.id.fbl_filter_type);
        fblFilterBrand = (FlexRadioGroup2) view.findViewById(R.id.fbl_filter_brand);
        fblFilterFree.setMultiSelect(false);
        fblFilterType.setMultiSelect(false);
        fblFilterBrand.setMultiSelect(true);

        isCollapses = new SparseBooleanArray();

        //仅空闲
        List<String> freeFilter = new ArrayList<String>();
        freeFilter.add("仅空闲");
        createRadioButton(freeFilter, fblFilterFree);

        /*mViewModel = MapsActivity.obtainViewModel(getActivity());
        //充电类型
        mViewModel.getAllChargingType().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> types) {
                if(types != null && !types.contains(ChargingType.ALL)) {
                    types.add(0, ChargingType.ALL);
                }
                createRadioButton(types, fblFilterType);
            }
        });
        //品牌
        mViewModel.getAllBrands().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> brands) {
                if(brands != null && !brands.contains(ChargingBrand.ALL)) {
                    brands.add(0, ChargingBrand.ALL);
                }
                createRadioButton(brands, fblFilterBrand);
            }
        });

        //仅空闲
        fblFilterFree.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewModel.getSelectedOnlyIdle().observe(SelectTypeFragment.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if(aBoolean) {
                            fblFilterFree.check("仅空闲");
                        }
                    }
                });
            }
        }, 100);
        //充电类型
        fblFilterType.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewModel.getSelectedChargingType().observe(SelectTypeFragment.this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String type) {
                        fblFilterType.check(type);
                    }
                });
            }
        }, 100);

        //品牌
        fblFilterBrand.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewModel.getSelectedBrand().observe(SelectTypeFragment.this, new Observer<List<String>>() {
                    @Override
                    public void onChanged(@Nullable List<String> selectedBrands) {
                        fblFilterBrand.checkBrands(selectedBrands);
                    }
                });
            }
        }, 100);*/

        view.findViewById(R.id.btn_reset).setOnClickListener(this);
        view.findViewById(R.id.btn_submit).setOnClickListener(this);

        setCancelable(true);

        return view;
    }

    private void createRadioButton(List<String> filters, final FlexRadioGroup2 radioGroup) {
        /**
         *  64dp菜单的边距{@link DrawerLayout#MIN_DRAWER_MARGIN}+10dp*2为菜单内部的padding=84dp
         */
        for (String filter : filters) {
            final RadioButton rb = (RadioButton) getLayoutInflater(null).inflate(R.layout.select_type_item, null);
            rb.setText(filter);
            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(BTN_WIDTH, BTN_HEIGHT);
            lp.bottomMargin = BTN_MARGIN;
            rb.setLayoutParams(lp);
            radioGroup.addView(rb);
        }
        isCollapses.put(radioGroup.getId(), false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                fblFilterFree.resetCheck();
                fblFilterType.resetCheck();
                fblFilterBrand.resetCheck();
                break;
            case R.id.btn_submit:
                LbsLog.e(TAG, "fblFilterBrand checkedId=" + fblFilterBrand.getCheckedId());
                LbsLog.e(TAG, "fblFilterType checkedId=" + fblFilterType.getCheckedId());

                dismissAllowingStateLoss();

                String type = ChargingType.ALL;
                List<String> brands = new ArrayList<>();
                RadioButton rbType = (RadioButton) getView().findViewById(fblFilterType.getCheckedId());
                if (rbType != null) {
                    type = rbType.getText().toString();
                }

                for(int checkedId : fblFilterBrand.getCheckedIds()) {
                    RadioButton rbBrand = (RadioButton) getView().findViewById(checkedId);
                    if (rbBrand != null) {
                        brands.add(rbBrand.getText().toString());
                    }
                }
                LbsLog.e(TAG, "selected type=" + type + ", brands=" + brands.toString());
                /*mViewModel.setSelectOnlyIdle(fblFilterFree.getCheckedId() != -1);
                mViewModel.setSelectedChargingType(type);
                mViewModel.setSelectBrand(brands);*/
                break;
        }
    }

    /**
     * 设置箭头
     */
    private void setArrow(TextView view, boolean isCollapse) {
        if (!isCollapse) {
            if (dropUp == null) {
                dropUp = getResources().getDrawable(R.drawable.ic_arrow_drop_up_black);
                dropUp.setBounds(0, 0, dropUp.getMinimumWidth(), dropUp.getMinimumHeight());
            }
            view.setCompoundDrawables(null, null, dropUp, null);
        } else {
            if (dropDown == null) {
                dropDown = getResources().getDrawable(R.drawable.ic_arrow_drop_down_black);
                dropDown.setBounds(0, 0, dropDown.getMinimumWidth(), dropDown.getMinimumHeight());
            }
            view.setCompoundDrawables(null, null, dropDown, null);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dismissAllowingStateLoss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fblFilterFree = null;
        fblFilterType = null;
        fblFilterBrand = null;
        dropUp = null;
        dropDown = null;
    }
}
