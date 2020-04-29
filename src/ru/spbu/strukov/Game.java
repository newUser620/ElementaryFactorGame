package ru.spbu.strukov;

import ru.spbu.strukov.PayOffs.PayOff;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Main class of Elementary Factor Game.
 *
 * Created on 21.11.2018 14:55:07
 *
 * @author Alexander Mikhailovich Kovshov
 */
public class Game {

    /**
     * Variables that set options of the game
     */
    static boolean canGamersMakeOffers = true;//false;
    static boolean ADD_GAMER = true;
    static boolean OUT_GAMER = false;
    TypeOfDecision decision;

    /**
     * coalitions and gamers contains ALL coalitions and gamers of the game
     * payoff is used in eliminateGamerFromCoalition method and later
     */
    ArrayList<Coalition> coalitions;//Объявляем хранилища коалиций и игроков
    ArrayList<Gamer> gamers;
    PayOff payoff;
    HashMap<Gamer, NumberProfit> bestCoalitionForGamer;
    boolean isLargeInitialCoalition;

    /**
     * Constructor that creates object with separate coalitions.
     *
     * @param n
     * @param payoff
     */
    Game(int n, PayOff payoff) {//переопределяем конструктор строка 70
        this(n, payoff, false);
    }

    /**
     * Constructor that creates object with large coalition or separate
     * coalitions.
     *
     * @param n
     * @param payoff
     * @param large If true --- large coalition will be created, else --- each
     * gqmer in separate coalition.
     */
    Game(int n, PayOff payoff, boolean large) {//задаем параметры игры
        this(n, payoff, large, TypeOfDecision.PERSONAL);
        
    }
    Game(int n, PayOff payoff, boolean large, TypeOfDecision decision) {//задаем параметры игры
        this.payoff = payoff;
        this.decision = decision;
        coalitions = new ArrayList<>(n);//игроков будет n
        gamers = new ArrayList<>(n);//коалиций будет n
        for (int i = 1; i <= n; i++) {//создаем n игроков и сразу же добавляем их в массив игроков gamers
            gamers.add(new Gamer(i, this));//this - ссылка на эту игру
        }
        isLargeInitialCoalition = large;
        createInitialCoalitions(large);
    }

    /**
     * Creates initial coalitions at the begining of the game. Looks for best
     * possible coalition for each gamer.
     *
     * @param large is responsible for creating coalition of ALL gamers and
     * coalitions. If true creates large coalition of all gamers. If false
     * creates separate coalition for each gamer.
     */
    private void createInitialCoalitions(boolean large) {
        bestCoalitionForGamer = new HashMap<>(gamers.size());
        lookForBestCoalitionsForGamers();
        
        if (!large) {//тут создаем для каждого игрока персональную коалицию
            for (Gamer gamer : gamers) {
                ArrayList g = new ArrayList(1);//массив из одного игрока
                g.add(gamer);
                createAndRregisterNewCoalition(g);//создаем и регестрируем для него коалицию
            }//так для каждого игрока из всего массива gamers
        } else {
            createAndRregisterNewCoalition(new ArrayList(gamers));//создаем и регистрируем глобальную коалицию для всех игроков
        }
    }

    /**
     * Constuctor creates game without param large
     *
     * @param payoff
     * @param ns array of gamers
     */
    Game(PayOff payoff, int... ns) {   //Sample   new Game(payoff, 5, 7, 11, 13)
        this(payoff, false, ns);
    }

    /**
     * Constructor creates game
     *
     * @param payoff
     * @param large
     * @param ns array of gamers
     */
    Game(PayOff payoff, boolean large, int... ns) {
        this(payoff, large, TypeOfDecision.PERSONAL, ns);
    }
    
