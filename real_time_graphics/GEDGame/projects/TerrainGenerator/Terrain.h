#pragma once
#include <random>
#include <time.h>
#include <SimpleImage.h>
#include "Vec3.h"
using namespace std;

//#define idx(x, y, w) ((x) + (y) * (w));
class Terrain
{
private:
	int iteration = 1;
	int smoothingPasses = 1;
	int kernelSize = 1;

	float* map;
	vec3* normals;
	vec3* colors;
	float roughness;
	int64_t resolution;

	default_random_engine generator;
	int64_t idx(int64_t x, int64_t y){return x  + y * resolution;}

	void initializeMap()
	{
		normal_distribution<float> dist(0.5, 0.5);
		float value = dist(generator);
		
		if (value < 0) value = 0;
		if (value > 1) value = 1;
		map[0] = dist(generator);

		value = dist(generator);
		if (value < 0) value = 0;
		if (value > 1) value = 1;
		map[idx(0, resolution - 1)] = value;

		value = dist(generator);
		if (value < 0) value = 0;
		if (value > 1) value = 1;
		map[idx(resolution - 1, 0)] = value;

		value = dist(generator);
		if (value < 0) value = 0;
		if (value > 1) value = 1;
		map[idx(resolution - 1, resolution - 1)] = value;

		iteration++;
	}

	float getHeight(int x, int y, int fallback = -1) 
	{
		if (x < 0 || x > resolution || y < 0 || y > resolution)  return fallback;
		int index = idx(x, y);
		if (index >= resolution * resolution || index < 0) return fallback;
		return map[index];
	}

	void setHeight(int x, int y, float value) 
	{
		normal_distribution<float> dist(0, pow(roughness, iteration));
		float offset = dist(generator);

		if (x < 0 || x > resolution || y < 0 || y > resolution) return;
		int index = idx(x, y);
		map[index] = value + offset;
	}

	void diamond(int x, int y, int step) 
	{
		int halfStep = step / 2;
		float total = getHeight(x,y) + getHeight(x + step, y) + getHeight(x, y + step) + getHeight(x + step, y + step);
		setHeight(x + halfStep, y + halfStep, total / 4.0f);
	}

	void square(int x, int y, int step)
	{
		int halfStep = step / 2;
		float value = 0.0f;
		float last = 0.0f;

		// Top Left Corner sets top
		value = 0.0f;
		value += getHeight(x, y);
		value += getHeight(x + step, y);
		value += getHeight(x + halfStep, y + halfStep);
		last = getHeight(x + halfStep, y - halfStep);
		if (last == -1) value /= 3.0f;
		else { value += last; value /= 4.0f; }
		setHeight(x + halfStep, y, value);

		// Top Right Corner sets right
		value = 0.0f;
		value += getHeight(x + step, y);
		value += getHeight(x + step, y + step);
		value += getHeight(x + halfStep, y + halfStep);
		last = getHeight(x + step + halfStep, y + halfStep);
		if (last == -1) value /= 3.0f;
		else { value += last; value /= 4.0f; }
		setHeight(x + step, y + halfStep, value);

		// Bottom right Corner sets bottom
		value = 0.0f;
		value += getHeight(x, y + step);
		value += getHeight(x + step, y + step);
		value += getHeight(x + halfStep, y + halfStep);
		last = getHeight(x + halfStep, y + step + halfStep);
		if (last == -1) value /= 3.0f;
		else { value += last; value /= 4.0f; }
		setHeight(x + halfStep, y + step, value);

		// Bottom left Corner sets left
		value = 0.0f;
		value += getHeight(x, y);
		value += getHeight(x, y + step);
		value += getHeight(x + halfStep, y + halfStep);
		last = getHeight(x - halfStep, y + halfStep);
		if (last == -1) value /= 3.0f;
		else { value += last; value /= 4.0f; }
		setHeight(x, y + halfStep, value);
	}
	
	void clampValues() 
	{
		for (int i = 0; i < resolution - 1; i++) 
		{
			if (map[i] < 0) map[i] = 0;
			if (map[i] > 1) map[i] = 1;
		}
	}

	void printValues() 
	{
		for(int x = 0; x < resolution; x++)
			for (int y = 0; y < resolution; y++)
				cout << map[idx(x, y)] << " ";
			cout << endl;
	}

	void smoothArray() {
		float* temp = new float[resolution * resolution];

		for (int i = 0; i < smoothingPasses; i++)
		{
			for (int i = 0; i < resolution; i++) {
				for (int j = 0; j < resolution; j++) {

					float sum = 0;
					int amount = 0;
					for (int x = -1; x <= 1; x++)
						for (int y = -1; y <= 1; y++) {
							int newX = i + x * kernelSize;
							int newY = j + y * kernelSize;

							if (newX >= 0 && newX < resolution && newY >= 0 && newY < resolution) {
								sum += map[idx(newX, newY)];
								amount++;
							}
						}

					float average = sum / amount;
					temp[idx(i, j)] = average;
				}
			}

			for (int i = 0; i < resolution * resolution; i++)
				map[i] = temp[i];
		}

		delete[] temp;
		temp = nullptr;
	}

