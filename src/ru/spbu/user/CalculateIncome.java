/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spbu.user;

import java.util.ArrayList;

public class CalculateIncome {
    
    /**
     * Method CalculateIncome Counts an Income for Gamer or Coalition
     *
     * @param gamerOrCoalition
     */
    public static void calculateIncome(NumberOwner gamerOrCoalition) {
        gamerOrCoalition.setIncome(sumOfElementaryFactors(gamerOrCoalition.getNumber()));
    }

    /**
     * Metod sumOfElementaryFactors calculates an amount of
     *
     * @param number elementary factors
     * @return amount
     */
    static int sumOfElementaryFactors(int number) {
        //сумма всех простых множителей числа number
        ArrayList<Integer> elFactors = elementaryFactors(number); //массив простых множителей числа number
        number = 0;
        for (Integer elFactor : elFactors) {
            number += elFactor;
        }
        return number;
    }

    /**
     * Method elementaryFactors Finds elementary factors of the integer number.
     *
     * @param number The number to split into Prime factors.
     * @return ArrayList Elementary factors.
     */
    static ArrayList<Integer> elementaryFactors(int number) {
        //ищем простые множители числа number
        ArrayList<Integer> ret = new ArrayList<>();
        int rest = number;
        boolean repeat;
        for (;;) {
            repeat = false;
            for (int i = 2; i <= Math.sqrt(rest); i++) {
                //i считается до квадрата искомого числа
                if (rest % i == 0) {
                    //если остаток от деления числа rest на i = 0
                    ret.add(i); //i-простой множитель этого числа, добавим его в массив
                    rest /= i;
                    repeat = true;
                    break;
                }
            }
            if (!repeat) {
                //сюда попадаем, если число rest - стал простым множителем
                ret.add(rest); //добавляем его в массив и выходим из внешнего цикла
                break;
            }
        }
        return ret;
    }
    
}