    Game(PayOff payoff, boolean large, TypeOfDecision decision, int... ns) {
        this.decision = decision;
        this.payoff = payoff;
        coalitions = new ArrayList<>(ns.length);
        gamers = new ArrayList<>(ns.length);
        for (int i = 0; i < ns.length; i++) {
            gamers.add(new Gamer(ns[i], this));
        }
        isLargeInitialCoalition = large;
        createInitialCoalitions(large);
    }

    /**
     * Creates and registers new coalition from ArayList of gamers.
     *
     * @param gamerList (type: ArrayList) adds to new coalition
     */
    private void createAndRregisterNewCoalition(ArrayList<Gamer> gamerList) {//создаем и регестрируем коалиции из массива игроков gamerList.
        //Gamer's pie set to value from old coalition.
//        for (Gamer gamer : gamerList) {
//            if (gamer.coalition == null || gamer.coalition.gamers.size() < 1) {
//                gamer.pie = gamer.getIncome();
//            } else {
//                gamer.pie = gamer.coalition.pies.get(gamer);
//            }
//        }
        Coalition coalition = Coalition.makeCoalition(gamerList);
        CalculateIncome.calculateIncome(coalition);
        payoff.calculateGamersPie(coalition);//Это вычисление доли каждого игрока в коалиции согласно дележу.
//        coalition.setPies();
        coalitions.add(coalition);//типа регистрируем
    }

    /**
     * Creates and registers new coalition from creates new ArrayList from a
     * fiew gamerArray
     *
     * @param gamerArray (type: Array) adds to new coalition
     */
    private void createAndRregisterNewCoalition(Gamer... gamerArray) {//если несколько игроков
        ArrayList<Gamer> gamerList = new ArrayList<>(gamerArray.length);
        for (int i = 0; i < gamerArray.length; i++) {
            gamerList.add(gamerArray[i]);
        }
        createAndRregisterNewCoalition(gamerList);
    }

    void play() {//запускаем игру
        for (int i = 0; i < 20; i++) { //Петля по ходам игры.
            //make coalitions
            clearOffers();
            //Делаем предложения
            makeOffers();
            //вывод
            for (Gamer gamer : gamers) {
                gamer.pie = gamer.coalition.pies.get(gamer);
                System.out.println(gamer + "->" + gamer.offer);
            }
            for (Coalition coalition : coalitions) {
                System.out.println(coalition + "->" + coalition.offer);
            }
            //Принимаем (не принимаем) предложения
            processOffers();
            //вывод
            int sumCoal = 0;//суммарный выигрыш всех коалиций
            for (Coalition coalition : coalitions) {
                sumCoal += coalition.getIncome;
            }
            int sumGamr = 0;//суммарный выигрыш всех игроков
            for (Gamer gamer : gamers) {
                sumGamr += gamer.getIncome;
            }
            int minLastChange = 1000;
            for (Coalition coalition : coalitions) {
                if (coalition.lastChange < minLastChange) {
                    minLastChange = coalition.lastChange;
                }
                coalition.lastChange++;
            }
            if (minLastChange > 5) {
                break;
            }
            System.out.println(" ------------------------------------------------------- " 
                    + i + " # " + sumCoal + " ~ " + sumGamr);
        }
        System.out.println("===============================  Game over ==============================");
    }

    /**
     * This method clears offers for everyone
     */
    void clearOffers() {//чистим предложения и у коалиций и у игроков
        for (Coalition coalition : coalitions) {
            coalition.clearOffer();
        }
        for (Gamer gamer : gamers) {
            gamer.clearOffer();
        }
    }

