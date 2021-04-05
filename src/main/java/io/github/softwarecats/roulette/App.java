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

import io.github.softwarecats.roulette.player.Player;
import io.github.softwarecats.roulette.player.PlayerFactory;
import io.github.softwarecats.roulette.player.PlayerType;

import java.util.Scanner;

public class App {

    protected static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Wheel wheel = new Wheel();
        BinBuilder binBuilder = new BinBuilder();
        binBuilder.buildBins(wheel);

        Table table = new Table(wheel);

        System.out.print("Player Type: ");
        String playerType = scanner.nextLine();
        Player player = PlayerFactory.getPlayer(PlayerType.valueOf(playerType), table);

        Game game = new Game(wheel, table);

        Simulator simulator = new Simulator(game, player);

        System.out.print("Session Duration: ");
        simulator.sessionDuration = Integer.parseInt(scanner.nextLine());

        System.out.print("Initial Stake: ");
        simulator.initialStake = Integer.parseInt(scanner.nextLine());

        System.out.print("Samples: ");
        simulator.samples = Integer.parseInt(scanner.nextLine());

        try {
            simulator.gather();
        } catch (InvalidBetException e) {
            System.out.println("Player placed invalid bet.");
        }

        System.out.println("Mean Duration: " + simulator.meanDuration);
        System.out.println("Duration STD: " + simulator.stdDuration);
        System.out.println("Mean Maximum: " + simulator.meanMaximum);
        System.out.println("Maximum STD: " + simulator.stdMaximum);
    }
}
