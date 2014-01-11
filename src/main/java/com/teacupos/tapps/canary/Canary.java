package com.teacupos.tapps.canary;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.teacupos.tapps.canary.util.PRunnable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;

public class Canary extends JavaPlugin {
    private Server server;
    private InetSocketAddress address;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        try {
            InetAddress host = InetAddress.getByName(getConfig().getString("host", "default").equals("default") ? Bukkit.getIp() : getConfig().getString("host"));
            int port = getConfig().getInt("port", 25588);
            address = new InetSocketAddress(host, port);
        } catch (UnknownHostException e) {
            getLogger().severe("Error regarding host and port. Please ensure values in config.yml are correct. Plugin will be disabled.");
            e.printStackTrace();
            getPluginLoader().disablePlugin(this);
            return;
        }

        getServer().getScheduler().runTaskAsynchronously(this, new PRunnable<Canary>(this) {

            @Override
            public void run(Canary canary) {
                canary.server = new Server(canary.address);
                canary.server.setHandler(new RequestHandler(canary));
                try {
                    canary.server.start();
                    canary.server.join();
                } catch (Exception e) {
                    canary.getLogger().severe("Could not start Canary HTTP server. Is the host and port available and open? Plugin will be disabled.");
                    e.printStackTrace();
                    canary.getPluginLoader().disablePlugin(canary);
                }
            }

        });
    }

    @Override
    public void onDisable() {

    }

}
