package com.kinglogic.game.Managers;

import com.badlogic.gdx.math.MathUtils;

import java.util.Random;

/**
 * Created by chris on 4/17/2018.
 */

public class PCGManager {
    private static PCGManager instance;
    private int seed;
    private Random randomizer;

    public static PCGManager ins() {
        if(instance == null)
            instance = new PCGManager();
        return instance;
    }

    private PCGManager(){
        seed = MathUtils.random(Integer.MAX_VALUE-1);
        randomizer = new Random(1);
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

    public boolean[][] generateBetterAsteroid(int size){
        boolean[][] asteroid = new boolean[size][size];
        float chanceToExist = .45f;//anywhere from .4(lots of systems, to .6 just a few pockets)
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
        System.out.println(DebugGen(asteroid));

        for (int i = 0; i < numSteps; i++) {
            asteroid = genStep(asteroid,starve,birthNum);
            System.out.println(DebugGen(asteroid));
        }
        return asteroid;
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




}
