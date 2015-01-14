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

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Configuration factory to retrieve {@link Config} objects.
 */
public class ConfigFactory {

    /**
     * Load a given {@link Config} from a file.
     *  
     * @param file  the config file
     * @return the {@link Config}
     * @throws BadConfigException
     * @throws FileNotFoundException
     */
    public static Config fromFile(String file) throws BadConfigException, FileNotFoundException {
        return fromFile(new File(file));
    }
    
    /**
     * Load a given {@link Config} from a file.
     *
     * @param file  the config file
     * @return the {@link Config}
     * @throws BadConfigException
     * @throws FileNotFoundException
     */
    public static Config fromFile(File file) throws BadConfigException, FileNotFoundException {
        return new XmlConfig(file);
    }

    private ConfigFactory() {
    }
}
