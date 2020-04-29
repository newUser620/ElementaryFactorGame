package ru.spbu.strukov.PayOffs;

import ru.spbu.strukov.Coalition;
import ru.spbu.strukov.Gamer;

/**
 * Делёж, при котором выигрыш коалиции распределяется пропорционально выигрышу
 * каждого игрока в его единичной коалиции.
 *
 * Created on 22.11.2018 2:53:08
 *
 * @author Alexander Mikhailovich Kovshov
 */
public class SingleProportionalPayOff implements PayOff {//тут считаем выигрыш каждого игрока, находящегося в текущей коалиции

    @Override
    public void calculateGamersPie(Coalition coalition) {
        for (Gamer gamer : coalition.gamers) {
            gamer.setRate(gamer.getIncome());//?
        }
        double sumRate = 0;
        for (Gamer gamer : coalition.gamers) {//суммарный выигрыш коалиции
            sumRate += gamer.getRate();
        }
//        if (coalition.name == -1) {
//            for (Gamer gamer : coalition.gamers) {
//                coalition.setPies(gamer, (double) coalition.getIncome() * gamer.getRate() / sumRate);
//            }
//
//        } else {
//            for (Gamer gamer : coalition.gamers) {
//                gamer.setPie((double) coalition.getIncome() * gamer.getRate() / sumRate);
//            }
//        }
        
        for (Gamer gamer : coalition.gamers) {                                  //Выигрыши игроков в коалиции
            coalition.setPies(gamer, (double) coalition.getIncome() * gamer.getRate() / sumRate);           
        }
        if (coalition.name != -1) {                                             //Если коалиция НЕ временная, то меняем pie игрока
            for (Gamer gamer : coalition.gamers) {
                gamer.setPie(coalition.getGamerPie(gamer));                     //Доля игрока в выигрыше коалиции.
            }
        }

    }

}
