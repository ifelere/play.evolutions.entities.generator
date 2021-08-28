/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Encapsulates options for generating slick data mapping for entities
 * @author ifelere
 */
public class SlickMappingOptions {
   
    private File destination;

    /**
     * Get the value of destination
     *
     * @return the value of destination
     */
    public File getDestination() {
        return destination;
    }

    /**
     * Set the value of destination
     *
     * @param destination new value of destination
     */
    public void setDestination(File destination) {
        this.destination = destination;
    }
    
        private String packageName;

    /**
     * Get the value of packageName
     *
     * @return the value of packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Set the value of packageName
     *
     * @param packageName new value of packageName
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
        private String wrappClassName = "Tables";

    /**
     * Get the value of wrappClassName
     *
     * @return the value of wrappClassName
     */
    public String getWrappClassName() {
        if (wrappClassName == null) {
            wrappClassName = "Tables";
        }
        return wrappClassName;
    }

    /**
     * Set the value of wrappClassName
     *
     * @param wrappClassName new value of wrappClassName
     */
    public void setWrappClassName(String wrappClassName) {
        this.wrappClassName = wrappClassName;
    }
    
        private String slickProfile;

    /**
     * Get the value of slickProfile
     *
     * @return the value of slickProfile
     */
    public String getSlickProfile() {
        return slickProfile;
    }

    /**
     * Set the value of slickProfile
     *
     * @param slickProfile new value of slickProfile
     */
    public void setSlickProfile(String slickProfile) {
        this.slickProfile = slickProfile;
    }

    
    private static final Pattern PACKAGE_REGEX = Pattern.compile("^[a-z\\.]+$");
    
    
        private String additionalImports;

    /**
     * Get the value of additionalImports
     *
     * @return the value of additionalImports
     */
    public String getAdditionalImports() {
        return additionalImports;
    }

    /**
     * Set the value of additionalImports
     *
     * @param additionalImports new value of additionalImports
     */
    public void setAdditionalImports(String additionalImports) {
        this.additionalImports = additionalImports;
    }

        private String extensions;

    /**
     * Get the value of extensions
     *
     * @return the value of extensions
     */
    public String getExtensions() {
        return extensions;
    }

    /**
     * Set the value of extensions
     *
     * @param extensions new value of extensions
     */
    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

    
    
    public List<String> validate() {
        List<String> errors = new ArrayList<>(5);
        if (StringUtils.isEmpty(packageName)) {
            errors.add("Package name is required");
        }
        else if (!PACKAGE_REGEX.asPredicate().test(packageName)) {
            errors.add(String.format("Invalid package name: %s", packageName));
        }
        if (destination == null) {
            errors.add("Destination directory is required");
        }else if (!destination.exists()) {
            errors.add(String.format("Destination '%s' does not exist", destination.getAbsolutePath()));
        }
        if (StringUtils.isEmpty(slickProfile)) {
            errors.add("Slick jdbc profile is required");
        }
        return errors;
    }


    private static final String KEY_DESTINATION = "destination";
    private static final String KEY_PACKAGE = "packageName";
    private static final String KEY_PROFILE = "PROFILE";
    
    private static final String KEY_EXTENSIONS = "EXTENSIONS";
    
    private static final String KEY_CLASS_NAME = "CLASS_NAME";
    
    private static final String KEY_IMPORTS = "IMPORTS";
    
    
    public void write(Preferences pref) {
        if (destination != null) {
            pref.put(KEY_DESTINATION, destination.getAbsolutePath());
        }
        pref.put(KEY_PACKAGE, packageName);
        pref.put(KEY_PROFILE, slickProfile);
        pref.put(KEY_EXTENSIONS, extensions);
        pref.put(KEY_CLASS_NAME, wrappClassName);
        pref.put(KEY_IMPORTS, additionalImports);
    }
    
    public void read(Preferences pref) {
        String x = pref.get(KEY_DESTINATION, "");
        if (!x.isEmpty()) {
            destination = new File(x);
        }
        wrappClassName = pref.get(KEY_CLASS_NAME, null);
        slickProfile = pref.get(KEY_PROFILE, null);
        packageName = pref.get(KEY_PACKAGE, null);
        extensions = pref.get(KEY_EXTENSIONS, null);
        additionalImports = pref.get(KEY_IMPORTS, null);
    }

}
