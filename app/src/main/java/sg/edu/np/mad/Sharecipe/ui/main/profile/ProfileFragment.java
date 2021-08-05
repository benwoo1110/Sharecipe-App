package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.LoginActivity;
import sg.edu.np.mad.Sharecipe.ui.common.OnSingleClickListener;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.CheckGroup;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.InputResult;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.RequiredFieldCheck;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, new PartialProfileFragment(App.getAccountManager().getAccount().getUserId()))
                .commit();

        Button editProfileButton = view.findViewById(R.id.editUserinfo);
        Button editPasswordButton = view.findViewById(R.id.passwordButton);
        Button logoutButton = view.findViewById(R.id.buttonLogout);
        Button deleteButton = view.findViewById(R.id.deletaAccountButton);

        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            getActivity().startActivity(intent);
        });

        editPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditPasswordActivity.class);
            getActivity().startActivity(intent);
        });

        logoutButton.setOnClickListener((OnSingleClickListener) v -> {
            new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?").setCancelable(false)
                    .setPositiveButton("Logout", (dialog, which) -> {
                        App.getAccountManager().logout().onSuccess(ignore -> {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        })
                        .onFailed(reason -> getActivity().runOnUiThread(() -> {
                            logoutButton.setEnabled(true);
                            Toast.makeText(getContext(), reason.getMessage(), Toast.LENGTH_SHORT).show();
                        }))
                        .onFailed(reason -> getActivity().runOnUiThread(() -> {
                            logoutButton.setEnabled(true);
                            Toast.makeText(getContext(), "Server error.", Toast.LENGTH_SHORT).show();
                        }));
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        logoutButton.setEnabled(true);
                    })
                    .show();
        });

        deleteButton.setOnClickListener((OnSingleClickListener) v -> {
            View deleteConfirmView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_confirm_input, null);
            TextInputLayout confirmInput = deleteConfirmView.findViewById(R.id.confirmInput);
            confirmInput.setStartIconDrawable(R.drawable.ic_round_lock_24);
            confirmInput.setHint("Password");
            confirmInput.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            CheckGroup checkGroup = new CheckGroup()
                    .add(confirmInput, new RequiredFieldCheck());

            AlertDialog confirmDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom)
                    .setView(deleteConfirmView)
                    .setTitle("Delete account")
                    .setMessage("Are you sure you want to delete account? You cannot undo this action. Please type in your password to confirm.")
                    .setCancelable(false)
                    .setPositiveButton("Delete", (dialog, which) -> { })
                    .setNegativeButton("Cancel", (dialog, which) -> deleteButton.setEnabled(true))
                    .show();

            confirmDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                InputResult inputResult = checkGroup.parseInputs();
                if (!inputResult.passedAllChecks()) {
                    return;
                }

                Toast.makeText(getContext(), "Deleting account...", Toast.LENGTH_SHORT).show();
                App.getAccountManager().delete(inputResult.get(confirmInput)).onSuccess(aVoid -> {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Account deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    });
                }).onFailed(voidFailed -> {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), voidFailed.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }).thenAccept(voidDataResult -> {
                    getActivity().runOnUiThread(() -> {
                        deleteButton.setEnabled(true);
                        confirmDialog.dismiss();
                    });
                });
            });
        });

        return view;
    }
}
