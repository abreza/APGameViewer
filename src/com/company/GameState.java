package com.company;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import javax.naming.Name;
import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState extends StateBasedGame{

    public static int firstX = 0, firstY = 0;
    public static AppGameContainer app;
    public static int width = 700;
    public static int height = 700;
    public static Map<Integer, MapViewer> mapViews = new HashMap<>();
    public static GameState gameState;
    public static ObjectView player;
    public static List<Image> playerUp = new ArrayList<>();
    public static List<Image> playerDown = new ArrayList<>();
    public static List<Image> playerRight = new ArrayList<>();
    public static List<Image> playerLeft = new ArrayList<>();
    public static int upNumber = 0, downNumber = 0, leftNumber = 0, rightNumber = 0;

    public GameState() throws SlickException {
        super("map-village");
        addMapView("map-village", 0);
        addMapView("map-farm", 1);
        addMapView("map-barn", 2);
        addMapView("map-greenhouse", 3);
        addMapView("map-jungle", 4);
        this.enterState(1);
    }
    public static void run() throws SlickException {
        app = new AppGameContainer(gameState = new GameState());
        app.setDisplayMode(width, height, false);
        app.start();
    }
    public void addMapView(String TXMName, int id){
        MapViewer mapViewer = new MapViewer(TXMName, id);
        mapViews.put(id, mapViewer);
        this.addState(mapViewer);
    }
    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        playerUp.add(new Image("/resource/person/person_up_0.png"));
        playerUp.add(new Image("/resource/person/person_up_1.png"));
        playerUp.add(new Image("/resource/person/person_up_2.png"));

        playerDown.add(new Image("/resource/person/person_down_0.png"));
        playerDown.add(new Image("/resource/person/person_down_1.png"));
        playerDown.add(new Image("/resource/person/person_down_2.png"));

        playerLeft.add(new Image("/resource/person/person_left_0.png"));
        playerLeft.add(new Image("/resource/person/person_left_1.png"));
        playerLeft.add(new Image("/resource/person/person_left_2.png"));

        playerRight.add(new Image("/resource/person/person_right_0.png"));
        playerRight.add(new Image("/resource/person/person_right_1.png"));
        playerRight.add(new Image("/resource/person/person_right_2.png"));


        player = new ObjectView(new Position(GameState.width / 8,GameState.height / 8,40,50), null, "0", ObjectView.Type.PLAYER);
        player.setImage(new Image("/resource/person/person_right_0.png"));
        this.getState(0).init(gameContainer, this);
        this.getState(1).init(gameContainer, this);
        List<ObjectView> objectViews = new ArrayList<>();
        objectViews.add(new ObjectView(new Position(0, 0, 50, 40 * 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(60 * 32 - 70, 0, 70, 40 * 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 23 * 32, 50), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(27 * 32 + 10, 0, 60 * 32 - (28 * 32 + 10), 50), null, "0", ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(0, 37 * 32 + 10, 22 * 32, 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(28 * 32, 37 * 32 + 10, 60 * 32 - (28 * 32), 32), null, "0", ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(28 * 32 + 10, 50, 64, 64), null, Names.WALL.name(), ObjectView.Type.ITEM));

        objectViews.add(new ObjectView(new Position(4 * 32 + 10, 32, 5 * 32, 4 * 32), null, Names.HOME.name(), ObjectView.Type.BUILDING));
        objectViews.add(new BuildingObjectView(new Position(23 * 32 + 10, 0, 4 * 32, 20), null,
                new Position(23 * 32 + 10, 0, 4 * 32, 20), Names.VIllage.name(), ObjectView.Type.BUILDING, 0));
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setPlayerXAndY(20, 300);
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setFirstXAndY(0, 0);
        objectViews.add(new BuildingObjectView(new Position(13 * 32 + 10, 42, 7 * 32, 4 * 32), null,
                new Position(16 * 32, 4 * 32, 42, 2 * 32), Names.BARN.name(), ObjectView.Type.BUILDING, 2));
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setPlayerXAndY(6 * 32, 6 * 32 - 15);
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setFirstXAndY(8 * 32, 22 * 32);
        objectViews.add(new BuildingObjectView(new Position(33 * 32 + 10, 32, 7 * 32, 5 * 32), null,
                new Position(35 * 32 + 10, 5 * 32, 3 * 32, 32), Names.GREENHOUSE.name(), ObjectView.Type.BUILDING, 3));
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setPlayerXAndY(6 * 32, 6 * 32 - 15);
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setFirstXAndY(8 * 32, 22 * 32);
        objectViews.add(new ObjectView(new Position(44 * 32 + 15, 2 * 32, 8 * 32 - 10, 5 * 32), null, Names.POND.name(), ObjectView.Type.BUILDING));
        objectViews.add(new BuildingObjectView(new Position(23 * 32 + 10, 39 * 32 - 20, 4 * 32, 20), null,
                new Position(23 * 32 + 10, 39 * 32 - 20, 4 * 32, 20), Names.FOREST.name(), ObjectView.Type.BUILDING, 4));
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setPlayerXAndY(17 * 32, 32);
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setFirstXAndY(15 * 32, 0);

        mapViews.get(1).objectViews = objectViews;


        objectViews = new ArrayList<>();
        objectViews.add(new ObjectView(new Position(-32, -32, 32, 8 * 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 20, 7 * 32 - 15), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(0, 11 * 32 - 15, 20, 8 * 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 40 * 32, 10), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(0, 18 * 32 - 10, 40 * 32, 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(38 * 32, 0, 32, 20 * 32), null, Names.WALL.name(), ObjectView.Type.ITEM));

        objectViews.add(new ObjectView(new Position(2 * 32, 1 * 32 + 10, 3 * 32 + 15, 2 * 32 + 10), null,  Names.GYM.name(), ObjectView.Type.BUILDING));
        objectViews.add(new ObjectView(new Position(12 * 32, 1 * 32 - 10, 3 * 32 + 5, 3 * 32 + 10), null,  Names.LABORATORY.name(), ObjectView.Type.BUILDING));
        objectViews.add(new ObjectView(new Position(21 * 32 + 10, 1 * 32, 3 * 32 + 10, 3 * 32 + 5), null,  Names.MARKET.name(), ObjectView.Type.BUILDING));
        objectViews.add(new ObjectView(new Position(31 * 32, 2 * 32 + 15, 2 * 32 + 10, 3 * 32 - 5), null,  Names.CAFE.name(), ObjectView.Type.BUILDING));

        objectViews.add(new ObjectView(new Position(2 * 32 + 5, 12 * 32 + 10, 3 * 32, 2 * 32 + 15), null,  Names.WORKSHOP.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(12 * 32, 12 * 32 + 15, 3 * 32 + 10, 3 * 32 - 15), null,  Names.CLINIC.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(31 * 32 + 5, 12 * 32 + 5, 2 * 32 + 20, 3 * 32 - 5), null,  Names.RANCH.name(), ObjectView.Type.BUILDING_ITEM));

        objectViews.add(new ObjectView(new Position(0, 5 * 32 - 20, 32 + 15, 84), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(5 * 32, 5 * 32 - 20, 32 + 15, 84), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(11 * 32, 5 * 32 - 20, 32 + 15, 84), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(15 * 32, 5 * 32 - 20, 32 + 15, 84), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(20 * 32, 4 * 32 - 20, 32 + 15, 116), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(20 * 32, 20, 32 + 15, 76), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(24 * 32, 4 * 32 - 20, 32 + 15, 116), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(24 * 32, 20, 32 + 15, 76), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(30 * 32, 5 * 32 - 20, 32 + 15, 84), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(0, 10 * 32 - 20, 32 + 15, 84), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(5 * 32, 10 * 32 - 20, 32 + 15, 84), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(11 * 32, 10 * 32 - 20, 32 + 15, 84), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(15 * 32, 10 * 32 - 20, 32 + 15, 84), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(18 * 32, 10 * 32 - 20, 64 + 30, 84), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(25 * 32, 10 * 32 - 20, 64 + 30, 84), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(30 * 32, 10 * 32 - 20, 32 + 15, 84), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(32, 32 , 32 + 15, 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(36 * 32, 20 , 32 + 15, 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(35 * 32, 6 * 32 + 20 , 32 + 15, 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(35 * 32, 11 * 32 + 20 , 32 + 15, 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(34 * 32, 15 * 32 + 20 , 32 + 15, 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(28 * 32, 15 * 32 + 20 , 32 + 15, 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(21 * 32, 15 * 32 + 20 , 32 + 15, 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(14 * 32, 15 * 32 + 20 , 32 + 15, 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(7 * 32, 15 * 32 + 20 , 32 + 15, 32), null, Names.WALL.name(), ObjectView.Type.ITEM));

        objectViews.add(new BuildingObjectView(new Position(-32, 7 * 32, 32 - 15, 10 * 32), null,
                new Position(-32, 7 * 32, 32 - 15, 10 * 32), Names.FARM.name(), ObjectView.Type.BUILDING, 1));
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setPlayerXAndY(220, 100);
        ((BuildingObjectView)objectViews.get(objectViews.size() - 1)).setFirstXAndY(18 * 32, -50);


        mapViews.get(0).objectViews = objectViews;

        objectViews = new ArrayList<>();
        objectViews.add(new BuildingObjectView(new Position(12 * 32, 28 * 32, 3 * 32, 2 * 32), null,
                new Position(12 * 32, 28 * 32, 3 * 32, 2 * 32), Names.FARM.name(), ObjectView.Type.BUILDING, 1));
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setFirstXAndY(0, 0);
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setPlayerXAndY(16 * 32, 6 * 32);
        objectViews.add(new ObjectView(new Position(32 - 15, 0 - 15, 3 *32, 4 * 32), null, Names.COW_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.COW_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.COW_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.COW_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.COW_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.COW_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.SHEEP_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.SHEEP_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.SHEEP_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.SHEEP_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.SHEEP_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.CHICKEN_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.CHICKEN_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.CHICKEN_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.CHICKEN_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.CHICKEN_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.CHICKEN_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.CHICKEN_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.CHICKEN_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.CHICKEN_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 0, 0), null, Names.CHICKEN_EMPTY.name(), ObjectView.Type.BUILDING_ITEM));
        mapViews.get(2).objectViews = objectViews;

        objectViews = new ArrayList<>();
        objectViews.add(new BuildingObjectView(new Position(12 * 32 - 15, 28 * 32, 4 * 32 + 30, 2 * 32), null,
                new Position(12 * 32 - 15, 28 * 32, 4 * 32, 2 * 32), Names.FARM.name(), ObjectView.Type.BUILDING, 1));
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setFirstXAndY(20 * 32, 0);
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setPlayerXAndY(16 * 32, 6 * 32);
        objectViews.add(new ObjectView(new Position(3 * 32 + 15, 6 * 32 + 15, 6 * 32, 7 * 32), null, "Field No.0", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(19 * 32 + 15, 6 * 32 + 15, 6 * 32, 7 * 32), null, "Field No.1", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(3 * 32 + 15, 17 * 32 + 15, 6 * 32, 7 * 32), null, "Field No.2", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(19 * 32 + 15, 17 * 32 + 15, 6 * 32, 7 * 32), null, "Field No.3", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(30, -1 * 32 + 15, 5 * 32 - 15, 4 * 32), null, Names.WEATHERMACHINE.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(0, -15, 2, 32 * 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(0, -15, 32 * 32, 2), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(29 * 32, -15, 2, 32 * 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(0, 29 * 32 - 45, 13 * 32, 2), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(17 * 32 , 29 * 32 - 45, 13 * 32, 2), null, Names.WALL.name(), ObjectView.Type.ITEM));
        mapViews.get(3).objectViews = objectViews;

        objectViews = new ArrayList<>();
        objectViews.add(new BuildingObjectView(new Position(31 * 32, 0, 5 * 32, 32), null,
                new Position(31 * 32, 0, 5 * 32, 32), Names.FARM.name(), ObjectView.Type.BUILDING, 1));
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setPlayerXAndY(220, 100);
        ((BuildingObjectView)objectViews.get(objectViews.size() - 1)).setFirstXAndY(18 * 32, 37 * 32 - 50);
        mapViews.get(4).objectViews = objectViews;
    }
}
