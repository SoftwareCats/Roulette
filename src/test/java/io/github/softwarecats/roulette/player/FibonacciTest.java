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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.*;

public class FibonacciTest {

    Wheel wheel;

    Table table;

    Fibonacci player;

    @Before
    public void setUp() {
        wheel = new Wheel();
        BinBuilder binBuilder = new BinBuilder();
        binBuilder.buildBins(wheel);

        table = new Table(wheel);

        player = new Fibonacci(table);
        player.stake = 100;
        player.roundsToGo = 250;
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
        // First bet should be 1
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(1, player.BLACK, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Second bet (won last) should be 1
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(1, player.BLACK, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Third bet (lost last) should be 1
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(1, player.BLACK, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Fourth bet (lost last) should be 2
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(2, player.BLACK, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Fifth bet (lost last) should be 3
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(3, player.BLACK, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Sixth bet (lost last) should be 3
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(5, player.BLACK, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Seventh bet (won last) should be 1 again
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(1, player.BLACK, player), getBets().get(0));
    }

    @Test
    public void newRound() {
        // Simulate round
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(1, player.BLACK, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // New round
        player.newRound();

        // Check reset
        assertEquals(1, player.current);
        assertEquals(0, player.previous);
    }

    @Test
    public void win() {
        player.win(new Bet(1, player.BLACK, player));

        assertEquals(1, player.current);
        assertEquals(0, player.previous);
    }

    @Test
    public void lose() {
        player.lose(null);

        assertEquals(1, player.current);
        assertEquals(1, player.previous);
    }

    protected List<Bet> getBets() {
        List<Bet> bets = new ArrayList<>();
        for (ListIterator<Bet> it = table.iterator(); it.hasNext(); ) {
            bets.add(it.next());
        }
        return bets;
    }

    protected void clearTable() {
        for (ListIterator<Bet> it = table.iterator(); it.hasNext(); ) {
            it.next();
            it.remove();
        }
    }
}