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
import io.github.softwarecats.roulette.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class GameTest {

    private Wheel wheel;

    private Random random;

    @Before
    public void setUp() {
        random = new Random();
        random.setSeed(1);
        wheel = new Wheel(new Random(1));
        BinBuilder binBuilder = new BinBuilder();
        binBuilder.buildBins(wheel);
    }

    @Test
    public void cycle() {
        Table table = new Table(wheel);

        final boolean[] won = new boolean[1];
        Player player = new Passenger57(table) {
            @Override
            public void win(Bet bet) {
                won[0] = true;
            }

            @Override
            public void lose(Bet bet) {
                won[0] = false;
            }
        };

        Game game = new Game(wheel, table);

        try {
            game.cycle(player);
        } catch (InvalidBetException e) {
            fail("Player placed invalid bet");
        }

        int wheelResult = random.nextInt(38);
        if (wheel.getBin(wheelResult).contains(wheel.getOutcomes("Black").get(0))) {
            assertTrue(won[0]);
        } else {
            assertFalse(won[0]);
        }
    }
}