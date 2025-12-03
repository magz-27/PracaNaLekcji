package com.example.pracanalekcji;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Zestawy znakow
    private final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String DIGITS = "0123456789";
    private final String SPECIAL_CHARS = "!@#$%^&*()_+-=";

    private String generatedPasswordResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // spinner
        Spinner spinner = findViewById(R.id.spinner2);
        String[] items = {"Kierownik", "Starszy programista", "Młodszy programista", "Tester"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }

        // pobieranie elementow
        EditText etName = findViewById(R.id.NameET);
        EditText etSurname = findViewById(R.id.SurnameET);

        Button btnGenerate = findViewById(R.id.GeneratePasswordBtn);
        Button btnConfirm = findViewById(R.id.ConfirmBTN);

        EditText etLength = findViewById(R.id.PasswordCharactersET);
        CheckBox cbUpper = findViewById(R.id.SmallAndBigLettersCB);
        CheckBox cbDigits = findViewById(R.id.NumbersCB);
        CheckBox cbSpecial = findViewById(R.id.SpecialCharactersCB);

        // generuj haslo
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // anticrash
                if (etLength.getText().toString().isEmpty()) return;

                int length = Integer.parseInt(etLength.getText().toString());


                generatedPasswordResult = generatePasswordLogic(length, cbUpper.isChecked(), cbDigits.isChecked(), cbSpecial.isChecked());

                showResultDialog("Wygenerowane hasło", generatedPasswordResult);
            }
        });

        // zatwierdz
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String surname = etSurname.getText().toString().trim();
                String position = spinner.getSelectedItem().toString();

                String message = "Dane pracownika: " + name + " " + surname + "\n" +
                        "Stanowisko: " + position + "\n" +
                        "Hasło: " + generatedPasswordResult;

                showResultDialog("Dane pracownika", message);
            }
        });
    }

    // Logika hasla
    private String generatePasswordLogic(int length, boolean useUpper, boolean useDigits, boolean useSpecial) {
        StringBuilder passwordBuilder = new StringBuilder();
        Random random = new Random();

        String allowedChars = LOWERCASE;

        if (useUpper) {
            allowedChars += UPPERCASE;
        }
        if (useDigits) {
            allowedChars += DIGITS;
        }
        if (useSpecial) {
            allowedChars += SPECIAL_CHARS;
        }

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            passwordBuilder.append(allowedChars.charAt(index));
        }

        return passwordBuilder.toString();
    }



    private void showResultDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
