package com.halewang.calculator.bean

/**
 * Created by shinezone on 2017/11/16.
 */
class AddOperator: OperatorInter
{
    override fun getResult(numberA: Double, numberB: Double): Double {
        return numberA + numberB
    }
}