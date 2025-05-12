package com.zhouzhou.cloud.websocketservice.service;

import org.springframework.stereotype.Component;

@Component
public class NettyBannerPrinter {

    public void printBanner(String host, int port) {
        String RED = "\u001B[31m";
        String GREEN = "\u001B[32m";
        String RESET = "\u001B[0m";

        String banner = "\n" +
                "+-----------------------------------------------------------------+\n" +
                "|   _   _      _ _                 _        _              _      |\n" +
                "|  | \\ | |    | | |               | |      | |            | |     |\n" +
                "|  |  \\| | ___| | | ___  ___  ___ | |_ ___ | |_ ___   ___ | | __  |\n" +
                "|  | . ` |/ _ \\ | |/ _ \\/ __|/ _ \\| __/ _ \\| __/ _ \\ / _ \\| |/ /  |\n" +
                "|  | |\\  |  __/ | |  __/\\__ \\ (_) | || (_) | || (_) | (_) |   <   |\n" +
                "|  |_| \\_|\\___|_|_|\\___||___/\\___/ \\__\\___/ \\__\\___/ \\___/|_|\\_\\  |\n" +
                "|                                                                 |\n" +
                "|      Netty - Websocket - Cluster   " + RED + "Successfully started !" + RESET + "       |\n" +
                "|  " + GREEN + "    Address: " + host + ":" + port + "       Author：Sr.Zhou" + RESET + "          |\n" +
                "+-----------------------------------------------------------------+\n";

        System.out.println(banner);

    }
}

