package xyz.oribuin.lilori.manager;

import xyz.oribuin.lilori.LilOri;

public abstract class Manager {

    protected final LilOri bot;

    public Manager(LilOri bot) {
        this.bot = bot;
    }

    /**
     * Loads the Manager's Settings
     */
    public abstract void enable();

}
