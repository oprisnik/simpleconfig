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

public class ExtendedComponentTest extends BaseComponentTest {


    protected Config mConfig;

    @Before
    public void init() throws BadConfigException, FileNotFoundException {
        mConfig = getXmlConfig("/extended-component.xml");
    }

    @Test
    public void testGetName() throws Exception {
        // we only use the base class here
        SimpleComponent component = mConfig.getComponentAndInit("extended1", SimpleComponent.class);

        assertThat(component).isNotNull();
        assertThat(component.getName()).isEqualTo("Awesome component");
    }

    @Test
    public void testGetExtendedInfo() throws Exception {
        ExtendedComponent component = mConfig.getComponentAndInit("extended1", ExtendedComponent.class);

        assertThat(component).isNotNull();
        assertThat(component.getName()).isEqualTo("Awesome component");
        assertThat(component.getExtendedInfo()).isEqualTo("Extended information");
    }

    @Test
    public void testGetName2() throws Exception {
        // we only use the base class here
        SimpleComponent component = mConfig.getComponentAndInit("extended2", SimpleComponent.class);

        assertThat(component).isNotNull();
        assertThat(component.getName()).isNull();
    }

    @Test
    public void testGetExtendedInfo2() throws Exception {
        ExtendedComponent component = mConfig.getComponentAndInit("extended2", ExtendedComponent.class);

        assertThat(component).isNotNull();
        assertThat(component.getName()).isNull();
        assertThat(component.getExtendedInfo()).isNull();
    }

    @Test
    public void testGetName3() throws Exception {
        // we only use the base class here
        SimpleComponent component = mConfig.getComponentAndInit("extended3", SimpleComponent.class, ExtendedComponent.class);

        assertThat(component).isNotNull();
        assertThat(component.getName()).isEqualTo("Awesome name 3");
    }

    @Test
    public void testGetExtendedInfo3() throws Exception {
        ExtendedComponent component = mConfig.getComponentAndInit("extended3", ExtendedComponent.class, ExtendedComponent.class);

        assertThat(component).isNotNull();
        assertThat(component.getName()).isEqualTo("Awesome name 3");
        assertThat(component.getExtendedInfo()).isEqualTo("Extended information 3");
    }

    @Test
    public void testGetName4() throws Exception {
        // we only use the base class here
        SimpleComponent component = mConfig.getComponentAndInit("extended4", SimpleComponent.class, SimpleComponent.class);

        // assert that the class value specified in the config file
        // is used instead of the default value
        assertThat(component instanceof ExtendedComponent).isTrue();

        assertThat(component).isNotNull();
        assertThat(component.getName()).isEqualTo("Awesome name 4");
    }

    @Test
    public void testGetExtendedInfo4() throws Exception {
        ExtendedComponent component = mConfig.getComponentAndInit("extended4", ExtendedComponent.class, ExtendedComponent.class);

        assertThat(component).isNotNull();
        assertThat(component.getName()).isEqualTo("Awesome name 4");
        assertThat(component.getExtendedInfo()).isEqualTo("Extended information 4");
    }

}