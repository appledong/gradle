/*
 * Copyright 2007-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.dependencies;

import org.gradle.api.UnknownDependencyNotation;
import org.gradle.api.dependencies.Dependency;
import org.gradle.api.dependencies.ExcludeRuleContainer;
import org.gradle.api.dependencies.DependencyConfigurationMappingContainer;

import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.List;

/**
* @author Hans Dockter
*/
public abstract class AbstractDependency implements Dependency {
    private Object userDependencyDescription;

    private DependencyDescriptorFactory dependencyDescriptorFactory = new DefaultDependencyDescriptorFactory();

    private ExcludeRuleContainer excludeRules = new DefaultExcludeRuleContainer();

    private DependencyConfigurationMappingContainer dependencyConfigurationMappings = new DefaultDependencyConfigurationMappingContainer();

    public AbstractDependency(Set<String> confs, Object userDependencyDescription) {
        if (!(isValidType(userDependencyDescription)) || !isValidDescription(userDependencyDescription)) {
            throw new UnknownDependencyNotation("Description " + userDependencyDescription + " not valid!");
        }
        dependencyConfigurationMappings.addMasters(confs.toArray(new String[confs.size()]));
        this.userDependencyDescription = userDependencyDescription;
    }

    public abstract boolean isValidDescription(Object userDependencyDescription);

    public abstract Class[] userDepencencyDescriptionType();

    private boolean isValidType(Object userDependencyDescription) {
        for (Class clazz : userDepencencyDescriptionType()) {
            if (clazz.isAssignableFrom(userDependencyDescription.getClass())) {
                return true; 
            }
        }
        return false;
    }

    public void initialize() {}

    public Object getUserDependencyDescription() {
        return userDependencyDescription;
    }

    public void setUserDependencyDescription(Object userDependencyDescription) {
        this.userDependencyDescription = userDependencyDescription;
    }

    public DependencyDescriptorFactory getDependencyDescriptorFactory() {
        return dependencyDescriptorFactory;
    }

    public void setDependencyDescriptorFactory(DependencyDescriptorFactory dependencyDescriptorFactory) {
        this.dependencyDescriptorFactory = dependencyDescriptorFactory;
    }

    public Dependency exclude(Map<String, String> excludeProperties) {
        excludeRules.add(excludeProperties);
        return this;
    }

    public Dependency exclude(Map<String, String> excludeProperties, List<String> confs) {
        excludeRules.add(excludeProperties, confs);
        return this;
    }

    public ExcludeRuleContainer getExcludeRules() {
        return excludeRules;
    }

    public void setExcludeRules(ExcludeRuleContainer excludeRules) {
        this.excludeRules = excludeRules;
    }

    public DependencyConfigurationMappingContainer getDependencyConfigurationMappings() {
        return dependencyConfigurationMappings;
    }

    public void setDependencyConfigurationMappings(DependencyConfigurationMappingContainer dependencyConfigurationMappings) {
        this.dependencyConfigurationMappings = dependencyConfigurationMappings;
    }

    public void dependencyConfigurations(String... dependencyConfigurations) {
        dependencyConfigurationMappings.add(dependencyConfigurations);
    }

    public void dependencyConfigurations(Map<String, List<String>> dependencyConfigurations) {
        dependencyConfigurationMappings.add(dependencyConfigurations);
    }
}
