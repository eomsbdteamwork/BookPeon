package tech.codegarage.dropdownmenuplus.typeview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import tech.codegarage.dropdownmenuplus.adapter.DoubleGridAdapter;
import tech.codegarage.dropdownmenuplus.interfaces.OnFilterDoneListener;

import java.util.List;

import tech.codegarage.dropdownmenuplus.R;
import tech.codegarage.dropdownmenuplus.util.FilterUtils;

/**
 * 两个gridView的筛选
 */
public class DoubleGridView extends LinearLayout implements View.OnClickListener {

    View parentView;
//    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Button btnConfirm;

    private List<String> mTopGridData;//第一个数据
    private List<String> mBottomGridList;//第二个数据
    private OnFilterDoneListener mOnFilterDoneListener;


    //构造
    public DoubleGridView(Context context) {
        this(context, null);
    }

    public DoubleGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DoubleGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DoubleGridView(Context context, AttributeSet attrs, int defStyleAttr,
                          int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {
        setBackgroundColor(Color.WHITE);
        //布局
        parentView = inflate(context, R.layout.filter_double_grid, this);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recyclerView);
        btnConfirm = (Button) parentView.findViewById(R.id.bt_confirm);

        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterUtils.instance().doubleGridTop = mTopSelectedTextView == null ? "" : (String) mTopSelectedTextView.getTag();
                FilterUtils.instance().doubleGridBottom = mBottomSelectedTextView == null ? "" : (String) mBottomSelectedTextView.getTag();

                if (mOnFilterDoneListener != null) {
                    mOnFilterDoneListener.onFilterDone(1, "标题", "");
                }
            }
        });
//        ButterKnife.bind(this, this);
    }


    public DoubleGridView setmTopGridData(List<String> mTopGridData) {
        this.mTopGridData = mTopGridData;
        return this;
    }

    public DoubleGridView setmBottomGridList(List<String> mBottomGridList) {
        this.mBottomGridList = mBottomGridList;
        return this;
    }

    //最终创建视图
    public DoubleGridView build() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 4);
        //设置四列
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                if (position == 0 || position == mTopGridData.size() + 1) {
                    return 4;
                }
                return 1;
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new DoubleGridAdapter(getContext(), mTopGridData, mBottomGridList, this));

        return this;
    }

    private TextView mTopSelectedTextView;
    private TextView mBottomSelectedTextView;

    @Override
    public void onClick(View v) {

        TextView textView = (TextView) v;
        String text = (String) textView.getTag();

        if (textView == mTopSelectedTextView) {
            mTopSelectedTextView = null;
            textView.setSelected(false);
        } else if (textView == mBottomSelectedTextView) {
            mBottomSelectedTextView = null;
            textView.setSelected(false);
        } else if (mTopGridData.contains(text)) {
            if (mTopSelectedTextView != null) {
                mTopSelectedTextView.setSelected(false);
            }
            mTopSelectedTextView = textView;
            textView.setSelected(true);
        } else {
            if (mBottomSelectedTextView != null) {
                mBottomSelectedTextView.setSelected(false);
            }
            mBottomSelectedTextView = textView;
            textView.setSelected(true);
        }
    }

    //设置监听
    public DoubleGridView setOnFilterDoneListener(OnFilterDoneListener listener) {
        mOnFilterDoneListener = listener;
        return this;
    }
}
