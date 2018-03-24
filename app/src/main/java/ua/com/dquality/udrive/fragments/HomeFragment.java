package ua.com.dquality.udrive.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.gesture.Gesture;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import ua.com.dquality.udrive.MainActivity;
import ua.com.dquality.udrive.R;

import android.util.Log;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import ua.com.dquality.udrive.helpers.CircleStatusDrawable;
import ua.com.dquality.udrive.helpers.Const;
import ua.com.dquality.udrive.helpers.OnSwipeTouchListener;
import ua.com.dquality.udrive.helpers.SlidingUpPanelLayout;
import ua.com.dquality.udrive.helpers.SlidingUpPanelLayout.PanelSlideListener;
import ua.com.dquality.udrive.helpers.SlidingUpPanelLayout.PanelState;
import ua.com.dquality.udrive.viewmodels.StatusLevel;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;


public class HomeFragment extends Fragment {
    private static final String TAG = "DemoActivity";

    private StatusLevel mLevel;
    private int mNextLevelPercentage;
    private int mUcoinsVal;
    private String mBarcodeVal;
    private int mPrevMonthTripsCountVal;
    private int mTodayTripsCountVal;
    private int mRemainsTripsCountVal;
    private double mBalanceVal;

    private SlidingUpPanelLayout mLayout;
    private ImageView mCardBarcodeImage;
    private TextView mCardCodeNumber;
    private TextView mCardType;
    private TextView mCardMonthText;

    private TextView mCircleState;

    private TextView mPrevMonthTripsTitle;
    private TextView mPrevMonthTripsCount;
    private TextView mTodayTripsCount;
    private TextView mRemainsTripsTitle;
    private TextView mRemainsTripsCount;
    private TextView mFinalStatusTitle;
    private TextView mFinalStatusName;

    private TextView mBalanceAmount;

    private SwipeRefreshLayout mRefreshMainSwipe;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View ret = inflater.inflate(R.layout.fragment_home, container, false);

        initModelParams();

        initSlidePanel(ret);

        initCardHolderState(ret);

        initCircleState(ret);

        initTripTitleAndCounterStats(ret);

        initBalance(ret);

        initSwipeToRefreshAction(ret);

