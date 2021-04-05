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
import io.github.softwarecats.roulette.InvalidBetException;
import io.github.softwarecats.roulette.Table;

import java.util.Set;

/**
 * Player places bets in Roulette. This an abstract class, with no actual body for the placeBets() method. However,
 * this class does implement the basic win() method used by all subclasses.
 */
public abstract class Player {

    /**
     * The player’s current stake. Initialized to the player’s starting budget.
     */
    public int stake;

    /**
     * The number of rounds left to play. Initialized by the overall simulation control to the maximum number of rounds
     * to play.
     */
    public int roundsToGo;

    /**
     * The Table used to place individual Bets. The Table contains the current Wheel from which the player can
     * get Outcomes used to build Bets.
     */
    protected Table table;

    /**
     * Constructs the Player with a specific Table for placing Bets.
     * Since the table has access to the Wheel, we can use this wheel to extract Outcome objects.
     *
     * @param table the table to use
     */
    public Player(Table table) {
        this.table = table;
    }

    /**
     * Returns true while the player is still active.
     *
     * @return true while the player is still active
     */
    public abstract boolean playing();

    /**
     * Updates the Table with the various Bets.
     *
     * @throws InvalidBetException if the Player attempts to place a bet which exceeds the table’s limit
     */
    public abstract void placeBets() throws InvalidBetException;

    /**
     * The game will notify a player of each spin using this method. This will be invoked even if the player places no
     * bets.
     *
     * @param outcomes the set of Outcome instances that are part of the current win
     */
    public void notifyWinners(Set<Outcome> outcomes) {

    }

    /**
     * Informs the Player that a new round has started and it should reset all its attributes.
     */
    public abstract void newRound();

    /**
     * Notification from the Game that the Bet was a winner. The amount of money won is available via bet method
     * winAmount().
     *
     * @param bet the bet which won
     */
    public void win(Bet bet) {
        stake += bet.winAmount();
    }

    /**
     * Notification from the Game that the Bet was a loser. Note that the amount was already deducted from the stake
     * when the bet was created.
     *
     * @param bet the bet which lost
     */
    public void lose(Bet bet) {

    }
}
