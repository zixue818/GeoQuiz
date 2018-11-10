package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String KEY_INDEX="index";
    private static final String TAG="QuizActivity";
    private static final int REQUEST_CODE_TO_CHEAT=0;
    private float answerCount=0;
    private float answerIsRightCount=0;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private boolean mIsCheater;

    //private Toast mToast;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private Question[] mQuestionsBank=new Question[]{
            new Question(R.string.question_australia,true),
            new Question(R.string.question_oceans,true),
            new Question(R.string.question_mideast,false),
            new Question(R.string.question_africa,false),
            new Question(R.string.question_americals,true),
            new Question(R.string.question_asia,true),
    };
    int mCurrentIndex=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate() called");
        setContentView(R.layout.activity_quiz);
        if(savedInstanceState!=null){
            mCurrentIndex=savedInstanceState.getInt(KEY_INDEX);
        }
        mQuestionTextView=(TextView)findViewById(R.id.question_text_view);
        updateQuestion();//show the question text
        mTrueButton=(Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                //3.7挑战任务，禁止一题多答
//                mTrueButton.setClickable(false);
//                mFalseButton.setClickable(false);
            }
        });
        mFalseButton=(Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              checkAnswer(false);
                //3.7挑战任务，禁止一题多答
//                mTrueButton.setClickable(false);
//                mFalseButton.setClickable(false);
            }
        });

        mNextButton=(ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mCurrentIndex=(mCurrentIndex+1)%mQuestionsBank.length;
//                mTrueButton.setClickable(true);
//                mFalseButton.setClickable(true);
                mIsCheater=false;
                updateQuestion();
            }
        });
        //mQuestionTextView.onClick==mNextButton(ImageButton).onClick
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex=(mCurrentIndex+1)%mQuestionsBank.length;
                updateQuestion();
            }

        });
        mPrevButton=(ImageButton)findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentIndex==0){
                    mCurrentIndex=mQuestionsBank.length;
                }
                mCurrentIndex=(mCurrentIndex-1)%mQuestionsBank.length;
                updateQuestion();
            }
        });
        mCheatButton=(Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start cheatActivity
                boolean answerIsTrue=mQuestionsBank[mCurrentIndex].isAnswerTrue();
                Intent intent=CheatActivity.newIntent(QuizActivity.this,answerIsTrue);
                startActivityForResult(intent,REQUEST_CODE_TO_CHEAT);
            }
        });



    }
    private void updateQuestion(){
//        Log.d(TAG,"Updating question text",new Exception());
        int question=mQuestionsBank[mCurrentIndex].getTextResId();//get the index of  current question
        mQuestionTextView.setText(question);
    }
    private void checkAnswer(boolean userPressTrue){
        boolean answerIsTrue=mQuestionsBank[mCurrentIndex].isAnswerTrue();//取得当前问题的答案,即Questions对象的isAnswerTrue字段。
        int messageResId=0;
        if(mIsCheater){
            messageResId=R.string.judgment_toast;
        }else {
            if(userPressTrue==answerIsTrue){
                messageResId=R.string.correct_toast;
                answerIsRightCount++;
            }else{
                messageResId=R.string.incorrect_toast;
            }
        }
        answerCount++;

        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
        if(answerCount==mQuestionsBank.length){
            Toast.makeText(this,(int)(answerIsRightCount/answerCount*100)+"",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode==REQUEST_CODE_TO_CHEAT){
            if(data==null){
                return;
            }
            mIsCheater=CheatActivity.wasAnswerShown(data);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX,mCurrentIndex);
    }

    //重写方法来观察生命周期

        @Override
        protected void onStart () {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

        @Override
        protected void onResume () {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

        @Override
        protected void onPause () {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
        @Override
        protected void onStop () {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

        @Override
        protected void onDestroy () {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }


}
