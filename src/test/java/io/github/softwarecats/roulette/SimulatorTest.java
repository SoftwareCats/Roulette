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

import io.github.softwarecats.roulette.player.Passenger57;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SimulatorTest {

    Wheel wheel;

    Table table;

    Passenger57 player;

    Game game;

    Simulator simulator;

    Random rng;

    @Before
    public void setUp() {
        rng = new Random(1);

        wheel = new Wheel(new Random(1));
        BinBuilder binBuilder = new BinBuilder();
        binBuilder.buildBins(wheel);

        table = new Table(wheel);

        player = new Passenger57(table);

        game = new Game(wheel, table);

        simulator = new Simulator(game, player);
    }

    @Test
    public void session() {
        List<Integer> sessionResult = null;
        try {
            sessionResult = simulator.session();
        } catch (InvalidBetException e) {
            fail("Player is broken, should not place invalid bet");
        }

        assertEquals(simulator.sessionDuration, sessionResult.size());

        int expectedStake = simulator.initialStake;
        for (int result : sessionResult) {
            Bin winningBin = wheel.getBin(rng.nextInt(38));
            boolean shouldWin = winningBin.contains(wheel.getOutcomes(Game.BET_NAMES.getString("black")).get(0));

            if (shouldWin) {
                expectedStake += player.getBaseBet();
            } else {
                expectedStake -= player.getBaseBet();
            }

            assertEquals(expectedStake, result);
        }
    }

    @Test
    public void gather() {
        simulator = new Simulator(game, player) {

            int sessionCount = 1;

            @Override
            public List<Integer> session() {
                return IntStream.range(0, sessionCount++).boxed().collect(Collectors.toList());
            }
        };

        try {
            simulator.gather();
        } catch (InvalidBetException e) {
            fail("Unit test for simulator.gather() is broken");
        }

        assertEquals(IntStream.range(0, simulator.samples)
                        .boxed()
                        .collect(Collectors.toList()),
                simulator.maxima);
        assertEquals(IntStream.rangeClosed(1, simulator.samples)
                        .boxed()
                        .collect(Collectors.toList()),
                simulator.durations);
    }
}