    /**
     * Considerates all possible coalitions and finds the best coalition for
     * each gamer having given allocation.
     */
    private void lookForBestCoalitionsForGamers() {
        if (!bestCoalitionForGamer.isEmpty()) {
            bestCoalitionForGamer.clear();     //На всякий случай. For any case.
        }
        for (Gamer gamer : gamers) {
            bestCoalitionForGamer.put(gamer, new NumberProfit());
        }
        int m = 0x1 << gamers.size();
        for (int k = 1; k < m; k++) {
            Coalition tempCoalition = Coalition.makeTemporaryCoalitionByItsOrderNumber(k, gamers);
            CalculateIncome.calculateIncome(tempCoalition);
//            for (Gamer gamer : tempCoalition.gamers) {
//                gamer.pie = gamer.getIncome();
//            }
            payoff.calculateGamersPie(tempCoalition);
            for (Gamer gamer : tempCoalition.gamers) {
                if (tempCoalition.getGamerPie(gamer) > bestCoalitionForGamer.get(gamer).pie) {
                    NumberProfit numCoalPie = bestCoalitionForGamer.get(gamer);
                    numCoalPie.coalitionNumber = k;
                    numCoalPie.pie = tempCoalition.getGamerPie(gamer);
                }
            }
        }
    }

    /**
     * This method gives offers
     */
    void makeOffers() {
        //Gamers make offers.
        if (canGamersMakeOffers) {
            for (Gamer gamer : gamers) {
                //Если игрок один в коалиции, то нет смысла искать союзников,
                //за него это сделает его собственная коалиция.
                if (gamer.coalition.gamers.size() == 1) {
                    continue;
                }
                if (gamer.coalition.lastChange <= 1) {
                    continue;
                }
                //Look for coalition. Игрок ищет сначала коалиции, к которым присоединится
                for (Coalition coalition : coalitions) {
                    //Own coalition.
                    if (coalition == gamer.coalition) {
                        //Is out of coalition better?
                        if (gamer.coalition.getGamerPie(gamer)
                             //   gamer.pie 
                                > gamer.getIncome) {
                            continue;
                        }
                        if (gamer.coalition.lastChange <= 1) {
                            continue;
                        }
                        gamer.offer = coalition;
                        gamer.offerPie = gamer.getIncome;
                    } //External coalition.
                    else {
                        if (coalition.lastChange > 1) {
                            boolean przn = true;
                            for (Gamer gamer1 : coalition.gamers) {
                                if (gamer1.offer != null) {
                                    przn = false;
                                }
                            }
                            if (przn == true) {
                                Coalition plusCoalition = checkForOffer(coalition, gamer, ADD_GAMER);
                                if (decision == TypeOfDecision.AVERAGE) {

                                    if (plusCoalition.offerAvIncome <= gamer.pie) {
                                        continue;
                                    }
                                    if (plusCoalition.offerAvIncome <= gamer.offerPie) {
                                        continue;
                                    }
                                    gamer.offer = coalition;
                                    gamer.offerPie = plusCoalition.offerAvIncome;
                                } else if (decision == TypeOfDecision.PERSONAL) {
                                    if (plusCoalition.getGamerPie(gamer) <= gamer.coalition.getGamerPie(gamer)) {
                                        continue;
                                    }
                                    if (plusCoalition.getGamerPie(gamer) <= gamer.offerPie) {
                                        continue;
                                    }
                                    gamer.offer = coalition;
                                    gamer.offerPie = plusCoalition.getGamerPie(gamer);
                                }
                            }
                        }
                    }
                }
                //Look for gamer. Теперь ищет игроков, к которым может присоединиться
                for (Gamer otherGamer : gamers) {
                    if (gamer.coalition.gamers.size() == 1) {
                        continue;
                    }
                    if (gamer == otherGamer) {
                        continue;
                    }
                    //Own coalition
                    if (otherGamer.coalition == gamer.coalition //Both players in the same coalition
                            && gamer.coalition.gamers.size() == 2) //and only two players in the coalition.
                    {
                        continue;
                    }
                    //Is new coalition is better?
                    Coalition gamerPlusGamer = checkForOffer(gamer, otherGamer);
                    if (decision == TypeOfDecision.AVERAGE) {

                        if (gamerPlusGamer.offerAvIncome <= gamer.pie) {
                            continue;
                        }
                        if (gamerPlusGamer.offerAvIncome <= gamer.offerPie) {
                            continue;
                        }
                        gamer.offer = otherGamer;
                        gamer.offerPie = gamerPlusGamer.offerAvIncome;
                    } else if (decision == TypeOfDecision.PERSONAL) {
                        if (gamerPlusGamer.getGamerPie(gamer) <= gamer.coalition.getGamerPie(gamer)) {
                            continue;
                        }
                        if (gamerPlusGamer.getGamerPie(gamer) <= gamer.offerPie) {
                            continue;
                        }
                        gamer.offer = otherGamer;
                        gamer.offerPie = gamerPlusGamer.getGamerPie(gamer);
                    }
                }
            }
        }
        //Coalitions make offers.
        for (Coalition coalition : coalitions) {
            //Look for gamer.
            for (Gamer gamer : gamers) {
                if (gamer.coalition.gamers.size() == 1) {
                    continue;
                }
                //Coalition contains gamer
                if (coalition.gamers.contains(gamer)) {
                    Coalition minusCoalition = checkForOffer(coalition, gamer, OUT_GAMER);
                    if (decision == TypeOfDecision.AVERAGE) {

                        if (minusCoalition.offerAvIncome < (double) coalition.getIncome / coalition.gamers.size()) {
                            continue;
                        }
                        if (minusCoalition.offerAvIncome <= coalition.offerAvIncome) {
                            continue;
                        }
                        coalition.offer = gamer;
                        coalition.offerAvIncome = minusCoalition.offerAvIncome;
                    } else if (decision == TypeOfDecision.PERSONAL) {
                        if (makeDecision(coalition, minusCoalition) <= 0) {
                            continue;
                        }
                        if (makeDecision(coalition, coalition.offer, minusCoalition) <= 0) {
                            continue;
                        }
                        coalition.offer = gamer;
                    }
                } else {//External gamer
                    Coalition plusCoalition = checkForOffer(coalition, gamer, ADD_GAMER);
                    if (decision == TypeOfDecision.AVERAGE) {

                        if (plusCoalition.offerAvIncome <= (double) coalition.getIncome / coalition.gamers.size()) {
                            continue;
                        }
                        if (plusCoalition.offerAvIncome <= gamer.pie) //Коалиция не хочет рисковать, приглашая богатого игрока  !!!
                        {
                            continue;                                  //он может отказаться. Лучше пригласить победннее, кто не откажется.
                        }
                        coalition.offer = gamer;
                        coalition.offerAvIncome = plusCoalition.offerAvIncome;
                    } else if (decision == TypeOfDecision.PERSONAL) {
                        if (makeDecision(coalition, plusCoalition) <= 0) {
                            continue;
                        }
                        if (makeDecision(coalition, coalition.offer, plusCoalition) <= 0) {
                            continue;
                        }
                        coalition.offer = gamer;
                    }
                }
            }
            //Look for coalition.
            for (Coalition otherCoalition : coalitions) {
                if (otherCoalition == coalition) {
                    continue;
                }
                ArrayList<Gamer> unitedGamers = new ArrayList<>(coalition.gamers);//объединяем всех геймеров двух коалиций
                unitedGamers.addAll(otherCoalition.gamers);
                Coalition unitedTemporaryCoalition = Coalition.makeTemporaryCoalition(unitedGamers);//для них создаем общую коалицию
                CalculateIncome.calculateIncome(unitedTemporaryCoalition);
                if (decision == TypeOfDecision.AVERAGE) {

                    unitedTemporaryCoalition.offerAvIncome
                            = (double) unitedTemporaryCoalition.getIncome / unitedTemporaryCoalition.gamers.size();//считаем относительную прибыль
                    if (unitedTemporaryCoalition.offerAvIncome < coalition.offerAvIncome) {
                        continue;
                    }
                    if (unitedTemporaryCoalition.offerAvIncome
                            <= ((double) coalition.getIncome / coalition.gamers.size())) {
                        continue;
                    }
                    //Тут Вопрос, нужно ли это???????????
                    if (unitedTemporaryCoalition.offerAvIncome < otherCoalition.offerAvIncome) {
                        continue;
                    }
                    if (unitedTemporaryCoalition.offerAvIncome
                            <= ((double) otherCoalition.getIncome / otherCoalition.gamers.size())) {
                        continue;
                    }
                    //Если уже кого-то коалиция хотела пригласить, а рассматриваемая другая коалиция
                    //даст такой же средний выигрыш на игрока, как и рассмотренная раннее,
                    //то выбор делается в пользу того, у кого меньше средний выигрыш на предыдущем шаге (сейчас).
                    //У такой коалиции (игрока) меньше вероятность получить более выгодное предложение.
                    if (unitedTemporaryCoalition.offerAvIncome == coalition.offerAvIncome) {
                        try {
                            double avIncome = coalition.offer instanceof Coalition
                                    ? coalition.offer.getIncome
                                    / ((Coalition) coalition.offer).gamers.size()
                                    : coalition.offer instanceof Gamer
                                            ? ((Gamer) coalition.offer).pie : 0;
                            if ((double) otherCoalition.getIncome / otherCoalition.gamers.size()
                                    > avIncome) {
                                continue;
                            }
                        } catch (ArithmeticException e) {
                            e.printStackTrace(System.err);
                            System.out.println("BAD Coalition! " + coalition.offer.toString());
                        }
                    }
                    coalition.offer = otherCoalition;
                    coalition.offerAvIncome = unitedTemporaryCoalition.offerAvIncome;
                } else if (decision == TypeOfDecision.PERSONAL) {
//                    payoff.calculateGamersPie(unitedTemporaryCoalition);
//                    unitedTemporaryCoalition.setPies();
                    if (makeDecision(coalition, unitedTemporaryCoalition) <= 0) {
                        continue;
                    }
                    if (makeDecision(coalition, coalition.offer, unitedTemporaryCoalition) <= 0) {
                        continue;
                    }
//                    if (makeDecision(otherCoalition, unitedTemporaryCoalition) <= 0) {
//                        continue;
//                    }
//                    if (makeDecision(otherCoalition, otherCoalition.offer, unitedTemporaryCoalition) <= 0) {
//                        continue;
//                    }
                    coalition.offer = otherCoalition;
                }
            }
        }
        //скорее всего лишняя операция
        for (Coalition coalition : coalitions) {
            payoff.calculateGamersPie(coalition);
        }
    }

