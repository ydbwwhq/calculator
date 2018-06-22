package com.halewang.calculator.bean


/**
 * Created by shinezone on 2017/11/16.
 */
class OperatorFactory
{
    companion object {
        fun createOperatorModel(operatorType: Operator): OperatorInter
        {
            when(operatorType)
            {
                Operator.ADD ->{
                    return AddOperator();
                }
                Operator.SUB ->{
                    return SubOperator();
                }
                Operator.MULTI ->{
                    return MultiOperator();
                }
                Operator.DIV ->{
                    return DivOperator();
                }
            }
            return AddOperator();
        }
    }
}