package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mainText;
    private TextView operationsText;
    private double previousNum = 0;
    private boolean isDotClicked = false;
    private boolean isOperation = false;
    private boolean isEqualsClicked = false;
    private CustomViewModel vModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vModel = new ViewModelProvider(this).get(CustomViewModel.class);
        vModel.getMainText().observe(this, text-> mainText.setText(text));
        vModel.getOperations().observe(this, op -> operationsText.setText(op));
        vModel.getPreviousNum().observe(this, num -> previousNum = num);
        vModel.getIsDotClicked().observe(this, dot -> isDotClicked = dot);
        vModel.getIsOperation().observe(this, opStatus -> isOperation = opStatus);
        vModel.getIsEqualsClicked().observe(this, equals -> isEqualsClicked = equals);
        mainText = findViewById(R.id.textMain);
        operationsText = findViewById(R.id.textOperations);
        mainText.setText("0");
        findViewById(R.id.mainView).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if(view.getWidth() > 0)
                {
                    mainText.setMinWidth(view.getWidth());
                    view.removeOnLayoutChangeListener(this);
                }
            }
        });
        findViewById(R.id.btnBackspace).setOnLongClickListener(view -> {
            vModel.setMainText("0");
            return true;
        });
    }

    public void onBackspaceClick(View view) {
        if(isEqualsClicked)
        {
            vModel.setMainText("0");
            vModel.setOperations("");
            isEqualsClicked = false;
        }
        else if (mainText.getText().toString().length() != 1)
            vModel.setMainText(mainText.getText().toString().substring(0, mainText.getText().toString().length() - 1));
        else
            vModel.setMainText("0");
    }

    public void onClearClick(View view) {
        vModel.setMainText("0");
        vModel.setOperations("");
    }

    public void onNumClick(View view)
    {
        if (isEqualsClicked)
        {
            vModel.setMainText("0");
            vModel.setOperations("");
            vModel.setIsEqualsClicked(false);
            vModel.setPreviousNum(0);
        }
        if (mainText.getText().toString().equals("0"))
        {
            if (view.getId() != R.id.btnDot)
                vModel.setMainText(((TextView)view).getText().toString());
            else
            {
                if (!isDotClicked)
                {
                    vModel.setIsDotClicked(true);
                    vModel.setMainText(String.format("%s.", mainText.getText().toString()));
                }
            }
        }
        else
        {
            if (view.getId() == R.id.btnDot)
            {
                if (!isDotClicked)
                {
                    if (!isOperation)
                    {
                        vModel.setIsDotClicked(true);
                        vModel.setMainText(String.format("%s.", mainText.getText().toString()));
                    }
                    else
                    {
                        vModel.setIsDotClicked(true);
                        vModel.setMainText("0.");
                        vModel.setIsOperation(false);
                    }
                }
            }
            else
            {
                if (!isOperation)
                {
                    vModel.setMainText(String.format("%s%s", mainText.getText().toString(), ((TextView) view).getText().toString()));
                }
                else
                {
                    vModel.setMainText(((TextView) view).getText().toString());
                    vModel.setIsOperation(false);
                }
            }
        }
        vModel.setPreviousNum(0);
    }

    public void onOperationClick(View view)
    {
        if (isEqualsClicked)
        {
            vModel.setOperations(mainText.getText().toString());
            vModel.setPreviousNum(Double.parseDouble(mainText.getText().toString()));
            vModel.setMainText("0");
            vModel.setIsEqualsClicked(false);
        }
        if (operationsText.getText().toString().length() == 0)
        {
            vModel.setOperations(String.format("%s%s", mainText.getText().toString(), ((TextView)view).getText().toString()));
            vModel.setMainText("0");
            vModel.setIsOperation(true);
        }
        else
        {
            noEqualsOperation();
            vModel.setIsOperation(true);
            vModel.setOperations(String.format("%s%s", operationsText.getText().toString(), ((TextView)view).getText().toString()));
        }
        vModel.setIsDotClicked(false);
    }

    private void noEqualsOperation() {
        StringBuilder num = new StringBuilder();
        StringBuilder op = new StringBuilder();

        for (int i = 0; i < operationsText.getText().toString().length(); i++)
        {
            if (operationsText.getText().toString().charAt(i) == '+' || operationsText.getText().toString().charAt(i) == '―'
                    || operationsText.getText().toString().charAt(i) == '÷' || operationsText.getText().toString().charAt(i) == '×')
            {
                if (i != 0)
                    if (operationsText.getText().toString().charAt(i-1) != '+' || operationsText.getText().toString().charAt(i-1) != '―'
                            || operationsText.getText().toString().charAt(i-1) != '÷' || operationsText.getText().toString().charAt(i-1) != '×')
                        op.append(operationsText.getText().toString().charAt(i));
                else num.append(operationsText.getText().toString().charAt(i));
            }
            else num.append(operationsText.getText().toString().charAt(i));
        }

        String number = num.toString();
        String operation = op.toString();

        double result = Double.parseDouble(number);
        switch (operation) {
            case "+":
                result = result + Double.parseDouble(mainText.getText().toString());
                break;
            case "―":
                result = result - Double.parseDouble(mainText.getText().toString());
                break;
            case "÷":
                if (Double.parseDouble(mainText.getText().toString()) != 0)
                    result = result / Double.parseDouble(mainText.getText().toString());
                else return;
                break;
            case "×":
                result = result * Double.parseDouble(mainText.getText().toString());
                break;
        }
        if(result % 1 == 0) {
            vModel.setOperations(String.valueOf((int)result));
            vModel.setMainText(String.valueOf((int)result));
        }
        else {
            vModel.setOperations(String.valueOf(result));
            vModel.setMainText(String.valueOf(result));
        }
    }

    public void onEqualsClick(View view) {
        if (operationsText.getText().toString().equals("") || isEqualsClicked)
            return;

        StringBuilder num = new StringBuilder();
        StringBuilder op = new StringBuilder();

        for (int i = 0; i < operationsText.getText().toString().length(); i++)
        {
            if (operationsText.getText().toString().charAt(i) == '+' || operationsText.getText().toString().charAt(i) == '―'
                    || operationsText.getText().toString().charAt(i) == '÷' || operationsText.getText().toString().charAt(i) == '×')
            {
                if (i != 0)
                    if (operationsText.getText().toString().charAt(i-1) != '+' || operationsText.getText().toString().charAt(i-1) != '―'
                            || operationsText.getText().toString().charAt(i-1) != '÷' || operationsText.getText().toString().charAt(i-1) != '×')
                    {
                            op.append(operationsText.getText().toString().charAt(i));
                    }
                else num.append(operationsText.getText().toString().charAt(i));
            }
            else num.append(operationsText.getText().toString().charAt(i));
        }

        String number = num.toString();
        number = number.replace("=", "");
        String operation = op.toString();
        double result = Double.parseDouble(number);

        switch (operation) {
            case "+":
                result = result + Double.parseDouble(mainText.getText().toString());
                break;
            case "―":
                result = result - Double.parseDouble(mainText.getText().toString());
                break;
            case "÷":
                result = result / Double.parseDouble(mainText.getText().toString());
                break;
            case "×":
                result = result * Double.parseDouble(mainText.getText().toString());
                break;
        }
        vModel.setOperations(String.format("%s%s=", operationsText.getText().toString(), mainText.getText().toString()));
        if(result % 1 == 0) {
            vModel.setMainText(String.valueOf((int)result));
        }
        else {
            vModel.setMainText(String.valueOf(result));
        }
        vModel.setIsOperation(false);
        vModel.setIsEqualsClicked(true);
    }

    public void onPercentClick(View view) {
        if(isOperation)
        {
            double res = Double.parseDouble(mainText.getText().toString())*(Double.parseDouble(mainText.getText().toString())/100.0);
            vModel.setMainText(String.valueOf(res));
        }
    }
}