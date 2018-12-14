package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.AI.DestructoEnemy;
import com.kinglogic.game.Actors.Voxel.Blocks.Voxel;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Actors.Voxel.VoxelUtils;
import com.kinglogic.game.Constants;
import com.kinglogic.game.Models.SectorState;
import com.kinglogic.game.Models.WorldState;
import com.kinglogic.game.Physics.DynamicGrid;
import com.kinglogic.game.Physics.Grid;
import com.kinglogic.game.Physics.StaticGrid;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by chris on 4/17/2018.
 */

public class PCGManager {
    private static PCGManager instance;
    private static long seed = System.nanoTime();
    private Random randomizer;

    public enum SECTOR_ARCHETYPE{
        VOID(0), RELIC(1), FIGHT(2), CAVE(3), DUNGEON(4);
        private final int value;
        SECTOR_ARCHETYPE(final int newValue) {value = newValue;}
        public int getValue() { return value; }
    }

    public enum SECTOR_THEME{
        VOID(0), ASTEROID(1), MINING(2), SHIP(3), STATION(4);
        private final int value;
        SECTOR_THEME(final int newValue) {value = newValue;}
        public int getValue() { return value; }
    }

//    public enum ROOM_ARCHETYPES{
//        NONE(0), CORE_PATH(1), SIDE_PATH(2), BIG_ROOM(3), SPECIAL(4);
//        private final int value;
//        ROOM_ARCHETYPES(final int newValue) {value = newValue;}
//        public int getValue() { return value; }
//    }

    public enum BLOCK_GEN_TYPES{
        NONE(0), BACKGROUND(1), FOREGROUND(2), OPTIONAL(3),
        PLATFORMING(4), OBSTACLE(5), SYSTEMATIC(6);
        private final int value;
        BLOCK_GEN_TYPES(final int newValue) {value = newValue;}
        public int getValue() { return value; }
    }

    public static PCGManager ins() {
        if(instance == null)
            instance = new PCGManager();
        return instance;
    }

    private PCGManager(){
        randomizer = new Random();
    }

    public boolean[][] generateBlandAsteroid(int size){
        //generation numbers picked by using
        //https://www.redblobgames.com/maps/terrain-from-noise/#demo
        float[][] noise = Noise(size,1f, 1f);
        noise = Noise(noise, 2f, 1f);

//        noise = Noise(noise, 4f, .5f);
//        noise = Noise(noise, 8f, .3f);
        float[][] littleBits = Noise(size, 64f, .7f);

        boolean[][] ast = new boolean[size][size];
        double e,nx,ny;
        double sizeD = (double)size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //generate islands
                e = 0;
                nx = ((float)i)/sizeD - 0.5;
                ny = ((float)j)/sizeD - 0.5;
                if(littleBits[i][j] > .5)
                    e = littleBits[i][j];
                double d = 2*Math.sqrt(nx*nx+ny*ny);

                e += ((noise[i][j]/2+.5) + .25) * (1.0 - 2.0*Math.pow(d, 10.0));
                //cut out space
                ast[i][j] = e > 0.5;

//                System.out.println(ast[i][j]);
            }
        }
        return ast;
    }

    public float[][] Noise(int size, float frequency, float amplitude){
        float[][] noise = new float[size][size];
        float phaseX = randomizer.nextFloat()*(2*MathUtils.PI);
        float phaseY = randomizer.nextFloat()*(2*MathUtils.PI);
        float phase = randomizer.nextFloat()*(2*MathUtils.PI);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                noise[i][j] = amplitude * noise(((float) i)/size, ((float) j)/size,frequency, phaseX,phaseY);
            }
        }
        return noise;
    }

    public float[][] Noise(float[][] noise, float frequency, float amplitude){
        float phaseX = randomizer.nextFloat()*(2*MathUtils.PI);
        float phaseY = randomizer.nextFloat()*(2*MathUtils.PI);
//        System.out.println("pahse"+ phase);
        for (int i = 0; i < noise.length; i++) {
            for (int j = 0; j < noise.length; j++) {
                noise[i][j] = (noise[i][j] + amplitude * noise(((float) i)/noise.length, ((float) j)/noise.length,frequency, phaseX, phaseY));
            }
        }
        return noise;
    }

    private float noise(float nx, float ny, float frequency, float seedX, float seedY){
        return (MathUtils.sin(2*MathUtils.PI * frequency*nx + seedX) + MathUtils.sin(2*MathUtils.PI * frequency*ny + seedY));

    }

    public boolean[][] generateBetterAsteroid(int size, float density){
        boolean[][] asteroid = new boolean[size][size];
        float chanceToExist = density;//anywhere from .4(lots of systems, to .6 just a few pockets)
        int starve = 3;
//        int overpop = 4;
        int birthNum = 4;
        int numSteps = 6;

        //generate the map
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                asteroid[i][j] = (randomizer.nextFloat() < chanceToExist);
            }
        }
