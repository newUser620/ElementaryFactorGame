package ru.spbu.strukov;

/**
 * Common class for gamers and coalitions
 * Contains common fields and methods for them
 * Created on 28.11.2018 15:02:32
 * @author Alexander Mikhailovich Kovshov
 */
public abstract class NumberOwner {//общий класс для всех игроков
    
    int getIncome; //сумма простых множителей собстевнного числа
    NumberOwner offer; //ссылочка на игрока или коалицию, предложение которой хотим принять
    
    
    abstract int getNumber();
    
    void setIncome(int income) {this.getIncome = income;}
    
    public int getIncome(){
        return this.getIncome;
    }
    
    NumberOwner getOffer(){return offer;}
    
    abstract double getProfit();
    
    abstract void clearOffer();

    public NumberOwner() {}

}
