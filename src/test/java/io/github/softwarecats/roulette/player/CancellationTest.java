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

public class CancellationTest {

    Wheel wheel;

    Table table;

    Cancellation player;

    @Before
    public void setUp() throws Exception {
        wheel = new Wheel();
        BinBuilder binBuilder = new BinBuilder();
        binBuilder.buildBins(wheel);

        table = new Table(wheel);

        player = new Cancellation(table);
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
    public void placeBetsCombLWW() {
        // First bet should be 1 + 6 = 7
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(7, player.OUTCOME, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Second bet (lost last) should be 1 + 7 = 8
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(8, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Third bet (won last) should be 2 + 6 = 8
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(8, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Fourth bet (won last) should be 3 + 5 = 8
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(8, player.OUTCOME, player), getBets().get(0));
    }

    @Test
    public void placeBetsCombLWL() {
        // First bet should be 1 + 6 = 7
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(7, player.OUTCOME, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Second bet (lost last) should be 1 + 7 = 8
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(8, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Third bet (won last) should be 2 + 6 = 8
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(8, player.OUTCOME, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Fourth bet (lost last) should be 2 + 8 = 10
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(10, player.OUTCOME, player), getBets().get(0));
    }

    @Test
    public void placeBetsCombLLW() {
        // First bet should be 1 + 6 = 7
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(7, player.OUTCOME, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Second bet (lost last) should be 1 + 7 = 8
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(8, player.OUTCOME, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Third bet (lost last) should be 1 + 8 = 9
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(9, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Fourth bet (won last) should be 2 + 7 = 9
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(9, player.OUTCOME, player), getBets().get(0));
    }

    @Test
    public void placeBetsCombLLL() {
        // First bet should be 1 + 6 = 7
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(7, player.OUTCOME, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Second bet (lost last) should be 1 + 7 = 8
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(8, player.OUTCOME, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Third bet (lost last) should be 1 + 8 = 9
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(9, player.OUTCOME, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Fourth bet (lost last) should be 1 + 9 = 10
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(10, player.OUTCOME, player), getBets().get(0));
    }

    @Test
    public void placeBetsCombWWW() {
        // First bet should be 1 + 6 = 7
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(7, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Second bet (won last) should be 2 + 5 = 7
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(7, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Third bet (won last) should be 3 + 4 = 7
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(7, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Fourth bet (won last) should be 1 + 6 = 7 again
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(7, player.OUTCOME, player), getBets().get(0));
    }

    @Test
    public void placeBetsCombWWL() {
        // First bet should be 1 + 6 = 7
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(7, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Second bet (won last) should be 2 + 5 = 7
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(7, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Third bet (won last) should be 3 + 4 = 7
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(7, player.OUTCOME, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Fourth bet (lost last) should be 3 + 7 = 10
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(10, player.OUTCOME, player), getBets().get(0));
    }

    @Test
    public void placeBetsCombWLW() {
        // First bet should be 1 + 6 = 7
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(7, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Second bet (won last) should be 2 + 5 = 7
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(7, player.OUTCOME, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Third bet (lost last) should be 2 + 7 = 9
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(9, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Fourth bet (won last) should be 3 + 5 = 8
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(8, player.OUTCOME, player), getBets().get(0));
    }

    @Test
    public void placeBetsCombWLL() {
        // First bet should be 1 + 6 = 7
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(7, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Second bet (won last) should be 2 + 5 = 7
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(7, player.OUTCOME, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Third bet (lost last) should be 2 + 7 = 9
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(9, player.OUTCOME, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Fourth bet (lost last) should be 2 + 9 = 11
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(11, player.OUTCOME, player), getBets().get(0));
    }

    @Test
    public void resetSequence() {
        assertEquals(6, player.sequence.size());

        player.sequence.clear();
        player.resetSequence();

        assertEquals(6, player.sequence.size());
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