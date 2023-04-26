package com.aregcraft.reforging.command;

import com.aregcraft.delta.api.FormattingContext;
import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.command.CommandWrapper;
import com.aregcraft.delta.api.command.RegisteredCommand;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.target.Target;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RegisteredCommand("reforge")
public class ReforgeCommand implements CommandWrapper {
    @InjectPlugin
    private Reforging plugin;

    @Override
    public boolean execute(Player sender, List<String> args) {
        var item = ItemWrapper.wrap(sender.getInventory().getItemInMainHand());
        if (args.size() != 1 || !Target.isTarget(item)) {
            return false;
        }
        item.setFormattingContext(FormattingContext.withPlugin(plugin));
        var reforge = plugin.getReforges().findAny(args.get(0));
        if (reforge == null || !reforge.isApplicable(item)) {
            return false;
        }
        reforge.apply(sender, item, plugin);
        return true;
    }

    @Override
    public List<String> suggest(Player sender, List<String> args) {
        return args.size() == 1 ? new ArrayList<>(plugin.getReforges().getIds()) : null;
    }
}
