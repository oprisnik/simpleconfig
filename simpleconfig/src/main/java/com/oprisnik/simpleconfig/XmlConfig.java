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


import org.apache.commons.configuration.AbstractHierarchicalFileConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * XML configuration.
 * The configuration can also have nested sub-configurations.
 * <p>
 * Example:
 * <p>
 * <config>
 * <test>Hello world!</test>
 * <subconfig>
 * <param>My value</param>
 * <file>test.txt</file>
 * </subconfig>
 * </config>
 */
public class XmlConfig extends Config {

    protected HierarchicalConfiguration mConfiguration;

    private File mFile;

    private XmlConfig mParent = null;

    public XmlConfig() {
        mConfiguration = new XMLConfiguration();
    }

    public XmlConfig(XmlConfig parent, HierarchicalConfiguration config) {
        mParent = parent;
        mConfiguration = config;
        mFile = parent.mFile;
    }

    public XmlConfig(File file) throws BadConfigException, FileNotFoundException {
        try {
            if (!file.exists()) {
                throw new FileNotFoundException("File not found " + file);
            }
            mConfiguration = new XMLConfiguration(file);
            // we could also use the XPath engine
            // for example for tables/table[@name='users']/fields/name
//            mConfiguration.setExpressionEngine(new XPathExpressionEngine());
            mFile = file;
        } catch (ConfigurationException e) {
            throw new BadConfigException(e.getMessage());
        }
    }


    @Override
    public String getProperty(String key) {
        return (String) mConfiguration.getProperty(key);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return mConfiguration.getBoolean(key, defaultValue);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return mConfiguration.getInt(key, defaultValue);
    }

    @Override
    public void setProperty(String key, String value) {
        mConfiguration.setProperty(key, value);
    }

    @Override
    public boolean hasProperty(String key) {
        return mConfiguration.containsKey(key);
    }

    @Override
    public Config getSubconfig(String key) {
        try {
            SubnodeConfiguration subnodeConfiguration = mConfiguration.configurationAt(key);
            return new XmlConfig(this, subnodeConfiguration);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <U> U getComponent(String key, Class<U> baseInterface) throws BadConfigException {
        String clazz = getProperty(key == null ? "[@class]" : (key + "[@class]"));
        if (clazz == null) {
            throw new BadConfigException("Could not find component " + key);
        }
        try {
            Class<? extends U> c = Class.forName(clazz).asSubclass(baseInterface);
            return c.newInstance();
        } catch (Exception e) {
            throw new BadConfigException(e.getMessage());
        }
    }


    @Override
    public InputStream getNestedInputStream(String key) throws BadConfigException {
        File f = null;
        try {
            f = new File(getProperty(key));
            if (!f.isAbsolute()) {
                // relative path -> get correct path
                f = new File(mFile.getParentFile(), getProperty(key));
            }
            return new FileInputStream(f);
        } catch (FileNotFoundException e) {
            throw new BadConfigException("Nested file '" + key + "' not found at '" + f + "'");
        }
    }

    @Override
    public OutputStream getNestedOutputStream(String key) throws BadConfigException {
        File f = null;
        try {
            f = new File(getProperty(key));
            if (!f.isAbsolute()) {
                // relative path -> get correct path
                f = new File(mFile.getParentFile(), getProperty(key));
            }
            File parent = f.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            return new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            throw new BadConfigException("Nested file '" + key + "' not found at '" + f + "'");
        }
    }

    @Override
    public String getNestedPath(String key) throws BadConfigException {
        File f = new File(getProperty(key));
        if (!f.isAbsolute()) {
            // relative path -> get correct path
            f = new File(mFile.getParentFile(), getProperty(key));
        }
        File parent = f.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        return f.getAbsolutePath();
    }

    @Override
    protected boolean hasCustomClass(String key) {
        String clazz = getProperty(key == null ? "[@class]" : (key + "[@class]"), null);
        return clazz != null;
    }

    @Override
    public Collection<String> getCollection(String key) {
        Object obj = mConfiguration.getProperty(key);
        if (obj instanceof Collection) {
            return (Collection<String>) obj;
        } else if (obj instanceof String) {
            Collection<String> coll = new ArrayList<String>(1);
            coll.add((String) obj);
            return coll;
        }
        return null;

    }

    @Override
    public void saveTo(OutputStream output) throws Exception {
        if (mConfiguration instanceof AbstractHierarchicalFileConfiguration) {
            ((AbstractHierarchicalFileConfiguration) mConfiguration).save(output);
        } else {
            throw new BadConfigException("Configuration not AbstractHierarchicalFileConfiguration!");
        }
    }

    @Override
    public void save() throws Exception {
        if (mParent != null) {
            mParent.save();
            return;
        }
        if (mConfiguration instanceof AbstractHierarchicalFileConfiguration) {
            ((AbstractHierarchicalFileConfiguration) mConfiguration).save();
        } else {
            throw new BadConfigException("Configuration not AbstractHierarchicalFileConfiguration!");
        }
    }
}
