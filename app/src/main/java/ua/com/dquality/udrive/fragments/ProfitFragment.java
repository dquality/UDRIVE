package ua.com.dquality.udrive.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.adapters.ProfitPageAdapter;

public class ProfitFragment extends Fragment {

    private ViewPager mProfitViewPager;
    private AppCompatButton mProfitHistoryButton;
    private AppCompatButton mProfitStatementButton;

    public ProfitFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_profit, container, false);

        ProfitPageAdapter mProfitPagerAdapter = new ProfitPageAdapter(getFragmentManager());

        mProfitViewPager = ret.findViewById(R.id.profit_view_pager);
        mProfitViewPager.setAdapter(mProfitPagerAdapter);
        mProfitViewPager.addOnPageChangeListener(onPageProfitChangeListener);

        mProfitHistoryButton = ret.findViewById(R.id.profit_history_button);
        mProfitStatementButton = ret.findViewById(R.id.profit_statement_button);

        mProfitHistoryButton.setOnClickListener(onProfitFilterClickListener);
        mProfitStatementButton.setOnClickListener(onProfitFilterClickListener);

        mProfitHistoryButton.performClick();
        return ret;
    }

    private final ViewPager.OnPageChangeListener onPageProfitChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

        }

        @Override
        public void onPageSelected(int position){
            selectProfitButton(position);
        }

        @Override
        public void onPageScrollStateChanged(int state){

        }

    };

    private final View.OnClickListener onProfitFilterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            if (!v.isSelected()) {
                int position = v.getId() == mProfitHistoryButton.getId()? 0:1;
                selectProfitButton(position);
                mProfitViewPager.setCurrentItem(position);
            }
        }
    };

    private void selectProfitButton(int position){
        boolean isLeftPosition = position == 0;
        mProfitHistoryButton.setSelected(isLeftPosition);

        mProfitStatementButton.setSelected(!isLeftPosition);
    }
}