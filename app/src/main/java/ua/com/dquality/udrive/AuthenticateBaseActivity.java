package ua.com.dquality.udrive;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import ua.com.dquality.udrive.data.HttpDataProvider;
import ua.com.dquality.udrive.helpers.SharedPreferencesManager;
import ua.com.dquality.udrive.interfaces.OnHttpCodeResultExposed;

public class AuthenticateBaseActivity extends AppCompatActivity {

    protected boolean mIsLoggedIn;

    @Override
    protected void onResume() {
        checkUserAuthentication();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        checkUserAuthentication();
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUserAuthentication();
    }

    private void checkUserAuthentication(){
        SharedPreferencesManager manager = new SharedPreferencesManager(getApplicationContext());
        mIsLoggedIn = manager.readIsLoggedInPreference();
        if(!mIsLoggedIn) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else{
            HttpDataProvider dataProvider = UDriveApplication.getHttpDataProvider();
            if(dataProvider.getDataModels().ActiveData == null ||
               dataProvider.getDataModels().HomeData == null ||
               dataProvider.getDataModels().ProfitHistoryData == null ||
               dataProvider.getDataModels().ProfitStatementData == null) {
                Intent intent = new Intent(this, LoadActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }

    public void showAccountReplenishmentDialog(){
        Dialog dialog = new Dialog(this, android.R.style.Theme_Material_Dialog_Alert);
        dialog.setTitle(R.string.account_replenishment_dialog_title);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window w = dialog.getWindow();
        w.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        w.setGravity(Gravity.CENTER);
        w.setBackgroundDrawableResource(R.color.colorPrimary);

        View dialogView = dialog.getLayoutInflater().inflate(R.layout.account_replenishment_amount_dialog, null);

        EditText inputAmount =  dialogView.findViewById(R.id.input_amount);
        AppCompatButton redirectToAccountReplenishmentButton =  dialogView.findViewById(R.id.btn_redirect_to_account_replenishment);
        redirectToAccountReplenishmentButton.setOnClickListener(v1 -> {
            UDriveApplication.getHttpDataProvider().tryRedirectToAccountReplenishment(Double.parseDouble(inputAmount.getText().toString()), onAccountReplenishmentDialogResultExposed);
        });

        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        dialog.show();
    }

    private OnHttpCodeResultExposed onAccountReplenishmentDialogResultExposed= new OnHttpCodeResultExposed(){

        @Override
        public void onResultExposed(Boolean isOkCode, Object responceData) {
            Intent intent = new Intent(AuthenticateBaseActivity.this, AccountReplenishmentActivity.class);
            startActivity(intent);
        }
    };

    public static void SetLogo(AppCompatActivity activity){
        android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setLogo(R.drawable.ic_activity_logo);
            actionBar.setTitle("");
        }
    }
}
