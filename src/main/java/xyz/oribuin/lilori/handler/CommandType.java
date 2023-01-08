package xyz.oribuin.lilori.handler;

public enum CommandType {
    SLASH, // Slash Command (Discord Command)
    MESSAGE, // Message Command (right-click on message)
    USER, // User Command (right-click on a user)
    LEGACY // Legacy Command (?example)
}
