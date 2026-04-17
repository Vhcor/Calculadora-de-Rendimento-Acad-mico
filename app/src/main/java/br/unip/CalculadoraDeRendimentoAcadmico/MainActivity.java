package br.unip.CalculadoraDeRendimentoAcadmico;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher; // Importante para o tempo real
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editNP1;
    private EditText editNP2;
    private EditText editMS;

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

        editNP1 = findViewById(R.id.notaNP1);
        editNP2 = findViewById(R.id.notaNP2);
        editMS = findViewById(R.id.MS);

        // Desativa a edição manual no campo de resultado para evitar confusão
        editMS.setFocusable(false);

        // Chama a função que vai monitorar a digitação
        configurarObservadores();
    }

    private void configurarObservadores() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                executarCalculo(); // Sempre que mudar o texto, tenta calcular
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        // Adiciona o vigia nos dois campos de entrada
        editNP1.addTextChangedListener(watcher);
        editNP2.addTextChangedListener(watcher);
    }

    private void executarCalculo() {
        String s1 = editNP1.getText().toString();
        String s2 = editNP2.getText().toString();

        // Só calcula se os dois campos não estiverem vazios
        if (!s1.isEmpty() && !s2.isEmpty()) {
            try {
                double n1 = Double.parseDouble(s1);
                double n2 = Double.parseDouble(s2);

                double resultado = Calculadora.MS(n1, n2);

                // Exibe o resultado formatado
                editMS.setText(String.format("%.2f", resultado));
            } catch (NumberFormatException e) {
                editMS.setText("");
            }
        } else {
            editMS.setText(""); // Limpa o resultado se apagar um dos campos
        }
    }
}