    /**
     * Creates temporary coalition to check possible offer. Temporary coalition
     * can be created by joining a gamer to an initial coalition or by
     * elimination a gamer from an initial coalition.
     *
     * @param coalition Initial coalition.
     * @param gamer Gamer to join or eliminate.
     * @param addGamer If true, gamer will be joined, else gamer will be
     * eliminated.
     * @return Temporary coalition.
     */
    private Coalition checkForOffer(Coalition coalition, Gamer gamer, boolean addGamer) {
        ArrayList<Gamer> changedGamers = new ArrayList<>(coalition.gamers);
        if (addGamer) {
            changedGamers.add(gamer);
        } else {
            changedGamers.remove(gamer);
        }
        Coalition changedTemporaryCoalition = Coalition.makeTemporaryCoalition(changedGamers);
        return checkForOffer(changedTemporaryCoalition);
    }

    private Coalition checkForOffer(Gamer gamer1, Gamer gamer2) {
        Coalition pairCoalition = Coalition.makeTemporaryCoalition(gamer1, gamer2);
        return checkForOffer(pairCoalition);
    }

    private Coalition checkForOffer(Coalition coalition) {
        CalculateIncome.calculateIncome(coalition);
        coalition.offerAvIncome
                = (double) coalition.getIncome / coalition.gamers.size();
        payoff.calculateGamersPie(coalition);
//        coalition.setPies();
        return coalition;
    }

