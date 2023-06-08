package com.ape.utility;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class NameFilter {
    public String getNamesWithFilter(String word){
        StringBuilder sb = new StringBuilder();
        String[] words = word.split(" ");
        if (words.length>1){
            for (String each : words) {
                if (each.length()>3) {
                    sb.append(each.substring(0, 1).toUpperCase());
                    sb.append(each.substring(1).toLowerCase());
                    sb.append(" ");
                }else{
                    sb.append(each.toUpperCase(new Locale("en","US")));
                    sb.append(" ");
                }
            }
        }else{
            if (words[0].length()>3){
                sb.append(words[0].substring(0, 1).toUpperCase());
                sb.append(words[0].substring(1).toLowerCase());
            }else{
                sb.append(words[0].toUpperCase(new Locale("en","US")));
            }
        }

        return sb.toString().trim();
    }
}
