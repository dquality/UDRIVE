package ua.com.dquality.udrive;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import ua.com.dquality.udrive.constants.Const;
import ua.com.dquality.udrive.viewmodels.AccountReplenishmentViewModel;
import ua.com.dquality.udrive.viewmodels.models.AccountReplenishmentModel;

public class AccountReplenishmentActivity extends AuthenticateBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!mIsLoggedIn){
            return;
        }

        setContentView(R.layout.activity_account_replenishment);

        AccountReplenishmentViewModel mViewModelData = ViewModelProviders.of(this).get(AccountReplenishmentViewModel.class);

        mViewModelData.getLiveDataModel().observe(this, accountReplenishmentModel -> {
            updateAccountReplenishmentData(accountReplenishmentModel);
        });

        AppCompatButton btn = findViewById(R.id.btn_perform_account_replenishment);
        btn.setOnClickListener(onPerformAccountReplenishmentClickListener);

        SetLogo(this);
    }

    private void updateAccountReplenishmentData(AccountReplenishmentModel model){
        TextView amountValueText = findViewById(R.id.amount_value);
        amountValueText.setText(String.format(Const.AMOUNT_FORMAT, model.Amount));

        TextView commissionAmountValueText = findViewById(R.id.commission_amount_value);
        commissionAmountValueText.setText(String.format(Const.AMOUNT_FORMAT, model.CommissionAmount));

        TextView sumValueText = findViewById(R.id.sum_value);
        sumValueText.setText(String.format(Const.AMOUNT_FORMAT, model.TotalAmount));
    }

    private final View.OnClickListener onPerformAccountReplenishmentClickListener = v -> {
        Intent intent = new Intent(AccountReplenishmentActivity.this, AccountReplenishmentWebActivity.class);
        startActivity(intent);
    };
}
