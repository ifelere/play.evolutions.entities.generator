/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

/**
 * Generates names to be used as class name or variable names from database table or column names
 * @author ifelere
 */
public class NameGenerator {
    
    private static final PluralMapping[] PLURAL_MAPPINGS = {
        new PluralMapping("([nkr])ies$", "$1ey"),
        new PluralMapping("([^nkr])ies$", "$1y"),
//        new PluralMapping("ses$", "s"),
        new PluralMapping("s$", "")
    };
    
    /**
     * Transforms the given text to an appropriate name for a class
     * @param text The SQL name
     * @return appropriate name for a class
     */
    public String generateClassName(String text) {
        if (text == null) {
            return null;
        }
        if (SHOULD_SPLIT.matcher(text).find()) {
            Stream<String> parts = Arrays.asList(SHOULD_SPLIT.split(text)).stream().map((x) -> StringUtils.capitalize(x.toLowerCase()));
            
            return makeSingular(parts.collect(Collectors.joining("")));
            
        }
        if (ALL_UPPER_CASE.matcher(text).matches()) {
            return makeSingular(StringUtils.capitalize(text.toLowerCase()));
        }
        if (!Character.isUpperCase(text.charAt(0))) {
            return makeSingular(StringUtils.capitalize(text));
        }
        return makeSingular(text);
        
    }
    
    /**
     * Transforms the given text to an appropriate name for a class variable or method name
     * @param text The SQL text
     * @return appropriate name for a class variable or method name
     */
    public String generateVariable(String text) {
        if (text == null) {
            return null;
        }
        if (SHOULD_SPLIT.matcher(text).find()) {
            List<String> list = Arrays.asList(SHOULD_SPLIT.split(text));
            String rest = list.stream().skip(1).map((x) -> StringUtils.capitalize(x.toLowerCase())).collect(Collectors.joining(""));
            return toLowerCaseFirst(list.get(0)) + rest;
        }
        if (ALL_UPPER_CASE.matcher(text).matches()) {
            return text.toLowerCase();
        }
        return toLowerCaseFirst(text);
    }
    
    /**
     * Gets text version where first character is lower case
     * @param text
     * @return 
     */
    public static String toLowerCaseFirst(String text) {
        if (Character.isUpperCase(text.charAt(0))) {
            return Character.toLowerCase(text.charAt(0)) + text.substring(1);
        }
        return text;
    }
    
     private static final Pattern SHOULD_SPLIT = Pattern.compile("\\s+|_|\\p{Punct}");
    
     private static final Pattern ALL_UPPER_CASE = Pattern.compile("^[A-Z]+$");
    
    private static String makeSingular(String text) {
        for (PluralMapping p : PLURAL_MAPPINGS) {
            Optional<String> r = p.handle(text);
            if (r.isPresent()) {
                return r.get();
            }
        }
        return text;
    }
    
    private static class PluralMapping {
        private final String replacement;

        private final Pattern lowerCasePattern, upperCasePattern;
        
        public PluralMapping(String pattern, String replacement) {
            this.replacement = replacement;
            lowerCasePattern = Pattern.compile(pattern.toLowerCase());
            upperCasePattern = Pattern.compile(pattern.toUpperCase());
        }
        
        public Optional<String> handle(String text) {
            Matcher m = lowerCasePattern.matcher(text);
            if (m.find()) {
                return Optional.of(m.replaceFirst(replacement.toLowerCase()));
            }
            m = upperCasePattern.matcher(text);
            if (m.find()) {
                return Optional.of(m.replaceFirst(replacement.toUpperCase()));
            }
            return Optional.empty();
        }
        
    }
}
