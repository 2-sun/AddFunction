package com.sun2.addfunction;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AFCommand implements CommandExecutor {
    AddFunction mainPlugin;
    HashMap<Player, Player> requests = new HashMap<>(); // teleport 요청들을 담을 변수


    public AFCommand(AddFunction plugin) {
        mainPlugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("af")) { // 입력한 명령어가 대소문자와 관계없이 'af'로 시작한다면
            if (!(sender instanceof Player)) return false; // 입력한게 플레이어가 아니라면(ex. console) false
            Player p = (Player) sender; // 입력한 플레이어의 타입 CommandSender => Player

            if (args.length == 0) { // 입력한 명령어가 '/af'라면
                helpMessage(p);
            }
            else if (args[0].equalsIgnoreCase("tp")) { // 입력한 명령어가 '/af tp'라면
                try {
                    Player target = mainPlugin.getServer().getPlayer(args[1]); // 입력받은 플레이어 저장
                    if (target == p) { // 입력받은 플레이어가 본인 이라면
                        p.sendMessage("cannot teleport to yourself");
                        return false;
                    }
                    target.sendMessage(p.getName() + " sent teleport request");
                    target.sendMessage("/af tpa : accept teleport request");
                    target.sendMessage("/af tpd : deny teleport request");

                    requests.put(target, p); // teleport 요청을 변수에 담음
                } catch (Exception e) { // 입력받은 것이 플레이어가 아니라면
                    p.sendMessage("enter correct player name");
                }
            }
            else if  (args[0].equalsIgnoreCase("tpa")) { // 입력한 명령어가 '/af tpa'라면
                if (requests.get(p) == null) { // teleport 요청을 담은 변수에 플레이어가 없다면
                    p.sendMessage("no received teleport request");
                }
                else {
                    requests.get(p).teleport(p.getLocation());
                    requests.get(p).sendMessage("teleported");
                }
            }
            else if (args[0].equalsIgnoreCase("tpd")) { // 입력한 명령어가 '/af tpd'라면
                if (requests.get(p) == null) {
                    p.sendMessage("no received teleport request");
                }
                else {
                    requests.get(p).sendMessage("denied teleport request");
                }
            }

            else if (args[0].equalsIgnoreCase("sethome")) { // 입력한 명령어가 '/af sethome'라면
                Location location = p.getLocation();
                mainPlugin.getConfig().set("homes." + p.getName(), location);
                // config에 아래와 같이 설정
                // home:
                //   player's name : 현재 플레이어 위치
                mainPlugin.saveConfig();
                p.sendMessage("home : " + "x: " + location.getBlockX() + " y: " + location.getBlockY() + " z: " + location.getBlockZ());
            }
            else if (args[0].equalsIgnoreCase("home")) { // 입력한 명령어가 '/af home'라면
                Location location = mainPlugin.getConfig().getLocation("homes." + p.getName()); // config에 저장된 위치 location 변수로 저장

                if (location == null) { // config에 저장된 위치가 없다면
                    p.sendMessage("set up home first");
                }
                else {
                    p.teleport(location);
                }
            }

            else if (args[0].equalsIgnoreCase("manageInv")) { // 입력한 명령어가 '/af manageInv'라면
                try {
                    Player target = mainPlugin.getServer().getPlayer(args[1]); // 입력받은 플레이어 저장
                    if (target == p) { // 입력받은 플레이어가 본인 이라면
                        p.sendMessage("cannot manage your inventory");
                        return false;
                    }
                    p.openInventory(getPlayerInventory(target));

                } catch (Exception e) { // 입력받은 것이 플레이어가 아니라면
                    p.sendMessage("enter correct player name");
                }
            }

            else if (args[0].equalsIgnoreCase("reload")) { // 입력한 명령어가 '/af reload'라면
                mainPlugin.reloadConfig();
                p.sendMessage("reload success");
            }

            else {
                helpMessage(p);
            }
        }
        return false;
    }

    private void helpMessage(Player p) {
        p.sendMessage("/af : help message");
        p.sendMessage("/af tp <player> : send teleport request");
        p.sendMessage("/af sethome : set this location to home");
        p.sendMessage("/af home : teleport to home");
        p.sendMessage("/af manageInv <player> : manage player inventory");
        p.sendMessage("/af reload : reload config");
    }

    private Inventory getPlayerInventory(Player p) {
        Inventory inv = mainPlugin.getServer().createInventory(null, 54, p.getName());
        ItemStack blackGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        PlayerInventory playerInventory = p.getInventory();

        ArrayList<ItemStack> itemStacks = new ArrayList<>(Arrays.asList(playerInventory.getStorageContents()));

        for (int i=0; i<36; i++) {
            inv.setItem(i, itemStacks.get(i));
        }
        for (int i=36; i<45; i++) {
            inv.setItem(i, blackGlass);
        }

        inv.setItem(45, p.getInventory().getItemInOffHand());
        inv.setItem(46, blackGlass);
        inv.setItem(47, playerInventory.getHelmet());
        inv.setItem(48, playerInventory.getChestplate());
        inv.setItem(49, playerInventory.getLeggings());
        inv.setItem(50, playerInventory.getBoots());
        inv.setItem(51, blackGlass);
        inv.setItem(52, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        inv.setItem(53, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));

        return inv;
    }
}
