package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.sax.StartElementListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_CHEATER ="false" ;
    private static final int REQUEST_CODE_CHEAT = 0;

    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButtin;

    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans,true),
            new Question(R.string.question_mideast,false),
            new Question(R.string.question_africa,false),
            new Question(R.string.qoestion_americas,false),
            new Question(R.string.question_asia,true),
    };

    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
//        Log.d(TAG, "Updating question text for question #" + mCurrentIndex,new Exception());
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater){
            messageResId = R.string.judgment_toast;
        }else {
            if(userPressedTrue == answerIsTrue){
                messageResId = R.string.correct_toast;
            }else{
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);  //引用已生成的组件,返回视图对象
        mQuestionTextView.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v) {
                        mCurrentIndex = (mCurrentIndex + 1) % mQuestionTextView.length();
                        updateQuestion();
                    }
                }
        );

        mCheatButtin = (Button) findViewById(R.id.cheat_button);
        mCheatButtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(QuizActivity.this,CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
//                startActivity(i);
                startActivityForResult(i,REQUEST_CODE_CHEAT);
            }
        });

        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER,false);
        }

        updateQuestion();

        mTrueButton=(Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(  //匿名内部类的返回值为参数
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        checkAnswer(true);
                    }
                });

        mFalseButton=(Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        checkAnswer(false);
                    }
                });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionTextView.length();
                mIsCheater = false;
                updateQuestion();
            }
        });

        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionTextView.length();
                updateQuestion();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            if (data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_CHEATER,mIsCheater);
    }

//    @Override
//    public void onStart(){
//        super.onStart();
//        Log.d(TAG, "onStart() called");
//    }
//    @Override
//    public void onPause(){
//        super.onPause();
//        Log.d(TAG, "onPause() called");
//    }
//    @Override
//    public void onResume(){
//        super.onResume();
//        Log.d(TAG,"onResume() called");
//    }
//    @Override
//    public void onStop(){
//        super.onStop();
//        Log.d(TAG,"onStop() called");
//    }
//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        Log.d(TAG, "onDestroy() called");
//    }
}
