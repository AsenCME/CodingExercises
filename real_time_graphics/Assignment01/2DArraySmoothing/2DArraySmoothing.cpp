#include <iostream>
#include <cstdlib>
#include <stdlib.h>
#include <time.h>
using namespace std;

#define idx(x, y, w) ((x) + (y) * (w));

void printArray(float* arr, int w, int h) {
    for (int i = 0; i < w; i++) {
        for (int j = 0; j < h; j++) {
            int index = idx(i, j, w);
            cout << arr[index] << " ";
        }
        cout << endl;
    }
}

void smoothArray(float* arr, int w, int h) {
    float* temp = new float[w * h];

    for (int i = 0; i < w; i++) {
        for (int j = 0; j < h; j++) {

            float sum = 0;
            int count = 0;
            for(int x = -1; x <= 1; x++)
                for (int y = -1; y <= 1; y++) {
                    int newX = i + x;
                    int newY = j + y;
                    
                    if (newX >= 0 && newX < w && newY >= 0 && newY < h) {
                        int index = idx(newX, newY, w);
                        sum += arr[index];
                        count++;
                    }
                }

            int currentIndex = idx(i, j, w);
            float average = sum / count;
            temp[currentIndex] = average;
        }
    }

    for (int i = 0; i < w * h; i++)
        arr[i] = temp[i];
    delete[] temp;
}


int main()
{
    int width, height;
    cin >> width;
    cin >> height;

    float* normal = new float[width * height];

    srand(time(NULL));
    for (int i = 0; i < width * height; i++)
        normal[i] = (float)rand()/RAND_MAX;

    cout << "Initial Array:\n";
    printArray(normal, width, height);

    cout << "\nSmooth Array:\n";
    smoothArray(normal, width, height);
    printArray(normal, width, height);

    delete[] normal;
}