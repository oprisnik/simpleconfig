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


/**
 * Simple test component that can be configured.
 */
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
