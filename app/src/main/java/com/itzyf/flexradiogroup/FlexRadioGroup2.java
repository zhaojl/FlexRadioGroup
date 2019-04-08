package com.itzyf.flexradiogroup;

import android.content.Context;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.google.android.flexbox.FlexboxLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlexRadioGroup2 extends FlexboxLayout {

    private boolean multiSelect = false;//多项选择
    private int mCheckedId = -1;
    private Set<Integer> mCheckedIds = new HashSet<>();

    private View.OnClickListener mOnClickListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;


    public FlexRadioGroup2(Context context) {
        super(context);
        init();
    }

    public FlexRadioGroup2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mOnClickListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof RadioButton) {
            final RadioButton button = (RadioButton) child;
            int id = button.getId();
            if(isMultiSelect()) {
                if (button.isChecked()) {
                    setCheckedStateForView(id, true);
                    mCheckedIds.add(id);
                } else {
                    setCheckedStateForView(id, false);
                    mCheckedIds.remove(id);
                }
            } else {
                if (button.isChecked()) {
                    if (mCheckedId != -1) {
                        setCheckedStateForView(mCheckedId, false);
                    }
                    setCheckedStateForView(id, true);
                    mCheckedId = id;
                }
            }
        }

        super.addView(child, index, params);
    }

    /**
     * 设置Checked状态到View中
     *
     * @param viewId
     * @param checked
     */
    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof RadioButton) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }

    public void check(String text) {
        if(TextUtils.isEmpty(text)) {
            return;
        }

        int toCheckedId = -1;
        for (int i = 0; i < getChildCount(); i++) {
            if(((RadioButton)getChildAt(i)).getText().equals(text)) {
                toCheckedId = getChildAt(i).getId();
                break;
            }
        }

        if(toCheckedId == -1) {
            return;
        }

        if(isMultiSelect()) {
            if(mCheckedIds.contains(toCheckedId)) {
                setCheckedStateForView(toCheckedId, false);
                mCheckedIds.remove(toCheckedId);
            } else {
                setCheckedStateForView(toCheckedId, true);
                mCheckedIds.add(toCheckedId);
            }
        } else {
            if(mCheckedId == toCheckedId) {
                return;
            }
            if(mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            setCheckedStateForView(toCheckedId, true);
            mCheckedId = toCheckedId;
        }
    }

    public void checkBrands(List<String> selectedBrands) {
        if(selectedBrands == null || selectedBrands.isEmpty()) {
            return;
        }
        for(String brand : selectedBrands) {
            check(brand);
        }
    }

    public void resetCheck() {
        int defaultCheckedId = getChildAt(0).getId();
        if(isMultiSelect()) {
            for(int id : mCheckedIds) {
                setCheckedStateForView(id, false);
            }
            mCheckedIds.clear();
            //设置默认选中
            setCheckedStateForView(defaultCheckedId, true);
            mCheckedIds.add(defaultCheckedId);

        } else {
            if (getChildCount() == 1) {
                setCheckedStateForView(mCheckedId, false);
                mCheckedId = -1;
                return;
            }
            if(mCheckedId == defaultCheckedId) {
                return;
            }
            if(mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            setCheckedStateForView(defaultCheckedId, true);
            mCheckedId = defaultCheckedId;
        }
    }

    public void setMultiSelect(boolean flag) {
        multiSelect = flag;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    @IdRes
    public int getCheckedId() {
        return mCheckedId;
    }

    @IdRes
    public Set<Integer> getCheckedIds() {
        return mCheckedIds;
    }

    /**
     * 此监听器在{@link PassThroughHierarchyChangeListener#onChildViewAdded}中设置了监听
     * 当RadioButton被点击时调用{@link CheckedStateTracker}方法
     */
    private class CheckedStateTracker implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            RadioButton button = (RadioButton) view;
            int id = button.getId();
            int defaultCheckedId = getChildAt(0).getId();
            if(isMultiSelect()) {
                if(id == defaultCheckedId) {
                    if(mCheckedIds.size() == 1 && mCheckedIds.contains(defaultCheckedId)) {
                        return;
                    } else {
                        resetCheck();
                    }
                } else {
                    if(mCheckedIds.contains(id)) {
                        setCheckedStateForView(id, false);
                        mCheckedIds.remove(id);

                        //设置默认选中
                        if(mCheckedIds.isEmpty()) {
                            setCheckedStateForView(defaultCheckedId, true);
                            mCheckedIds.add(defaultCheckedId);
                        }
                    } else {
                        setCheckedStateForView(defaultCheckedId, false);
                        mCheckedIds.remove(defaultCheckedId);
                        setCheckedStateForView(id, true);
                        mCheckedIds.add(id);

                        if(mCheckedIds.size() == getChildCount() - 1 && !mCheckedIds.contains(defaultCheckedId)) {
                            resetCheck();
                        }
                    }
                }
            } else {
                if(mCheckedId == id) {
                    if(getChildCount() == 1) {
                        if(mCheckedId != -1) {
                            setCheckedStateForView(id, false);
                            mCheckedId = -1;
                        } else {
                            setCheckedStateForView(id, true);
                            mCheckedId = id;
                        }
                    } else {
                        setCheckedStateForView(id, false);

                        //设置默认选中
                        setCheckedStateForView(defaultCheckedId, true);
                        mCheckedId = defaultCheckedId;
                    }
                    return;
                }

                if(mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                setCheckedStateForView(id, true);
                mCheckedId = id;
            }
        }
    }

    /**
     * 当布局添加或者删除View时的监听器
     */
    private class PassThroughHierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * 子View添加时，调用此方法
         */
        public void onChildViewAdded(View parent, View child) {
            if (parent == FlexRadioGroup2.this && child instanceof RadioButton) { //如果子View没有Id，则生产一个id给view
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = ViewIdGenerator.generateViewId();
                    child.setId(id);
                }
                //
                /**
                 *原RadioGroup中调用的是{@link RadioButton#setOnCheckedChangeWidgetListener}
                 * 但由于被隐藏，无法调用，反射也无法找到，故此使用{@link  RadioButton#setOnCheckedChangeListener }
                 * 造成的后果就是子view中不能再去使用{@link  RadioButton#setOnCheckedChangeListener }监听器
                 */

                child.setOnClickListener(mOnClickListener);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void onChildViewRemoved(View parent, View child) {
            if (parent == FlexRadioGroup2.this && child instanceof RadioButton) {
                ((RadioButton) child).setOnCheckedChangeListener(null);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
}
