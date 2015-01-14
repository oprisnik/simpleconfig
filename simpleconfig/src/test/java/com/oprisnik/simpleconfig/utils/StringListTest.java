/*
 * Copyright 2015 Alexander Oprisnik
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

package com.oprisnik.simpleconfig.utils;

import com.oprisnik.simpleconfig.BadConfigException;
import com.oprisnik.simpleconfig.BaseComponentTest;
import com.oprisnik.simpleconfig.Config;
import org.junit.Test;

import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;

public class StringListTest extends BaseComponentTest {

    @Test
    public void testStringList1() throws BadConfigException, IOException {
        Config config = getXmlConfig("/string-list.xml");

        // list1 with nested file
        StringList list = config.getComponentAndInit("string-list1", StringList.class, StringList.class);

        assertThat(list).isNotNull();

        assertThat(list.contains("Hello World!")).isTrue();
        assertThat(list.contains("This is another string 123.")).isTrue();
        assertThat(list.contains("Not in list :)")).isFalse();

        assertThat(list.checkPrefix("TestIsAPrefixInTheList")).isTrue();
        assertThat(list.checkPrefix("Hello World is not a prefix since the ! is missing")).isFalse();
    }

    @Test
    public void testStringList2() throws BadConfigException, IOException {
        Config config = getXmlConfig("/string-list.xml");

        // list 2 with list defined in XML
        StringList list = config.getComponentAndInit("string-list2", StringList.class);

        assertThat(list).isNotNull();

        assertThat(list.contains("Hello World!")).isTrue();
        assertThat(list.contains("It works! :)")).isTrue();
        assertThat(list.contains("Nothing")).isFalse();

        assertThat(list.checkPrefix("Hello World! is a prefix")).isTrue();
        assertThat(list.checkPrefix("Hello World is not a prefix since the ! is missing")).isFalse();
    }

    @Test
    public void testStringListSingleItem() throws BadConfigException, IOException {
        Config config = getXmlConfig("/string-list.xml");

        StringList list = config.getComponentAndInit("string-list3", StringList.class);

        assertThat(list).isNotNull();

        assertThat(list.contains("SingleItem")).isTrue();
        assertThat(list.contains("It works! :)")).isFalse();
        assertThat(list.contains("Nothing")).isFalse();

        assertThat(list.checkPrefix("SingleItem Hello")).isTrue();
        assertThat(list.checkPrefix("Hello World is not a prefix since the ! is missing")).isFalse();
    }


}