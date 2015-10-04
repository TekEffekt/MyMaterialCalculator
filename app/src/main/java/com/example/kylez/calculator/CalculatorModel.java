package com.example.kylez.calculator;

import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.*;

/**
 * Created by kylez on 10/4/15.
 */
public class CalculatorModel
{
    public ArrayDeque<String> computeQueue;
    String[] opTypesArray = {"x", "/", "+", "-"};
    ArrayList<String> opTypes = new ArrayList<String>(Arrays.asList(opTypesArray));

    public CalculatorModel()
    {
        computeQueue = new ArrayDeque<String>();
    }

    public String computeResult()
    {
        String operand1 = "";
        String operand2 = "";
        String opType = null;
        double result = 0;

        try
        {
            while(computeQueue.size() > 0)
            {
                String element = computeQueue.removeFirst();

                if(!opTypes.contains(element) && opType == null)
                {
                    operand1 = operand1 + element;
                    continue;
                } else if(opType == null)
                {
                    opType = element;
                    continue;
                }

                if(!opTypes.contains(element) && opType != null)
                {
                    operand2 = operand2 + element;
                    continue;
                } else if(opType != null)
                {
                    result = performOperation(Double.parseDouble(operand1), Double.parseDouble(operand2), opType);
                    opType = null;
                    operand2 = "";
                    operand1 = result+"";
                    computeQueue.addFirst(element);
                    continue;
                }
            }

            if(operand1.length()>=1 && operand2.length() >=1 && opType != null)
            {
                result = performOperation(Double.parseDouble(operand1), Double.parseDouble(operand2), opType);
            }

            DecimalFormat format = new DecimalFormat("0.#");

            return format.format(result);
        } catch(Exception ex)
        {
            return "Error";
        }
    }

    private double performOperation(double operand1, double operand2, String opType)
    {
        double result = 0;

        switch (opType)
        {
            case "x":
                result = operand1 * operand2; break;
            case "/":
                result = operand1 / operand2; break;
            case "+":
                result = operand1 + operand2; break;
            case "-":
                result = operand1 - operand2; break;
        }

        return result;
    }

}
