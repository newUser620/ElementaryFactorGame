package ru.spbu.user.PayOffs;

import ru.spbu.user.Coalition;

public interface PayOff {

    /**
     * Divides the coalition's profit among gamers.
     * @param coalition 
     */
    void calculateGamersPie(Coalition coalition);

}

