package it.polimi.ingsw.view.client;

public enum ClientStatus {
    LOGIN, //you can only log in
    CREATE_OR_SELECT_GAME, //you can create or select a game
    GAME, //you can select or insert tiles, and you can chat
    ENDED_GAME, //you can only exit
    ERROR // network error
}