    private int makeDecision(Coalition coalition, NumberOwner participant, Coalition tempCoalition) {

        if (participant == null) {
            return 1;
        }
        Coalition lastCoalition = null;
        if (participant instanceof Coalition) {
            ArrayList<Gamer> unitedGamers = new ArrayList<>(coalition.gamers);
            unitedGamers.addAll(((Coalition) participant).gamers);
            lastCoalition = Coalition.makeTemporaryCoalition(unitedGamers);
            CalculateIncome.calculateIncome(lastCoalition);
            payoff.calculateGamersPie(lastCoalition);
//            lastCoalition.setPies();
            return makeDecision(lastCoalition, tempCoalition);
        } else {
            if (coalition.gamers.contains((Gamer) participant)) {
                lastCoalition = checkForOffer(coalition, (Gamer) participant, OUT_GAMER);
            } else {
                lastCoalition = checkForOffer(coalition, (Gamer) participant, ADD_GAMER);
            }
            return makeDecision(lastCoalition, tempCoalition);
        }
    }

    private int makeDecision(Coalition coalition, Coalition tempCoalition) {
        payoff.calculateGamersPie(tempCoalition);
//        tempCoalition.setPies();
        int checkOffer = 0;
        if (coalition.gamers.size() < tempCoalition.gamers.size()) {//выбираем, по какому массиву игроков пройтись, чтобы не выйти за границы

            for (Gamer gamer : coalition.gamers) {
                if (!tempCoalition.gamers.contains(gamer)) {//без этого не работает 
                                                            //return new Game(new ShapleyPayOff(), true, 5, 8, 13);
                    continue;
                }
                if (tempCoalition.getGamerPie(gamer) > coalition.getGamerPie(gamer)) {
                    checkOffer++;
                } else {
                    checkOffer--;
                }
            }
        } else {

            for (Gamer gamer : tempCoalition.gamers) {
                if (!coalition.gamers.contains(gamer)) {
                    continue;
                }
                if (tempCoalition.getGamerPie(gamer) > coalition.getGamerPie(gamer)) {
                    checkOffer++;
                } else {
                    checkOffer--;
                }
            }
        }
        payoff.calculateGamersPie(coalition);//скорее всего лишняя операция

        return checkOffer; //если tempCoalition не устраивает в обоих случаях
    }

