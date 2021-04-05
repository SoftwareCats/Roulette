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

import java.util.*;

/**
 * Game manages the sequence of actions that defines the game of Roulette. This includes notifying the Player to place
 * bets, spinning the Wheel and resolving the Bets actually present on the Table.
 */
public class Game {

    // Locale
    public static final Locale LOCALE = Locale.US;
    public static final ResourceBundle BET_NAMES = ResourceBundle.getBundle("i18n.BetNames", LOCALE);

    // Inside Bet payouts
    public static final int STRAIGHT_BET_PAYOUT = 35;
    public static final int SPLIT_BET_PAYOUT = 17;
    public static final int STREET_BET_PAYOUT = 11;
    public static final int CORNER_BET_PAYOUT = 8;
    public static final int FIVE_BET_PAYOUT = 6;
    public static final int LINE_BET_PAYOUT = 5;

    // Outside Bet payouts
    public static final int DOZEN_BET_PAYOUT = 2;
    public static final int COLUMN_BET_PAYOUT = 2;
    public static final int EVEN_MONEY_BET_PAYOUT = 1;

    // Defaults
    public static final int TABLE_LIMIT = Integer.MAX_VALUE;
    public static final int TABLE_MINIMUM = 1;

    /**
     * The Wheel that returns a randomly selected Bin of Outcomes.
     */
    private final Wheel wheel;
    /**
     * The Table which contains the Bets placed by the Player.
     */
    private final Table table;

    /**
     * Constructs a new Game, using a given Wheel and Table.
     *
     * @param wheel the Wheel instance which produces random events
     * @param table the Table instance which holds bets to be resolved
     */
    public Game(Wheel wheel, Table table) {
        this.wheel = wheel;
        this.table = table;
    }

    /**
     * This will execute a single cycle of play with a given Player.
     *
     * @param player the individual player that places bets, receives winnings and pays losses
     * @throws InvalidBetException if the Player attempts to place a bet which exceeds the table’s limit
     */
    public void cycle(Player player) throws InvalidBetException {
        // Skip turn if player is not playing
        if (!player.playing()) {
            return;
        }

        // Notify player to place bets
        player.placeBets();

        // Spin wheel for winners
        Bin winningBin = wheel.next();

        // See which bets won or lost
        List<Bet> winningBets = new ArrayList<>();
        List<Bet> losingBets = new ArrayList<>();
        for (ListIterator<Bet> it = table.iterator(); it.hasNext(); ) {
            Bet bet = it.next();
            if (winningBin.contains(bet.outcome)) {
                winningBets.add(bet);
            } else {
                losingBets.add(bet);
            }
            it.remove();
        }

        // Tell player about if his bets won or lost
        for (Bet bet : winningBets) {
            player.win(bet);
        }
        for (Bet bet : losingBets) {
            player.lose(bet);
        }

        // Tell player about outcomes that would have won
        player.notifyWinners(winningBin);

        // Update player round counter
        player.roundsToGo -= 1;
    }
}
