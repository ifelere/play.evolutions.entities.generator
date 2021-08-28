/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

/**
 *
 * @author ifelere
 */
public class SQLParseException extends Exception {
    private int line = -1;

    public SQLParseException(int line) {
        this.line = line;
    }

    public SQLParseException(int line, String string) {
        super(string);
        this.line = line;
    }

    public SQLParseException(int line, String string, Throwable thrwbl) {
        super(string, thrwbl);
        this.line = line;
    }

    public SQLParseException(int line, Throwable thrwbl) {
        super(thrwbl);
        this.line = line;
    }

    public SQLParseException(int line, String string, Throwable thrwbl, boolean bln, boolean bln1) {
        super(string, thrwbl, bln, bln1);
        this.line = line;
    }

    public SQLParseException() {
    }

    public SQLParseException(String string) {
        super(string);
    }

    public SQLParseException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public SQLParseException(Throwable thrwbl) {
        super(thrwbl);
    }

    public SQLParseException(String string, Throwable thrwbl, boolean bln, boolean bln1) {
        super(string, thrwbl, bln, bln1);
    }

    public int getLine() {
        return line;
    }
    
    
    
    
}
