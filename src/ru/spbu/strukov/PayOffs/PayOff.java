package ru.spbu.strukov.PayOffs;

import ru.spbu.strukov.Coalition;

/**
 * Interface of Payoff for everyone
 * 
 * Created on 21.11.2018 15:07:40
 * @author Alexander Mikhailovich Kovshov
 */
public interface PayOff {

    /**
     * Divides the coalition's profit among gamers.
     * @param coalition 
     */
    void calculateGamersPie(Coalition coalition);

}

