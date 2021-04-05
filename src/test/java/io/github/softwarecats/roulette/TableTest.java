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

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

public class TableTest {

    @Test
    public void placeBet() {
        Bet bet1 = new Bet(2, new Outcome("Name 1", 1));
        Bet bet2 = new Bet(4, new Outcome("Name 2", 6));

        Table table = new Table(new Wheel());

        try {
            table.placeBet(bet1);
            table.placeBet(bet2);
        } catch (InvalidBetException e) {
            fail();
        }

        ArrayList<Bet> bets = new ArrayList<>();
        for (ListIterator<Bet> it = table.iterator(); it.hasNext(); ) {
            bets.add(it.next());
        }

        assertArrayEquals(bets.toArray(), List.of(bet1, bet2).toArray());
    }
}