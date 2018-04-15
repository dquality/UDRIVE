package ua.com.dquality.udrive;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import ua.com.dquality.udrive.data.HttpDataProvider;
import ua.com.dquality.udrive.interfaces.OnHttpCodeResultExposed;
import ua.com.dquality.udrive.interfaces.OnRefreshHideListener;

public class LoginActivity extends AppCompatActivity {

    private EditText mInputPhone;
    private EditText mInputCode;
    private TextInputLayout mInputPhoneLabel;
    private TextInputLayout mInputCodeLabel;
    private boolean mGetCodeExecuted = false;

    private AppCompatButton mLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            mGetCodeExecuted = savedInstanceState.getBoolean("GetCodeExecuted");
        }

        setContentView(R.layout.activity_login);

        mInputCode = findViewById(R.id.input_code);
        mInputPhone = findViewById(R.id.input_phone);

        mInputPhoneLabel = findViewById(R.id.input_phone_label);
        mInputCodeLabel = findViewById(R.id.input_code_label);

        mInputCode.addTextChangedListener(codeTextWatcher);
        mInputPhone.addTextChangedListener(phoneTextWatcher);

        mLogin = findViewById(R.id.btn_login);
        mLogin.setOnClickListener(loginClickListener);

        RaiseTextChangedEvents();
    }

    private View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!mGetCodeExecuted)
            {
                UDriveApplication.getHttpDataProvider().LoginByPhone(mInputPhone.getText().toString(), onPhoneResultExposed);
            }
            else{
                if(mInputPhone.isEnabled()){
                    UDriveApplication.getHttpDataProvider().LoginByPhone(mInputPhone.getText().toString(), onPhoneResultExposed);
                }
                else{
                    UDriveApplication.getHttpDataProvider().LoginByCode(mInputCode.getText().toString(), onCodeResultExposed);
                }
            }

            RaiseTextChangedEvents();
        }
    };

    private OnHttpCodeResultExposed onPhoneResultExposed = isOkCode -> {
        if(isOkCode){
            mGetCodeExecuted = true;
        }
    };

    private OnHttpCodeResultExposed onCodeResultExposed = isOkCode -> {
        if(isOkCode){
            String accessToken = UDriveApplication.getHttpDataProvider().mAccessToken;
            if(accessToken != null && !accessToken.isEmpty()) {
                LoginActivity currentActivity = LoginActivity.this;
                Intent intent = new Intent(currentActivity, LoadActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                currentActivity.startActivity(intent);
                currentActivity.finish();
            }
        }
    };

    private void RaiseTextChangedEvents(){
        String codeText = mInputCode.getText().toString();
        codeTextWatcher.onTextChanged(codeText,0, 0, codeText.length());

        String phoneText = mInputPhone.getText().toString();
        phoneTextWatcher.onTextChanged(phoneText,0, 0, phoneText.length());
    }

    private  TextWatcher codeTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
            if(s.length() != 0){
                mInputPhone.setEnabled(false);
                mInputPhoneLabel.setEnabled(false);
                if(mGetCodeExecuted) {
                    mLogin.setText(getResources().getText(R.string.login));
                    mLogin.setBackgroundResource(R.drawable.selector_add_accrual_background);
                }
            }
            else{
                mLogin.setText(getResources().getText(R.string.get_code));
                mLogin.setBackgroundResource(R.drawable.selector_login_button_background);
                mInputPhone.setEnabled(true);
                mInputPhoneLabel.setEnabled(true);
            }
        }
    };

    private  TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
            if(!mGetCodeExecuted){
                mInputCode.setEnabled(false);
                mInputCodeLabel.setEnabled(false);
            }
            else {
                mInputCode.setEnabled(true);
                mInputCodeLabel.setEnabled(true);
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("GetCodeExecuted", true);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGetCodeExecuted = savedInstanceState.getBoolean("GetCodeExecuted");
    }
}
