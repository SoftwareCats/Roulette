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

import io.github.softwarecats.roulette.Table;

public class PlayerFactory {
    public static Player getPlayer(PlayerType type, Table table) {
        switch (type) {
            case CANCELLATION:
                return new Cancellation(table);
            case FIBONACCI:
                return new Fibonacci(table);
            case MARTINGALE:
                return new Martingale(table);
            case ONE_THREE_TWO_SIX:
                return new OneThreeTwoSix(table);
            case PASSENGER57:
                return new Passenger57(table);
            case RANDOM_CHOICE:
                return new RandomChoice(table);
            case SEVEN_REDS:
                return new SevenReds(table);
            default:
                return new Player(table) {
                    @Override
                    public boolean playing() {
                        return false;
                    }

                    @Override
                    public void placeBets() {

                    }

                    @Override
                    public void newRound() {

                    }
                };
        }
    }
}
