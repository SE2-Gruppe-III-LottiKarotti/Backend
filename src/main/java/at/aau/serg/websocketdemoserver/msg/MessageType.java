package at.aau.serg.websocketdemoserver.msg;


public enum MessageType {
    SPIELER, TEST, GAMEBOARD, OPEN_ROOM, JOIN_ROOM, LIST_ROOMS, DRAW_CARD, CHAT, HEARTBEAT

    /**folgende Nachrichten werden benötigt
     * -TEST (init message at start)
     * -OPEN_OR_JOIN_ROOM (msg for open room && join room)
     * -CHAT (msg for chat)
     * -ROOM_LIST (msg to ask for room list and to return roomList as arrayList)
     * -DRAW_CARD (msg to draw card)
     * -GAMEBOARD (msg to fetch gameboard)
     * -HEARTBEAT (msg to keep connection on purpose alive)
     * */
}