//        System.out.println(DebugGen(asteroid));

        for (int i = 0; i < numSteps; i++) {
            asteroid = genStep(asteroid,starve,birthNum);
//            System.out.println(DebugGen(asteroid));
        }
        return asteroid;
    }
    public float[][] genDensityMap(boolean[][] state, float min, float max){
        float[][] density = new float[state.length][state[0].length];
        float delta = (max-min)/8f;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                int nghbrs = countNeighbors(i,j,state);
                density[i][j] = min+(delta*nghbrs);

            }
        }
        return density;
    }

    private boolean[][] genStep(boolean[][] state, int starve, int birthNum){
        boolean[][] newState = new boolean[state.length][state.length];
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                int nghbrs = countNeighbors(i,j,state);
                if(state[i][j]){
                    if(nghbrs < starve)
                        newState[i][j] = false;
                    else
                        newState[i][j] = true;
                }else {
                    if(nghbrs > birthNum)
                        newState[i][j] = true;
                    else
                        newState[i][j] = false;
                }
            }
        }
        return newState;
    }

    private int countNeighbors(int x, int y, boolean[][] oldState){
        int count = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int nx = x+i;
                int ny = y+j;
                if(i == 0 && j == 0) {
                    //do nothing with center
                }else {
                    if (nx < 0 || ny < 0 || nx >= oldState.length || ny >= oldState.length) {
                        //count+=1;
                    } else if (oldState[nx][ny]) {
                        count+=1;
                    }
                }
            }
        }
        return count;
    }

    private String DebugGen(boolean[][] state){
        String print = "";
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                if(state[i][j])
                    print+=" 0";
                else
                    print+=" _";
            }
            print+="\n";
        }
        return print;
    }


    /**
     * Generates the layout of each sector in the world
     * @return
     */
    public SECTOR_ARCHETYPE[][] generateWorldMap(){
        SECTOR_ARCHETYPE[][] universe = new SECTOR_ARCHETYPE[Constants.NUM_SECTORS][Constants.NUM_SECTORS];
        for (int i = 0; i < Constants.NUM_SECTORS; i++) {
            for (int j = 0; j < Constants.NUM_SECTORS; j++) {
                //just for dev
                universe[i][j] = SECTOR_ARCHETYPE.CAVE;
                //todo use this instead
//                float gen = randomizer.nextFloat();
//                if((gen < .25f))
//                    universe[i][j] = SECTOR_ARCHETYPE.CAVE;//"STATION/COLONY/SHIP/BASE"
//                else if(gen < .5f)
//                    universe[i][j] = SECTOR_ARCHETYPE.FIGHT;//FIGHT
//                else {
//                    if(gen < .75f)
//                        universe[i][j] = SECTOR_ARCHETYPE.RELIC;//Relic
//                    else
//                        universe[i][j] = SECTOR_ARCHETYPE.VOID;//Void
//                }
            }
        }
        return universe;
    }

    /**
     * This will be used to generate a new sector
     */
    public SectorState generateSectorState(WorldState world, int xPos, int yPos){
        SECTOR_ARCHETYPE toBuild = world.sectorsData[xPos][yPos];
        SectorState state = genCAVE(xPos,yPos);

        //todo uncomment this
//        switch (toBuild){
//            case VOID://empty sector
//                state = new SectorState(xPos,yPos,SECTOR_ARCHETYPE.VOID);
//                break;
//            case RELIC://relic
//                break;
//            case FIGHT://fight
//                break;
//            case CAVE://STATION/COLONY/SHIP/BASE(expansion)
//                break;
//            case DUNGEON://MAJOR THING(upgrade)
//                break;
//            default:
//                state = new SectorState(xPos,yPos,SECTOR_ARCHETYPE.VOID);
//        }
        return state;
    }

    private SectorState genCAVE(int x, int y){
        SectorState cave = new SectorState(x,y,SECTOR_ARCHETYPE.CAVE);


        int[][] roomMap = new int[Constants.NUM_ROOMS_PER_STATION][Constants.NUM_ROOMS_PER_STATION];
        ArrayList<VoxelUtils.Index> neighbors = new ArrayList<VoxelUtils.Index>();
        //random start point
        Random r = new Random();

        //place it
        int startX = Math.abs(r.nextInt())%Constants.NUM_ROOMS_PER_STATION;
        int startY = Math.abs(r.nextInt())%Constants.NUM_ROOMS_PER_STATION;

        //todo if sector maps become templates(ships, asteroids, ect may need to check if the pos is != -1)
        roomMap[startX][startY] = 0;//core path
        //place neighbors
        CarvePath(roomMap,startX,startY,neighbors);

        //branch off
        int numRooms = 0;
        int targetRooms = 32;
        VoxelUtils.Index cur = new VoxelUtils.Index(startX,startY);
        do {
            if(neighbors.size() > 0) {
                cur = neighbors.remove(0);
                numRooms += CarvePath(roomMap, cur.x, cur.y, neighbors);
            }else{
                numRooms += CarvePath(roomMap, cur.x, cur.y, neighbors);
            }
        } while(neighbors.size() > 0 && numRooms < targetRooms);
        //connect the start to everything around it
        //todo make this a special room?
        connectAllRoomsAt(roomMap,startX,startY);

        //build big rooms

        // build special rooms

        // read template file and create voxelCollection
        RoomTemplateData rtd = new RoomTemplateData();
        Voxel[][] sector = new Voxel[Constants.ROOM_SIZE*roomMap.length+4][Constants.ROOM_SIZE*roomMap.length+4];
        for (int i = 0; i < roomMap.length; i++) {
            for (int j = 0; j < roomMap[0].length; j++) {
                int[][] roomData = rtd.getRoomData(roomMap[i][j],1);
                populateVoxelsFromRoomData(sector,roomData,i,j);
            }
        }



        //add it to the world
        VoxelCollection v = new VoxelCollection(sector, new Vector2(-Constants.MAX_GRID_SIZE*ResourceManager.VOXEL_PIXEL_SIZE/2,-Constants.MAX_GRID_SIZE*ResourceManager.VOXEL_PIXEL_SIZE/2),0);
        Grid g = new StaticGrid(v);
        WorldManager.ins().addGridToWorld(g);


            return cave;
    }

    /**
     *
     * @param roomMap
     * @param cX
     * @param cY
     * @param neighbors
     * @return the number of rooms created
     */
    private static int CarvePath(int[][] roomMap, int cX, int cY, ArrayList<VoxelUtils.Index> neighbors){
        System.out.println("carve path called");
        Random r;
        r = new Random();
        int roomsMade = 0;


        int numBranch = Math.abs(r.nextInt())%5;
        for (int i = 0; i < numBranch+1; i++) {
            switch (Math.abs(r.nextInt())%4){
                case 0://down
                    if(cY - 1 >= 0 && roomMap[cX][cY-1] == 0){
                        System.out.println("generating down");
                        roomMap[cX][cY] |= VoxelUtils.FIFTEEN_PATCH.BB.getValue();
                        roomMap[cX][cY-1] |= VoxelUtils.FIFTEEN_PATCH.TT.getValue();
                        neighbors.add(new VoxelUtils.Index(cX, cY-1));
                        roomsMade++;
                    }
                    break;
                case 1://left
                    if(cX - 1 >= 0 && roomMap[cX-1][cY] == 0){
                        System.out.println("generating left");
                        roomMap[cX][cY] |= VoxelUtils.FIFTEEN_PATCH.RR.getValue();
                        roomMap[cX-1][cY] |= VoxelUtils.FIFTEEN_PATCH.LL.getValue();
                        neighbors.add(new VoxelUtils.Index(cX-1, cY));
                        roomsMade++;
                    }
                    break;
                case 2://right
                    if(cX + 1 < roomMap.length && roomMap[cX+1][cY] == 0){
                        System.out.println("generating right");
                        roomMap[cX][cY] |= VoxelUtils.FIFTEEN_PATCH.LL.getValue();
                        roomMap[cX+1][cY] |= VoxelUtils.FIFTEEN_PATCH.RR.getValue();
                        neighbors.add(new VoxelUtils.Index(cX+1, cY));
                        roomsMade++;
                    }
                    break;
                case 3: //up
                    if(cY + 1 < roomMap[0].length && roomMap[cX][cY+1] == 0){
                        System.out.println("generating up");
                        roomMap[cX][cY] |= VoxelUtils.FIFTEEN_PATCH.TT.getValue();
                        roomMap[cX][cY+1] |= VoxelUtils.FIFTEEN_PATCH.BB.getValue();
                        neighbors.add(new VoxelUtils.Index(cX, cY+1));
                        roomsMade++;
                    }
                    break;
            }
        }
        return roomsMade;
    }

    /**
     * Returns the 15 march indicating that those blocks are not null
     * @param map the rooms
     * @param x
     * @param y
     * @return
     */
    private static int marchRoomNotNULL(int[][] map, int x, int y){
        int marchNum = 0;
        if(x+1 < map[0].length && map[x+1][y] > 0)
            marchNum |= VoxelUtils.FIFTEEN_PATCH.RR.getValue();
        if(y+1 < map[0].length && map[x][y+1] > 0)
            marchNum |= VoxelUtils.FIFTEEN_PATCH.TT.getValue();
        if(x-1 >= map[0].length && map[x-1][y] > 0)
            marchNum |= VoxelUtils.FIFTEEN_PATCH.LL.getValue();
        if(y-1 >= 0 && map[x][y-1] > 0)
            marchNum |= VoxelUtils.FIFTEEN_PATCH.BB.getValue();
        return marchNum;
    }

    private static void connectAllRoomsAt(int[][] roomMap, int cX, int cY){
        if(cY - 1 >= 0 && roomMap[cX][cY-1] == 0){
            System.out.println("generating down");
            roomMap[cX][cY] |= VoxelUtils.FIFTEEN_PATCH.BB.getValue();
            roomMap[cX][cY-1] |= VoxelUtils.FIFTEEN_PATCH.TT.getValue();
        }

        if(cX - 1 >= 0 && roomMap[cX-1][cY] == 0){
            System.out.println("generating left");
            roomMap[cX][cY] |= VoxelUtils.FIFTEEN_PATCH.RR.getValue();
            roomMap[cX-1][cY] |= VoxelUtils.FIFTEEN_PATCH.LL.getValue();
        }

        if(cX + 1 < roomMap.length && roomMap[cX+1][cY] == 0){
            System.out.println("generating right");
            roomMap[cX][cY] |= VoxelUtils.FIFTEEN_PATCH.LL.getValue();
            roomMap[cX+1][cY] |= VoxelUtils.FIFTEEN_PATCH.RR.getValue();
        }

        if(cY + 1 < roomMap[0].length && roomMap[cX][cY+1] == 0){
            System.out.println("generating up");
            roomMap[cX][cY] |= VoxelUtils.FIFTEEN_PATCH.TT.getValue();
            roomMap[cX][cY+1] |= VoxelUtils.FIFTEEN_PATCH.BB.getValue();
        }
    }

    /**
     * Populates Voxels
     * @param voxels
     * @param roomData
     * @param roomXOffset 0 to max rooms. not individual voxels, its whole rooms
     * @param roomYOffset 0 to max rooms. not individual voxels, its whole rooms
     */
    private void populateVoxelsFromRoomData(Voxel[][] voxels, int[][] roomData, int roomXOffset, int roomYOffset){
        boolean genOptional = randomizer.nextBoolean();
        for (int i = 0; i < roomData.length; i++) {
            for (int j = 0; j < roomData[0].length; j++) {
                //todo theme stuff???? march stuff, alt stuff
                //+2 is to acount for the border breaking everything
                if(roomData[i][j] != 3 || genOptional)
                    voxels[i+(roomXOffset*Constants.ROOM_SIZE)+2][j+(roomYOffset*Constants.ROOM_SIZE)+2] = mapIDtoVoxel(roomData[i][j], 15, 1);
            }
        }
    }

    private static Voxel mapIDtoVoxel(int id, int marchNum, int altNum){
        Voxel toBuild = null;
        switch (id){
            case 0:
                break;
            case 1:
                toBuild = Voxel.Build(IDs.BASE_TEX);
                break;
            case 2:
                toBuild = Voxel.Build(IDs.METAL_TEX);
                break;
            case 3:
                toBuild = Voxel.Build(IDs.METAL_TEX);
        }
        return toBuild;
    }

    /**
     * Load template data, it comes in with 0,0 in the top left.
     * @return
     */
    public class RoomTemplateData{
        int numRoomTypes = Constants.NUM_ROOM_TYPES;
        int numAlts = Constants.NUM_ALTS;
        int roomSize = Constants.ROOM_SIZE;
        int [][] rawDat;

        public RoomTemplateData(){
                File templates = Gdx.files.internal("roomTemplates.txt").file();
                rawDat = new int[numRoomTypes * roomSize][roomSize * numAlts];
                try {
                    Scanner inFileScanner = new Scanner(templates);
                    int i = 0; int j = 0;
                    while (inFileScanner.hasNext() && j / roomSize < numAlts){
                        rawDat[i][j] = inFileScanner.nextInt();
                        i++;
                        if(i >= rawDat.length){
                            i = 0;
                            j++;
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        }

        /**
         * 0,0 in the lower left hand corner
         * @param id the type of room
         * @param altNum (1 to the number of alt types)
         * @return
         */
        public int[][] getRoomData(int id, int altNum){
            int[][] roomData = new int[roomSize][roomSize];
            if(altNum <= 0)
                altNum = 1;
            if(id < 0)
                throw new RuntimeException("room id cant be negative");
            //start at (0,16) and go (+,-)
            for (int i = roomSize * id; i < roomSize * (id+1); i++) {
                for (int j = (roomSize * altNum)-1; j >= roomSize * (altNum-1); j--) {
                    roomData[i - roomSize * id][((roomSize * altNum) - 1) - j] = rawDat[i][j];
                }
            }
            return roomData;
        }


    }




}
