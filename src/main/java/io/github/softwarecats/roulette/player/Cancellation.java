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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Cancellation uses the cancellation betting system. This player allocates their available budget into a
 * sequence of bets that have an accelerating potential gain as well as recouping any losses.
 */
public class Cancellation extends Player {

    /**
     * This is the player’s preferred Outcome.
     */
    protected final Outcome OUTCOME;

    /**
     * This Deque keeps the bet amounts; wins are removed from this list and losses are appended to this list. The
     * current bet is the first value plus the last value.
     */
    protected Deque<Integer> sequence = new ArrayDeque<>();

    /**
     * This uses the Cancellation.resetSequence() method to initialize the sequence of numbers
     * used to establish the bet amount. This also picks a suitable even money Outcome, for example, black.
     *
     * @param table the table to use
     */
    public Cancellation(Table table) {
        super(table);

        resetSequence();

        OUTCOME = table.WHEEL.getOutcomes(Game.BET_NAMES.getString("black")).get(0);
    }

    @Override
    public boolean playing() {
        return (calcBetAmount() <= stake) && (roundsToGo > 0);
    }

    /**
     * Creates a bet from the sum of the first and last values of sequence and the preferred outcome.
     *
     * @throws InvalidBetException if the Player attempts to place a bet which exceeds the table’s limit
     */
    @Override
    public void placeBets() throws InvalidBetException {
        Bet bet = new Bet(calcBetAmount(), OUTCOME, this);
        table.placeBet(bet);
    }

    @Override
    public void newRound() {
        resetSequence();
    }

    /**
     * Uses the superclass method to update the stake with an amount won. It then removes the fist and last element
     * from sequence.
     *
     * @param bet the bet which won
     */
    @Override
    public void win(Bet bet) {
        super.win(bet);

        sequence.pollFirst();
        sequence.pollLast();
    }

    /**
     * Uses the superclass method to update the stake with an amount lost. It then appends the sum of the first and list
     * elements of sequence to the end of sequence as a new Integer value.
     *
     * @param bet the bet which lost
     */
    @Override
    public void lose(Bet bet) {
        super.lose(bet);

        sequence.add(bet.amountBet);
    }

    /**
     * Puts the initial sequence of six Integer instances into the sequence variable. These Integers are built
     * from the values 1 through 6.
     */
    protected void resetSequence() {
        sequence = new ArrayDeque<>(List.of(1, 2, 3, 4, 5, 6));
    }

    protected int calcBetAmount() {
        if (sequence.size() == 0) {
            resetSequence();
        }

        if (sequence.size() == 1) {
            return sequence.peek();
        } else {
            return (sequence.peekFirst() == null ? 0 : sequence.peekFirst()) +
                    (sequence.peekLast() == null ? 0 : sequence.peekLast());
        }
    }
}
