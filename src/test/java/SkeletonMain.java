import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class SkeletonMain {
    public static void main(String[] args) {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        // Adds as many player as you need to test your game
        // gameRunner.addAgent(Agent1.class);
        // gameRunner.addAgent(Agent2.class);

        // Another way to add a player
        gameRunner.addAgent("./src/test/Agent");
        gameRunner.addAgent("./src/test/Agent");

        gameRunner.start();
    }
}
