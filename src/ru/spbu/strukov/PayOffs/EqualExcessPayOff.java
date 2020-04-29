package ru.spbu.strukov.PayOffs;

import ru.spbu.strukov.Coalition;
import ru.spbu.strukov.Gamer;

/**
 * Делёж при котором игроки получают долю, равную сумме их возможного одиночного
 * выигрыша и равной для всех игроков коалиционной премии. Если выигрыш коалиции
 * меньше суммарных одиночных выигрышей игроков, то премия будет отрицательной.
 *
 * Created on 20.03.2019 12:35:33
 *
 * @author Alexander Mikhailovich Kovshov
 */
public class EqualExcessPayOff implements PayOff {

    @Override
    public void calculateGamersPie(Coalition coalition) {
        double excess = 0;
        for (Gamer gamer : coalition.gamers) {
            excess += gamer.getIncome();                                             //Суммарный выигрыш игроков при игре в одиночной коалиции.
        }
        excess -= coalition.getIncome();                                             //Разность выигрыша коалиции и суммарного выигрыша.
        excess /= -coalition.gamers.size();                                     //Доля разности на игрока.
//        if (coalition.name == -1) {//Если коалиция временная, то меняем pie только в самой коалиции
//            for (Gamer gamer : coalition.gamers) {
//                coalition.setPies(gamer, gamer.getIncome() + excess);                                  //Доля игрока в выигрыше коалиции.
//            }
//        } else {//если постоянная, то меняем pie самого игрока
//            for (Gamer gamer : coalition.gamers) {
//                gamer.setPie(gamer.getIncome() + excess);                                  //Доля игрока в выигрыше коалиции.
//            }
//        }
        for (Gamer gamer : coalition.gamers) {                                  //Выигрыши игроков в коалиции
            coalition.setPies(gamer, gamer.getIncome() + excess);               //Доля игрока в выигрыше коалиции.
        }
        if (coalition.name != -1) {                                             //Если коалиция НЕ временная, то меняем pie игрока
            for (Gamer gamer : coalition.gamers) {
                gamer.setPie(coalition.getGamerPie(gamer));                       //Доля игрока в выигрыше коалиции.
            }
        }
    }

}
