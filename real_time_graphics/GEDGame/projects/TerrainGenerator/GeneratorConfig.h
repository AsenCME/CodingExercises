#pragma once
#include <string>
#include <iostream>
using namespace std;
class GeneratorConfig
{
private:
	int resolution;
	string out_height;
	string out_color;
	string out_normal;

public:
	GeneratorConfig(char* argv[])
	{
		if (string(argv[1]) == "-r")
		{
			resolution = stoi(argv[2]);
			if (resolution <= 0)
				throw exception("Invalid resolution argument");
		}
		else
			throw exception("Missing resolution argument");

		if (string(argv[3]) == "-o_height")
			out_height = argv[4];
		else
			throw exception("Missing name for output height file");

		if (string(argv[5]) == "-o_color")
			out_color = argv[6];
		else
			throw exception("Missing name for output color file");

		if (string(argv[7]) == "-o_normal")
			out_normal = argv[8];
		else
			throw exception("Missing name for output normal file");
	}

	int getResolution() { return resolution; }
	string getHeightFileName() { return out_height; }
	string getColorFileName() { return out_color; }
	string getNormalFileName() { return out_normal; }
};

