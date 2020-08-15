package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CalculatorActivity extends AppCompatActivity {

    public static final String KEY_RESULT_STRING = "resultString";
    private TextView mTextViewResult;

    private Button mButtonDot, mButtonEqual, mButtonSum, mButtonSub, mButtonMultiply, mButtonDivison,
            mButtonDelete, mButtonZero, mButtonOne, mButtonTwo, mButtonThree, mButtonFour,
            mButtonFive, mButtonSix, mButtonSeven, mButtonEight, mButtonNine;

    private HorizontalScrollView mScrollView;
    private int[] buttonNumber = {R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_dot};

    private int[] buttonOperatotr = {R.id.btn_sum, R.id.btn_sub, R.id.btn_mul, R.id.btn_div};

    private double memory = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        findViews();
        if (savedInstanceState != null){
            mTextViewResult.setText(savedInstanceState.getString(KEY_RESULT_STRING));
            flag = false;
        }
        setListeners();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_RESULT_STRING, mTextViewResult.getText().toString());
    }

    @SuppressLint("WrongViewCast")
    private void findViews() {
        mTextViewResult = findViewById(R.id.txtview_result);
        mButtonEqual = findViewById(R.id.btn_equal);
        mButtonDelete = findViewById(R.id.btn_delete);
        mScrollView = findViewById(R.id.scroll);
    }

    private void showNumber(int id) {
        switch (id) {
            case R.id.btn_dot:
                setNumber(R.string.button_dot);
                break;
            case R.id.btn_1:
                setNumber(R.string.button_1);
                break;
            case R.id.btn_2:
                setNumber(R.string.button_2);
                break;
            case R.id.btn_3:
                setNumber(R.string.button_3);
                break;
            case R.id.btn_4:
                setNumber(R.string.button_4);
                break;
            case R.id.btn_5:
                setNumber(R.string.button_5);
                break;
            case R.id.btn_6:
                setNumber(R.string.button_6);
                break;
            case R.id.btn_7:
                setNumber(R.string.button_7);
                break;
            case R.id.btn_8:
                setNumber(R.string.button_8);
                break;
            case R.id.btn_9:
                setNumber(R.string.button_9);
                break;
            default:
                setNumber(R.string.button_0);
                break;
        }
    }

    private void setNumber(int resID) {
        mTextViewResult.setText(mTextViewResult.getText().toString() + getResources().getString(resID));
        mScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        Toast.makeText(this, resID, Toast.LENGTH_SHORT).show();
    }

    private void showOperator(int id) {
        switch (id) {
            case R.id.btn_sum:
                setOperators(R.string.button_sum);
                break;
            case R.id.btn_sub:
                setOperators(R.string.button_sub);
                break;
            case R.id.btn_mul:
                setOperators(R.string.button_multiply);
                break;
            case R.id.btn_div:
                setOperators(R.string.button_divison);
                break;
        }
    }

    private void setOperators(int resID) {
        mTextViewResult.setText(mTextViewResult.getText().toString() + " " + getResources().getString(resID) + " ");
        Toast.makeText(this, resID, Toast.LENGTH_SHORT).show();
    }

    boolean flag = true;

    private void setListeners() {

        for (final int id : buttonNumber)
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mTextViewResult.getText().toString().equals("Error"))
                        mTextViewResult.setText("0");

                    while (id == R.id.btn_0 && flag) {
                        mTextViewResult.setText("0");
                        Toast.makeText(CalculatorActivity.this, R.string.button_0, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (flag) {
                        flag = false;
                        mTextViewResult.setText("");
                    }
                    showNumber(id);
                }
            });

        for (final int id : buttonOperatotr) {
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showOperator(id);
                }
            });
        }

        mButtonEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CalculatorActivity.this, R.string.button_equal, Toast.LENGTH_SHORT).show();
                calculate();

                if (mTextViewResult.getText().toString().equals("Infinity")) {
                    mTextViewResult.setText("Error");
                    reset();
                }
            }
        });

        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
                mTextViewResult.setText("0");
            }
        });
    }

    private void reset() {
        memory = 0;
        flag = true;
    }

    public void calculate() {
        String text = mTextViewResult.getText().toString();
        List<Double> numbers = new ArrayList<>();
        List<Character> chars = new ArrayList<>();
        int temp = 0;

        outer:
        for (int i = 0; i < text.length(); i++) {

            if (text.charAt(i) == '+' || text.charAt(i) == '-'
                    || text.charAt(i) == '*' || text.charAt(i) == '/') {

                String number = text.substring(temp, (i - 1));
                String numberR;
                String numberL;
                double point;
                for (int j = 0; j < number.length(); j++) {
                    if (number.charAt(j) == '.') {
                        numberR = number.substring(0, j);
                        numberL = number.substring(j + 1);
                        point = Math.pow(10, ((number.length() - 1) - j));
                        number = numberR + numberL;
                        numbers.add(Double.parseDouble(number) / point);
                        temp = i + 2;
                        chars.add(text.charAt(i));
                        continue outer;
                    }
                }
                numbers.add(Double.parseDouble(number));
                temp = i + 2;
                chars.add(text.charAt(i));
            }
        }
        numbers.add(Double.parseDouble(text.substring(temp)));

        memory = numbers.get(0);
        int counter = 1;
        for (int i = 0; i < chars.size(); i++) {
            memory = calculateResult(memory, numbers.get(counter), chars.get(i));
            counter++;
        }
    }

    public double calculateResult(double input1, double input2, char operator) {

        double result;
        switch (operator) {
            case '+':
                result = sum(input1, input2);
                break;
            case '-':
                result = sub(input1, input2);
                break;
            case '*':
                result = multiply(input1, input2);
                break;
            case '/':
                result = division(input1, input2);
                break;
            default:
                result = 0;
                break;
        }
        mTextViewResult.setText(String.valueOf(result));

        return result;
    }

    public static double sum(double input1, double input2) {
        return input1 + input2;
    }

    public static double sub(double input1, double input2) {
        return input1 - input2;
    }

    public static double multiply(double input1, double input2) {
        return input1 * input2;
    }

    public static double division(double input1, double input2) {
        return input1 / input2;
    }

}