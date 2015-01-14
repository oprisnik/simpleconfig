
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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.Collection;

/**
 * Abstract config interface.
 * It allows to retrieve various properties, subconfigurations, and components.
 */
public abstract class Config {

    /**
     * Save the configuration.
     * * 
     * @throws Exception
     */
    public abstract void save() throws Exception;

    /**
     * Save the configuration to the given output stream.
     * 
     * @param output the output stream to save the config to
     * @throws Exception
     */
    public abstract void saveTo(OutputStream output) throws Exception;

    /**
     * Get the property with the given key.
     * 
     * @param key the key of the property
     * @return the property
     */
    public abstract String getProperty(String key);

    /**
     * Check if the configuration has a certain property
     *  
     * @param key the key of the property
     * @return true if the property exists
     */
    public abstract boolean hasProperty(String key);

    /**
     * Get a boolean property for a given key.
     *  
     * @param key the key for the property
     * @param defaultValue the default value to use if the property is not defined
     *                                          
     * @return the property or the default value if the property is not defined
     */
    public abstract boolean getBoolean(String key, boolean defaultValue);

    /**
     * Get an int property for a given key.
     *
     * @param key the key for the property
     * @param defaultValue the default value to use if the property is not defined
     *
     * @return the property or the default value if the property is not defined
     */
    public abstract int getInt(String key, int defaultValue);

    /**
     * Get a subconfiguration for a given key.
     *  
     * @param key the key for the subconfiguration
     * @return the subconfiguration or null if no subconfig with the given key is found
     */
    public abstract Config getSubconfig(String key);

    /**
     * Set or create the property with the given key to the given value.
     *  
     * @param key the key of the property
     * @param value the desired value
     */
    public abstract void setProperty(String key, String value);

    /**
     * Get a nested input stream for the given key.
     *  
     * @param key the key for the nested stream property
     * @return the nested stream
     * @throws BadConfigException
     */
    public abstract InputStream getNestedInputStream(String key) throws BadConfigException;
    
    /**
     * Get a nested output stream for the given key.
     *
     * @param key the key for the nested stream property
     * @return the nested stream
     * @throws BadConfigException
     */
    public abstract OutputStream getNestedOutputStream(String key) throws BadConfigException;

    /**
     * Get a nested path for a given key.
     *  
     * @param key the key for the nested path
     * @return the nested path
     * @throws BadConfigException
     */
    public abstract String getNestedPath(String key) throws BadConfigException;
    
    /**
     * Get a collection of values.
     * <p/>
     * Example:
     * <p/>
     * <list>
     * <string>data 1</string>
     * <string>data 2</string>
     * </list>
     * <p/>
     * By calling getCollection("list.string"), you get a collection containing "data 1" and "data 2".
     *
     * @param key the key for the collection
     * @return the string collection
     */
    public abstract Collection<String> getCollection(String key);


    /**
     * Check if the component with the given key has a custom class.
     *  
     * @param key the key of the component
     * @return true if a custom class has been defined
     */
    protected abstract boolean hasCustomClass(String key);

    /**
     * Get the defined component from the config object.
     * The component has to implement the given base interface or extend the given base class. 
     * The actual class of the component has to be defined in the configuration file.
     * If no class is defined, {@link com.oprisnik.simpleconfig.BadConfigException} will be thrown.
     *
     *  
     * @param baseInterface the base interface / class of the component.
     * @param <U> the base interface / class to be returned
     * @return the component
     * @throws BadConfigException
     */
    public <U> U getComponent(Class<U> baseInterface) throws BadConfigException {
        return getComponent(null, baseInterface);
    }

    /**
     * Get the component with a certain key.
     * The component has to implement the given base interface or extend the given base class. 
     * The actual class of the component has to be defined in the configuration file.
     * If no class is defined, {@link com.oprisnik.simpleconfig.BadConfigException} will be thrown. 
     *  
     * @param key the key for the component 
     * @param baseInterface the base interface / class of the component.
     * @param <U> the base interface / class to be returned
     * @return the component
     * @throws BadConfigException
     */
    public abstract <U> U getComponent(String key, Class<U> baseInterface) throws BadConfigException;
    
