package com.example.qp;

import java.util.ArrayList;
import java.util.Random;

public class Quotes {

    public ArrayList<String> quoteList = new ArrayList<>();

    public Quotes(){
        populateArray();
    }

    public void populateArray(){
        quoteList.add("\"Don’t Let Yesterday Take Up Too Much Of Today.\"” \n– Will Rogers");
        quoteList.add("\"Dreams are a lot like rainbows, only idiots follow them.\"” \n– Unknown");
        quoteList.add("\"The past cannot be changed. The future is yet in your power.\"” \n– Unknown");
        quoteList.add("\"Life is tough, but it's tougher if you're stupid\"” \n– Unknown");
        quoteList.add("\"Quality is not an act, it is a habit.\"” \n– Aristotle");
        quoteList.add("\"Well done is better than well said.\"” \n– Benjamin Franklin");
    }

    public String getRandomQuote(){
        Random random = new Random();
        int ranNum = random.nextInt(quoteList.size() - 1);
        return quoteList.get(ranNum);
    }

}
