/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spbu.user.PayOffs;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import ru.spbu.user.CalculateIncome;
import ru.spbu.user.Coalition;
import ru.spbu.user.Gamer;
import ru.spbu.user.Permutations.PermutationGenerator;
import ru.spbu.user.Permutations.Permutations;

public class ShapleyPayOff implements PayOff {

//    ArrayList<Gamer> copyGamers = new ArrayList<>();
    HashMap<Gamer, Double> sumPies;

    @Override
    public void calculateGamersPie(Coalition coalition) {
        if (coalition.gamers.size() == 1) {
            Gamer gamer = coalition.gamers.get(0);
            CalculateIncome.calculateIncome(gamer);
//            if(coalition.name == -1){//если коалиция временная и состоит из одного игрока
//                                     //то нужно явно назначить pie в коалиции для данного игрока
//                coalition.setPies(gamer, gamer.getIncome());
//            }
            coalition.setPies(gamer, gamer.getIncome());//Eсли коалиция состоит из одного игрока
                                                        //то нужно явно назначить pie в коалиции для данного игрока.
            return;
            
        }
        
        sumPies = new HashMap<>(coalition.gamers.size());
        for (Gamer gamer : coalition.gamers) {
            sumPies.put(gamer, new Double(0));
        }
        
        //создаем n! штук перестановок игроков
        Permutations<Gamer> permutations = new Permutations<>(coalition.gamers);
        BigInteger numberOfCanges = PermutationGenerator.getFactorial(coalition.gamers.size());
        
        //считаем рейтинг каждого игрока
        calculateGamersRate(permutations, numberOfCanges);
        
//        if (coalition.name == -1) {//Если коалиция временная, то меняем pie только в самой коалиции
//            for (Gamer gamer : coalition.gamers) {
//                coalition.setPies(gamer, sumPies.get(gamer) / numberOfCanges.doubleValue());
//            }
//        } else{//если постоянная, то меняем pie самого игрока
//            for (Gamer gamer : coalition.gamers) {
//                gamer.setPie(sumPies.get(gamer) / numberOfCanges.doubleValue());//Double.parseDouble(numberOfCanges);
//            }
//        }
        
        for (Gamer gamer : coalition.gamers) {                                  //Выигрыши игроков в коалиции
            coalition.setPies(gamer, sumPies.get(gamer) / numberOfCanges.doubleValue());           
        }
        if (coalition.name != -1) {                                             //Если коалиция НЕ временная, то меняем pie игрока
            for (Gamer gamer : coalition.gamers) {
                gamer.setPie(coalition.getGamerPie(gamer));                     //Доля игрока в выигрыше коалиции.
            }
        }
    }

    
    public void calculateGamersRate(Permutations<Gamer> permutations, BigInteger numberOfChanges) {
        
        for (List<Gamer> permutation : permutations) {
            Coalition coalition = null;
            for (int i = 0; i < permutation.size(); i++) {
                Gamer gamer = permutation.get(i);
                if (i == 0) {//инициализируем новую коалицию
                    sumPies.put(gamer, sumPies.get(gamer) + gamer.getIncome());
                    coalition = Coalition.makeTemporaryCoalition(gamer);
                    CalculateIncome.calculateIncome(coalition);
                } else {
                    int beforeAddingGamerIncome = coalition.getIncome();
                    coalition.gamers.add(gamer);
                    CalculateIncome.calculateIncome(coalition);
                    sumPies.put(gamer, sumPies.get(gamer) + coalition.getIncome() - beforeAddingGamerIncome);
                }
            }
            
        }
    }

}
