/*
 * Copyright © Bowen Wu 2021.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.softwarecats.roulette.player;

import io.github.softwarecats.roulette.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * RandomChoice is a Player who places bets in Roulette. This player makes random bets around the layout.
 */
public class RandomChoice extends Player {

    /**
     * A Random Number Generator which will return the next random number.
     * When writing unit tests, we will want to patch this with a mock object to return a known sequence of bets.
     */
    protected final Random RNG;

    /**
     * All possible outcomes.
     */
    protected final List<Outcome> ALL_OUTCOMES;

    /**
     * The Base bet.
     */
    protected int baseBet = Game.TABLE_MINIMUM;

    /**
     * This uses the super() construct to invoke the superclass constructor using the Table.
     * <p>
     * It will also use the wheel associated with the table to get the set of bins. The set of bins is then used to create the
     * pool of outcomes for creating bets.
     *
     * @param table the Table which will accept the bests
     * @param rng   the random number generator
     */
    public RandomChoice(Table table, Random rng) {
        super(table);
        RNG = rng;
        ALL_OUTCOMES = new ArrayList<>(table.WHEEL.getAllOutcomes().values());
    }

    /**
     * This uses the super() construct to invoke the superclass constructor using the Table.
     * <p>
     * This will create a random.Random random number generator.
     * <p>
     * It will also use the wheel associated with the table to get the set of bins. The set of bins is then used to create the
     * pool of outcomes for creating bets.
     *
     * @param table the Table which will accept the bests
     */
    public RandomChoice(Table table) {
        this(table, new Random());
    }

    @Override
    public boolean playing() {
        return (baseBet <= stake) && (roundsToGo > 0);
    }

    /**
     * Updates the Table with a randomly placed bet.
     *
     * @throws InvalidBetException if the Player attempts to place a bet which exceeds the table’s limit
     */
    @Override
    public void placeBets() throws InvalidBetException {
        Outcome outcomeToBet = ALL_OUTCOMES.get(RNG.nextInt(ALL_OUTCOMES.size()));
        table.placeBet(new Bet(baseBet, outcomeToBet, this));
    }

    @Override
    public void newRound() {

    }
}
