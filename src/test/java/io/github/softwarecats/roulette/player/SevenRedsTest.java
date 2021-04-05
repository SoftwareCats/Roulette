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
import org.junit.Before;
import org.junit.Test;

import java.util.ListIterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SevenRedsTest {

    Outcome RED;
    Outcome BLACK;
    Wheel wheel;
    Table table;
    SevenReds player;

    @Before
    public void setUp() {
        wheel = new Wheel();
        BinBuilder binBuilder = new BinBuilder();
        binBuilder.buildBins(wheel);

        table = new Table(wheel);

        player = new SevenReds(table);
        player.stake = 100;

        RED = wheel.getOutcomes(Game.BET_NAMES.getString("red")).get(0);
        BLACK = wheel.getOutcomes(Game.BET_NAMES.getString("black")).get(0);
    }

    @Test
    public void placeBets() {
        // Make sure the player doesn't place bets after 1-6th Red
        for (int i = 0; i < 7; i++) {
            try {
                player.placeBets();
            } catch (InvalidBetException e) {
                fail("Player should not place invalid bet");
            }

            player.notifyWinners(Set.of(RED));

            int count = 0;
            ListIterator<Bet> iterator = table.iterator();
            for (; iterator.hasNext(); ++count) iterator.next();
            assertEquals(0, count);
        }
        // After this block player would have seen 7 Reds

        // Make sure the player starts placing bets after 7th Red
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not place invalid bet");
        }
        Bet bet = table.iterator().next();
        assertEquals(BLACK, bet.outcome);
        assertEquals(player.baseBet, bet.amountBet);

        // Clear the table
        for (ListIterator<Bet> it = table.iterator(); it.hasNext(); ) {
            it.next();
            it.remove();
        }

        // Make sure the player doubles the bet after a loss
        player.lose(bet);
        player.notifyWinners(Set.of(RED));
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not place invalid bet");
        }
        bet = table.iterator().next();
        assertEquals(BLACK, bet.outcome);
        assertEquals(player.baseBet * 2, bet.amountBet);

        // Clear the table
        for (ListIterator<Bet> it = table.iterator(); it.hasNext(); ) {
            it.next();
            it.remove();
        }

        // Make sure the player stops placing bets after the last 7 bets are not Red
        player.win(bet);
        player.notifyWinners(Set.of(BLACK));
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not place invalid bet");
        }
        int count = 0;
        ListIterator<Bet> iterator = table.iterator();
        for (; iterator.hasNext(); ++count) iterator.next();
        assertEquals(0, count);
    }
}