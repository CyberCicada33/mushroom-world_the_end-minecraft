package sakrater.group.mushroomkiller;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class MushroomKiller extends JavaPlugin implements Listener {

    private String message;
    private boolean enableLogging;
    private Logger logger;
    private List<Material> mushroomTypes;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigSettings();
        initMushroomTypes();
        getServer().getPluginManager().registerEvents(this, this);
        logger = getLogger();
        logger.info("MushroomKiller插件已启用！");
    }
    @Override
    public void onDisable() {
        logger.info("MushroomKiller插件已禁用！");
    }
    private void loadConfigSettings() {
        message = getConfig().getString("message", "§c你不能在末地放置蘑菇!");
        enableLogging = getConfig().getBoolean("enable-logging", true);
    }
    private void initMushroomTypes() {
        mushroomTypes = Arrays.asList(
                Material.RED_MUSHROOM,
                Material.BROWN_MUSHROOM,
                Material.RED_MUSHROOM_BLOCK,
                Material.BROWN_MUSHROOM_BLOCK,
                Material.MUSHROOM_STEM
        );
        try {
            mushroomTypes.add(Material.valueOf("FLOWERING_RED_MUSHROOM"));
            mushroomTypes.add(Material.valueOf("FLOWERING_BROWN_MUSHROOM"));
            getLogger().info("成功添加开花蘑菇类型检测");
        } catch (IllegalArgumentException e) {
            getLogger().warning("当前服务器版本不支持开花蘑菇，已跳过这些类型的检测");
        }
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Material placedMaterial = event.getBlock().getType();
        if (!mushroomTypes.contains(placedMaterial)) {
            return;
        }
        World world = event.getBlock().getWorld();
        if (world.getEnvironment() != World.Environment.THE_END) {
            return;
        }
        event.setCancelled(true);
        event.getPlayer().sendMessage(message);
        if (enableLogging) {
            logger.info("玩家 " + event.getPlayer().getName() +
                    " 尝试在末地放置 " + placedMaterial.name() + "，已阻止。");
        }
    }
}