    /**
     * Get the component with a certain key.
     * The component has to implement the given base interface or extend the given base class. 
     * The actual class of the component has to be defined in the configuration file.
     * If no class is defined, the given default implementation will be used.
     *
     * @param key the key for the component 
     * @param baseInterface the base interface / class of the component.
     * @param defaultImplementation the default implementation to use if no custom class has been defined                      
     * @param <U> the base interface / class to be returned
     * @return the component
     * @throws BadConfigException
     */
    public <U> U getComponent(String key, Class<U> baseInterface, Class<? extends U> defaultImplementation)
            throws BadConfigException {
        if (hasCustomClass(key)) {
            return getComponent(key, baseInterface);
        }
        try {
            if (defaultImplementation == null) {
                return null;
            }
            return defaultImplementation.newInstance();
        } catch (Exception e) {
            throw new BadConfigException("Could not instantiate default implementation "
                    + defaultImplementation);
        }
    }
    
    /**
     * Get and initialize the defined component from the config object.
     * The component has to implement the given base interface or extend the given base class.
     * The component will be initialized with the given component configuration.
     * Hence, the base interface has to implement the {@link com.oprisnik.simpleconfig.Configurable} interface.
     * The actual class of the component has to be defined in the configuration file.
     * If no class is defined, {@link com.oprisnik.simpleconfig.BadConfigException} will be thrown.
     *
     *
     * @param baseInterface the base interface / class of the component.
     * @param <U> the base interface / class to be returned
     * @return the component
     * @throws BadConfigException
     */
    public <U extends Configurable> U getComponentAndInit(Class<U> baseInterface) throws BadConfigException {
        U instance = getComponent(baseInterface);
        instance.init(this);
        return instance;
    }

    /**
     * Get and initialize the component with a certain key.
     * The component has to implement the given base interface or extend the given base class.
     * The component will be initialized with the given component configuration.
     * Hence, the base interface has to implement the {@link com.oprisnik.simpleconfig.Configurable} interface.
     * The actual class of the component has to be defined in the configuration file.
     * If no class is defined, {@link com.oprisnik.simpleconfig.BadConfigException} will be thrown.
     *
     * @param key the key for the component 
     * @param baseInterface the base interface / class of the component.
     * @param <U> the base interface / class to be returned
     * @return the component
     * @throws BadConfigException
     */
    public <U extends Configurable> U getComponentAndInit(String key, Class<U> baseInterface)
            throws BadConfigException {
        Config conf = getSubconfig(key);
        if (conf == null) {
            throw new BadConfigException("Could not load component for key: " + key);
        }
        return conf.getComponentAndInit(baseInterface);
    }
    
    /**
     * Get and initialize the component with a certain key.
     * The component has to implement the given base interface or extend the given base class. * 
     * The component will be initialized with the given component configuration.
     * Hence, the base interface has to implement the {@link com.oprisnik.simpleconfig.Configurable} interface.
     * The actual class of the component has to be defined in the configuration file.
     * If no class is defined, the given default implementation will be used.
     *
     * @param key the key for the component 
     * @param baseInterface the base interface / class of the component.
     * @param defaultImplementation the default implementation to use if no custom class has been defined                     
     * @param <U> the base interface / class to be returned
     * @return the component
     * @throws BadConfigException
     */
    public <U extends Configurable> U getComponentAndInit(String key, Class<U> baseInterface,
                                                          Class<? extends U> defaultImplementation)
            throws BadConfigException {
        if (hasCustomClass(key)) {
            return getComponentAndInit(key, baseInterface);
        }
        Config conf = getSubconfig(key);
        if (conf == null) {
            throw new BadConfigException("Could not load component for key: " + key);
        }
        try {
            if (defaultImplementation != null) {
                U instance = defaultImplementation.newInstance();
                instance.init(conf);
                return instance;
            }
        } catch (Exception e) {
            throw new BadConfigException("Could not instantiate default implementation " + defaultImplementation + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Check if the component with the given key has been defined.
     *  
     * @param key the key for the component
     * @return true if the component has been defined
     */
    public boolean hasComponent(String key) {
        Config conf = getSubconfig(key);
        return conf != null;
    }

    /**
     * Get the property with the given key.
     * If the property is not defined, the default value will be returned.
     *  
     * @param key the key of the property
     * @param defaultValue the default value
     * @return the property or the default value if the property has not been defined
     */
    public String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return (val == null) ? defaultValue : val;
    }

    /**
     * Load an object from a nested input stream.
     * 
     * @see #getNestedInputStream(String)
     *
     * @param key the key for the nested input stream
     * @return the object loaded from the given input stream
     * @throws BadConfigException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public Object readObjectFromNestedStream(String key)
            throws BadConfigException, ClassNotFoundException, IOException {
        Object o = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(getNestedInputStream(key));
            o = ois.readObject();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (Exception e) {
                }
            }
        }
        return o;
    }
}
