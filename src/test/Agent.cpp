#include <iostream>
#include <vector>

using namespace std;

int VOID = -2;
int NEUTRAL = -1;

int UP = 0;
int RIGHT = 1;
int DOWN = 2;
int LEFT = 3;

class Cell {
  public:
    int x, y;
    int owner;
    Cell* neighbours[4];
    bool occupied = false;
    Cell() = default;
};

class Unit {
    public:
    int x, y;
    int id;
    int owner;
    int level;
    Unit() = default;
};

class Building {
    public:
    int x, y;
    int owner;
    int type;
    Building() = default;
};

int MAP_WIDTH;
int MAP_HEIGHT;
bool isInside(int x, int y) {
    return x >= 0 && x < MAP_WIDTH && y >= 0 && y < MAP_HEIGHT;
}

bool isValid(int x, int y, vector<vector<Cell>> const& map) {
    return isInside(x, y) && map[x][y].owner != VOID;
}

int main() {
    cin >> MAP_WIDTH >> MAP_HEIGHT;
    cerr << MAP_WIDTH << " " << MAP_HEIGHT << endl;
    vector<vector<Cell>> map (MAP_WIDTH, vector<Cell> (MAP_HEIGHT));
    while (1) {
        int gold;
        cin >> gold; cin.ignore();
        cerr << "gold: " << gold << endl;
    
        for (int y = 0; y < MAP_HEIGHT; ++y) {
            for (int x = 0; x < MAP_WIDTH; ++x) {
                map[x][y].x = x;
                map[x][y].y = y;
                map[x][y].occupied = false;
                int owner;
                cin >> owner; cin.ignore();
                map[x][y].owner = owner;
                cerr << map[x][y].owner << " ";

            }
            cerr << endl;
        }
        cerr << endl;

        for (int y = 0; y < MAP_HEIGHT; ++y) {
            for (int x = 0; x < MAP_WIDTH; ++x) {
                map[x][y].neighbours[UP]    = (isValid(x, y-1, map)) ? &map[x][y-1] : nullptr;
                map[x][y].neighbours[RIGHT] = (isValid(x+1, y, map)) ? &map[x+1][y] : nullptr;
                map[x][y].neighbours[DOWN]  = (isValid(x, y+1, map)) ? &map[x][y+1] : nullptr;
                map[x][y].neighbours[LEFT]  = (isValid(x-1, y, map)) ? &map[x-1][y] : nullptr;
            }
        }

        int buildingCount;
        cin >> buildingCount;
        cerr << "buildingCount count: " << buildingCount << endl;

        vector<Building> buildings;
        for (int i = 0; i < buildingCount; ++i) {
            Building building;
            cin >> building.owner >> building.type >> building.x >> building.y; cin.ignore();
            cerr << building.owner << " " << building.type << " " << building.x << " " << building.y << endl;
            buildings.push_back(building);
        }

        int unitCount;
        cin >> unitCount;
        cerr << "unit count: " << unitCount << endl;

        vector<Unit> units;
        for (int i = 0; i < unitCount; ++i) {
            Unit unit;
            cin >> unit.id >> unit.owner >> unit.level >> unit.x >> unit.y; cin.ignore();
            cerr << unit.id << " " << unit.owner << " " << unit.level << " " << unit.x << " " << unit.y << endl;
            map[unit.x][unit.y].occupied = true;
            units.push_back(unit);
        }

        cerr << "finished reading" << endl;
        bool first = true;

        // move
        for (auto& unit : units) {
            if (unit.owner != 0)
                continue;

            Cell const& cell = map[unit.x][unit.y];
            for (int dir = 0; dir < 4; ++dir) {
                Cell* neighbour = cell.neighbours[dir];
                if (neighbour == nullptr || neighbour->owner == 0 || neighbour->occupied)
                    continue;
                cout << (first ? "" : " ") << "MOVE " << unit.id << " " << neighbour->x << " " << neighbour->y << ";";
                neighbour->owner = 0;
                first = false;
                // break;
            }
        }
        cerr << "finished moving" << endl;
        // train
        bool mad = true;
        while (gold >= 20 && mad) {
            mad = false;
            for (int x = 0; x < MAP_WIDTH; ++x) {
                for (int y = 0; y < MAP_HEIGHT; ++y) {
                    if (gold < 20)
                        break;
                    if (map[x][y].owner == VOID || map[x][y].owner == 0)
                        continue;
                    bool can = false;
                    for (int dir = 0; dir < 4; ++dir)
                        if (map[x][y].neighbours[dir] != nullptr && map[x][y].neighbours[dir]->owner == 0 && !map[x][y].neighbours[dir]->occupied)
                            can = true;
                    if (can) {
                        cout << (first ? "" : " ") << "TRAIN " << 1 << " " << x << " " << y << ";";
                        first = false;
                        map[x][y].owner = 0;
                        mad = true;
                        gold -= 20;
                    }
                }
            }
        }
        cerr << "finished training" << endl;
        cout << endl;
    }
    return 0;
}