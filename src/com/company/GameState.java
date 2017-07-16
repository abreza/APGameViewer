package com.company;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import javax.naming.Name;
import java.lang.reflect.Array;
import java.util.*;

public class GameState extends StateBasedGame{

    public static int firstX = 0, firstY = 0;
    public static AppGameContainer app;
    public static int width = 700;
    public static int height = 700;
    public static Map<Integer, MapViewer> mapViews = new HashMap<>();
    public static GameState gameState;
    public static Animal player;
    public static List<Animal> animals;
    public GameState() throws SlickException {
        super("map-village");
        addMapView("map-village", 0);
        addMapView("map-farm", 1);
        addMapView("map-barn", 2);
        addMapView("map-greenhouse", 3);
        addMapView("map-jungle", 4);
        addMapView("map-cave", 5);
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

        List<Image> up = new ArrayList<>();
        up.add(new Image("/resource/person/person_up_0.png").getScaledCopy(40, 50));
        up.add(new Image("/resource/person/person_up_1.png").getScaledCopy(40, 50));
        up.add(new Image("/resource/person/person_up_2.png").getScaledCopy(40, 50));
        List<Image> down = new ArrayList<>();
        down.add(new Image("/resource/person/person_down_0.png").getScaledCopy(40, 50));
        down.add(new Image("/resource/person/person_down_1.png").getScaledCopy(40, 50));
        down.add(new Image("/resource/person/person_down_2.png").getScaledCopy(40, 50));
        List<Image> left = new ArrayList<>();
        left.add(new Image("/resource/person/person_left_0.png").getScaledCopy(40, 50));
        left.add(new Image("/resource/person/person_left_1.png").getScaledCopy(40, 50));
        left.add(new Image("/resource/person/person_left_2.png").getScaledCopy(40, 50));
        List<Image> right = new ArrayList<>();
        right.add(new Image("/resource/person/person_right_0.png").getScaledCopy(40, 50));
        right.add(new Image("/resource/person/person_right_1.png").getScaledCopy(40, 50));
        right.add(new Image("/resource/person/person_right_2.png").getScaledCopy(40, 50));
        player = new Animal(new Position(GameState.width / 8,GameState.height / 8,40,50), null, "0",
                ObjectView.Type.PLAYER, up , down, left, right, "player");
        player.setImage(right.get(0).getScaledCopy(player.getPosition().width, player.getPosition().height));

        this.getState(0).init(gameContainer, this);
        this.getState(1).init(gameContainer, this);
        List<ObjectView> objectViews = new ArrayList<>();
        animals = new ArrayList<>();
        objectViews.add(new ObjectView(new Position(0, 0, 50, 40 * 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(60 * 32 - 70, 0, 70, 40 * 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(0, 0, 23 * 32, 50), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(27 * 32 + 10, 0, 60 * 32 - (28 * 32 + 10), 50), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(0, 37 * 32 + 10, 22 * 32, 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(28 * 32, 37 * 32 + 10, 60 * 32 - (28 * 32), 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(28 * 32 + 10, 50, 64, 64), null, Names.WALL.name(), ObjectView.Type.ITEM));

        objectViews.add(new ObjectView(new Position(4 * 32 + 10, 32, 5 * 32, 4 * 32), null, Names.HOME.name(), ObjectView.Type.BUILDING));
        objectViews.add(new BuildingObjectView(new Position(23 * 32 + 10, 0, 4 * 32, 20), null,
                new Position(23 * 32 + 10, 0, 4 * 32, 20), Names.VILLAGE.name(), ObjectView.Type.BUILDING, 0));
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
        objectViews.add(new ObjectView(new Position(4 * 32 + 10, 10 * 32 - 15, 6 * 32, 7 * 32),null , Names.TREE.name() + " No.0", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(13 * 32 + 10, 10 * 32 - 15, 6 * 32, 7 * 32), null, Names.TREE.name() + " No.1", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(4 * 32 + 10, 20 * 32 - 15, 6 * 32, 7 * 32), null, Names.TREE.name() + " No.2", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(13 * 32 + 10, 20 * 32 - 15, 6 * 32, 7 * 32), null, Names.TREE.name() + " No.3", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(4 * 32 + 10, 30 * 32 - 15, 6 * 32, 7 * 32), null, Names.TREE.name() + " No.4", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(13 * 32 + 10, 30 * 32 - 15, 6 * 32, 7 * 32), null, Names.TREE.name() + " No.5", ObjectView.Type.BUILDING_ITEM));
        mapViews.get(1).setObjectViews(objectViews);
        objectViews.add(new ObjectView(new Position(28 * 32 + 10, 11 * 32 - 15, 6 * 32, 6 * 32), null, Names.FIELD.name() + " " + "No.0", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(38 * 32 + 10, 11 * 32 - 15, 6 * 32, 6 * 32), null, Names.FIELD.name() + " " + "No.1", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(48 * 32 + 10, 11 * 32 - 15, 6 * 32, 6 * 32), null, Names.FIELD.name() + " " + "No.2", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(28 * 32 + 10, 21 * 32 - 15, 6 * 32, 6 * 32), null, Names.FIELD.name() + " " + "No.3", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(38 * 32 + 10, 21 * 32 - 15, 6 * 32, 6 * 32), null, Names.FIELD.name() + " " + "No.4", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(48 * 32 + 10, 21 * 32 - 15, 6 * 32, 6 * 32), null, Names.FIELD.name() + " " + "No.5", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(28 * 32 + 10, 31 * 32 - 15, 6 * 32, 6 * 32), null, Names.FIELD.name() + " " + "No.6", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(38 * 32 + 10, 31 * 32 - 15, 6 * 32, 6 * 32), null, Names.FIELD.name() + " " + "No.7", ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new ObjectView(new Position(48 * 32 + 10, 31 * 32 - 15, 6 * 32, 6 * 32), null, Names.FIELD.name() + " " + "No.8", ObjectView.Type.BUILDING_ITEM));
        mapViews.get(1).setObjectViews(objectViews);
        GameState.player.currentObjectViews = objectViews;

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


        mapViews.get(0).setObjectViews(objectViews);

        objectViews = new ArrayList<>();
        objectViews.add(new BuildingObjectView(new Position(12 * 32, 28 * 32, 3 * 32, 2 * 32), null,
                new Position(12 * 32, 28 * 32, 3 * 32, 2 * 32), Names.FARM.name(), ObjectView.Type.BUILDING, 1));
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setFirstXAndY(0, 0);
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setPlayerXAndY(16 * 32, 6 * 32);




        List<Image> cowUp = new ArrayList<>();
        cowUp.add(new Image("/resource/cow/cu0.png"));
        cowUp.add(new Image("/resource/cow/cu1.png"));
        cowUp.add(new Image("/resource/cow/cu2.png"));
        cowUp.add(new Image("/resource/cow/cu3.png"));
        List<Image> cowLeft = new ArrayList<>();
        cowLeft.add(new Image("/resource/cow/cl0.png"));
        cowLeft.add(new Image("/resource/cow/cl1.png"));
        cowLeft.add(new Image("/resource/cow/cl2.png"));
        cowLeft.add(new Image("/resource/cow/cl3.png"));
        List<Image> cowRight = new ArrayList<>();
        cowRight.add(new Image("/resource/cow/cr0.png"));
        cowRight.add(new Image("/resource/cow/cr1.png"));
        cowRight.add(new Image("/resource/cow/cr2.png"));
        cowRight.add(new Image("/resource/cow/cr3.png"));
        List<Image> cowDown = new ArrayList<>();
        cowDown.add(new Image("/resource/cow/cd0.png"));
        cowDown.add(new Image("/resource/cow/cd1.png"));
        cowDown.add(new Image("/resource/cow/cd2.png"));
        cowDown.add(new Image("/resource/cow/cd3.png"));

        animals.add(new Animal(new Position(32 - 15, 130, 2 * 32, 3 * 32), new Image("/resource/cow/cu0.png"), Names.COW_EMPTY.name(),
                ObjectView.Type.ANIMAL, cowUp, cowDown, cowLeft, cowRight, "cow"));
        animals.add(new Animal(new Position(6 * 32 - 15, 130, 2 * 32, 3 * 32), new Image("/resource/cow/cu0.png"), Names.COW_EMPTY.name(),
                ObjectView.Type.ANIMAL, cowUp, cowDown, cowLeft, cowRight, "cow"));
        animals.add(new Animal(new Position(11 * 32 - 15, 130, 2 * 32, 3 * 32), new Image("/resource/cow/cu0.png"), Names.COW_EMPTY.name(),
                ObjectView.Type.ANIMAL, cowUp, cowDown, cowLeft, cowRight, "cow"));
        animals.add(new Animal(new Position(3 * 32 - 15, 6 * 32, 2 * 32, 3 * 32), new Image("/resource/cow/cd0.png"), Names.COW_EMPTY.name(),
                ObjectView.Type.ANIMAL, cowUp, cowDown, cowLeft, cowRight, "cow"));
        animals.add(new Animal(new Position(9 * 32 - 15, 6 * 32, 2 * 32, 3 * 32), new Image("/resource/cow/cd0.png"), Names.COW_EMPTY.name(),
                ObjectView.Type.ANIMAL, cowUp, cowDown, cowLeft, cowRight, "cow"));

        List<Image> sheepUp = new ArrayList<>();
        sheepUp.add(new Image("/resource/sheep/shu0.png"));
        sheepUp.add(new Image("/resource/sheep/shu1.png"));
        sheepUp.add(new Image("/resource/sheep/shu2.png"));
        sheepUp.add(new Image("/resource/sheep/shu3.png"));
        List<Image> sheepLeft = new ArrayList<>();
        sheepLeft.add(new Image("/resource/sheep/shl0.png"));
        sheepLeft.add(new Image("/resource/sheep/shl1.png"));
        sheepLeft.add(new Image("/resource/sheep/shl2.png"));
        sheepLeft.add(new Image("/resource/sheep/shl3.png"));
        List<Image> sheepRight = new ArrayList<>();
        sheepRight.add(new Image("/resource/sheep/shr0.png"));
        sheepRight.add(new Image("/resource/sheep/shr1.png"));
        sheepRight.add(new Image("/resource/sheep/shr2.png"));
        sheepRight.add(new Image("/resource/sheep/shr3.png"));
        List<Image> sheepDown = new ArrayList<>();
        sheepDown.add(new Image("/resource/sheep/shd0.png"));
        sheepDown.add(new Image("/resource/sheep/shd1.png"));
        sheepDown.add(new Image("/resource/sheep/shd2.png"));
        sheepDown.add(new Image("/resource/sheep/shd3.png"));

        animals.add(new Animal(new Position(15 * 32 - 15, 130, 3 * 16, 4 * 16 - 10), new Image("/resource/sheep/shu0.png"), Names.SHEEP_EMPTY.name(),
                ObjectView.Type.ANIMAL, sheepUp, sheepDown, sheepLeft, sheepRight, "sheep"));
        animals.add(new Animal(new Position(20 * 32 - 15, 130, 3 * 16, 4 * 16 - 10), new Image("/resource/sheep/shu0.png"), Names.SHEEP_EMPTY.name(),
                ObjectView.Type.ANIMAL, sheepUp, sheepDown, sheepLeft, sheepRight, "sheep"));
        animals.add(new Animal(new Position(25 * 32 - 15, 130, 3 * 16, 4 * 16 - 10), new Image("/resource/sheep/shu0.png"), Names.SHEEP_EMPTY.name(),
                ObjectView.Type.ANIMAL, sheepUp, sheepDown, sheepLeft, sheepRight, "sheep"));
        animals.add(new Animal(new Position(19 * 32 - 15, 7 * 32, 3 * 16, 4 * 16 - 10), new Image("/resource/sheep/shd0.png"), Names.SHEEP_EMPTY.name(),
                ObjectView.Type.ANIMAL, sheepUp, sheepDown, sheepLeft, sheepRight, "sheep"));
        animals.add(new Animal(new Position(24 * 32 - 15, 7 * 32, 3 * 16, 4 * 16 - 10), new Image("/resource/sheep/shd0.png"), Names.SHEEP_EMPTY.name(),
                ObjectView.Type.ANIMAL, sheepUp, sheepDown, sheepLeft, sheepRight, "sheep"));

        List<Image> chickenUp = new ArrayList<>();
        chickenUp.add(new Image("/resource/chicken/chu0.png"));
        chickenUp.add(new Image("/resource/chicken/chu1.png"));
        chickenUp.add(new Image("/resource/chicken/chu2.png"));
        chickenUp.add(new Image("/resource/chicken/chu3.png"));
        List<Image> chickenLeft = new ArrayList<>();
        chickenLeft.add(new Image("/resource/chicken/chl0.png"));
        chickenLeft.add(new Image("/resource/chicken/chl1.png"));
        chickenLeft.add(new Image("/resource/chicken/chl2.png"));
        chickenLeft.add(new Image("/resource/chicken/chl3.png"));
        List<Image> chickenRight = new ArrayList<>();
        chickenRight.add(new Image("/resource/chicken/chr0.png"));
        chickenRight.add(new Image("/resource/chicken/chr1.png"));
        chickenRight.add(new Image("/resource/chicken/chr2.png"));
        chickenRight.add(new Image("/resource/chicken/chr3.png"));
        List<Image> chickenDown = new ArrayList<>();
        chickenDown.add(new Image("/resource/chicken/chd0.png"));
        chickenDown.add(new Image("/resource/chicken/chd1.png"));
        chickenDown.add(new Image("/resource/chicken/chd2.png"));
        chickenDown.add(new Image("/resource/chicken/chd3.png"));
        animals.add(new Animal(new Position(32 - 15, 17 * 32 - 15, 32, 25), new Image("/resource/chicken/chu0.png"), Names.CHICKEN_EMPTY.name(),
                ObjectView.Type.ANIMAL, chickenUp, chickenDown, chickenLeft, chickenRight, "chicken"));
        animals.add(new Animal(new Position(3 * 32 - 15, 17 * 32 - 15, 32, 25), new Image("/resource/chicken/chu0.png"), Names.CHICKEN_EMPTY.name(),
                ObjectView.Type.ANIMAL, chickenUp, chickenDown, chickenLeft, chickenRight, "chicken"));
        animals.add(new Animal(new Position(5 * 32 - 15, 17 * 32 - 15, 32, 25), new Image("/resource/chicken/chu0.png"), Names.CHICKEN_EMPTY.name(),
                ObjectView.Type.ANIMAL, chickenUp, chickenDown, chickenLeft, chickenRight, "chicken"));
        animals.add(new Animal(new Position(7 * 32 - 15, 17 * 32 - 15, 32, 25), new Image("/resource/chicken/chu0.png"), Names.CHICKEN_EMPTY.name(),
                ObjectView.Type.ANIMAL, chickenUp, chickenDown, chickenLeft, chickenRight, "chicken"));
        animals.add(new Animal(new Position(9 * 32 - 15, 17 * 32 - 15, 32, 25), new Image("/resource/chicken/chu0.png"), Names.CHICKEN_EMPTY.name(),
                ObjectView.Type.ANIMAL, chickenUp, chickenDown, chickenLeft, chickenRight, "chicken"));
        animals.add(new Animal(new Position(32 - 15, 26 * 32 - 15, 32, 25), new Image("/resource/chicken/chu0.png"), Names.CHICKEN_EMPTY.name(),
                ObjectView.Type.ANIMAL, chickenUp, chickenDown, chickenLeft, chickenRight, "chicken"));
        animals.add(new Animal(new Position(3 * 32 - 15, 26 * 32 - 15, 32, 25), new Image("/resource/chicken/chu0.png"), Names.CHICKEN_EMPTY.name(),
                ObjectView.Type.ANIMAL, chickenUp, chickenDown, chickenLeft, chickenRight, "chicken"));
        animals.add(new Animal(new Position(5 * 32 - 15, 26 * 32 - 15, 32, 25), new Image("/resource/chicken/chu0.png"), Names.CHICKEN_EMPTY.name(),
                ObjectView.Type.ANIMAL, chickenUp, chickenDown, chickenLeft, chickenRight, "chicken"));
        animals.add(new Animal(new Position(7 * 32 - 15, 26 * 32 - 15, 32, 25), new Image("/resource/chicken/chu0.png"), Names.CHICKEN_EMPTY.name(),
                ObjectView.Type.ANIMAL, chickenUp, chickenDown, chickenLeft, chickenRight, "chicken"));
        animals.add(new Animal(new Position(9 * 32 - 15, 26 * 32 - 15, 32, 25), new Image("/resource/chicken/chu0.png"), Names.CHICKEN_EMPTY.name(),
                ObjectView.Type.ANIMAL, chickenUp, chickenDown, chickenLeft, chickenRight, "chicken"));


        ObjectView obj = new ObjectView(new Position(32 - 15, 0 - 15, 3 * 32, 4 * 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ITEM);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(6 * 32 - 15, 0 - 15, 3 * 32, 4 * 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ITEM);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(11 * 32 - 15, 0 - 15, 3 * 32, 4 * 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(3 * 32 - 15, 10 * 32 - 15, 3 * 32, 4 * 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(9 * 32 - 15, 10 * 32 - 15, 3 * 32, 4 * 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);

        obj = new ObjectView(new Position(15 * 32 - 15, 0 - 15, 3 * 32, 4 * 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(20 * 32 - 15, 0 - 15, 3 * 32, 4 * 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(25 * 32 - 15, 0 - 15, 3 * 32, 4 * 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(19 * 32 - 15, 10 * 32 - 15, 3 * 32, 4 * 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(24 * 32 - 15, 10 * 32 - 15, 3 * 32, 4 * 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(32 - 15, 15 * 32 - 15, 32, 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(3 * 32 - 15, 15 * 32 - 15, 32, 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(5 * 32 - 15, 15 * 32 - 15, 32, 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(7 * 32 - 15, 15 * 32 - 15, 32, 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(9 * 32 - 15, 15 * 32 - 15, 32, 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(32 - 15, 27 * 32 - 15, 32, 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(3 * 32 - 15, 27 * 32 - 15, 32, 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(5 * 32 - 15, 27 * 32 - 15, 32, 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(7 * 32 - 15, 27 * 32 - 15, 32, 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        obj = new ObjectView(new Position(9 * 32 - 15, 27 * 32 - 15, 32, 32), null, Names.ANIMAL_FOOD.name(), ObjectView.Type.ANIMAL);
        for (Animal animal:animals) {
            animal.animalFood.add(obj);
        }
        objectViews.add(obj);
        objectViews.add(new ObjectView(new Position(0, -15, 2, 32 * 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(0, -15, 32 * 32, 2), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(29 * 32, -15, 2, 32 * 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(0, 29 * 32 - 45, 13 * 32, 2), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(16 * 32 , 29 * 32 - 45, 14 * 32, 2), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(14 * 32 - 15, 0 - 15, 32, 10 * 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(32 - 15, 14 * 32 - 15, 12 * 32, 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(16 * 32 - 15, 14 * 32 - 15, 12 * 32, 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(12 * 32 - 15, 19 * 32 - 15, 32, 9 * 32), null, Names.WALL.name(), ObjectView.Type.ITEM));
        objectViews.add(new ObjectView(new Position(16 * 32 - 15, 19 * 32 - 15, 32, 9 * 32), null, Names.WALL.name(), ObjectView.Type.ITEM));


        mapViews.get(2).setObjectViews(objectViews);
        for (Animal animal : animals) {
            animal.currentObjectViews = new ArrayList<>(objectViews);
            animal.currentObjectViews.remove(animal);
        }
        for (Animal animal:animals) {
            animal.Animation_SPEED = 50;
        }
        new Thread(() -> {
            while (true){
                for (Animal animal:animals) {
                    animal.move();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(() -> {
            int flag;
            while (true){
                for (Animal animal:animals) {
                    flag = ((int)(Math.random() * 300) * 2 - 1) * (animal.moveX * 2 + ((int)(Math.random() * 2) * 2 - 1));
                    flag /= Math.abs(flag);
                    animal.moveX = flag * ((int)(Math.random() * 30));
                    flag = ((int)(Math.random() * 300) * 2 - 1) * (animal.moveY * 2 + ((int)(Math.random() * 2) * 2 - 1));
                    flag /= Math.abs(flag);
                    animal.moveY = flag * ((int)(Math.random() * 30));
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        mapViews.get(2).setAnimals(animals);

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
        mapViews.get(3).setObjectViews(objectViews);

        objectViews = new ArrayList<>();
        objectViews.add(new BuildingObjectView(new Position(31 * 32, 0, 7 * 32, 32), null,
                new Position(31 * 32, 0, 7 * 32, 32), Names.FARM.name(), ObjectView.Type.BUILDING, 1));
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setPlayerXAndY(220, 70);
        ((BuildingObjectView)objectViews.get(objectViews.size() - 1)).setFirstXAndY(18 * 32, 37 * 32 - 50);
        objectViews.add(new ObjectView(new Position(-15, 33 * 32 - 30, 40 * 32, 7 * 32), null, Names.RIVER.name(), ObjectView.Type.BUILDING_ITEM));
        objectViews.add(new BuildingObjectView(new Position(32, -15, 5 * 32, 3 * 32), null,
                new Position(32, -15, 5 * 32, 3 * 32), Names.CAVE.name(), ObjectView.Type.BUILDING, 5));
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setPlayerXAndY(330, 110);
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setFirstXAndY(0, 100);
        mapViews.get(4).setObjectViews(objectViews);

        objectViews = new ArrayList<>();
        objectViews.add(new BuildingObjectView(new Position(10 * 32, 32, 4 * 32, 3 * 32), null,
                new Position(10 * 32, 32, 4 * 32, 3 * 32), Names.FOREST.name(), ObjectView.Type.BUILDING, 4));
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setPlayerXAndY(3 * 32, 32);
        ((BuildingObjectView) objectViews.get(objectViews.size() - 1)).setFirstXAndY(0, 150);
        mapViews.get(5).setObjectViews(objectViews);

    }
}
