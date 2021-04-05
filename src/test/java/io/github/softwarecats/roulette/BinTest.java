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
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class BinTest {

    @Test
    public void testConstructor() {
        Outcome outcome1 = new Outcome("Name 1", 1);
        Outcome outcome2 = new Outcome("Name 2", 2);
        Outcome[] outcomes = new Outcome[]{outcome1, outcome2};

        Bin bin1 = new Bin();

        Bin bin2 = new Bin(outcomes);

        Bin bin3 = new Bin(new ArrayList<>(Arrays.asList(outcomes)));
    }
}