    /**
     * This method provides the process of accepting or cancelling offers
     */
    private void processOffers() {
        for (Gamer gamer : gamers) {
            if (gamer.offer == null) {
                continue;
            }
            if (gamer.offer instanceof Coalition
                    && ((Coalition) gamer.offer).gamers.contains(gamer)) {
                //Gamer separation.             //Разве для выхода игрока из коалиции требуется согласие коалиции ???
                                                // No. I've already fixed this
                separateGamer(gamer);                                       //Игрок уходит из коалиции.
                continue;
            }else if (gamer.offer.offer == gamer) {                                   //Согласие обоюдное.
                if (gamer.offer instanceof Gamer) {
                    joinGamerToGamer(gamer, (Gamer) gamer.offer);               //Создаётся парная коалиция.
                } else {
                    joinGamerToCoalition(gamer, (Coalition) gamer.offer);       //Переход игрока в другую коалицию.
                }
            }
        }
        for (int i = 0; i < coalitions.size(); i++) {
            Coalition coalition = coalitions.get(i);
            if (coalition.offer == null) {
                continue;
            }
            if (coalition.offer instanceof Gamer
                    && coalition.gamers.contains((Gamer) coalition.offer)) {
                //Gamer separation.
                separateGamer((Gamer) coalition.offer);                         //Коалиция выгоняет игрока вне зависимости от его согласия.
                continue;
            } else if (coalition.offer.offer == coalition) {                      //Согласие обоюдное.
                if (coalition.offer instanceof Gamer) {
                    joinGamerToCoalition((Gamer) coalition.offer, coalition);   //Игрок входит в коалицию
                } else {                                                        //Коалиция присоединяет коалицию.
                    joinCoalitionToCoalition(coalition, (Coalition) coalition.offer);
                }
            }
        }
    }

