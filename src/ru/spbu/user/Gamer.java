package ru.spbu.user;

import java.util.Locale;

public class Gamer extends NumberOwner{

    private final int ownNumber;
    Coalition coalition;//ссылка на коалицию, в которой состоит игрок
    double rate;
    double pie;//выигрыш, который получит игрок, находясь в коалиции
//    private Game game;
    double offerPie;
//    boolean decision;// true -> если согласен на изменение состава коалиции
                     // false -> если против
    
    /**
     * Contructor for gamer
     * @param i his own number and serial number
     * @param game 
     */
    Gamer(int i, Game game) {//конструктор. Изначально у каждого игрока собственное число = его номеру
        ownNumber = i;
        calculateIncome();
        pie = getIncome();
    }
    
    /**
     * Calculates income
     */
    private void calculateIncome() {
        CalculateIncome.calculateIncome(this);
    }

    /**
     * для вывода
     * @return string
     */
    @Override
    public String toString(){
        return "[" + ownNumber + "_" + String.format(Locale.UK, "%5.2f", (double)pie) +"(" + coalition.name + ")" +"]";
    }
    
    /**
     * Отладочный вывод для игроков, не имеющих ссылку на коалицию.
     * @return 
     */
    public String toShortString(){
        return "[" + ownNumber + "_" + pie +"]";
    }

    /**
     * Takes own number
     * @return own number
     */
    @Override
    public int getNumber() {//получаем собственное число
        return ownNumber;
    }

    /**
     * Gets income
     * @return income 
     */
    @Override
    double getProfit() {
        return pie;
    }
    
    /**
     * clears offers
     */
    @Override
    void clearOffer() {
        offer = null;
        offerPie = 0;
    }
    
    public void setPie(double pie){
        this.pie = pie;
    }
    
    public void setRate(double rate){
        this.rate = rate;
    }
    
    public double getRate(){
        return this.rate;
    }

    public double getPie() {
        return pie;
    }
    
}
