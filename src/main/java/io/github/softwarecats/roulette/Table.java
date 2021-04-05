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

package io.github.softwarecats.roulette;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Table contains all the Bet s created by the Player. A table also has a betting limit, and the sum of all of a player’s
 * bets must be less than or equal to this limit. We assume a single Player in the simulation.
 */
public class Table {

    public final Wheel WHEEL;

    /**
     * This is a list of the Bets currently active. These will result in either wins or losses to the Player.
     */
    private final LinkedList<Bet> BETS = new LinkedList<>();

    /**
     * This is the table limit. The sum of the bets from a Player must be less than or equal to this limit.
     */
    private final int LIMIT;

    /**
     * This is the table minimum. Each individual bet from a Player must be greater than this limit.
     */
    private final int MINIMUM;

    /**
     * Instantiates a new Table.
     * Creates an empty list of bets.
     */
    public Table(Wheel wheel) {
        this(wheel, Game.TABLE_LIMIT, Game.TABLE_MINIMUM);
    }

    public Table(Wheel wheel, int limit, int minimum) {
        this.WHEEL = wheel;
        this.LIMIT = limit;
        this.MINIMUM = minimum;
    }

    /**
     * Validates the table-limit rules:
     * The sum of all bets is less than or equal to the table limit.
     * All bet amounts are greater than or equal to the table minimum.
     *
     * @throws InvalidBetException if the bets don’t pass the table limit rules
     */
    public void validate() throws InvalidBetException {
        // Minimum check
        for (Bet bet : BETS) {
            if (bet.amountBet < MINIMUM) {
                throw new InvalidBetException();
            }
        }

        // Maximum check
        int sum = 0;
        for (Bet bet : BETS) {
            sum += bet.amountBet;
        }
        if (sum > LIMIT) {
            throw new InvalidBetException();
        }
    }

    /**
     * Adds this bet to the list of working bets.
     *
     * @param bet a Bet instance to be added to the table
     * @throws InvalidBetException if the bets don’t pass the table limit rules
     */
    public void placeBet(Bet bet) throws InvalidBetException {
        if (bet.parent != null) {
            if (bet.parent.stake >= bet.amountBet) {
                bet.parent.stake -= bet.amountBet;
            } else {
                throw new InvalidBetException();
            }
        }

        BETS.add(bet);

        validate();
    }

    /**
     * Returns a ListIterator over the available list of Bet instances.
     *
     * @return iterator over all bets
     */
    public ListIterator<Bet> iterator() {
        return BETS.listIterator();
    }

    /**
     * @return String representation of all current bets.
     */
    @Override
    public String toString() {
        return BETS.toString();
    }
}
