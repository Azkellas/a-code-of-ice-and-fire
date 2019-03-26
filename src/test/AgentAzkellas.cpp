#include <iostream>
#include <vector>

using namespace std;

int VOID = -2;
int NEUTRAL = -1;

int UP = 0;
int RIGHT = 1;
int DOWN = 2;
int LEFT = 3;

class Unit;
class Building;

class Cell {
  public:
    int x, y;
    int owner;
    Cell* neighbours[4];
    bool occupied = false;
    Unit* unit = nullptr;
    Building* building = nullptr;
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

int HQ = 0;
int MINE = 1;
int TOWER = 2;
class Building {
    public:
    int x {-1}, y;
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
    srand(0);
    cin >> MAP_WIDTH >> MAP_HEIGHT;
    cerr << MAP_WIDTH << " " << MAP_HEIGHT << endl;
    vector<vector<Cell>> map (MAP_WIDTH, vector<Cell> (MAP_HEIGHT));
    while (1) {
        int gold;
        cin >> gold; cin.ignore();
        cerr << "gold: " << gold << endl;
        int income, opponentGold, opponentIncome;
        cin >> income; cin.ignore();
        cin >> opponentGold; cin.ignore();
        cin >> opponentIncome; cin.ignore();
        
        for (int y = 0; y < MAP_HEIGHT; ++y) {
            string line;
            getline(cin, line);
            for (int x = 0; x < MAP_WIDTH; ++x) {
                map[x][y].x = x;
                map[x][y].y = y;
                map[x][y].occupied = false;
                map[x][y].unit = nullptr;
                map[x][y].building = nullptr;
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
        buildings.reserve(buildingCount);
        Building enemyHQ;
        Building myHQ;
        int mineCount = 0;
        int towerCount = 0;
        for (int i = 0; i < buildingCount; ++i) {
            Building building;
            cin >> building.owner >> building.type >> building.x >> building.y; cin.ignore();
            cerr << building.owner << " " << building.type << " " << building.x << " " << building.y << endl;
            
            buildings.push_back(building);
            map[building.x][building.y].occupied = true;
            map[building.x][building.y].building = &buildings.back();

            if (building.type == HQ && building.owner == 1) {
                enemyHQ = building;
            }
            if (building.type == HQ && building.owner == 0) {
                myHQ = building;
            }
            if (building.type == MINE && building.owner == 0) {
                mineCount++;
            }
            if (building.type == TOWER && building.owner == 0) {
                towerCount++;
            }
        }

        if (enemyHQ.x == -1) {
            cerr << "no eneymy hq\n";
        }
        int unitCount;
        cin >> unitCount;
        cerr << "unit count: " << unitCount << endl;

        vector<Unit> units;
        units.reserve(unitCount);
        for (int i = 0; i < unitCount; ++i) {
            Unit unit;
            cin >> unit.owner >> unit.id >> unit.level >> unit.x >> unit.y; cin.ignore();
            cerr << unit.owner << " " << unit.id << " " << unit.level << " " << unit.x << " " << unit.y << endl;
            units.push_back(unit);
            map[unit.x][unit.y].occupied = true;
            map[unit.x][unit.y].unit = &units.back();
        }

        cerr << "finished reading" << endl;
        bool first = true;

        int actionsMade = 0;
    
        // move
        int myUnits = 0;
        for (auto& unit : units) {
            if (unit.owner != 0)
                continue;

            ++myUnits;
            int score = -1000;
            int bestDir = -1;
            Cell const& cell = map[unit.x][unit.y];
            for (int dir = 0; dir < 4; ++dir) {
                Cell* neighbour = cell.neighbours[dir];
                // impossible move
                if (neighbour == nullptr || (neighbour->owner == 0 && neighbour->occupied)) {
                    continue;
                }
                // cannot kill ennemy
                if (unit.level != 3 && neighbour->unit != nullptr && neighbour->unit->level >= unit.level) {
                    continue;
                }
                Building target = neighbour->owner == 0 ? enemyHQ : enemyHQ;
                int cellScore = 100000 * (neighbour->owner != 0) + 10* (100 - (std::abs(neighbour->x - target.x) + std::abs(neighbour->y - target.y))) + rand() % 10;
                if (neighbour->occupied) {
                    cellScore += 1000000;
                }
                if (cellScore > score) {
                    score = cellScore;
                    bestDir = dir;
                }
            }

            if (bestDir != -1) {
                Cell* neighbour = cell.neighbours[bestDir];
                cout << (first ? "" : " ") << "MOVE " << unit.id << " " << neighbour->x << " " << neighbour->y << ";";
                actionsMade++;
                map[unit.x][unit.y].occupied = false;
                map[unit.x][unit.y].unit = nullptr;
                map[neighbour->x][neighbour->y].occupied = true;
                map[neighbour->x][neighbour->y].unit = &unit;
                unit.x = neighbour->x;
                unit.y = neighbour->y;
                neighbour->owner = 0;
                first = false;
            }
        }
        cerr << "finished moving" << endl;

        // train kill
        bool trainDone = true;
        // while (gold >= 20 && trainDone) {
        //     trainDone = false;
        //     int bestScore = 0;
        //     Cell target;
        //     for (int x = 0; x < MAP_WIDTH; ++x) {
        //         for (int y = 0; y < MAP_HEIGHT; ++y) {
        //             if (map[x][y].owner == VOID || map[x][y].owner == 0)
        //                 continue;
        //             bool can = false;
        //             for (int dir = 0; dir < 4; ++dir)
        //                 if (map[x][y].neighbours[dir] != nullptr && map[x][y].neighbours[dir]->owner == 0)
        //                     can = true;
        //             if (map[x][y].unit != nullptr) {
        //                 int enLevel = map[x][y].unit->level;
        //                 if (enLevel >= 2 && gold < 30) {
        //                     can = false;
        //                 } else if (gold < 20) {
        //                     can = false;
        //                 }
        //             } else {
        //                 can = false;
        //             }
        //             if (map[x][y].building != nullptr && map[x][y].building->type == TOWER && gold < 20) {
        //                 can = false;
        //             }
        //             int score = 100 - (std::abs(x - myHQ.x) + std::abs(y - myHQ.y));
        //             if (map[x][y].occupied) {
        //                 score += 1000000;
        //             }
        //             if (can && score > bestScore) {
        //                 bestScore = score;
        //                 target = map[x][y];
        //             }
        //         }
        //     }
        //     if (bestScore != 0) {
        //         int levelMin = 1;
        //         int levelMax = 3;
        //         if (gold < 30) {
        //             levelMax = 2;
        //         }
        //         if (gold < 20) {
        //             levelMax = 1;
        //         }
        //         if (target.unit != nullptr) {
        //             levelMin = min(target.unit->level, 3);
        //         }
        //         int level = levelMin;
        //         cout << (first ? "" : " ") << "TRAIN " << level << " " << target.x << " " << target.y << ";";
        //         actionsMade++;
        //         trainDone = true;
        //         first = false;
        //         map[target.x][target.y].owner = 0;
        //         gold -= level * 10;
        //     }
        // }

        // build danger towers
        bool buildDone = true;
        while (gold >= 15 && buildDone) {
            buildDone = false;
            Cell target;
            int bestScore = 1e6;
            for (int x = 0; x < MAP_WIDTH; ++x) {
                for (int y = 0; y < MAP_HEIGHT; ++y) {
                    if (gold < 20)
                        break;
                    if (map[x][y].owner != 0 || map[x][y].occupied)
                        continue;
                    bool possible = false;
                    for (int dir = 0; dir < 4; ++dir) {
                        Cell* neighbour = map[x][y].neighbours[dir];
                        if (neighbour != nullptr && neighbour->owner == 1 && neighbour->unit != nullptr) {
                            possible = true;
                        }
                    }
                    int score = std::abs(x - myHQ.x) + std::abs(y - myHQ.y);
                    if (possible && score < bestScore) {
                        bestScore = score;
                        target = map[x][y];
                    }
                }
            }
            if (bestScore != 1e6) {
                cout << (first ? "" : " ") << "BUILD " << "TOWER" << " " << target.x << " " << target.y << ";";
                gold -= 15;
                buildDone = true;
                actionsMade++;
                map[target.x][target.y].occupied = true;
                first = false;
            }
        }

        // train
        trainDone = true;
        while (gold >= 10 && myUnits < 10 && trainDone) {
            trainDone = false;
            int bestScore = 0;
            Cell target;
            for (int x = 0; x < MAP_WIDTH; ++x) {
                for (int y = 0; y < MAP_HEIGHT; ++y) {
                    if (map[x][y].owner == VOID || map[x][y].owner == 0)
                        continue;
                    bool can = false;
                    for (int dir = 0; dir < 4; ++dir)
                        if (map[x][y].neighbours[dir] != nullptr && map[x][y].neighbours[dir]->owner == 0)
                            can = true;
                    if (map[x][y].unit != nullptr) {
                        int enLevel = map[x][y].unit->level;
                        if (enLevel >= 2 && gold < 30) {
                            can = false;
                        } else if (gold < 20) {
                            can = false;
                        }
                    }
                    if (map[x][y].building != nullptr && map[x][y].building->type == TOWER && gold < 20) {
                        can = false;
                    }
                    int score = 100 - (std::abs(x - myHQ.x) + std::abs(y - myHQ.y));
                    if (map[x][y].unit != nullptr) {
                        score += 1000000;
                    }
                    if (can && score > bestScore) {
                        bestScore = score;
                        target = map[x][y];
                    }
                }
            }
            if (bestScore != 0) {
                int levelMin = 1;
                int levelMax = 3;
                if (gold < 30) {
                    levelMax = 2;
                }
                if (gold < 20) {
                    levelMax = 1;
                }
                if (target.unit != nullptr) {
                    levelMin = min(target.unit->level + 1, 3);
                }
                int level = levelMin;
                cout << (first ? "" : " ") << "TRAIN " << level << " " << target.x << " " << target.y << ";";
                actionsMade++;
                trainDone = true;
                first = false;
                map[target.x][target.y].owner = 0;
                gold -= level * 10;
            }
        }

        // build mines
        buildDone = true;
        while (gold >= 20 && buildDone && mineCount < towerCount + 5) {
            buildDone = false;
            Cell target;
            int bestScore = 1e6;
            for (int x = 0; x < MAP_WIDTH; ++x) {
                for (int y = 0; y < MAP_HEIGHT; ++y) {
                    if (gold < 20)
                        break;
                    if (map[x][y].owner != 0 || map[x][y].occupied)
                        continue;
                    int score = std::abs(x - myHQ.x) + std::abs(y - myHQ.y);
                    if (score < bestScore) {
                        bestScore = score;
                        target = map[x][y];
                    }
                }
            }
            if (bestScore != 1e6) {
                cout << (first ? "" : " ") << "BUILD " << "MINE" << " " << target.x << " " << target.y << ";";
                gold -= 20;
                buildDone = true;
                mineCount++;
                actionsMade++;
                map[target.x][target.y].occupied = true;
                first = false;
            }
        }

        // build towers
        buildDone = true;
        while (gold >= 15 && buildDone) {
            buildDone = false;
            int bestScore = 1e6;
            Cell target;
            for (int x = 0; x < MAP_WIDTH; ++x) {
                for (int y = 0; y < MAP_HEIGHT; ++y) {
                    if (gold < 20)
                        break;
                    if (map[x][y].owner != 0 || map[x][y].occupied)
                        continue;
                    bool possible = false;
                    for (int dir = 0; dir < 4; ++dir) {
                        Cell* neighbour = map[x][y].neighbours[dir];
                        if (neighbour != nullptr && neighbour->owner == 1) {
                            possible = true;
                        }
                    }
                    int score = std::abs(x - myHQ.x) + std::abs(y - myHQ.y);
                    if (possible && score < bestScore) {
                        bestScore = score;
                        target = map[x][y];
                    }
                }
            }
            if (bestScore != 1e6) {
                cout << (first ? "" : " ") << "BUILD " << "TOWER" << " " << target.x << " " << target.y << ";";
                gold -= 15;
                buildDone = true;
                actionsMade++;
                map[target.x][target.y].occupied = true;
                first = false;
            }
        }

        cerr << "finished training" << endl;
        cerr << "end gold: " << gold << endl;
        if (actionsMade == 0) {
            cout << "WAIT";
        }
        cout << endl;
    }
    return 0;
}