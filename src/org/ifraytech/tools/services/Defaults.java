/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools.services;

import java.io.File;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.ifraytech.tools.ui.SlickMappingOptions;

/**
 * Holds defaults used throughout the application
 * @author ifelere
 */
public class Defaults {
    private static final String CATCH_KEY = "app.defaults";
    private Preferences preferences;
    
    
    private static final String KEY_LAST_DESTINATION_DIR = "last_output_directory";
    private static final String KEY_OVERWRITE = "should_overwrite";
    private static final String KEY_SOURCE_DIR = "last_source_directory";
    private static final String KEY_LAST_NAMESPACE = "last_namespace";
    
    private static final String KEY_MAPPING_OPTIONS = "last_mapping_options";
    
    private SlickMappingOptions lastMappingOptions;

    /**
     * Get the value of lastMappingOptions
     *
     * @return the value of lastMappingOptions
     */
    public SlickMappingOptions getLastMappingOptions() {
        if (lastMappingOptions == null) {
            SlickMappingOptions sm = new SlickMappingOptions();
            lastMappingOptions = sm;
            sm.read(getPreferences().node(KEY_MAPPING_OPTIONS));
        }
        return lastMappingOptions;
    }

    /**
     * Set the value of lastMappingOptions
     *
     * @param lastMappingOptions new value of lastMappingOptions
     */
    public void setLastMappingOptions(SlickMappingOptions lastMappingOptions) {
        this.lastMappingOptions = lastMappingOptions;
    }

    
    
    private static Defaults instance;

    public static Defaults getInstance() {
        if (instance == null) {
            instance = new Defaults();
            instance.load();
        }
        return instance;
    }
    
    
    
    
    private File lastOutputDirectory;

    /**
     * Get the value of lastOutputDirectory
     *
     * @return the value of lastOutputDirectory
     */
    public File getLastOutputDirectory() {
        return lastOutputDirectory;
    }

    /**
     * Set the value of lastOutputDirectory
     *
     * @param lastOutputDirectory new value of lastOutputDirectory
     */
    public void setLastOutputDirectory(File lastOutputDirectory) {
        this.lastOutputDirectory = lastOutputDirectory;
    }
    
        private boolean overwrite;

    /**
     * Get the value of overwrite
     *
     * @return the value of overwrite
     */
    public boolean isOverwrite() {
        return overwrite;
    }

    /**
     * Set the value of overwrite
     *
     * @param overwrite new value of overwrite
     */
    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    
        private File lastSourceDirectory;

    /**
     * Get the value of lastSourceDirectory
     *
     * @return the value of lastSourceDirectory
     */
    public File getLastSourceDirectory() {
        return lastSourceDirectory;
    }

    /**
     * Set the value of lastSourceDirectory
     *
     * @param lastSourceDirectory new value of lastSourceDirectory
     */
    public void setLastSourceDirectory(File lastSourceDirectory) {
        this.lastSourceDirectory = lastSourceDirectory;
    }
    
        private String lastNameSpace;

    /**
     * Get the value of lastNameSpace
     *
     * @return the value of lastNameSpace
     */
    public String getLastNameSpace() {
        return lastNameSpace;
    }

    /**
     * Set the value of lastNameSpace
     *
     * @param lastNameSpace new value of lastNameSpace
     */
    public void setLastNameSpace(String lastNameSpace) {
        this.lastNameSpace = lastNameSpace;
    }

    
    public void load() {
        Optional.of(getPreferences()).ifPresent((pref) -> {
            this.lastOutputDirectory = getFile(pref, KEY_LAST_DESTINATION_DIR);
            this.overwrite = pref.getBoolean(KEY_OVERWRITE, true);
            this.lastSourceDirectory = getFile(pref, KEY_SOURCE_DIR);
            this.lastNameSpace = pref.get(KEY_LAST_NAMESPACE, "");
        });
    }
    
    public void save() {
        Optional.of(getPreferences()).ifPresent((pref) -> {
            saveFile(pref, KEY_LAST_DESTINATION_DIR, lastOutputDirectory);
            saveFile(pref, KEY_SOURCE_DIR, lastSourceDirectory);
            pref.putBoolean(KEY_OVERWRITE, overwrite);
            pref.put(KEY_LAST_NAMESPACE, lastNameSpace);
            
            
            try {
                pref.flush();
                if (lastMappingOptions != null) {
                    Preferences cp = pref.node(KEY_MAPPING_OPTIONS);
                    lastMappingOptions.write(cp);
                    cp.flush();
                }
            } catch (BackingStoreException ex) {
                Logger.getLogger(Defaults.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(Defaults.class).node(CATCH_KEY);
        }
        return preferences;
    }
    
    private static File getFile(Preferences p, String key) {
        String value = p.get(key, "");
        if (!value.isEmpty()) {
            File f = new File(value);
            if (f.exists()) {
                return f;
            }
        }
        return null;
    }
    
    
    private static void saveFile(Preferences p, String key, File value) {
        if (value == null) {
            p.remove(key);
        } else {
            p.put(key, value.getAbsolutePath());
        }
    }
    


}
