/*
 * Copyright 2015 Tomas David
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tomasdavid.vehicleroutingproblem.adapters;

/**
 * Statistic item for navigation drawer. Consists from image, name and value in one line.
 *
 * @author Tomas David
 */
public class StatisticItem {

    /**
     * Id of resource image.
     */
    private int imageResId;

    /**
     * Item name.
     */
    private String name;

    /**
     * Item value.
     */
    private String value;

    /**
     * Default constructor.
     * @param imageResId Id of resource image.
     * @param name Item name.
     * @param value Item value.
     */
    public StatisticItem(int imageResId, String name, String value) {
        this.imageResId = imageResId;
        this.name = name;
        this.value = value;
    }

    /**
     * Returns id of resource image.
     * @return Id of resource image.
     */
    public int getImageResId() {
        return imageResId;
    }

    /**
     * Returns name of item.
     * @return Item name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns value of item.
     * @return Item value.
     */
    public String getValue() {
        return value;
    }
}
