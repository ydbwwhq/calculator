package com.halewang.calculator.activity

import android.app.Activity
import android.content.BroadcastReceiver
import android.graphics.drawable.*
import android.os.Bundle
import android.text.Editable
import android.text.TextPaint
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.halewang.util.Util
import com.halewang.calculator.R;
import com.halewang.calculator.bean.Operator
import com.halewang.calculator.bean.OperatorFactory
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : Activity(),View.OnClickListener,TextWatcher{

   private var w = 0;
    var mTotal = 0.0;
    var mNowNum = "";
    var mOperator = Operator.UNKNOW;
    private var mScreenW  = 0;
    private var mFontPxSize = 0.0f;
    private var mMaxFontPxSize = 0.0f;
    private lateinit var mBtnArr:Array<Button>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)







        val kPaddingW = Util.dpToPIX(this,10.0f)
        w = (Util.getDeviceW(this) - 5 * kPaddingW )/ 4;
        setViewAttr(ll1);
        setViewAttr(ll2);
        setViewAttr(ll3);
        setViewAttr(ll4);


        mFontPxSize = resultTV.paint.textSize;
        mMaxFontPxSize = mFontPxSize;
        mScreenW = Util.getDeviceW(this);

        var params = equalBtn.layoutParams;
        params.height = w ;
        equalBtn.layoutParams = params;




        var stateDrawable = StateListDrawable();


        var pressDrawable = GradientDrawable()
        pressDrawable.cornerRadius = (params.height /2.0).toFloat()
        pressDrawable.setColor(resources.getColor(R.color.colorfab27b));



        var defaultDrawable = GradientDrawable()
        defaultDrawable.cornerRadius = (params.height /2.0).toFloat()
        defaultDrawable.setColor(resources.getColor(R.color.numberColor));


        stateDrawable.addState(intArrayOf(android.R.attr.state_pressed),pressDrawable);
        stateDrawable.addState(intArrayOf(android.R.attr.state_selected),pressDrawable);

        stateDrawable.addState(intArrayOf(),defaultDrawable);




        equalBtn.background = stateDrawable




        mBtnArr = arrayOf(btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,addBtn,subBtn,multiBtn,divBtn,pointBtn,equalBtn,clearBtn)
        btn0.tag = 0;
        btn1.tag = 1;
        btn2.tag = 2;
        btn3.tag = 3;
        btn4.tag = 4;
        btn5.tag = 5;
        btn6.tag = 6;
        btn7.tag = 7;
        btn8.tag = 8;
        btn9.tag = 9;
        divBtn.tag   = 100;
        multiBtn.tag = 101;
        subBtn.tag   = 102;
        addBtn.tag   = 103;
        clearBtn.tag = 105;
        pointBtn.tag = 104;
        equalBtn.tag = 106;



        resultTV.addTextChangedListener(this)



        setUpAD()

    }

   private fun setUpAD()
    {
        MobileAds.initialize(this,"ca-app-pub-5096850339378215~2761816718");
        val adRequest = AdRequest.Builder().build();
        ad.loadAd(adRequest);
        ad.adListener = object:AdListener(){
            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                System.out.print("error=" + p0);
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
            }
        }
    }

    fun setViewAttr(layout:RelativeLayout){
        for (i in 0..(layout.childCount - 1))
        {
            var btn = layout.getChildAt(i);
            var params = btn.layoutParams;
            params.width = w;
            params.height = params.width;
            btn.layoutParams = params;



            val stateDrawable = StateListDrawable()
            var pressDrawable = GradientDrawable()
            pressDrawable.cornerRadius = (params.width / 2.0).toFloat()
            pressDrawable.setColor(resources.getColor(R.color.colorfab27b));






            var defaultDrawable = GradientDrawable()
            defaultDrawable.cornerRadius = (params.height /2.0).toFloat()
            defaultDrawable.setColor(resources.getColor(R.color.numberColor));



            if(i == layout.childCount - 1)
            {
                defaultDrawable.setColor(resources.getColor(R.color.colorFF8A02))
                pressDrawable.setColor(resources.getColor(R.color.colorfab27b))
            }else
            {
                defaultDrawable.setColor(resources.getColor(R.color.numberBgColor))
                pressDrawable.setColor(resources.getColor(R.color.numberPressColor))
            }

            stateDrawable.addState(intArrayOf(android.R.attr.state_pressed),pressDrawable);
            stateDrawable.addState(intArrayOf(android.R.attr.state_selected),pressDrawable);
            stateDrawable.addState(intArrayOf(),defaultDrawable);



            btn.background = stateDrawable;
        }
    }
    override fun onClick(v: View) {

        val tag = v.tag as Int
        for( btn:Button?  in mBtnArr)
        {
            btn?.isSelected = btn?.tag == tag
           // Log.i("hh","tag=${btn?.tag} selected=${btn?.isSelected}");
        }

        when (v.tag as Int) {
             in 0..9 -> {
                handleClickNum(tag);
             }
             in 100..106->{
                 handleClickOperator(tag)
             }

        }
    }
    fun handleClickNum(number:Int)
    {
        val number = "$mNowNum$number"
        mNowNum = number;
        setResult(number);
    }
    fun setResult(number: String)
    {
        val index = number.indexOf(".");
        var num = "";
        if(index > 0 && index < number.length - 1)
        {
            num = number.substring(index+1,number.length);
            if(num.toIntOrNull() == 0)
            {
                resultTV.text = number.substring(0,index);
            }else
            {
                resultTV.text = number;
            }
            Log.i("hh","num:$num")
        }else
        {
            resultTV.text = number;
        }

    }
    fun compute():Double
    {
        var result = 0.0;
        //Log.i("hh","operatorType:$mOperator");
        when(mOperator)
        {
            Operator.UNKNOW->{
                if(!mNowNum.equals(""))
                {
                    result =  mNowNum.toDouble() + mTotal;
                }else
                {
                    result = mTotal;
                }

            }
            in Operator.DIV..Operator.ADD->{
                val  calculate = OperatorFactory.createOperatorModel(mOperator);
                var nowNum = if(mNowNum.equals("") ){0.0}else{mNowNum.toDouble()}
                result = calculate.getResult(mTotal,nowNum);
                //Log.i("hh","结果$result")
            }

        }
        mTotal = result;
        mNowNum = "";
        return result;

    }
    fun resetComputer()
    {
        mTotal = 0.0;
        mNowNum = "";
        mOperator = Operator.UNKNOW;
        setResult("0");
    }
    fun handleClickOperator(tag:Int)
    {
        when(tag)
        {
            104->{
                if(mNowNum.indexOf(".") > 0)
                {
                    return;
                }
                mNowNum = when(mNowNum)
                {
                    "" -> {
                        "0."
                    }
                    else -> {
                        "$mNowNum."
                    }
                }
                setResult(mNowNum)
                return;
            }
            105->{
                resetComputer();
                return;
            }

        }

        when(tag)
        {
            106->{
              //等号
                //mOperator = Operator.UNKNOW;
                val  result = compute();
                setResult("$result");
            }

            100->{
                //除法
                if(mOperator == Operator.DIV)
                {
                    return;
                }
                if(mNowNum == "" && tag in 100..103)
                {
                    mOperator = Operator.DIV;
                    return;
                }


                val  result = compute();
                setResult("$result");
                mOperator = Operator.DIV;


            }
            101->{
                //乘法
                if(mOperator == Operator.MULTI)
                {
                    return;
                }
                if(mNowNum == "" && tag in 100..103)
                {
                    mOperator = Operator.MULTI;
                    return;
                }

                val  result = compute();
                setResult("$result");
                mOperator = Operator.MULTI;
            }
            102->{
                //减法
                if(mOperator == Operator.SUB)
                {
                    return;
                }
                if(mNowNum == "" && tag in 100..103)
                {
                    mOperator = Operator.SUB;
                    return;
                }
                val  result = compute();
                setResult("$result");
                mOperator = Operator.SUB;
            }
            103->{
                //加法
                if(mOperator == Operator.ADD)
                {
                    return;
                }
                if(mNowNum == "" && tag in 100..103)
                {
                    mOperator = Operator.ADD;
                    return;
                }
                val  result = compute();
                setResult("$result");
                mOperator = Operator.ADD;
            }
        }

    }

    override fun afterTextChanged(p0: Editable?) {
        var avaiW = mScreenW - resultTV.paddingLeft - resultTV.paddingRight - Util.dpToPIX(this,20.0f) - 10 ;

        val paint = TextPaint(resultTV.getPaint());
        val content = p0.toString();
        if (avaiW <= 0) {
            return;
        }
        while (paint.measureText(content) > avaiW)
        {
            mFontPxSize -- ;
            paint.textSize = mFontPxSize;
        }

        paint.textSize = mMaxFontPxSize;
        if(paint.measureText(content) < avaiW)
        {
            resultTV.setTextSize(TypedValue.COMPLEX_UNIT_PX,mMaxFontPxSize);
            mFontPxSize = mMaxFontPxSize;
        }else
        {
            while (paint.measureText(content) < avaiW * 0.8)
            {
                mFontPxSize ++ ;
                paint.textSize = mFontPxSize;
            }
            resultTV.setTextSize(TypedValue.COMPLEX_UNIT_PX,mFontPxSize);
        }
        Log.i("hh","result:$content");

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }


    override fun onDestroy() {
        super.onDestroy()

    }


}


