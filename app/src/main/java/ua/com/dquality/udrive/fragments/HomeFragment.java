package ua.com.dquality.udrive.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import ua.com.dquality.udrive.R;

import android.util.Log;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import ua.com.dquality.udrive.viewmodels.models.HomeModel;
import ua.com.dquality.udrive.viewmodels.models.StatusLevel;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static ua.com.dquality.udrive.viewmodels.models.StatusLevel.Platinum;


public class HomeFragment extends HomeBaseFragment implements OnRefreshHideListener {

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mParentView = inflater.inflate(R.layout.fragment_home, container, false);

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
        mLayout.setFadeOnClickListener(view -> mLayout.setPanelState(PanelState.COLLAPSED));


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
        accrualButton.setOnClickListener(v -> ((AuthenticateBaseActivity)getActivity()).showAccountReplenishmentDialog());

        mRefreshMainSwipe = mParentView.findViewById(R.id.refresh_main_swipe);

        mRefreshMainSwipe.setOnRefreshListener(
                () -> UDriveApplication.getHttpDataProvider().refreshAllData(getActivity(), HomeFragment.this)
        );

        onCreateViewBase();

        return mParentView;
    }

    @Override
    protected void onChangeHomeData(HomeModel hModel) {
        super.onChangeHomeData(hModel);

        updateCardHolderState();

        updateCircleState();

        updateTripTitleAndCounterStats();

        updateBalance();
    }

    private StatusLevel getNextMonthLevel(){
        StatusLevel lvl = getDataModel().NextMonthLevel;
        return lvl == null ? StatusLevel.Classic : lvl;
    }

    private int getStatusLevelDrawable(StatusLevel lvl) {
        switch (lvl) {
            case Classic:
                return R.drawable.selector_card_classic_background;
            case Gold:
                return R.drawable.selector_card_gold_background;
            case Platinum:
                return R.drawable.selector_card_platinum_background;
            default:
                return R.drawable.selector_card_undefined_background;
        }
    }

    private void updateCardHolderState(){
        StatusLevel lvl  = mViewModelData.getCurrentLevel();

        cardHolderState.setBackgroundResource(getStatusLevelDrawable(lvl));

        if(mCardType != null){
            mCardType.setText(getStatusLevel(lvl));
        }
        if(mCardCodeNumber != null){
            String barCode = getDataModel().getBarcode();
            if(barCode != null && !barCode.isEmpty()){
                StringBuilder formattedBarcode = new StringBuilder();
                char[] charArray = barCode.toCharArray();
                for (int i=0; i< charArray.length; i++) {
                    if(i == 7){
                        formattedBarcode.append("    ");
                    }
                    if(i == 1){
                        formattedBarcode.append(" ");
                    }
                    formattedBarcode.append(String.valueOf(charArray[i]));
                }
                mCardCodeNumber.setText(formattedBarcode.toString());
            }
        }
        if(mCardMonthText != null){
            mCardMonthText.setText(getPrevMonth());
        }

        int cardTextColor = ContextCompat.getColor(getContext(), lvl == Platinum ? R.color.colorTextTitlePlatinum : R.color.colorPrimaryLight);
        mCardCodeNumber.setTextColor(cardTextColor);
        mCardType.setTextColor(cardTextColor);
        mCardMonthText.setTextColor(cardTextColor);
        mCardBarcodeImage.setImageResource(lvl == Platinum ? R.drawable.ic_bar_code_plat_114dp :R.drawable.ic_bar_code_114dp);

    }

    private final View.OnClickListener onCardHolderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Dialog dialog = new Dialog(getActivity(), R.style.CardHolderPopupStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            View dialogView = dialog.getLayoutInflater().inflate(R.layout.bar_code_popup_view, null);

            Bitmap bmp = getDataModel().getBarcodeBitmap(true);
            ((ImageView) dialogView.findViewById(R.id.img_bar_code_qr)).setImageBitmap(bmp);
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

    private void updateCircleState(){

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


            //String ucTitle = getString(R.string.title_circle_ucoins);
            String ucTitle = getCurrentMonth();

            SpannableString titleSpan = new SpannableString(ucTitle);
            titleSpan.setSpan(new AbsoluteSizeSpan(titleTextAppStyle.getDimensionPixelSize(0, 0)), 0, ucTitle.length(), SPAN_INCLUSIVE_INCLUSIVE);

            mCircleState.setText(TextUtils.concat(spanVal, "\n", titleSpan));
            headLineTextAppStyle.recycle();
            titleTextAppStyle.recycle();
        }
    }

    private void updateTripTitleAndCounterStats(){

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

                StatusLevel nextLevel = nextMonthLevel.next();

                int lvlColor = getStatusLevelColor(nextLevel);
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
            mCircleState.setBackgroundDrawable(new CircleStatusDrawable(getContext(), mViewModelData.getCurrentLevel(), getDataModel().NextLevelPercentage));
        }
    }

    private void updateBalance(){

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

    private String getCurrentMonth(){
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat(Const.LONG_MONTH_FROMAT, new Locale(Const.CULTURE)).format(cal.getTime());
    }

    private String getPrevMonth(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        return new SimpleDateFormat(Const.LONG_MONTH_FROMAT, new Locale(Const.CULTURE)).format(cal.getTime());
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
        mRefreshMainSwipe.postDelayed(() -> mRefreshMainSwipe.setRefreshing(false), 0);
    }
}

