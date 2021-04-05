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

/**
 * Passenger57 constructs a Bet based on the Outcome named "Black".
 * This is a very persistent player indeed.
 */
public class Passenger57 extends Player {

    protected final Outcome BLACK;

    protected int baseBet = Game.TABLE_MINIMUM;

    /**
     * Constructs the Player with a specific Table for placing Bets.
     * Since the table has access to the Wheel, we can use this wheel to extract Outcome objects.
     *
     * @param table the table to use
     */
    public Passenger57(Table table) {
        super(table);

        BLACK = table.WHEEL.getOutcomes(Game.BET_NAMES.getString("black")).get(0);
    }

    public int getBaseBet() {
        return baseBet;
    }

    @Override
    public boolean playing() {
        return (stake >= baseBet) && (roundsToGo > 0);
    }

    @Override
    public void placeBets() throws InvalidBetException {
        table.placeBet(new Bet(baseBet, BLACK, this));
    }

    @Override
    public void newRound() {

    }

    @Override
    public void win(Bet bet) {
        super.win(bet);
    }

    @Override
    public void lose(Bet bet) {
        super.lose(bet);
    }
}
