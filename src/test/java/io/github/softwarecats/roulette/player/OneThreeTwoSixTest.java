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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.*;

public class OneThreeTwoSixTest {

    Wheel wheel;

    Table table;

    OneThreeTwoSix player;

    @Before
    public void setUp() {
        wheel = new Wheel();
        BinBuilder binBuilder = new BinBuilder();
        binBuilder.buildBins(wheel);

        table = new Table(wheel);

        player = new OneThreeTwoSix(table);
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
        // First bet should have multiplier x1
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(player.baseBet, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Second bet (won last) should have multiplier x3
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(player.baseBet * 3, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Third bet (won last) should have multiplier x2
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(player.baseBet * 2, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Fourth bet (won last) should have multiplier x6
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(player.baseBet * 6, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Fifth bet (won last) should have multiplier x1 again
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(player.baseBet, player.OUTCOME, player), getBets().get(0));

        player.win(getBets().get(0));
        clearTable();

        // Sixth bet (won last) should have multiplier x3 again
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(player.baseBet * 3, player.OUTCOME, player), getBets().get(0));

        player.lose(getBets().get(0));
        clearTable();

        // Losing last bet resets state to NoWins and multiplier is x1
        try {
            player.placeBets();
        } catch (InvalidBetException e) {
            fail("Player should not fail in placing bets");
        }

        assertEquals(new Bet(player.baseBet, player.OUTCOME, player), getBets().get(0));
    }

    @Test
    public void win() {
        // NoWins to OneWin
        player.win(new Bet(1, new Outcome("Name", 1), player));
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.ONE_WIN), player.state);

        // OneWin to TwoWins
        player.win(new Bet(1, new Outcome("Name", 1), player));
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.TWO_WINS), player.state);

        // TwoWins to ThreeWins
        player.win(new Bet(1, new Outcome("Name", 1), player));
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.THREE_WINS), player.state);

        // ThreeWins to NoWins
        player.win(new Bet(1, new Outcome("Name", 1), player));
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.NO_WINS), player.state);
    }

    @Test
    public void lose() {
        // NoWins to NoWins
        player.state = player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.NO_WINS);
        player.lose(new Bet(1, new Outcome("Name", 1), player));
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.NO_WINS), player.state);

        // OneWin to NoWins
        player.state = player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.ONE_WIN);
        player.lose(new Bet(1, new Outcome("Name", 1), player));
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.NO_WINS), player.state);

        // TwoWins to NoWins
        player.state = player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.TWO_WINS);
        player.lose(new Bet(1, new Outcome("Name", 1), player));
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.NO_WINS), player.state);

        // ThreeWins to NoWins
        player.state = player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.THREE_WINS);
        player.lose(new Bet(1, new Outcome("Name", 1), player));
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.NO_WINS), player.state);
    }

    @Test
    public void state() {
        OneThreeTwoSix.State state = player.new State(player, 2) {
            @Override
            public OneThreeTwoSix.State nextWon() {
                return null;
            }
        };

        // Check constructor assigned correct fields
        assertEquals(player, state.player);
        assertEquals(2, state.multiplier);

        // Check methods
        assertEquals(new Bet(player.baseBet * 2, player.OUTCOME, player), state.currentBet());

        assertEquals(state.nextLost(), player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.NO_WINS));
    }

    @Test
    public void stateFactory() {
        // Correct state is retrieved
        assertTrue(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.NO_WINS) instanceof OneThreeTwoSix.NoWins);
        assertTrue(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.ONE_WIN) instanceof OneThreeTwoSix.OneWin);
        assertTrue(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.TWO_WINS) instanceof OneThreeTwoSix.TwoWins);
        assertTrue(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.THREE_WINS) instanceof OneThreeTwoSix.ThreeWins);

        for (OneThreeTwoSix.StateType type : OneThreeTwoSix.StateType.values()) {
            assertEquals(player.STATE_FACTORY.getState(type), player.STATE_FACTORY.states.get(type));
        }

        // All states reference the correct player
        for (OneThreeTwoSix.StateType type : OneThreeTwoSix.StateType.values()) {
            assertEquals(player, player.STATE_FACTORY.getState(type).player);
        }
    }

    @Test
    public void noWins() {
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.NO_WINS).player, player);
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.NO_WINS).multiplier, 1);
    }

    @Test
    public void oneWin() {
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.ONE_WIN).player, player);
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.ONE_WIN).multiplier, 3);
    }

    @Test
    public void twoWins() {
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.TWO_WINS).player, player);
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.TWO_WINS).multiplier, 2);
    }

    @Test
    public void threeWins() {
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.THREE_WINS).player, player);
        assertEquals(player.STATE_FACTORY.getState(OneThreeTwoSix.StateType.THREE_WINS).multiplier, 6);
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