	void generateTerrain()
	{
		cout << "Generating Height Map..." << endl;
		initializeMap();

		for (int step = resolution - 1; step >= 1; step /= 2)
		{
			for (int y = 0; y < resolution - 1; y += step)
				for (int x = 0; x < resolution - 1; x += step)
					diamond(x, y, step);

			iteration++;

			for (int y = 0; y < resolution - 1; y += step)
				for (int x = 0; x < resolution - 1; x += step)
					square(x, y, step);

			iteration++;
		}
	}
	void generateNormalMap() 
	{
		cout << "Generating Normal Map..." << endl;
		for (int x = 0; x < resolution; x++)
			for (int y = 0; y < resolution; y++)
			{
				float dfu = (getHeight(x + 1, y, 0) - getHeight(x - 1, y, 0)) / 2;
				float dfv = (getHeight(x, y + 1, 0) - getHeight(x, y - 1, 0)) / 2;
				vec3 n((-dfu) * resolution, (-dfv) * resolution, 1);
				n.normalize();
				normals [idx(x, y)] = n;
			}
	}
	void generateColorMap() {
		cout << "Generating Color Map..." << endl;
		GEDUtils::SimpleImage t1 = GEDUtils::SimpleImage("../../../../external/textures/gras15.jpg");
		GEDUtils::SimpleImage t2 = GEDUtils::SimpleImage("../../../../external/textures/mud02.jpg");
		GEDUtils::SimpleImage t3 = GEDUtils::SimpleImage("../../../../external/textures/ground02.jpg");
		GEDUtils::SimpleImage t4 = GEDUtils::SimpleImage("../../../../external/textures/rock4.jpg");
		int* tileHeight = new int[4];
		float a1, a2, a3;
		float* r = new float[5];
		float* g = new float[5];
		float* b = new float[5];
		float x = 0, y = 0;

		tileHeight[0] = t1.getHeight();
		tileHeight[1] = t2.getHeight();
		tileHeight[2] = t3.getHeight();
		tileHeight[3] = t4.getHeight();

		t1.getPixel(x, y, r[0], g[0], b[0]);
		t2.getPixel(x, y, r[1], g[1], b[1]);
		t3.getPixel(x, y, r[2], g[2], b[2]);
		t4.getPixel(x, y, r[3], g[3], b[3]);

		//calculating seemless texturing using modulo referencing the tileTexture Height/Width

		for (int64_t x = 0; x < resolution; x++) {
			for (int64_t y = 0; y < resolution; y++) {
				t1.getPixel(x % tileHeight[0], y % tileHeight[0], r[0], g[0], b[0]);
				t2.getPixel(x % tileHeight[1], y % tileHeight[1], r[1], g[1], b[1]);
				t3.getPixel(x % tileHeight[2], y % tileHeight[2], r[2], g[2], b[2]);
				t4.getPixel(x % tileHeight[3], y % tileHeight[3], r[3], g[3], b[3]);

				calcAlphas(getHeight(x, y), 1 - normals[idx(x, y)].z, a1, a2, a3);

				r[4] = calcBlending(a1, a2, a3, r[0], r[1], r[2], r[3]);
				g[4] = calcBlending(a1, a2, a3, g[0], g[1], g[2], g[3]);
				b[4] = calcBlending(a1, a2, a3, b[0], b[1], b[2], b[3]);

				vec3 color(r[4], g[4], b[4]);
				colors[idx(x, y)] = color;
			}
		}
	}

	void calcAlphas(float height, float slope, float& alpha1, float& alpha2, float& alpha3) {
		alpha1 = (1 - height) * slope;
		alpha2 = height;
		alpha3 = height * slope;
	}

	float calcBlending(float a1, float a2, float a3, float c1, float c2, float c3, float c0) {
		return a3 * c3 + (1 - a3) * (a2 * c2 + (1 - a2) * (a1 * c1 + (1 - a1) * c0));
	}

	bool saveImage(float* data, string fileName)
	{
		GEDUtils::SimpleImage image(resolution - 1, resolution - 1);
		int size = resolution * resolution;
		for (int x = 0; x < resolution - 2; x++)
			for (int y = 0; y < resolution - 2; y++)
				image.setPixel(x, y, data[idx(x, y)]);
		return image.save(fileName.c_str());
	}
	bool saveImage(vec3* data, string fileName)
	{
		GEDUtils::SimpleImage image(resolution - 1, resolution - 1);
		int size = resolution * resolution;
		for (int x = 0; x < resolution - 2; x++)
			for (int y = 0; y < resolution - 2; y++)
			{
				vec3 value = data[idx(x, y)];
				image.setPixel(x, y, value.x, value.y, value.z);
			}
				
		return image.save(fileName.c_str());
	}

public:
	Terrain(int res, float r, int passes = 0, int kernel = 1)
	{
		resolution = res + 1;
		roughness = r;
		smoothingPasses = passes;
		kernelSize = kernel;

		map = new float[resolution * resolution];
		normals = new vec3[resolution * resolution];
		colors = new vec3[resolution * resolution];
		generator = default_random_engine(time(nullptr));
	}
	void generateMaps() 
	{
		generateTerrain();
		cout << "Smoothing Height Field..." << endl;
		smoothArray();
		generateNormalMap();
		generateColorMap();
	}
	void saveImages(string heightMapName, string normalMapName, string colorMapName) 
	{
		cout << "Saving Height map..." << endl;
		if (!saveImage(map, heightMapName)) 
		{
			cout << "Something went wrong..." << endl;
			exit(0);
		}

		cout << "Saving Normal Map..." << endl;
		if (!saveImage(normals, normalMapName))
		{
			cout << "Something went wrong..." << endl;
			exit(0);
		}

		cout << "Saving Color Map..." << endl;
		if (!saveImage(colors, colorMapName))
		{
			cout << "Something went wrong..." << endl;
			exit(0);
		}
	}
};

