# Abstrakte Klassen (Real World Example)
Ich wurde schon öfter gefragt, für was man "im echten Leben" nun wirklich abstrakte Klassen oder Interfaces brauchen könnte. Daher hier ein Beispiel, welches ich so oder so ähnlich schon des öfteren umgesetzt habe.

## Discord Bot
Nehmen wir an, wir wollen einen Discord-Bot schreiben:

```java
class BotEventListener extends ListenerAdapter {

    @Override
    public void onMessageReceived (MessageReceivedEvent event) {
        String username = event.getAuthor().getName();
        String message = event.getMessage().getText();

        System.out.println("Nachricht von " + username + ": " + message);
    }

}
```

Die Methode `onMessageReceived` wird von der Discordbibliothek unserer Wahl aufgerufen, sobald der Discord-Bot eine neue Nachricht empfängt:

![img](assets/discord-1.png)

Die Klassen `ListenerAdapter` und `MessageReceivedEvent` sind von den Discordbilbiothek. Bis jetzt haben wir also nichts, außer eine Nachricht in die Konsole schreiben lassen, sobald der Discord-Bot eine Nachricht empfängt.

Nun aber zu den abstrakten Klassen:

### Command-System
Wir möchten für unseren Discord-Bot, der aktuell noch ziemlich unnütz ist, Befehle hinzfügen. Z. B. 
* `!uhrzeit`,
* `!ping`, 
* `!hallo`

#### Der "normale" Weg
Wollten wir diese Befehle jetzt umsetzen ohne abstrakte Klassen oder Interfaces, sähe unser Code ziemlich schnell sehr unübersichtlich aus:

```java
class UhrzeitCommand {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    public static void execute(DiscordBot bot, User user, Message message) {
        bot.send(message.getChannel(), "Es ist " + sdf.format(new Date()));
    }
}

class PingCommand {
    public static void execute(DiscordBot bot, User user, Message message) {
        bot.send(message.getChannel(), "Pong, @" + user.getName()));
    }
}

class HalloCommand {
    public static void execute(DiscordBot bot, User user, Message message) {
        bot.send(message.getChannel(), "Hallo Welt!");
    }
}

[...]

@Override
public void onMessageReceived (MessageReceivedEvent event) {
    String author = event.getAuthor();
    String message = event.getMessage();

    if (message.equalsIgnoreCase("!uhrzeit")) {
        UhrzeitCommand.execute(bot, author, message);
        return;
    }

    if (message.equalsIgnoreCase("!ping")) {
        PingCommand.execute(bot, author, message);
        return;
    }

    if (message.equalsIgnoreCase("!hallo")) {
        HalloCommand.execute(bot, author, message);
        return;
    }
}
```

Wie man nun sieht, wird das `MessageReceivedEvent` sehr schnell unübersichtlich. Möchten wir jetzt auch noch die Rechte des Nutzers überprüfen, sähe dieses Event etwa so aus:

```java
@Override
public void onMessageReceived (MessageReceivedEvent event) {
    String author = event.getAuthor();
    String message = event.getMessage();

    if (message.equalsIgnoreCase("!uhrzeit")) {
        if (author.getGroups().contains(Groups.Admin)) {
            UhrzeitCommand.execute(bot, author, message);
        } else {
            bot.send(message.getChannel(), "Du hast keine Rechte für diesen Befehl.");
        }
        return;
    }

    if (message.equalsIgnoreCase("!ping")) {
        if (author.getGroups().contains(Groups.Moderator)) {
            PingCommand.execute(bot, author, message);
        } else {
            bot.send(message.getChannel(), "Du hast keine Rechte für diesen Befehl.");
        }
        return;
    }

    if (message.equalsIgnoreCase("!hallo")) {
        if (author.getGroups().contains(Groups.User)) {
            HalloCommand.execute(bot, author, message);
        } else {
            bot.send(message.getChannel(), "Du hast keine Rechte für diesen Befehl.");
        }
        return;
    }
}
```

