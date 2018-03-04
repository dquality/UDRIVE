package ua.com.dquality.udrive.fragments;

import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.dquality.udrive.R;

import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ua.com.dquality.udrive.helpers.CircleStatusDrawable;
import ua.com.dquality.udrive.helpers.SlidingUpPanelLayout;
import ua.com.dquality.udrive.helpers.SlidingUpPanelLayout.PanelSlideListener;
import ua.com.dquality.udrive.helpers.SlidingUpPanelLayout.PanelState;
import ua.com.dquality.udrive.viewmodels.StatusLevel;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;


public class HomeFragment extends Fragment {
    private static final String TAG = "DemoActivity";

    private SlidingUpPanelLayout mLayout;
    private TextView mCardCodeNumber;
    private TextView mCardType;
    private TextView mCardMonthText;
    private TextView mCircleState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View ret = inflater.inflate(R.layout.fragment_home, container, false);

        initSlidePanel(ret);

        initCardHolderState(ret);

        initCircleState(ret);
        // Inflate the layout for this fragment
        return ret;
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

        TextView t = parentView.findViewById(R.id.name);
        t.setText("Slide up");

    }

    private void initCardHolderState(View parentView){
        RelativeLayout cardHolderState =  parentView.findViewById(R.id.card_holder_state);
        mCardCodeNumber = parentView.findViewById(R.id.card_barcode_number_text);
        mCardType = parentView.findViewById(R.id.card_type_text);
        mCardMonthText = parentView.findViewById(R.id.card_month_text);

        if(cardHolderState != null) {
            cardHolderState.setBackgroundResource(R.drawable.selector_card_classic_background);
        }
        if(mCardType != null){
            mCardType.setText("Classic");
        }
        if(mCardCodeNumber != null){
            mCardCodeNumber.setText("3356 4673 7990 5332");
        }
        if(mCardMonthText != null){
            String month = new SimpleDateFormat("LLLL", new Locale("ru")).format(new Date());
            mCardMonthText.setText(month);
        }
    }

    private void initCircleState(View parentView){
        mCircleState =  parentView.findViewById(R.id.circle_state);
        if(mCircleState != null){

            setCircleValue(337);

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

    private void setCircleDrawable(){
        if(mCircleState != null){
            mCircleState.setBackgroundDrawable(new CircleStatusDrawable(getContext(), StatusLevel.Classic, 80));
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

