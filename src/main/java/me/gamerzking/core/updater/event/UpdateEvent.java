package me.gamerzking.core.updater.event;

import me.gamerzking.core.updater.UpdateType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by GamerzKing on 4/18/2016.
 */
public class UpdateEvent extends Event 
{
    private static final HandlerList handlers = new HandlerList();
    private UpdateType type;

    public UpdateEvent(UpdateType type)
    {
        this.type = type;
    }

    public UpdateType getType()
    {
        return type;
    }

    public HandlerList getHandlers() 
    {
        return handlers;
    }

    public static HandlerList getHandlerList() 
    {
        return handlers;
    }
}