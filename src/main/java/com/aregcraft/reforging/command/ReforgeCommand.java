package com.aregcraft.reforging.command;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.command.CommandWrapper;
import com.aregcraft.delta.api.command.RegisteredCommand;
import com.aregcraft.delta.api.item.ItemWrapper;
import com.aregcraft.reforging.Reforging;
import com.aregcraft.reforging.Weapon;
import org.bukkit.entity.Player;

import java.util.List;

@RegisteredCommand("reforge")
public class ReforgeCommand implements CommandWrapper {
    @InjectPlugin
    private Reforging plugin;

    @Override
    public boolean execute(Player sender, List<String> args) {
        var item = ItemWrapper.wrap(sender.getInventory().getItemInMainHand());
        if (args.size() != 1 || !Weapon.isWeapon(item)) {
            return false;
        }
        var reforge = plugin.getReforge(args.get(0));
        if (reforge == null) {
            return false;
        }
        reforge.apply(sender, item, plugin);
        return true;
    }

    @Override
    public List<String> suggest(Player sender, List<String> args) {
        return args.size() == 1 ? plugin.getReforgeIds() : null;
    }
}
