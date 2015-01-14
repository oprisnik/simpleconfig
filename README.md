SimpleConfig - Java project configuration the easy way.

With SimpleConfig you can create XML configuration files that can then be easily accessed from code.

Some examples can be found [here](simpleconfig/src/test).



## Basics

A very simple XML-Configuration could look like this:

    <config>
        <text>Hello world!</text>
        <debug>true</debug>
    </config>

We can then use this configuration in Java:

    Config config = ConfigFactory.fromFile("my-config.xml");

    String txt = config.getProperty("text"); // will be "Hello world!"
    boolean debug = config.getBoolean("debug", false); // will be "true"

## Nested Configurations

SimpleConfig also supports nested configurations:

    <config>
        <user1>
            <name>Alex</name>
            <points>123</points>
        </user1>

        <user2>
            <name>Tom</name>
            <points>10</points>
        </user2>
    </config>

We can then use this in Java:

    Config config = ConfigFactory.fromFile("my-config.xml");

    // get the configuration for user1
    Config user1 = config.getSubconfig("user1");
    String name1 = user1.getProperty("name"); // Alex
    int points1 = user1.getInt("points", 0); // 123

    // get the configuration for user2
    Config user2 = config.getSubconfig("user2");
    String name2 = user1.getProperty("name"); // Tom
    int points2 = user1.getInt("points", 0); // 10


## Components

You can also configure and create components (Java objects)
defined in the config file.

For example, consider the following class:

    package com.oprisnik.simpleconfig;

    public class SimpleComponent implements Configurable {

      public static final String KEY_NAME = "name";

      private String mName = null;

      @Override
      public void init(Config config) throws BadConfigException {
        mName = config.getProperty(KEY_NAME, mName);
      }

      public String getName() {
        return mName;
      }
    }

The `SimpleComponent` implements the `Configurable` interface.
Hence it can be configured using our config file:

    <config>
        <component1 class="com.oprisnik.simpleconfig.SimpleComponent">
            <name>Alex</name>
        </component1>

        <component2 class="com.oprisnik.simpleconfig.SimpleComponent">
          <name>Tom</name>
        </component2>
    </config>


In Java, we can access it as follows:

    SimpleComponent c1 = config.getComponentAndInit("component1", SimpleComponent.class);
    String name1 = c1.getName(); // Alex

    SimpleComponent c2 = config.getComponentAndInit("component2", SimpleComponent.class);
    String name2 = c2.getName(); // Tom

### Default Class Values

As you can see, we have to define a `class` attribute in the XML file.
If we want to omit this `class` attribute, we can add a default value to our Java code:

XML:

    <config>
        <component1>
            <name>Alex</name>
        </component1>
    </config>

Java:

    SimpleComponent c1 = config.getComponentAndInit("component1",
                                SimpleComponent.class, SimpleComponent.class);


## Copyright


    Copyright 2015 Alexander Oprisnik

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
