package com.familyset.randomchatting.ui.signIn.authMethodPick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.familyset.randomchatting.R;
import com.familyset.randomchatting.util.ActivityUtils;

public class AuthMethodPickActivity extends AppCompatActivity {
    protected static final int RC_SIGN_IN = 100;

    private AuthMethodPickPresenter mAuthMethodPickPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_method_pick);

        AuthMethodPickFragment authMethodPickFragment =
                (AuthMethodPickFragment) getSupportFragmentManager().findFragmentById(R.id.auth_method_pick_frame_layout);

        if (authMethodPickFragment == null) {
            // Create the fragment
            authMethodPickFragment = AuthMethodPickFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), authMethodPickFragment, R.id.auth_method_pick_frame_layout);
        }

        mAuthMethodPickPresenter = new AuthMethodPickPresenter(authMethodPickFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            startActivity(data);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("SDF", "destroy auth act");
    }
}
