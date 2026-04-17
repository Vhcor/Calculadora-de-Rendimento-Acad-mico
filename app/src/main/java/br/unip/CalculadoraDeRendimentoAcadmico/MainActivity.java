package br.unip.CalculadoraDeRendimentoAcadmico;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editNP1, editNP2, editMS, editFaltaPNumber, editExame;
    private TextView txtStatus;
    private SeekBar seekBar;
    private TableRow rowExame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialização
        editNP1 = findViewById(R.id.notaNP1);
        editNP2 = findViewById(R.id.notaNP2);
        editMS = findViewById(R.id.MS);
        editFaltaPNumber = findViewById(R.id.faltaPNumber);
        editExame = findViewById(R.id.notaExame);
        txtStatus = findViewById(R.id.txtStatus);
        seekBar = findViewById(R.id.seekBar);
        rowExame = findViewById(R.id.rowExame);

        editMS.setFocusable(false);
        editFaltaPNumber.setFocusable(false); // Opcional: deixar a SeekBar controlar

        configurarListeners();
    }

    private void configurarListeners() {
        // 1. Ouvinte da SeekBar
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editFaltaPNumber.setText(progress + "%");
                executarCalculo();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 2. Ouvinte dos campos de texto (NP1, NP2 e Nota do Exame)
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence s, int i, int i1, int i2) { executarCalculo(); }
            @Override public void afterTextChanged(Editable s) {}
        };

        editNP1.addTextChangedListener(watcher);
        editNP2.addTextChangedListener(watcher);
        editExame.addTextChangedListener(watcher);
    }

    private void executarCalculo() {
        try {
            String s1 = editNP1.getText().toString();
            String s2 = editNP2.getText().toString();
            int presenca = seekBar.getProgress();

            if (s1.isEmpty() || s2.isEmpty()) {
                limparResultados();
                return;
            }

            double n1 = Double.parseDouble(s1);
            double n2 = Double.parseDouble(s2);
            double ms = Calculadora.MS(n1, n2);
            editMS.setText(String.format("%.2f", ms));

            // REGRA 1: Presença menor que 75% -> Reprovado Direto
            if (presenca < 75) {
                txtStatus.setText("REPROVADO POR FALTA");
                txtStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                rowExame.setVisibility(View.GONE);
            }
            // REGRA 2: Média >= 7 e Presença OK -> Aprovado
            else if (ms >= 7.0) {
                txtStatus.setText("APROVADO");
                txtStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                rowExame.setVisibility(View.GONE);
            }
            // REGRA 3: Média < 7 e Presença OK -> EXAME
            else {
                txtStatus.setText("EXAME");
                txtStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                rowExame.setVisibility(View.VISIBLE);

                // Lógica do Exame
                String sExame = editExame.getText().toString();
                if (!sExame.isEmpty()) {
                    double notaExame = Double.parseDouble(sExame);
                    double mediaFinal = Calculadora.MF(ms, notaExame);

                    if (mediaFinal >= 5.0) {
                        txtStatus.setText("APROVADO (PÓS EXAME)");
                        txtStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    } else {
                        txtStatus.setText("REPROVADO NO EXAME");
                        txtStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    }
                }
            }
        } catch (Exception e) {
            limparResultados();
        }
    }

    private void limparResultados() {
        editMS.setText("");
        txtStatus.setText("");
        rowExame.setVisibility(View.GONE);
    }
}