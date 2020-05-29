#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

int main()
{
    vector<int> vect;
    int n;
    cin >> n;
    while (n != 0) {
        vect.push_back(n);
        cin >> n;
    }
    sort(vect.begin(), vect.end(), greater<int>());

    for (auto x : vect)
        cout << x << " ";
}