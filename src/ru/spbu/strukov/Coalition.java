package ru.spbu.strukov;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created on 21.11.2018 15:02:05
 * @author Alexander Mikhailovich Kovshov
 */
public class Coalition extends NumberOwner{
    
    private static int count = 0;
    
    private static final boolean REAL_COALITION = true;
    private static final boolean TEMP_COALITION = false;
    
    public ArrayList<Gamer> gamers;//тут будут лежать геймеры коалиции
    //private Game game;
    public final int name;
    double offerAvIncome;
    double payOffIncome;
    
    public int lastChange = 0;
    
    HashMap<Gamer, Double> pies;//каждому игроку соотеветвует его выигрыш в коалиции
    
    
    public static Coalition makeTemporaryCoalition(Gamer... gamers) {
        return new Coalition(TEMP_COALITION, gamers);
    }
    
    public static Coalition makeTemporaryCoalition(ArrayList<Gamer> gamers) {
        return new Coalition(TEMP_COALITION, gamers);
    }
    
    public static Coalition makeCoalition(Gamer... gamers) {// no occurrences
        return new Coalition(REAL_COALITION, gamers);
    }
    
    public static Coalition makeCoalition(ArrayList<Gamer> gamers) {
        return new Coalition(REAL_COALITION, gamers);
    }
    
    private Coalition(boolean realCoalition, Gamer... gamers) {
        this.gamers = new ArrayList<>(gamers.length);
        for (Gamer gamer : gamers) {
            this.gamers.add(gamer);
        }
        name = realCoalition ? gamersAssign() : -1;
        pies = new HashMap<>();
    }
    
    private Coalition(boolean realCoalition, ArrayList<Gamer> gamers) {
        this.gamers = gamers;
        name = realCoalition ? gamersAssign() : -1;
        pies = new HashMap<>();
    }
    
    /**
     * This method creates links on this coalition for gamers in current coalition
     */
    private int gamersAssign() {
//        name = ++count;
        for (Gamer gamer : gamers) {
            gamer.coalition = this;
        }
        return ++count;
    }
    
    @Override
    public String toString(){
        String out = "";//
        int sum = 0;
        for (Gamer gamer : gamers) {
            out += gamer.toString();
            sum += gamer.getNumber();
        }
        out = "{" + name + ":" +  sum + "_" +  getIncome + ";" + out + " <" + lastChange + ">}";
        return out;
    }

    /**
     * counts an own number of coalition
     * @return own number
     */
    @Override
    public int getNumber() {//собственное число коалиции = сумма собственных чисел ее участникков
        int sum = 0;
        for (Gamer gamer : gamers) {
            sum += gamer.getNumber();
        }
        return sum;
    }

    /**
     * To take an icome
     * @return a value of income
     */
    @Override
    double getProfit() {
        return getIncome;
    }
    
    /**
     * 
     * @return true if all gamers in this coalition does not 
     */
    boolean isSolidary() {
        if ((offer instanceof Gamer) && (gamers.contains((Gamer)offer)) ){
                return false; //Own gamer will be eliminated from this coalition.
        }
        for (Gamer gamer : gamers) {
            if (gamer.offer != null) return false;
        }
        return true;
    }

    /**
     * Clears offers
     */
    @Override
    void clearOffer() {
        offer = null;
        offerAvIncome = 0;
    }
        
    public static Coalition makeTemporaryCoalitionByItsOrderNumber(int n, ArrayList<Gamer> allGamers) {
        ArrayList<Gamer> gamers = new ArrayList<>();
        for (int i = 0, k = 1; i < allGamers.size(); i++, k <<= 1) {
            if ((n & k) != 0) {
                gamers.add(allGamers.get(i));
            }
        }
        return gamers.isEmpty() ? null : makeTemporaryCoalition(gamers);
    }
    
//    public void setPies(){//для постоянной коалиции
//        for (Gamer gamer : gamers) {
//            double pie = gamer.pie;
//            pies.put(gamer, pie);
//        }
//        
//    }
    
    public void setPies(Gamer gamer, double pie){//для временной коалиции
        pies.put(gamer, pie);
    }
    
    public double getGamerPie(Gamer gamer){
        if (pies == null) {System.out.println("C NULL " + this); throw new RuntimeException("P XXXXXXXX");}
        if (gamer == null) {System.out.println("G NULL " + this); throw new RuntimeException("G XXXXXXXX");}
        if (this.pies.get(gamer) == null) {System.out.println("R NULL " + this + "    %     " + gamer); throw new RuntimeException("G XXXXXXXX");}
        return this.pies.get(gamer);
    }
    
    public void restoreGamerPies() {
         for (Gamer gamer : gamers) {
             gamer.pie = pies.get(gamer);
         }
    }
}
