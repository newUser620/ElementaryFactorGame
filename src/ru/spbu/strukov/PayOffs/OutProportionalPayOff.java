/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spbu.strukov.PayOffs;

import java.util.ArrayList;
import ru.spbu.strukov.CalculateIncome;
import ru.spbu.strukov.Coalition;
import ru.spbu.strukov.Gamer;

/**
 * Дележ, пропорциональный вкладу каждого игрока в коалицию, а именно,
 * пропорционально потери в выигрыше коалиции при выходе игрока из неё. При
 * таком дележе выигрыш отдельного игрока может быть отрицательным, если его
 * выход из коалиции увеличит её выигрыш.
 *
 * Created on 02.12.2018 7:03:11
 *
 * @author Alexander Mikhailovich Kovshov
 */
public class OutProportionalPayOff implements PayOff {

    @Override
    public void calculateGamersPie(Coalition coalition) {
        for (Gamer gamer : coalition.gamers) {
            ArrayList<Gamer> minusGamers = new ArrayList<>(coalition.gamers);//создаем новый массив игроков на основе предыдущего
            minusGamers.remove(gamer);//убираем текущего игрока gamer (так пройдемся по всем игрокам изначального массива)
            Coalition minusGamerCoalition
                    = //new Coalition(minusGamers);//создаем новую коалицию без текущего игрока gamer
                    Coalition.makeTemporaryCoalition(minusGamers);
            CalculateIncome.calculateIncome(minusGamerCoalition);//считаем выигрыш новой коалиции без игрока gamer
            gamer.setRate(coalition.getIncome() - minusGamerCoalition.getIncome());
        }
        double sumRate = 0;
        //Что делать, если суммарный рейтинг оказажется нулевым?
        //Можно просто не учитывать игроков с отрицательным рейтингом 
        //и ничего им не давать.
        for (Gamer gamer : coalition.gamers) {
            if (gamer.getRate() > 0)
            sumRate += gamer.getRate();
        }
//        if (coalition.name == -1) {//Если коалиция временная, то меняем pie только в самой коалиции
//            for (Gamer gamer : coalition.gamers) {
//                coalition.setPies(gamer, (double) coalition.getIncome() * gamer.getRate() / sumRate);
//            }
//        } else {//если постоянная, то меняем pie самого игрока
//            for (Gamer gamer : coalition.gamers) {
//                gamer.setPie((double) coalition.getIncome() * gamer.getRate() / sumRate);
//            }
//        }
        
        for (Gamer gamer : coalition.gamers) {                                  //Выигрыши игроков в коалиции
            coalition.setPies(gamer, 
                    gamer.getRate() <= 0 ? 0 :
                        (double) coalition.getIncome() * gamer.getRate() / sumRate);             
        }
        if (coalition.name != -1) {                                             //Если коалиция НЕ временная, то меняем pie игрока
            for (Gamer gamer : coalition.gamers) {
                gamer.setPie(coalition.getGamerPie(gamer));                       //Доля игрока в выигрыше коалиции.
            }
        }

    }

}
