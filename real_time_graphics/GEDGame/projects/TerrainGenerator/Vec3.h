#pragma once
#include <cmath>
struct vec3
{
public:
	float x;
	float y;
	float z;
	vec3()
	{
		x = 0;
		y = 0;
		z = 0;
	}
	vec3(float _x, float _y, float _z)
	{
		x = _x;
		y = _y;
		z = _z;
	}

	float mag()
	{
		return sqrt(x * x + y * y + z * z);
	}

	void normalize()
	{
		float mag = this->mag();
		x /= mag;
		y /= mag;
		z /= mag;
		x = (x + 1) / 2;
		y = (y + 1) / 2;
		z = (z + 1) / 2;
	}
};

