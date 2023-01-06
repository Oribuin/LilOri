package xyz.oribuin.lilori.manager;

import xyz.oribuin.lilori.LilOri;

public abstract class Manager {

    protected final LilOri bot;

    protected Manager(LilOri bot) {
        this.bot = bot;
    }

    /**
     * Loads the Manager's Settings
     */
    public abstract void enable();

    /**
     * Unloads the Manager's Settings
     */
    public abstract void disable();

}
