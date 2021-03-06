package com.example.donmykl.myleancloudapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.AVAnalytics;

public class RegisterActivity extends AppCompatActivity {
  private AutoCompleteTextView mUsernameView;
  private AutoCompleteTextView mPhoneView;
  private AutoCompleteTextView mEmailView;
  private EditText mPasswordView;
  private View mProgressView;
  private View mRegisterFormView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    // Set up the register form.
    mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
    mEmailView= (AutoCompleteTextView) findViewById(R.id.email);
    mPhoneView= (AutoCompleteTextView)findViewById(R.id.phone);

    mPasswordView = (EditText) findViewById(R.id.password);
    mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.register || id == EditorInfo.IME_NULL) {
          attemptRegister();
          return true;
        }
        return false;
      }
    });

    Button musernameSignInButton = (Button) findViewById(R.id.username_register_button);
    musernameSignInButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        attemptRegister();
      }
    });

    mRegisterFormView = findViewById(R.id.register_form);
    mProgressView = findViewById(R.id.register_progress);
  }

  private void attemptRegister() {
    mUsernameView.setError(null);
    mPhoneView.setError(null);
    mEmailView.setError(null);
    mPasswordView.setError(null);

    String username = mUsernameView.getText().toString();
    String phone = mPhoneView.getText().toString();
    String email = mEmailView.getText().toString();
    String password = mPasswordView.getText().toString();

    boolean cancel = false;
    View focusView = null;

    if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
      mPasswordView.setError(getString(R.string.error_invalid_password));
      focusView = mPasswordView;
      cancel = true;
    }
    if (!TextUtils.isEmpty(phone) && !isPhoneValid(phone)) {
      mPhoneView.setError(getString(R.string.error_incorrect_phone));
      focusView = mPhoneView;
      cancel = true;
    }
    if (!TextUtils.isEmpty(email) && !isEmailValid(email)) {
      mEmailView.setError(getString(R.string.error_invalid_email));
      focusView = mEmailView;
      cancel = true;
    }

    if (TextUtils.isEmpty(username) && !isUsernameValid(username)) {
      mUsernameView.setError(getString(R.string.error_field_required));
      focusView = mUsernameView;
      cancel = true;
    }

    if (cancel) {
      focusView.requestFocus();
    } else {
      showProgress(true);

      AVUser user = new AVUser();// ?????? AVUser ????????????
      user.setUsername(username);// ???????????????
        user.setMobilePhoneNumber(phone);
        user.setEmail(email);
      user.setPassword(password);// ????????????
      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(AVException e) {
          if (e == null) {
            // ??????????????????????????????????????????????????? AVUser.getCurrentUser()
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            RegisterActivity.this.finish();
          } else {
            // ?????????????????????????????????????????????????????????????????????
            showProgress(false);
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
          }
        }
      });
    }
  }

  private boolean isUsernameValid(String username) {
    //TODO: Replace this with your own logic
    return username.length() > 3;
  }
  private boolean isEmailValid(String email) {
    //TODO: Replace this with your own logic
    return email.contains("@");
  }
  private boolean isPhoneValid(String phone) {
    //TODO: Replace this with your own logic
    return phone.length() > 10;
  }


  private boolean isPasswordValid(String password) {
    //TODO: Replace this with your own logic
    return password.length() > 4;
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
  private void showProgress(final boolean show) {
    // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
    // for very easy animations. If available, use these APIs to fade-in
    // the progress spinner.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

      mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
      mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
              show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
      });

      mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
      mProgressView.animate().setDuration(shortAnimTime).alpha(
              show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
      });
    } else {
      // The ViewPropertyAnimator APIs are not available, so simply show
      // and hide the relevant UI components.
      mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
      mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onPause() {
    super.onPause();
    AVAnalytics.onPause(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    AVAnalytics.onResume(this);
  }
}

