package cz.perwin.digitalclock.core;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Date;

public class CountdownEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Clock clock;
    private final Date endtime;

    public CountdownEndEvent(Clock clock) {
        this.clock = clock;
        this.endtime = new Date();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Clock getClock() {
        return this.clock;
    }

    public Date getEndTime() {
        return this.endtime;
    }
}
