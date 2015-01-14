# SimpleConfig

[![Build Status](https://travis-ci.org/oprisnik/simpleconfig.svg)](https://travis-ci.org/oprisnik/simpleconfig)

SimpleConfig - Java project configuration the easy way.

With SimpleConfig you can create XML configuration files that can then be easily accessed from code.

Some examples can be found [here](simpleconfig/src/test).



## Basics

A very simple XML-Configuration could look like this:

```xml
<config>
    <text>Hello world!</text>
    <debug>true</debug>
</config>
```

We can then use this configuration in Java (see [Config.java](simpleconfig/src/main/java/com/oprisnik/simpleconfig/Config.java)):

```java
Config config = ConfigFactory.fromFile("my-config.xml");

String txt = config.getProperty("text"); // will be "Hello world!"
boolean debug = config.getBoolean("debug", false); // will be "true"
```

## Nested Configurations

SimpleConfig also supports nested configurations:

```xml
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
```
We can then use this in Java:

```java
Config config = ConfigFactory.fromFile("my-config.xml");

// get the configuration for user1
Config user1 = config.getSubconfig("user1");
String name1 = user1.getProperty("name"); // Alex
int points1 = user1.getInt("points", 0); // 123

// get the configuration for user2
Config user2 = config.getSubconfig("user2");
String name2 = user1.getProperty("name"); // Tom
int points2 = user1.getInt("points", 0); // 10
```


## Components

You can also configure and create components (Java objects)
defined in the config file.

For example, consider the following class:

```java
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
```

The `SimpleComponent` implements the [`Configurable`](simpleconfig/src/main/java/com/oprisnik/simpleconfig/Configurable.java) interface.
Hence it can be configured using our config file:

```xml
<config>
    <component1 class="com.oprisnik.simpleconfig.SimpleComponent">
        <name>Alex</name>
    </component1>

    <component2 class="com.oprisnik.simpleconfig.SimpleComponent">
      <name>Tom</name>
    </component2>
</config>
```

In Java, we can access it as follows:

```java
SimpleComponent c1 = config.getComponentAndInit("component1", SimpleComponent.class);
String name1 = c1.getName(); // Alex

SimpleComponent c2 = config.getComponentAndInit("component2", SimpleComponent.class);
String name2 = c2.getName(); // Tom
```

The components do not necessarily need to implement the `Configurable` interface though.
You can load any class from your configuration file if you want to.
In this case the component will not be configured, only a new instance will be created using
the default constructor.

In Java, you can get any component (without initializing it) by calling

```java
MyClass something = config.getComponent("something", MyClass.class);
```

### Default Class Values

As you can see, we have to define a `class` attribute in the XML file.
If we want to omit this `class` attribute, we can add a default value to our Java code:

XML:

```xml
<config>
    <component>
        <name>Alex</name>
    </component>
</config>
```
Java:

```java
SimpleComponent component = config.getComponentAndInit("component",
                            SimpleComponent.class, SimpleComponent.class);

String name = component.getName(); // "Alex"
```

Since we did not specify a `class` attribute for `component1`, the default implementation
is used.

Suppose we have the following class:

```java
public class ExtendedComponent extends SimpleComponent {

  @Override
  public String getName() {
    return "Extended: " + super.getName();
  }
}
```

Now we can modify the XML configuration file from above:
```xml
<config>
  <component class="com.oprisnik.simpleconfig.ExtendedComponent">
    <name>Alex</name>
  </component>
</config>
```

And use the same Java code as before:

```java
SimpleComponent component = config.getComponentAndInit("component",
SimpleComponent.class, SimpleComponent.class);

String name = component.getName(); // "Extended: Alex"
```

The name will now be `Extended: Alex` since we have specified a
`class` attribute in the configuration file and the default value is not used.

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
