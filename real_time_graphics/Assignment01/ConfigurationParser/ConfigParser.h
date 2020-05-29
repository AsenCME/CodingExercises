#pragma once
#include <iostream>
#include <string>
#include <fstream>
using namespace std;



class ConfigParser
{

public:
	struct Color {
		float r, g, b;
	};
	void load(const string &path) {
		ifstream stream = ifstream(path);

		if (stream.is_open()) {
			string key;
			stream >> key;

			while (stream.peek() != EOF) {
				if (key == "spinning") stream >> spinning;
				else if (key == "spinSpeed") stream >> spinSpeed;
				else if (key == "backgroundColor") stream >> backgroundColor.r >> backgroundColor.g >> backgroundColor.b;
				else if (key == "terrainWidth") stream >> terrainWidth;
				else if (key == "terrainDepth") stream >> terrainDepth;
				else if (key == "terrainHeight") stream >> terrainHeight;
				else if (key == "terrainPath") stream >> terrainPath;
				else cout << "Unknown parameter: " << key << endl;
				stream >> key;
			}
		}
		stream.close();
	}
	float get_spinning() { return spinning; }
	float get_spinSpeed() { return spinSpeed; }
	float get_terrainDepth() { return terrainDepth; }
	float get_terrainWidth() { return terrainWidth; }
	float get_terrainHeight() { return terrainHeight; }
	string get_terrainPath() { return terrainPath; }
	Color get_backgroundColor() { return backgroundColor; }
private:
	float spinning;
	float spinSpeed;
	Color backgroundColor;
	string terrainPath;
	float terrainWidth;
	float terrainDepth;
	float terrainHeight;

};

