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

import static org.junit.Assert.assertEquals;

public class PlayerTest {

    Wheel wheel;

    Table table;

    Player player;

    @Before
    public void setUp() {
        wheel = new Wheel();
        BinBuilder binBuilder = new BinBuilder();
        binBuilder.buildBins(wheel);

        table = new Table(wheel);

        player = new Player(table) {
            @Override
            public boolean playing() {
                return true;
            }

            @Override
            public void placeBets() {

            }

            @Override
            public void newRound() {

            }
        };
        player.stake = 100;
    }

    @Test
    public void win() {
        // Simulate placing bet
        player.stake -= 5;

        // Simulate winning bet
        player.win(new Bet(5, new Outcome("Name", 1)));

        assertEquals(100 + 5, player.stake);
    }
}