package com.dudaev.khalid.tools;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class File {

    String aaa;

    /**
     * 
     * @param path
     * @return
     */
    public String read(String path) {
        String out = "";
        Charset encoding = StandardCharsets.UTF_8;
        return this.read(path, encoding);
    }

    /**
     * 
     * @param path
     * @param encoding
     * @return
     */
    public String read(String path, Charset encoding) {
        java.io.File file = new java.io.File(path);
        path = file.getAbsolutePath();
        String out = "";

        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            out = new String(encoded, encoding);
        } catch (IOException e) {
            System.err.println("ERROR READ FILE: " + e);
        }

        return out;
    }
}
