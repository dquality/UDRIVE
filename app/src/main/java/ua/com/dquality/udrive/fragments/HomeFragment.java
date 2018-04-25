package ua.com.dquality.udrive.fragments;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.dquality.udrive.AuthenticateBaseActivity;
import ua.com.dquality.udrive.MainActivity;
import ua.com.dquality.udrive.R;

import android.util.Log;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.helpers.CircleStatusDrawable;
import ua.com.dquality.udrive.constants.Const;
import ua.com.dquality.udrive.helpers.OnSwipeTouchListener;
import ua.com.dquality.udrive.interfaces.OnRefreshHideListener;
import ua.com.dquality.udrive.sliding.SlidingUpPanelLayout;
import ua.com.dquality.udrive.sliding.SlidingUpPanelLayout.PanelSlideListener;
import ua.com.dquality.udrive.sliding.SlidingUpPanelLayout.PanelState;
import ua.com.dquality.udrive.viewmodels.HomeViewModel;
import ua.com.dquality.udrive.viewmodels.models.HomeModel;
import ua.com.dquality.udrive.viewmodels.models.StatusLevel;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static ua.com.dquality.udrive.viewmodels.models.StatusLevel.Platinum;


public class HomeFragment extends Fragment implements OnRefreshHideListener {
    private HomeViewModel mViewModelData;

    private View mParentView;

    private SlidingUpPanelLayout mLayout;
    private  RelativeLayout cardHolderState;

    private ImageView mCardBarcodeImage;
    private TextView mCardCodeNumber;
    private TextView mCardType;
    private TextView mCardMonthText;

    private TextView mCircleState;

    private TextView mPrevMonthTripsTitle;
    private TextView mPrevMonthTripsCount;
    private TextView mWeekTripsCount;
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

        mParentView = inflater.inflate(R.layout.fragment_home, container, false);

