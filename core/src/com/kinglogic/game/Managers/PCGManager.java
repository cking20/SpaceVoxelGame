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
}
