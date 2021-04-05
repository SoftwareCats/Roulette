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
import java.util.Random;

import static org.junit.Assert.*;

public class RandomChoiceTest {

    Wheel wheel;

    Table table;

    RandomChoice player;

    Random rng = new Random(1);

    List<Outcome> ALL_OUTCOMES;

    @Before
    public void setUp() {
        wheel = new Wheel();
        BinBuilder binBuilder = new BinBuilder();
        binBuilder.buildBins(wheel);

        ALL_OUTCOMES = new ArrayList<>(wheel.getAllOutcomes().values());

        table = new Table(wheel);

        player = new RandomChoice(table, new Random(1));
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
        for (int i = 0; i < 1000; i++) {
            // Generate expected outcome with known RNG
            Outcome expectedOutcome = ALL_OUTCOMES.get(rng.nextInt(ALL_OUTCOMES.size()));
            Bet expectedBet = new Bet(player.baseBet, expectedOutcome, player);

            // Player bet placing
            player.stake = player.baseBet;
            try {
                player.placeBets();
            } catch (InvalidBetException e) {
                fail("Player should not fail in placing bet");
            }

            // Get the bet player placed
            Bet actualBet = table.iterator().next();
            for (ListIterator<Bet> it = table.iterator(); it.hasNext(); ) {
                it.next();
                it.remove();
            }

            assertEquals(expectedBet, actualBet);
        }
    }
}