        mViewModelData = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);

        mLayout = mParentView.findViewById(R.id.sliding_layout);

        mLayout.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("DemoActivity", "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                Log.i("DemoActivity", "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(PanelState.COLLAPSED);
            }
        });


        cardHolderState =  mParentView.findViewById(R.id.card_holder_state);
        cardHolderState.setOnClickListener(onCardHolderClickListener);

        mCardBarcodeImage = mParentView.findViewById(R.id.card_barcode_image);
        mCardCodeNumber = mParentView.findViewById(R.id.card_barcode_number_text);
        mCardType = mParentView.findViewById(R.id.card_type_text);
        mCardMonthText = mParentView.findViewById(R.id.card_month_text);

        mCircleState =  mParentView.findViewById(R.id.circle_state);

        mPrevMonthTripsTitle = mParentView.findViewById(R.id.prev_month_trips_title);
        mPrevMonthTripsCount = mParentView.findViewById(R.id.prev_month_trips_count);

        mWeekTripsCount = mParentView.findViewById(R.id.week_trips_count);

        mRemainsTripsTitle = mParentView.findViewById(R.id.remains_trips_title);
        mRemainsTripsCount = mParentView.findViewById(R.id.remains_trips_count);

        mFinalStatusTitle = mParentView.findViewById(R.id.final_status_title);
        mFinalStatusName = mParentView.findViewById(R.id.final_status_name);

        mBalanceAmount = mParentView.findViewById(R.id.balance_amount);

        AppCompatButton accrualButton =  mParentView.findViewById(R.id.accrual_button);
        accrualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthenticateBaseActivity)getActivity()).showAccountReplenishmentDialog();
            }
        });

        mRefreshMainSwipe = mParentView.findViewById(R.id.refresh_main_swipe);

        mRefreshMainSwipe.setOnRefreshListener(
                () -> UDriveApplication.getHttpDataProvider().refreshAllData(getActivity(), HomeFragment.this)
        );

        mViewModelData.getHomeData().observe(this, new Observer<HomeModel>() {
            @Override
            public void onChanged(@Nullable HomeModel homeModel) {
                updateHomeData();
            }
        });

        return mParentView;
    }

    private void updateHomeData(){

        updateCardHolderState();

        updateCircleState();

        updateTripTitleAndCounterStats();

        updateBalance();
    }

    private HomeModel getDataModel(){
        return mViewModelData.getHomeData().getValue();
    }

    private StatusLevel getCurrentLevel(){
        StatusLevel lvl = getDataModel().Level;
        return lvl == null ? StatusLevel.Undefined : lvl;
    }

    private StatusLevel getNextMonthLevel(){
        StatusLevel lvl = getDataModel().NextMonthLevel;
        return lvl == null ? StatusLevel.Classic : lvl;
    }

    public void updateCardHolderState(){
        StatusLevel lvl  = getCurrentLevel();

        if(cardHolderState != null) {
            switch (lvl) {
                case Classic:
                    cardHolderState.setBackgroundResource(R.drawable.selector_card_classic_background);
                    break;
                case Gold:
                    cardHolderState.setBackgroundResource(R.drawable.selector_card_gold_background);
                    break;
                case Platinum:
                    cardHolderState.setBackgroundResource(R.drawable.selector_card_platinum_background);
                    break;
                default:
                    cardHolderState.setBackgroundResource(R.drawable.selector_card_undefined_background);
                    break;
            }
        }
        if(mCardType != null){
            mCardType.setText(getStatusLevel(lvl));
        }
        if(mCardCodeNumber != null){
            String barCode = getDataModel().getBarcode();
            if(barCode != null && !barCode.isEmpty()){
                String formattedBarcode = "";
                char[] charArray = barCode.toCharArray();
                for (int i=0; i< charArray.length; i++) {
                    if(i == 7){
                        formattedBarcode+= "    ";
                    }
                    if(i == 1){
                        formattedBarcode+= " ";
                    }
                    formattedBarcode+= String.valueOf(charArray[i]);
                }
                mCardCodeNumber.setText(formattedBarcode);
            }
        }
        if(mCardMonthText != null){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            String month = new SimpleDateFormat(Const.LONG_MONTH_FROMAT, new Locale(Const.CULTURE)).format(cal.getTime());
            mCardMonthText.setText(month);
        }

        int cardTextColor = ContextCompat.getColor(getContext(), lvl == Platinum ? R.color.colorTextTitlePlatinum : R.color.colorPrimaryLight);
        mCardCodeNumber.setTextColor(cardTextColor);
        mCardType.setTextColor(cardTextColor);
        mCardMonthText.setTextColor(cardTextColor);
        mCardBarcodeImage.setImageResource(lvl == Platinum ? R.drawable.ic_bar_code_plat_114dp :R.drawable.ic_bar_code_114dp);

    }

    private View.OnClickListener onCardHolderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Dialog dialog = new Dialog(getActivity(), R.style.CardHolderPopupStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            View dialogView = dialog.getLayoutInflater().inflate(R.layout.bar_code_popup_view, null);

            Bitmap bmp = getDataModel().getBarcodeBitmap();
            if(bmp != null){
                ((ImageView) dialogView.findViewById(R.id.img_bar_code_qr)).setImageBitmap(bmp);
            }
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

    public void updateCircleState(){

        if(mCircleState != null){

            setCircleValue(getDataModel().UcoinsCount);

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

    public void updateTripTitleAndCounterStats(){

        setPrevMonthTripTitle();

        setRemainsBlock();

        setTripsCount();
    }

    private void setPrevMonthTripTitle(){
        if(mPrevMonthTripsTitle != null){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            String prevMonth = new SimpleDateFormat(Const.LONG_MONTH_FROMAT, new Locale(Const.CULTURE)).format(cal.getTime());
            mPrevMonthTripsTitle.setText(String.format(getString(R.string.prev_month_trip_title_tmpl), prevMonth));
        }
    }

    private void setRemainsBlock(){
        StatusLevel nextMonthLevel = getNextMonthLevel();
        if(nextMonthLevel != null && mRemainsTripsTitle != null && mRemainsTripsCount != null && mFinalStatusTitle != null && mFinalStatusName != null){

            if(nextMonthLevel == Platinum){
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
                Context ctx  = getContext();
                int lvlColor = 0;
                StatusLevel nextLevel = nextMonthLevel.next();
                switch (nextLevel){
                    case Classic:
                        lvlColor=ContextCompat.getColor(ctx, R.color.colorTextTitleClassic);
                        break;
                    case Gold:
                        lvlColor=ContextCompat.getColor(ctx, R.color.colorTextTitleGold);
                        break;
                    case Platinum:
                        lvlColor=ContextCompat.getColor(ctx, R.color.colorTextTitlePlatinum);
                        break;
                }
                String lvlStr = getStatusLevel(nextLevel);
                SpannableString lvlSpan = new SpannableString(lvlStr.toUpperCase());
                lvlSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, lvlStr.length(), SPAN_INCLUSIVE_INCLUSIVE);
                lvlSpan.setSpan(new ForegroundColorSpan(lvlColor), 0, lvlStr.length(), SPAN_INCLUSIVE_INCLUSIVE);

                mRemainsTripsTitle.setText(TextUtils.concat(remainTripStr, " ", lvlSpan));
            }
        }
    }

    private void setTripsCount(){
        if(mPrevMonthTripsCount != null) mPrevMonthTripsCount.setText(String.valueOf(getDataModel().PrevMonthTripsCount));
        if(mWeekTripsCount != null) mWeekTripsCount.setText(String.valueOf(getDataModel().WeekTripsCount));
        if(mRemainsTripsCount != null) mRemainsTripsCount.setText(String.valueOf(getDataModel().RemainsTripsCount));
    }

    private void setCircleDrawable(){
        if(mCircleState != null){
            mCircleState.setBackgroundDrawable(new CircleStatusDrawable(getContext(), getCurrentLevel(), getDataModel().NextLevelPercentage));
        }
    }

    private String getStatusLevel(StatusLevel lvl) {
        switch (lvl) {
            case Classic:
                return getString(R.string.title_status_classic);
            case Gold:
                return getString(R.string.title_status_gold);
            case Platinum:
                return getString(R.string.title_status_platinum);
            default:
                    return getString(R.string.title_status_undefined);
        }
    }

    public void updateBalance(){

        setBalanceAmount();
    }

    private void setBalanceAmount(){
        if(mBalanceAmount != null){
            Context ctx  = getContext();
            double balanceAmount = getDataModel().BalanceAmount;
            mBalanceAmount.setTextColor(ContextCompat.getColor(ctx, balanceAmount < 0 ? R.color.colorError : R.color.colorAccent));
            mBalanceAmount.setText(String.format(Const.AMOUNT_FORMAT, balanceAmount));
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

    public void onRefreshShow(){
        mRefreshMainSwipe.setRefreshing(true);
        UDriveApplication.getHttpDataProvider().refreshAllData(getActivity(), HomeFragment.this);
    }

    @Override
    public void onRefreshHide() {
        mRefreshMainSwipe.postDelayed(new Runnable() {
            public void run() {
                mRefreshMainSwipe.setRefreshing(false);
            }
        }, 0);
    }
}

