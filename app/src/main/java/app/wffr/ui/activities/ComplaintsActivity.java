package app.wffr.ui.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import app.wffr.MainActivity;
import app.wffr.R;
import app.wffr.WffrBaseActivity;
import app.wffr.databinding.ActivityComplaintsBinding;
import app.wffr.listeners.UserComplaintsListener;
import app.wffr.models.Complaint;
import app.wffr.utils.utils;
import app.wffr.viewmodels.ComplaintsViewModel;

public class ComplaintsActivity extends WffrBaseActivity {

    private ActivityComplaintsBinding binding;
    private ComplaintsViewModel complaintsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_complaints);



        complaintsViewModel = new ViewModelProvider(this).get(ComplaintsViewModel.class);
        complaintsViewModel.init(new UserComplaintsListener() {
            @Override
            public void onComplete(@NonNull String message) {
                binding.progressBar.setVisibility(View.GONE);
                if (message.equals("success")){
                    Toast.makeText(ComplaintsActivity.this, getString(R.string.sent_success), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ComplaintsActivity.this, getString(R.string.sent_error), Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
        /*if (!utils.internetIsConnected()){
            startActivity(new Intent(this, NoInternetActivity.class));
        }*/
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        binding.btnBack.setOnClickListener(v -> onBackPressed());


    }

    private void validateData() {

        String title = binding.txtTopic.getText().toString();
        String note = binding.txtNotes.getText().toString();

        if (TextUtils.isEmpty(title)){
            Toast.makeText(this, getString(R.string.note_title_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.isValidFirstChar(title)){
            Toast.makeText(this, R.string.first_char_error, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.isValidWord(title)){
            Toast.makeText(this, getString(R.string.note_topic_error), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(note)){
            Toast.makeText(this, getString(R.string.note_desc_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.isValidFirstChar(note)){
            Toast.makeText(this, R.string.first_char_error, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.isValidWord(note)){
            Toast.makeText(this, getString(R.string.note_desc_error), Toast.LENGTH_SHORT).show();
            return;
        }

        Complaint complaint = new Complaint(
                title,
                note
        );

        complaintsViewModel.insertComplaint(complaint, MainActivity.user.getId());
        binding.progressBar.setVisibility(View.VISIBLE);

    }


}