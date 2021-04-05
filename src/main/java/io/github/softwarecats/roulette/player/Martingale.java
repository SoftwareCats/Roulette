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

/**
 * Martingale is a Player who places bets in Roulette. This player doubles their bet on every loss and resets their
 * bet to a base amount on each win.
 */
public class Martingale extends Player {

    protected final Outcome BLACK;

    protected int baseBet = Game.TABLE_MINIMUM;

    /**
     * The number of losses. This is the number of times to double the bet.
     */
    protected int lossCount = 0;

    /**
     * The the bet multiplier, based on the number of losses. This starts at 1, and is reset to 1 on each win. It is doubled
     * in each loss. This is always equal to 2 to the power of lossCount.
     */
    protected int betMultiple = 1;

    /**
     * Constructs the Player with a specific Table for placing Bets.
     * Since the table has access to the Wheel, we can use this wheel to extract Outcome objects.
     *
     * @param table the table to use
     */
    public Martingale(Table table) {
        super(table);
        BLACK = table.WHEEL.getOutcomes(Game.BET_NAMES.getString("black")).get(0);
    }

    @Override
    public boolean playing() {
        return (baseBet * betMultiple <= stake) && (roundsToGo > 0);
    }

    /**
     * Updates the Table with a bet on “black”. The amount bet is 2lossCount, which is the value of betMultiple.
     *
     * @throws InvalidBetException if the Player attempts to place a bet which exceeds the table’s limit
     */
    @Override
    public void placeBets() throws InvalidBetException {
        table.placeBet(new Bet(baseBet * betMultiple, BLACK, this));
    }

    @Override
    public void newRound() {
        lossCount = 0;
        betMultiple = 1;
    }

    /**
     * Uses the superclass win() method to update the stake with an amount won. This method then resets loss-
     * Count to zero, and resets betMultiple to 1.
     *
     * @param bet the bet which won
     */
    @Override
    public void win(Bet bet) {
        super.win(bet);
        lossCount = 0;
        betMultiple = 1;
    }

    /**
     * Uses the superclass lose() to do whatever bookkeeping the superclass already does. Increments lossCount
     * by 1 and doubles betMultiple.
     *
     * @param bet the bet which lost
     */
    @Override
    public void lose(Bet bet) {
        super.lose(bet);
        lossCount += 1;
        betMultiple *= 2;
    }
}
