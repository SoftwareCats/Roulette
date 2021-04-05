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
import io.github.softwarecats.roulette.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ListIterator;

import static org.junit.Assert.*;

public class MartingaleTest {

    Wheel wheel;

    Table table;

    Martingale player;

    @Before
    public void setUp() {
        wheel = new Wheel();
        BinBuilder binBuilder = new BinBuilder();
        binBuilder.buildBins(wheel);

        table = new Table(wheel);
        player = new Martingale(table);
        player.stake = 100;
    }

    @Test
    public void playing() {
        player.stake = 100;
        player.roundsToGo = 1;
        assertTrue(player.playing());

        player.stake = 0;
        assertFalse(player.playing());

        player.stake = 100;
        player.roundsToGo = 0;
        assertFalse(player.playing());
    }

    @Test
    public void placeBets() {
        // Initial bet should be normal
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bet under the designated test condition");
        }

        for (ListIterator<Bet> it = table.iterator(); it.hasNext(); ) {
            Bet bet = it.next();
            assertEquals(player.baseBet, bet.amountBet);
            it.remove();
        }

        // Bet after losing should be doubled
        player.lose(new Bet(1, new Outcome("Name", 1)));

        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bet under the designated test condition");
        }

        for (ListIterator<Bet> it = table.iterator(); it.hasNext(); ) {
            Bet bet = it.next();
            assertEquals(player.baseBet * 2, bet.amountBet);
            it.remove();
        }

        // Bet after winning should reset
        player.win(new Bet(1, new Outcome("Name", 1)));

        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bet under the designated test condition");
        }

        for (ListIterator<Bet> it = table.iterator(); it.hasNext(); ) {
            Bet bet = it.next();
            assertEquals(player.baseBet, bet.amountBet);
            it.remove();
        }
    }
}