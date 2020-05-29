#include "ConfigParser.h"

void main() {
	ConfigParser parser;
	parser.load("../game.cfg");

	cout << "Spinning: " << parser.get_spinning() << endl;
	cout << "Spin speed: " << parser.get_spinSpeed() << endl;
	cout << "Background color: Red - " << parser.get_backgroundColor().r << ", Green - " << parser.get_backgroundColor().g << ", Blue - " << parser.get_backgroundColor().b << endl;
	cout << "Terrain path: " << parser.get_terrainPath() << endl;
	cout << "Terrain width: " << parser.get_terrainWidth() << endl;
	cout << "Terrain depth: " << parser.get_terrainDepth() << endl;
	cout << "Terrain height: " << parser.get_terrainHeight() << endl;
}