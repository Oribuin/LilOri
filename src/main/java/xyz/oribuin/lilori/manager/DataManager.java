package xyz.oribuin.lilori.manager;

import xyz.oribuin.lilori.LilOri;

import java.util.HashMap;
import java.util.Map;

public class DataManager extends Manager {

    private final Map<String, String> faqMap = new HashMap<>();

    protected DataManager(LilOri bot) {
        super(bot);
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }
}