    /**
     * Gamers decided to make coalition with each other
     *
     * @param gamer current gamer
     * @param offer an offer for current gamer
     */
    private void joinGamerToGamer(Gamer gamer, Gamer offer) {
        if (gamer.coalition.gamers.size() == 1) { //Impossible.
            joinGamerToCoalition(offer, gamer.coalition);
            return;
        }
        if (offer.coalition.gamers.size() == 1) { //Impossible.
            joinGamerToCoalition(gamer, offer.coalition);
            return;
        }
//        eliminateGamerFromCoalition(gamer);
//        eliminateGamerFromCoalition(offer);
        eliminateGamerFromCoalition(gamer,offer);
        createAndRregisterNewCoalition(gamer, offer);
    }

//    /**
//     * Убирает игрока из этой коалиции.
//     * Ссылка на коалицию у игрока сохраняется,
//     * чтобы при создании новой коалиции не сбросить
//     * выигрыш этого игрока в покидаемой коалиции,
//     * учитываемый при дележе, зависящем от прежних 
//     * выигрышей игроков.
//     *
//     * @param gamer Исключаемый игрок.
//     */
//    private void eliminateGamerFromCoalition(Gamer gamer) {
//        gamer.coalition.lastChange = 0;//new code
//        gamer.clearOffer();
//        gamer.coalition.clearOffer();
//        gamer.coalition.gamers.remove(gamer);               //Игрок убирается из перчня игроков коалиции.
//        gamer.coalition.restoreGamerPies();                 //Паям игроков присваиваются их текущие значения (на всякий случай, если паи изменялись временными коалициями).
//        CalculateIncome.calculateIncome(gamer.coalition);   //Вычисляется выигрыш коалиции уже без ушедшего игрока.
//        payoff.calculateGamersPie(gamer.coalition);         //Делёжка выигрыша коалиции между игроками.                
////        gamer.coalition.setPies();                          //Запись в коалицию новых выигрышей игроков. 
////        gamer.coalition = null;                             //Игрок теперь вне всяких коалиций.
////        gamer.pie = gamer.getIncome();                      //Выигрыш вышедшего из коалиции игрока равен его одиночному выигрышу.
//    }

    /**

    /**
     * Убирает игрока из этой коалиции.
     * Ссылка на коалицию у игрока сохраняется,
     * чтобы при создании новой коалиции не сбросить
     * выигрыш этого игрока в покидаемой коалиции,
     * учитываемый при дележе, зависящем от прежних 
     * выигрышей игроков.
     *
     * @param gamers Исключаемые игроки.
     */
    private void eliminateGamerFromCoalition(Gamer... gamers) {
        for (Gamer gamer : gamers) {
            gamer.coalition.lastChange = 0;//new code
            gamer.clearOffer();
            gamer.coalition.clearOffer();
            gamer.coalition.gamers.remove(gamer);               //Игрок убирается из перчня игроков коалиции.
        }
        for (Gamer gamer : gamers) {
//        gamer.coalition.restoreGamerPies();                 //Паям игроков присваиваются их текущие значения (на всякий случай, если паи изменялись временными коалициями).
            CalculateIncome.calculateIncome(gamer.coalition);   //Вычисляется выигрыш коалиции уже без ушедшего игрока.
            payoff.calculateGamersPie(gamer.coalition);         //Делёжка выигрыша коалиции между игроками.                
//        gamer.coalition.setPies();                          //Запись в коалицию новых выигрышей игроков. 
//        gamer.coalition = null;                             //Игрок теперь вне всяких коалиций.
//        gamer.pie = gamer.getIncome();                      //Выигрыш вышедшего из коалиции игрока равен его одиночному выигрышу.
    
        }
    }

