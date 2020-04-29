package ru.spbu.strukov.PayOffs;

import ru.spbu.strukov.Coalition;
import ru.spbu.strukov.Gamer;

/**
 * Шлю добавления к коду, которые я сделал для создания нового дележа,
 * который должен побуждать игроков объединяться, если выигрыш новой коалиции
 * превосходит сумму выигрышей прежних коалиций.
 * Разность между новым выигрышем коалиции и суммой старых делится между игроками
 * пропорционально их выигрышам до объединения.
 * Новых полей в класс Gamer я не стал вводить, поскольку Вы уже и так сохраняете выигрыши
 * игроков в классе Coalition. Но мне пришлось перед изменениями в коалициях присваивать
 * полю pie каждого игрока значение его выигрыша, хранимое в его коалиции.
 * В этом случае, при образовании новой коалиции, когда вычисляется доля каждого игрока,
 * поле pie хранит выигрыш игрока до объединения.
 *
 * Created on 01.11.2019 19:12:48
 *
 * @author Alexander Mikhailovich Kovshov
 */
public class PreProportionalExcessPayOff implements PayOff {

    @Override
    public void calculateGamersPie(Coalition coalition) {
        double sumPies = 0;                                                     //Сумма выигрышей игроков 
        for (Gamer gamer : coalition.gamers) {                                  //на предыдущем шаге до изменения 
            sumPies += gamer.getPie();                                          //или создания коалиции.
        }
        double excess = coalition.getIncome() - sumPies;
//        if (coalition.name == -1) {//Если коалиция временная, то меняем pie только в самой коалиции
//            for (Gamer gamer : coalition.gamers) {//если постоянная, то меняем pie самого игрока
//                coalition.setPies(gamer, gamer.getPie() * (1. + excess / sumPies));
//            }
//
//        } else {
//            for (Gamer gamer : coalition.gamers) {//если постоянная, то меняем pie самого игрока
//                gamer.setPie(gamer.getPie() * (1. + excess / sumPies));
//            }
//        }
        
        for (Gamer gamer : coalition.gamers) {                                  //Выигрыши игроков в коалиции                                  //Выигрыши игроков в коалиции
            coalition.setPies(gamer, gamer.getPie() * (1. + excess / sumPies));            
        }
        if (coalition.name != -1) {                                              //Если коалиция НЕ временная, то меняем pie игрока
            for (Gamer gamer : coalition.gamers) {
                gamer.setPie(coalition.getGamerPie(gamer));                     //Доля игрока в выигрыше коалиции.
            }
        }
    }

}
