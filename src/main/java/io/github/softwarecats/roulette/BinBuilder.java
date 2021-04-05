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

import io.github.softwarecats.casino.event.Outcome;

import java.util.Set;

/**
 * BinBuilder creates the Outcomes for all of the 38 individual Bin on a Roulette wheel.
 */
public class BinBuilder {

    /**
     * Initializes the BinBuilder.
     */
    public BinBuilder() {

    }

    /**
     * Creates the Outcome instances and uses the addOutcome() method to place each Outcome in the
     * appropriate Bin of wheel.
     *
     * @param wheel the Wheel with Bins that must be populated with Outcomes
     */
    public void buildBins(Wheel wheel) {
        // Five Bets
        Outcome five = new Outcome(Game.BET_NAMES.getString("five"), Game.FIVE_BET_PAYOUT);
        wheel.addOutcome(0, five);
        wheel.addOutcome(1, five);
        wheel.addOutcome(2, five);
        wheel.addOutcome(3, five);
        wheel.addOutcome(37, five);

        // Straight Bets
        for (int i = 1; i < 37; i++) {
            wheel.addOutcome(i, new Outcome(String.valueOf(i), Game.STRAIGHT_BET_PAYOUT));
        }

        wheel.addOutcome(0, new Outcome("0", Game.STRAIGHT_BET_PAYOUT));
        wheel.addOutcome(37, new Outcome("00", Game.STRAIGHT_BET_PAYOUT));

        // Split Bets

        // Left-Right
        for (int r = 0; r < 12; r++) {
            // First-Second column numbers
            int n = 3 * r + 1;
            Outcome split = new Outcome(
                    Game.BET_NAMES.getString("split") + String.format(" %d-%d", n, n + 1),
                    Game.SPLIT_BET_PAYOUT);
            wheel.addOutcome(n, split);
            wheel.addOutcome(n + 1, split);

            // Second-Third column numbers
            n = 3 * r + 2;
            split = new Outcome(
                    Game.BET_NAMES.getString("split") + String.format(" %d-%d", n, n + 1),
                    Game.SPLIT_BET_PAYOUT);
            wheel.addOutcome(n, split);
            wheel.addOutcome(n + 1, split);
        }

        // Up-Down
        for (int n = 1; n < 34; n++) {
            Outcome split = new Outcome(
                    Game.BET_NAMES.getString("split") + String.format(" %d-%d", n, n + 3),
                    Game.SPLIT_BET_PAYOUT);
            wheel.addOutcome(n, split);
            wheel.addOutcome(n + 3, split);
        }

        // Street Bets
        for (int r = 0; r < 12; r++) {
            int n = 3 * r + 1;
            Outcome street = new Outcome(
                    Game.BET_NAMES.getString("street") + String.format(" %d-%d-%d", n, n + 1, n + 2),
                    Game.STREET_BET_PAYOUT);
            wheel.addOutcome(n, street);
            wheel.addOutcome(n + 1, street);
            wheel.addOutcome(n + 2, street);
        }

        // Corner Bets
        for (int r = 0; r < 11; r++) {
            // Column 1-2 Corner
            int n = 3 * r + 1;
            Outcome corner = new Outcome(
                    Game.BET_NAMES.getString("corner") + String.format(" %d-%d-%d-%d", n, n + 1, n + 3, n + 4),
                    Game.CORNER_BET_PAYOUT);
            wheel.addOutcome(n, corner);
            wheel.addOutcome(n + 1, corner);
            wheel.addOutcome(n + 3, corner);
            wheel.addOutcome(n + 4, corner);

            // Column 2-3 Corner
            n = 3 * r + 2;
            corner = new Outcome(
                    Game.BET_NAMES.getString("corner") + String.format(" %d-%d-%d-%d", n, n + 1, n + 3, n + 4),
                    Game.CORNER_BET_PAYOUT);
            wheel.addOutcome(n, corner);
            wheel.addOutcome(n + 1, corner);
            wheel.addOutcome(n + 3, corner);
            wheel.addOutcome(n + 4, corner);
        }

        // Line Bets
        for (int r = 0; r < 11; r++) {
            int n = 3 * r + 1;
            Outcome line = new Outcome(
                    Game.BET_NAMES.getString("line") + String.format(" %d-%d-%d-%d-%d-%d", n, n + 1, n + 2, n + 3, n + 4, n + 5),
                    Game.LINE_BET_PAYOUT);
            wheel.addOutcome(n, line);
            wheel.addOutcome(n + 1, line);
            wheel.addOutcome(n + 2, line);
            wheel.addOutcome(n + 3, line);
            wheel.addOutcome(n + 4, line);
            wheel.addOutcome(n + 5, line);
        }

        // Dozen Bets
        for (int d = 0; d < 3; d++) {
            Outcome dozen = new Outcome(
                    Game.BET_NAMES.getString("dozen") + String.format(" %d-%d", d + 1, d + 12),
                    Game.DOZEN_BET_PAYOUT);
            for (int m = 0; m < 12; m++) {
                wheel.addOutcome(12 * d + m + 1, dozen);
            }
        }

        // Column Bets
        for (int c = 0; c < 3; c++) {
            Outcome column = new Outcome(
                    Game.BET_NAMES.getString("column") + String.format(" %d", c + 1),
                    Game.COLUMN_BET_PAYOUT);
            for (int r = 0; r < 12; r++) {
                wheel.addOutcome(3 * r + c + 1, column);
            }
        }

        // Even-Money Bets
        for (int n = 1; n < 37; n++) {
            Outcome red = new Outcome(Game.BET_NAMES.getString("red"), Game.EVEN_MONEY_BET_PAYOUT);
            Outcome black = new Outcome(Game.BET_NAMES.getString("black"), Game.EVEN_MONEY_BET_PAYOUT);
            Outcome even = new Outcome(Game.BET_NAMES.getString("even"), Game.EVEN_MONEY_BET_PAYOUT);
            Outcome odd = new Outcome(Game.BET_NAMES.getString("odd"), Game.EVEN_MONEY_BET_PAYOUT);
            Outcome high = new Outcome(Game.BET_NAMES.getString("high"), Game.EVEN_MONEY_BET_PAYOUT);
            Outcome low = new Outcome(Game.BET_NAMES.getString("low"), Game.EVEN_MONEY_BET_PAYOUT);

            // High Low
            if (n < 19) {
                wheel.addOutcome(n, low);
            } else {
                wheel.addOutcome(n, high);
            }

            // Even Odd
            if (n % 2 == 0) {
                wheel.addOutcome(n, even);
            } else {
                wheel.addOutcome(n, odd);
            }

            // Red Black
            if (Set.of(1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36).contains(n)) {
                wheel.addOutcome(n, red);
            } else {
                wheel.addOutcome(n, black);
            }
        }
    }
}
