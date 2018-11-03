package com.contestantbots.team;

import com.contestantbots.util.GameStateLogger;
import com.scottlogic.hackathon.client.Client;
import com.scottlogic.hackathon.game.Bot;
import com.scottlogic.hackathon.game.GameState;
import com.scottlogic.hackathon.game.Move;

import com.scottlogic.hackathon.game.Player;
import com.scottlogic.hackathon.game.Collectable;
import com.scottlogic.hackathon.game.*;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class ExampleBotDmytro extends Bot {
    private final GameStateLogger gameStateLogger;

    public ExampleBotDmytro() {
        super("ExampleBotDmytro");
        gameStateLogger = new GameStateLogger(getId());
    }

    @Override
    public List<Move> makeMoves(final GameState gameState) {
        gameStateLogger.process(gameState);

        GameMap map = gameState.getMap();
        List<Move> moves = new ArrayList<Move>();
        Set<Player> players = gameState.getPlayers();
        Set<Collectable> collectables = gameState.getCollectables();

        for(Player p : players){
            if(p.getOwner() == this.getId()){
                List<CollectableDistance> distances = new ArrayList<CollectableDistance>();
                for(Collectable c : collectables){
                    distances.add(new CollectableDistance(c, p.getPosition(), map));
                }
                Collections.sort(distances);

                System.out.println(distances.size());

                Move move = new MoveImpl(p.getId(), Direction.EAST);
                try{
                    Optional<Direction> dir = map.directionsTowards(p.getPosition(), distances.get(0).collectable.getPosition()).findFirst();
                    if(dir.isPresent()){
                        move = new MoveImpl(p.getId(), dir.get());
                    }

                } catch(Exception e){
                    
                }
                
                moves.add(move);
            }
        }

        //Move m = new MoveImpl(playerId, direction);

        return moves;
    }

    public class MoveImpl implements Move {
        private UUID playerId;
        private Direction direction;
        public MoveImpl(UUID playerId, Direction direction) {
            this.playerId = playerId;
            this.direction = direction;
        }
        @Override
        public UUID getPlayer() {
            return playerId;
        }
        @Override
        public Direction getDirection() {
            return direction;
        }
    }

    public class CollectableDistance implements Comparable<CollectableDistance>{
        public Collectable collectable;
        public int distance;
        public CollectableDistance(Collectable c, Position pos, GameMap map){
            this.collectable = c;
            this.distance = map.distance(this.collectable.getPosition(), pos);
        }

        @Override
        public int compareTo(CollectableDistance cd){
            return this.distance - cd.distance;
        }
    }

    /*
     * Run this main as a java application to test and debug your code within your IDE.
     * After each turn, the current state of the game will be printed as an ASCII-art representation in the console.
     * You can study the map before hitting 'Enter' to play the next phase.
     */
    public static void main(String ignored[]) throws Exception {

        final String[] args = new String[]{
                /*
                Pick the map to play on
                -----------------------
                Each successive map is larger, and has more out-of-bounds positions that must be avoided.
                Make sure you only have ONE line uncommented below.
                 */
                "--map",
//                    "VeryEasy",
                    "Easy",
//                    "Medium",
//                    "LargeMedium",
//                    "Hard",

                /*
                Pick your opponent bots to test against
                ---------------------------------------
                Every game needs at least one opponent, and you can pick up to 3 at a time.
                Uncomment the bots you want to face, or specify the same opponent multiple times to face multiple
                instances of the same bot.
                 */
                "--bot",
//                    "Default", // Players move in random directions
                    "Milestone1", // Players just try to stay out of trouble
//                    "Milestone2", // Some players gather collectables, some attack enemy players, and some attack enemy spawn points
//                    "Milestone3", // Strategy dynamically updates based on the current state of the game

                /*
                Enable debug mode
                -----------------
                This causes all Bots' 'makeMoves()' methods to be invoked from the main thread,
                and prevents them from being disqualified if they take longer than the usual time limit.
                This allows you to run in your IDE debugger and pause on break points without timing out.

                Comment this line out if you want to check that your bot is running fast enough.
                 */
                "--debug",

                // Use this class as the 'main' Bot
                "--className", ExampleBotDmytro.class.getName()
        };

        Client.main(args);
    }

}
