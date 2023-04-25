package it.polimi.ingsw.utils.request;

public class LoginRequest {
    private String nickname;

    public LoginRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
