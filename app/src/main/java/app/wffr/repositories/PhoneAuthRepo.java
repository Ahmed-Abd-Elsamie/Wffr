package app.wffr.repositories;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import app.wffr.R;
import app.wffr.api.LoginRegisterListener;

public class PhoneAuthRepo {


    private FirebaseAuth auth;
    private LoginRegisterListener loginRegisterListener;
    private FirebaseUser firebaseUser;

    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private boolean isResend = false;

    private Activity context;



    public PhoneAuthRepo(Activity context, LoginRegisterListener loginRegisterListener){
        this.context = context;
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        this.loginRegisterListener = loginRegisterListener;
    }


    public void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                context,
                mCallbacks);
    }

    public void resendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                context,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                resendToken);             // ForceResendingToken from callbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                loginRegisterListener.onVerificationComplete(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            loginRegisterListener.onVerificationFailed(e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            resendToken = forceResendingToken;
            loginRegisterListener.onVerificationCodeSent("تم ارسال كود التفعيل");
            Toast.makeText(context, "تم ارسال الكود", Toast.LENGTH_SHORT).show();
        }
    };

    public void verifyVerificationCode(String otp) {
        if (verificationId != null){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
            //Toast.makeText(context, credential.getSmsCode(), Toast.LENGTH_SHORT).show();
            signInWithPhoneAuthCredential(credential);
        }else {
            //showRegisterMsg("لم يتم ادخال الكود الصحيح", 0);
            loginRegisterListener.onVerificationFailed("لم يتم ادخال الكود الصحيح");
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(context, task -> {
                    if (task.isSuccessful()) {
                        loginRegisterListener.onVerificationComplete(credential.getSmsCode());
                    } else {
                        String message = "";
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            message = "ادخل الكود الصحيح";
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }else {
                            message = "حدث خطأ سنحاول اصلاحه...";
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(e -> {
            loginRegisterListener.onFailure("لم يتم ادخال الكود الصحيح");
        });
    }

    private void showRegisterMsg(String msg, int state) {
        AlertDialog builder = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_message, null);
        builder.setView(view);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btnOk = view.findViewById(R.id.btn_done);
        TextView txtMsg = view.findViewById(R.id.txt_message);
        ImageView imgMsg = view.findViewById(R.id.img_message);

        txtMsg.setText(msg);
        if (state == 0){ // error
            imgMsg.setImageResource(R.drawable.ic_round_close_green_35);
        }


        btnOk.setOnClickListener(v -> {
            builder.dismiss();
        });

        builder.show();
    }

}