        return ret;
    }

    private void initModelParams(){
        mLevel = StatusLevel.Classic;
        mNextLevelPercentage = 20;
        mUcoinsVal = 444;
        mBarcodeVal = "3356 4673 7990 5332";

        mPrevMonthTripsCountVal = 1450;
        mTodayTripsCountVal = 1;
        mRemainsTripsCountVal = 80;
        mBalanceVal = 16245.4;
    }

    private void initSlidePanel(View parentView){
        /*        ListView lv =  ret.findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Set panel state anchored", Toast.LENGTH_SHORT).show();
                mLayout.setAnchorPoint(0.7f);
                mLayout.setPanelState(PanelState.ANCHORED);
            }
        });

        List<String> your_array_list = Arrays.asList(
                "This",
                "Is",
                "An",
                "Example",
                "ListView",
                "That",
                "You",
                "Can",
                "Scroll",
                ".",
                "It",
                "Shows",
                "How",
                "Any",
                "Scrollable",
                "View",
                "Can",
                "Be",
                "Included",
                "As",
                "A",
                "Child",
                "Of",
                "SlidingUpPanelLayout"
        );

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                your_array_list );

        lv.setAdapter(arrayAdapter);*/

        mLayout = parentView.findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(PanelState.COLLAPSED);
            }
        });

        //ImageView t = parentView.findViewById(R.id.name);


    }

    private void initCardHolderState(View parentView){
        RelativeLayout cardHolderState =  parentView.findViewById(R.id.card_holder_state);
        cardHolderState.setOnClickListener(onCardHolderClickListener);

        mCardBarcodeImage= parentView.findViewById(R.id.card_barcode_image);
        mCardCodeNumber = parentView.findViewById(R.id.card_barcode_number_text);
        mCardType = parentView.findViewById(R.id.card_type_text);
        mCardMonthText = parentView.findViewById(R.id.card_month_text);

        if(cardHolderState != null) {
            switch (mLevel){
                case Classic:
                    cardHolderState.setBackgroundResource(R.drawable.selector_card_classic_background);
                    break;
                case Gold:
                    cardHolderState.setBackgroundResource(R.drawable.selector_card_gold_background);
                    break;
                case Platinum:
                    cardHolderState.setBackgroundResource(R.drawable.selector_card_platinum_background);
                    break;
            }
        }
        if(mCardType != null){
            mCardType.setText(getStatusLevel(mLevel));
        }
        if(mCardCodeNumber != null){
            mCardCodeNumber.setText(mBarcodeVal);
        }
        if(mCardMonthText != null){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            String month = new SimpleDateFormat("LLLL", new Locale(Const.CULTURE)).format(cal.getTime());
            mCardMonthText.setText(month);
        }

        int cardTextColor = ContextCompat.getColor(getContext(), mLevel == StatusLevel.Platinum ? R.color.colorTextTitlePlatinum : R.color.colorPrimaryLight);
        mCardCodeNumber.setTextColor(cardTextColor);
        mCardType.setTextColor(cardTextColor);
        mCardMonthText.setTextColor(cardTextColor);
        mCardBarcodeImage.setImageResource(mLevel == StatusLevel.Platinum ? R.drawable.ic_bar_code_plat_114dp :R.drawable.ic_bar_code_114dp);

    }

    private View.OnClickListener onCardHolderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Dialog dialog = new Dialog(getActivity(), R.style.CardHolderPopupStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            View dialogView = dialog.getLayoutInflater().inflate(R.layout.bar_code_popup_view, null);

            dialogView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
                @Override
                public void onSwipeUp() {
                    super.onSwipeUp();
                    dialog.cancel();
                }
            });

            dialog.setContentView(dialogView);
            dialog.setCancelable(true);
            dialog.show();
        }
    };

    private void initCircleState(View parentView){
        mCircleState =  parentView.findViewById(R.id.circle_state);
        if(mCircleState != null){

            setCircleValue(mUcoinsVal);

            setCircleDrawable();
        }
    }

    private void setCircleValue(int ucoins){
        if(mCircleState != null) {

            int[] textSizeAttr = {android.R.attr.textSize};
            TypedArray headLineTextAppStyle = getContext().obtainStyledAttributes(R.style.TextAppearance_AppCompat_Headline, textSizeAttr);
            TypedArray titleTextAppStyle = getContext().obtainStyledAttributes(R.style.TextAppearance_AppCompat_Medium, textSizeAttr);

            String ucVals = String.valueOf(ucoins);
            SpannableString spanVal = new SpannableString(ucVals);
            spanVal.setSpan(new AbsoluteSizeSpan(headLineTextAppStyle.getDimensionPixelSize(0, 0)), 0, ucVals.length(), SPAN_INCLUSIVE_INCLUSIVE);
            spanVal.setSpan(new StyleSpan(Typeface.BOLD), 0, ucVals.length(), SPAN_INCLUSIVE_INCLUSIVE);


            String ucTitle = getString(R.string.title_circle_ucoins);
            SpannableString titleSpan = new SpannableString(ucTitle);
            titleSpan.setSpan(new AbsoluteSizeSpan(titleTextAppStyle.getDimensionPixelSize(0, 0)), 0, ucTitle.length(), SPAN_INCLUSIVE_INCLUSIVE);

            mCircleState.setText(TextUtils.concat(spanVal, "\n", titleSpan));
        }
    }

    private void initTripTitleAndCounterStats(View parentView){
        mPrevMonthTripsTitle = parentView.findViewById(R.id.prev_month_trips_title);
        mPrevMonthTripsCount = parentView.findViewById(R.id.prev_month_trips_count);

        mTodayTripsCount = parentView.findViewById(R.id.today_trips_count);

        mRemainsTripsTitle = parentView.findViewById(R.id.remains_trips_title);
        mRemainsTripsCount = parentView.findViewById(R.id.remains_trips_count);

        mFinalStatusTitle = parentView.findViewById(R.id.final_status_title);
        mFinalStatusName = parentView.findViewById(R.id.final_status_name);

        setPrevMonthTripTitle();

        setRemainsBlock(mLevel);

        setTripsCount();
    }

    private void setRemainsBlock(StatusLevel current){
        if(mRemainsTripsTitle != null && mRemainsTripsCount != null && mFinalStatusTitle != null && mFinalStatusName != null){

            if(mLevel == StatusLevel.Platinum){
                mFinalStatusTitle.setVisibility(View.VISIBLE);
                mFinalStatusName.setVisibility(View.VISIBLE);

                mRemainsTripsTitle.setVisibility(View.GONE);
                mRemainsTripsCount.setVisibility(View.GONE);
            }
            else{
                mFinalStatusTitle.setVisibility(View.GONE);
                mFinalStatusName.setVisibility(View.GONE);

                mRemainsTripsTitle.setVisibility(View.VISIBLE);
                mRemainsTripsCount.setVisibility(View.VISIBLE);

                String remainTripStr = getString(R.string.remain_trips_title ).toUpperCase();
                StatusLevel nextLevel = mLevel.next();
                Context ctx  = getContext();
                int lvlColor = nextLevel == StatusLevel.Gold ? ContextCompat.getColor(ctx, R.color.colorTextTitleGold) : ContextCompat.getColor(ctx, R.color.colorTextTitlePlatinum);
                String lvlStr = getStatusLevel(nextLevel);
                SpannableString lvlSpan = new SpannableString(lvlStr.toUpperCase());
                lvlSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, lvlStr.length(), SPAN_INCLUSIVE_INCLUSIVE);
                lvlSpan.setSpan(new ForegroundColorSpan(lvlColor), 0, lvlStr.length(), SPAN_INCLUSIVE_INCLUSIVE);

                mRemainsTripsTitle.setText(TextUtils.concat(remainTripStr, " ", lvlSpan));
            }
        }
    }

    private void setPrevMonthTripTitle(){
        if(mPrevMonthTripsTitle != null){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            String prevMonth = new SimpleDateFormat("LLLL", new Locale(Const.CULTURE)).format(cal.getTime());
            mPrevMonthTripsTitle.setText(String.format(getString(R.string.prev_month_trip_title_tmpl), prevMonth));
        }
    }

    private void setTripsCount(){
        if(mPrevMonthTripsCount != null) mPrevMonthTripsCount.setText(String.valueOf(mPrevMonthTripsCountVal));
        if(mTodayTripsCount != null) mTodayTripsCount.setText(String.valueOf(mTodayTripsCountVal));
        if(mRemainsTripsCount != null) mRemainsTripsCount.setText(String.valueOf(mRemainsTripsCountVal));
    }

    private void setCircleDrawable(){
        if(mCircleState != null){
            mCircleState.setBackgroundDrawable(new CircleStatusDrawable(getContext(), mLevel, mNextLevelPercentage));
        }
    }

    private String getStatusLevel(StatusLevel lvl){
        switch (lvl){
            case Classic:
                return getString(R.string.title_status_classic);
            case Gold:
                return getString(R.string.title_status_gold);
            case Platinum:
                return getString(R.string.title_status_platinum);
        }
        return null;
    }

    private void initBalance(View parentView){
        mBalanceAmount = parentView.findViewById(R.id.balance_amount);

        AppCompatButton accrualButton =  parentView.findViewById(R.id.accrual_button);
        accrualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.accrual_button_title,Toast.LENGTH_SHORT).show();
            }
        });

        setBalanceAmount();
    }

    private void initSwipeToRefreshAction(View parentView) {

        mRefreshMainSwipe = parentView.findViewById(R.id.refresh_main_swipe);

        mRefreshMainSwipe.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    new Thread(new Runnable() {
                        public void run() {
                            mRefreshMainSwipe.postDelayed(new Runnable() {
                                public void run() {
                                    mRefreshMainSwipe.setRefreshing(false);
                                }
                            }, 5000);
                        }
                    }).start();
                }
            }
        );
    }

    private void setBalanceAmount(){
        if(mBalanceAmount != null){
            Context ctx  = getContext();
            mBalanceAmount.setTextColor(ContextCompat.getColor(ctx, mBalanceVal < 0 ? R.color.colorError : R.color.colorAccent));
            mBalanceAmount.setText(String.format("%1$,.2f",mBalanceVal));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.COLLAPSED);
        }
    }
}

