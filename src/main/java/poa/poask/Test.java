package poa.poask;

import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import poa.poask.util.reflection.*;
import poa.poask.util.reflection.common.SendPacket;

public class Test implements CommandExecutor {
    @SneakyThrows
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player){
          //  SendPacket.sendPacket(player, SetEquipmentPacket.packet(player.getTargetEntity(10).getEntityId(), args[0], player.getInventory().getItemInMainHand()));

        }
        return false;
    }
}
