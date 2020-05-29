#include <iostream>
#include <string>
#include "GeneratorConfig.h"
#include <random>
#include <time.h>
#include <TextureGenerator.h>
#include "Terrain.h"
using namespace std;


int main(int argc, char* argv[])
{
    if (argc < 9) 
    {
        string usage = "-r <resolution> -o_height <output heightfield filename> -o_color <output color filename> -o_normal <output normal filename>";
        cout << "Usage: " << argv[0] << " " << usage << endl;
        return 0;
    }

    try
    {
        GeneratorConfig gconf(argv);
        int res = gconf.getResolution();
        
        Terrain terrain(res, 0.7f, 100, 1);
        terrain.generateMaps();
        terrain.saveImages(gconf.getHeightFileName(),gconf.getNormalFileName(), gconf.getColorFileName());
    }
    catch (const std::exception& ex)
    {
        cout << ex.what() << endl;
    }
}


