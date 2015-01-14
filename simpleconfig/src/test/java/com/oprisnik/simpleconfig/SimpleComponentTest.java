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

import java.io.FileNotFoundException;

import static com.google.common.truth.Truth.assertThat;

public class SimpleComponentTest extends BaseComponentTest {


    protected Config mConfig;

    @Before
    public void init() throws BadConfigException, FileNotFoundException {
        mConfig = getXmlConfig("/simple-component.xml");
    }

    @Test
    public void testGetName() throws Exception {
        SimpleComponent component = mConfig.getComponentAndInit("component1", SimpleComponent.class);

        assertThat(component).isNotNull();
        assertThat(component.getName()).isEqualTo("Awesome component");
    }

    @Test
    public void testGetName2() throws Exception {
        SimpleComponent component = mConfig.getComponentAndInit("component2", SimpleComponent.class);

        assertThat(component).isNotNull();
        assertThat(component.getName()).isNull();
    }

    @Test
    public void testComponent3() throws Exception {
        SimpleComponent component = mConfig.getComponentAndInit("component3", SimpleComponent.class, SimpleComponent.class);
        assertThat(component).isNotNull();
        assertThat(component.getName()).isEqualTo("Hello");
    }

    @Test(expected = BadConfigException.class)
    public void testComponent3Invalid() throws Exception {
        SimpleComponent component = mConfig.getComponentAndInit("component3", SimpleComponent.class);
    }

    @Test
    public void testComponent4() throws Exception {
        SimpleComponent component = mConfig.getComponentAndInit("component4", SimpleComponent.class, SimpleComponent.class);
        
        assertThat(component).isNotNull();
        assertThat(component.getName()).isNull();
    }
}