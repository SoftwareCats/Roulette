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

import io.github.softwarecats.casino.event.Outcome;
import io.github.softwarecats.roulette.Bet;
import io.github.softwarecats.roulette.Game;
import io.github.softwarecats.roulette.InvalidBetException;
import io.github.softwarecats.roulette.Table;

/**
 * Fibonacci uses the Fibonacci betting system. This player allocates their available budget into a sequence
 * of bets that have an accelerating potential gain.
 */
public class Fibonacci extends Player {

    /**
     * This is the player’s preferred Outcome.
     */
    protected final Outcome BLACK;

    /**
     * This is the most recent bet amount. Initially, this is 1.
     */
    protected int current = 1;

    /**
     * This is the bet amount previous to the most recent bet amount. Initially, this is zero.
     */
    protected int previous = 0;

    /**
     * Initialize the Fibonacci player.
     *
     * @param table the table to use
     */
    public Fibonacci(Table table) {
        super(table);

        BLACK = table.WHEEL.getOutcomes(Game.BET_NAMES.getString("black")).get(0);
    }

    @Override
    public boolean playing() {
        return (current <= stake) && (roundsToGo > 0);
    }

    @Override
    public void placeBets() throws InvalidBetException {
        table.placeBet(new Bet(current, BLACK, this));
    }

    @Override
    public void newRound() {
        current = 1;
        previous = 0;
    }

    /**
     * Uses the superclass method to update the stake with an amount won. It resets recent and previous to their
     * initial values of 1 and 0.
     *
     * @param bet the bet which won
     */
    @Override
    public void win(Bet bet) {
        super.win(bet);

        current = 1;
        previous = 0;
    }

    /**
     * Uses the superclass method to update the stake with an amount lost. This will go “forwards” in the sequence. It
     * updates recent and previous as follows.<p>
     * next -> recent + previous<p>
     * previous -> recent<p>
     * recent -> next<p>
     *
     * @param bet the bet which lost
     */
    @Override
    public void lose(Bet bet) {
        super.lose(bet);

        int next = current + previous;
        previous = current;
        current = next;
    }
}
