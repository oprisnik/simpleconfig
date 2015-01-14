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

package com.oprisnik.simpleconfig;


import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

import static com.google.common.truth.Truth.assertThat;

public class XmlConfigTest extends BaseComponentTest {

    protected Config mConfig;

    @Before
    public void init() throws BadConfigException, FileNotFoundException {
        mConfig = getXmlConfig("/xml-config-test.xml");
    }

    @Test
    public void testGetProperty() throws BadConfigException, FileNotFoundException {
        assertThat(mConfig.hasProperty("test-property")).isTrue();
        assertThat(mConfig.getProperty("test-property")).isEqualTo("Hello world!");
    }

    @Test
    public void testSubConfig() throws BadConfigException, FileNotFoundException {
        // subconfig itself is not a property
        assertThat(mConfig.hasProperty("subconfig")).isFalse();

        // get it
        Config subconfig = mConfig.getSubconfig("subconfig");
        assertThat(subconfig).isNotNull();
        assertThat(subconfig.getProperty("something")).isEqualTo("Test");
        assertThat(subconfig.getProperty("something-else[@attr]")).isEqualTo("hello");
        assertThat(mConfig.getSubconfig("nothing")).isNull();
    }

    @Test
    public void testNestedFiles() throws BadConfigException, IOException {
        assertThat(mConfig.hasProperty("file")).isTrue();
        InputStream is = mConfig.getNestedInputStream("file");

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            // check if the file can be read and if it is correct
            String data = br.readLine();
            assertThat(data).isEqualTo("This is a nested file.");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        }
    }

    @Test
    public void testCollections() throws BadConfigException, IOException {
        Collection<String> list = mConfig.getCollection("list.string");
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(4);
        int cur = 1;
        for (String s : list) {
            assertThat(s).isEqualTo("data" + cur);
            cur++;
        }
    }

    @Test
    public void testCollectionsSingleItem() throws BadConfigException, IOException {
        Collection<String> list = mConfig.getCollection("list1.string");
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.iterator().next()).isEqualTo("single item");
    }
}
