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

import io.github.softwarecats.roulette.player.Player;

import java.util.Objects;

/**
 * Bet associates an amount with an Outcome and a Player.
 */
public class Bet {

    /**
     * The amount of the bet.
     */
    public int amountBet;

    /**
     * The Outcome on which the bet is placed.
     */
    public Outcome outcome;

    /**
     * The player who created the bet.
     */
    public Player parent;

    public Bet(int amountBet, Outcome outcome) {
        this.amountBet = amountBet;
        this.outcome = outcome;
        this.parent = null;
    }

    /**
     * Create a new Bet of a specific amount on a specific outcome.
     *
     * @param amountBet the amount of the bet
     * @param outcome   the Outcome we are betting on
     */
    public Bet(int amountBet, Outcome outcome, Player parent) {
        this.amountBet = amountBet;
        this.outcome = outcome;
        this.parent = parent;
    }

    /**
     * Uses the Outcome‘s winAmount to compute the amount won, given the amount of this bet. Note that the
     * amount bet must also be added in. A 1:1 outcome (e.g. a bet on Red) pays the amount bet plus the amount won.
     *
     * @return amount won
     */
    public int winAmount() {
        return amountBet + outcome.winAmount(amountBet);
    }

    /**
     * Returns the amount bet as the amount lost. This is the cost of placing the bet.
     *
     * @return amount lost
     */
    public int loseAmount() {
        return amountBet;
    }

    /**
     * Returns a string representation of this bet. Note that this method will delegate the much of the work to the
     * toString() method of the Outcome.
     *
     * @return string representation of this bet with the form "amount on outcome"
     */
    @Override
    public String toString() {
        return amountBet + " on " + outcome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bet bet = (Bet) o;
        return amountBet == bet.amountBet && outcome.equals(bet.outcome) && Objects.equals(parent, bet.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountBet, outcome, parent);
    }
}