    /**
     * Deletes gamer from his own personal coalition
     *
     * @param gamer
     */
    private void separateGamer(Gamer gamer) {
        eliminateGamerFromCoalition(gamer);
//        gamer.coalition = null;                     //Now the gamer is out of any coalition.
        createAndRregisterNewCoalition(gamer);
    }

    /**
     * Adds gamer to coalition
     *
     * @param gamer wants to join
     * @param offer wants to accept gamer
     */
    private void joinGamerToCoalition(Gamer gamer, Coalition offer) {
        gamer.pie = gamer.coalition.pies.get(gamer);
        offer.restoreGamerPies();
        if (gamer.coalition.gamers.size() == 1) { //Impossible.
            joinCoalitionToCoalition(offer, gamer.coalition);
            return;
        }
        eliminateGamerFromCoalition(gamer);
        gamer.coalition = offer;
        offer.lastChange = 0; //new code
        gamer.clearOffer();
        offer.clearOffer();
        offer.gamers.add(gamer);
        CalculateIncome.calculateIncome(offer);
        payoff.calculateGamersPie(offer);
//        offer.setPies();
    }

    /**
     * Two coalitions decided to be together
     *
     * @param namer
     * @param offer
     */
    private void joinCoalitionToCoalition(Coalition namer, Coalition offer) {
        boolean change = false;
        //What coalition will give name to united coalition?
        if (namer.getIncome < offer.getIncome) {
            change = true;
        } else if (namer.getIncome == offer.getIncome && namer.offerAvIncome < offer.offerAvIncome) {
            change = true;
        } else if (namer.name > offer.name) {
            change = true;
        }
        if (change) {
            Coalition t = namer;
            namer = offer;
            offer = t;
        }
        
        namer.restoreGamerPies(); 
        offer.restoreGamerPies();
        
        coalitions.remove(offer);
        namer.gamers.addAll(offer.gamers);
        for (Gamer offerGamer : offer.gamers) {
            offerGamer.coalition = namer;
        }
        namer.clearOffer();
        namer.lastChange = 0;
        CalculateIncome.calculateIncome(namer);
        payoff.calculateGamersPie(namer);
//        namer.setPies();

    }

    public static void main(String[] args) {
        Game game = ForTest.getGame();
//        game.decision = TypeOfDecision.PERSONAL;
        game.play();
        String out = "";
        int sumCoal = 0;
        for (Coalition coalition : game.coalitions) {
            out += coalition + "\r\n";
            sumCoal += coalition.getIncome;
        }
        System.out.println(out);
        int sumGamr = 0;
        for (Gamer gamer : game.gamers) {
            sumGamr += gamer.getIncome;
        }
        System.out.println("______Summary coalitions income ~ Summary single gamers income _______  " + sumCoal + " ~ " + sumGamr + " \r\n");
        System.out.println("=========== Best coalition for each gamer ==================== "
                + game.payoff.getClass().getSimpleName() + " " + (game.isLargeInitialCoalition ? "LARGE" : "single")
                + " =================");
        for (Gamer gamer : game.bestCoalitionForGamer.keySet()) {
            NumberProfit numCoalPie = game.bestCoalitionForGamer.get(gamer);
            String inout = Integer.toBinaryString(numCoalPie.coalitionNumber);
            while(inout.length() < game.gamers.size()) inout ="0" + inout;
            System.out.println(gamer + " ~~~~~~~~~ "
                    + "" + String.format(Locale.US, "%6.2f", numCoalPie.pie) + ";"
                    + "" + String.format(Locale.US, "%4d", gamer.getIncome()) +";"
                    + "" + String.format(Locale.US, "%4d", numCoalPie.coalitionNumber) + "="
                    + inout + ";"
                    + Coalition.makeTemporaryCoalitionByItsOrderNumber(numCoalPie.coalitionNumber, game.gamers)
            );
        }
        System.out.println("\r\n####################################################################" 
                + "#############################################################################\r\n");
    }

}

