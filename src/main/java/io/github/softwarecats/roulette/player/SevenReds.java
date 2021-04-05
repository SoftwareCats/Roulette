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

import io.github.softwarecats.roulette.Game;
import io.github.softwarecats.roulette.InvalidBetException;
import io.github.softwarecats.roulette.Outcome;
import io.github.softwarecats.roulette.Table;

import java.util.Set;

/**
 * SevenReds is a Martingale player who places bets in Roulette. This player waits until the wheel has spun red
 * seven times in a row before betting black.
 */
public class SevenReds extends Martingale {

    /**
     * The Outcome Red.
     */
    protected final Outcome RED;

    /**
     * The number of reds yet to go. This starts at 7 , is reset to 7 on each non-red outcome, and decrements by 1 on
     * each red outcome.
     */
    protected int redCount = 0;

    /**
     * Constructs the Player with a specific Table for placing Bets.
     * Since the table has access to the Wheel, we can use this wheel to extract Outcome objects.
     *
     * @param table the table to use
     */
    public SevenReds(Table table) {
        super(table);
        RED = table.WHEEL.getOutcomes(Game.BET_NAMES.getString("red")).get(0);
    }

    /**
     * If redCount is zero, this places a bet on black, using the bet multiplier.
     *
     * @throws InvalidBetException if the Player attempts to place a bet which exceeds the table’s limit
     */
    @Override
    public void placeBets() throws InvalidBetException {
        if (redCount >= 7) {
            super.placeBets();
        }
    }

    /**
     * This is notification from the Game of all the winning outcomes. If this vector includes red, redCount is
     * decremented. Otherwise, redCount is reset to 7.
     *
     * @param outcomes the Outcome set from a Bin
     */
    @Override
    public void notifyWinners(Set<Outcome> outcomes) {
        if (outcomes.contains(RED)) {
            redCount++;
        } else {
            redCount = 0;
        }
    }

    @Override
    public void newRound() {
        super.newRound();
        redCount = 0;
    }
}