Und diese Schreibarbeit hat man bei gerade mal 3 Befehlen.

#### Der "abstrakte" Weg
Nun lösen wir unser Befehlesystem durch abstrakte Klassen. Dafür erstellen wir eine Abstrakte Klasse, welche einen Befehl repräsentiert. 

Diese Klasse soll drei abstrakte Methoden beinhalten:

* Der Befehl, auf den der Bot hören soll,
* die Gruppe, die ein Nutzer braucht, um einen Befehl ausführen zu können 
* und eine Methode, welcher ausgeführt werden soll, wenn ein Nutzer im Chat den `!{Befehl}` eingibt.

```java
abstract class BotCommand {
    public abstract String command();
    public abstract Group group();
    public abstract void execute(DiscordBot bot, User user, Message message);
}
```

Nun erstellen wir die Befehle und erben von `BotCommand`

```java
/**
  * !uhrzeit
  * Gibt die Uhrzeit zurück
  */
class UhrzeitCommand extends BotCommand {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
   
    @Override
    public String command() {
        return "!uhrzeit";
    }

    @Override
    public Group group() {
        return Groups.Admin;
    }

    @Override
    public void execute(DiscordBot bot, User user, Message message) {
        bot.send(message.getChannel(), "Es ist " + sdf.format(new Date()));
    }
}

/**
  * !ping
  * Antwortet "pong!"
  */
class PingCommand {
    @Override
    public String command() {
        return "!ping";
    }

    @Override
    public Group group() {
        return Groups.Moderator;
    }

    @Override
    public void execute(DiscordBot bot, User user, Message message) {
        bot.send(message.getChannel(), "Pong, @" + user.getName()));
    }
}

/**
  * !hallo
  * Antwortet "Hallo Welt!"
  */
class HalloCommand {
    @Override
    public String command() {
        return "!hallo";
    }

    @Override
    public Group group() {
        return Groups.User;
    }

    @Override
    public void execute(DiscordBot bot, User user, Message message) {
        bot.send(message.getChannel(), "Hallo Welt!");
    }
}
```

Und nun brauchen wir folgendes im `MessageReceivedEvent`:

```java

/**
  * Beinhaltet alle Befehle
  * Sobald es einen neuen Befehl gibt, wird dieser hier lediglich angehänt
  */
private static final BotCommand[] commands = new BotCommand[] {
    new UhrzeitCommand(),
    new PingCommand(),
    new HalloCommand()
};

@Override
public void onMessageReceived (MessageReceivedEvent event) {
    String author = event.getAuthor();
    String message = event.getMessage();
    
    // Gehe alle Befehle durch
    for (BotCommand command : commands) {
        // Prüfe ob die eingegebene Nachricht einen Befehl anspricht
        if (message.equalsIgnoreCase(command.command())) {
            // Prüfe die Berechtigung vom Befehl
            if (author.getGroups().contains(command.group())) {
                // User hat Rechte, also führe den Befehl aus
                command.execute(bot, author, message);
            } else {
                // User hat keine Rechte
                bot.send(
                    message.getChannel(), 
                    author.getName() + ", du hast keine Rechte für `" + command.command() + "`"
                );
            }
            // for-schleife abbrechen, da wir bereits einen Befehl gefunden haben
            break;
        }
    }
}
```

Macht man nun 10 neue Befehle, muss man diese Befehle lediglich dem Array `commands` anhängen.
Auch wenn der "abstrakte" Weg auf den ersten Blick nach mehr Schreibaufwand aussieht, es wird nach nur ein paar mehr Befehlen deutlich, wie viel angenehmer dieser Weg ist.

**=> Weniger Schreibearbeit und weniger Fehler anfällig (man vergisst z.B. nicht die Rechteüberprüfung, verschreibt sich, o. Ä.)**