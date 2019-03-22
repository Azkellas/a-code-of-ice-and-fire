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
        int income;
        int opponentGold;
        int opponentIncome;
        cin >> gold;
        cin >> income;
        cin >> opponentGold;
        cin >> opponentIncome;
        cin.ignore();
        cerr << "gold: " << gold << endl;
    
        for (int y = 0; y < MAP_HEIGHT; ++y) {
            string line;
            getline(cin, line);
            for (int x = 0; x < MAP_WIDTH; ++x) {
                map[x][y].x = x;
                map[x][y].y = y;
                map[x][y].occupied = false;
                char data = line[x];
                int owner;
                if (data == '#')
                    owner = -2;
                else if (data == '.')
                    owner = -1;
                else if (data == 'O' || data == 'o')
                    owner = 0;
                else
                    owner = 1;
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
            map[building.x][building.y].occupied = true;

            buildings.push_back(building);
        }

        int unitCount;
        cin >> unitCount;
        cerr << "unit count: " << unitCount << endl;

        vector<Unit> units;
        for (int i = 0; i < unitCount; ++i) {
            Unit unit;
            cin >> unit.owner >> unit.id >> unit.level >> unit.x >> unit.y; cin.ignore();
            cerr << unit.owner << " " << unit.id << " " << unit.level << " " << unit.x << " " << unit.y << endl;
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
                map[unit.x][unit.y].occupied = false;
                map[neighbour->x][neighbour->y].occupied = true;
                neighbour->owner = 0;
                first = false;
                break;
            }
        }
        cerr << "finished moving" << endl;
        // build
        bool mad = (rand() % 100) < 30;
        while (gold >= 15 && mad) {
            mad = false;
            for (int x = 0; x < MAP_WIDTH; ++x) {
                for (int y = 0; y < MAP_HEIGHT; ++y) {
                    if (gold < 15)
                        break;
                    if (map[x][y].owner != 0 || map[x][y].occupied)
                        continue;
                    if (rand() % 100 < 50)
                    {
                        cout << (first ? "" : " ") << "BUILD " << "TOWER" << " " << x << " " << y << ";";
                        cerr << (first ? "" : " ") << "BUILD " << "TOWER" << " " << x << " " << y << ";";
                        gold -= 15;
                    }
                    else {
                        cout << (first ? "" : " ") << "BUILD " << "MINE" << " " << x << " " << y << ";";
                        cerr << (first ? "" : " ") << "BUILD " << "MINE" << " " << x << " " << y << ";";
                        gold -= 20;
                    }
                    map[x][y].occupied = true;
                    first = false;
                    mad = (rand() % 100 < 10);
                }
            }
        }
        // train
        mad = true;
        while (gold >= 20 && mad) {
            mad = false;
            for (int x = 0; x < MAP_WIDTH; ++x) {
                for (int y = 0; y < MAP_HEIGHT; ++y) {
                    if (gold < 10)
                        break;
                    if (map[x][y].owner == VOID || map[x][y].owner == 0)
                        continue;
                    bool can = false;
                    for (int dir = 0; dir < 4; ++dir)
                        if (map[x][y].neighbours[dir] != nullptr && map[x][y].neighbours[dir]->owner == 0)
                            can = true;
                    if (can) {
                        int random = rand()% 100;
                        int level;
                        if (random < 20)
                            level = 3;
                        else if (random < 70)
                            level = 2;
                        else
                            level = 1;
                        if (gold < level * 10)
                            continue;
                        cout << (first ? "" : " ") << "TRAIN " << level << " " << x << " " << y << ";";
                        first = false;
                        map[x][y].owner = 0;
                        mad = true;
                        gold -= level * 10;
                    }
                }
            }
        }
        cerr << "finished training" << endl;
        cerr << "end gold: " << gold << endl;
        cout << endl;
    }
    return 0;
}