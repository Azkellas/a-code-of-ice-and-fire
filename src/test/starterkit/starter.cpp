#include <iostream>
#include <string>
#include <vector>
#include <list>
#include <algorithm>

using namespace std;

enum BuildingType {
    HQ
};

enum CommandType {
    WAIT,
    MOVE,
    TRAIN
};

ostream &operator<<(ostream &os, CommandType cmdType) {
    switch (cmdType) {
        case WAIT:
            return os << "WAIT";
        case MOVE:
            return os << "MOVE";
        case TRAIN:
            return os << "TRAIN";
    }
    return os;
}

class Position {
public:

    int x, y;

    Position(int x, int y) : x(x), y(y) {
    }
};

class Command {

public:

    CommandType t;
    Position p;
    int idOrLevel;

    Command(CommandType t, int idOrLevel, const Position &p) : t(t), idOrLevel(idOrLevel), p(p) {
    }

    void print() {
        cout << t << " " << idOrLevel << " " << p.x << " " << p.y << ";";
    }
};

class Unit {

public:

    int id;
    int owner;
    int level;
    Position p;

    Unit(int x, int y, int id, int level, int owner) : p(x, y), id(id), level(level), owner(owner) {
    }

    void debug() {
        cerr << "unit of level " << level << " at " << p.x << " " << p.y << " owned by " << owner;
    }

    bool isOwned() {
        return owner == 0;
    }
};

class Building {

public:

    Position p;
    BuildingType t;
    int owner;

    Building(int x, int y, int t, int owner) : p(x, y), t(static_cast<BuildingType>(t)), owner(owner) {
    }

    void debug() {
        cerr << t << " at " << p.x << " " << p.y << " owned by " << owner;
    }

    bool isHQ() {
        return t == HQ;
    }

    bool isOwned() {
        return owner == 0;
    }
    
};

class Game {

    list<Unit> units;

    list<Building> buildings;

    int gold, income;

    list<Command> commandes;

public:

    void debug() {
        for_each(units.begin(), units.end(), [](Unit &u) { u.debug(); });
        for_each(buildings.begin(), buildings.end(), [](Building &u) { u.debug(); });
    }

    // not useful in Wood 3
    void init() {
        int numberMineSpots;
        cin >> numberMineSpots; cin.ignore();
        for (int i = 0; i < numberMineSpots; i++) {
            int x;
            int y;
            cin >> x >> y; cin.ignore();
        }
    }

    void update() {

        units.clear();
        buildings.clear();
        commandes.clear();

        // READ TURN INPUT
        cin >> gold; cin.ignore();
        cin >> income; cin.ignore();

        int opponentGold;
        cin >> opponentGold; cin.ignore();
        int opponentIncome;
        cin >> opponentIncome; cin.ignore();

        for (int i = 0; i < 12; i++) {
            string line;
            cin >> line; cin.ignore();
            cerr << line << endl;
        }
        int buildingCount;
        cin >> buildingCount; cin.ignore();
        for (int i = 0; i < buildingCount; i++) {
            int owner;
            int buildingType;
            int x;
            int y;
            cin >> owner >> buildingType >> x >> y; cin.ignore();
            buildings.push_back(Building(x, y, buildingType, owner));
        }
        int unitCount;
        cin >> unitCount; cin.ignore();
        for (int i = 0; i < unitCount; i++) {
            int owner;
            int unitId;
            int level;
            int x;
            int y;
            cin >> owner >> unitId >> level >> x >> y; cin.ignore();
            units.push_back(Unit(x, y, unitId, level, owner));
        }
    }

    void buildOutput() {
        // @TODO "core" of the AI
        trainUnits();
        
        moveUnits();
    }

    void output() {
        for_each(commandes.begin(), commandes.end(), [](Command &c) {
            c.print();
        });
        cout << "WAIT" <<  endl;
    }


private:

    void trainUnits() {
        Position trainingPosition = findTrainingPosition();

        if (gold > 30) {
            commandes.push_back(Command(TRAIN, 1, trainingPosition));
        }
    }
    
    // move to the center
    void moveUnits() {
        Position center(5, 5);

        for (auto &unit : units) {
            if (unit.isOwned()) {
                commandes.push_back(Command(MOVE, unit.id, center));
            }
        }
    }

    // train near the HQ for now
    Position findTrainingPosition() {
        const Building &HQ = getHQ();

        if (HQ.p.x == 0) {
            return Position(0, 1);
        }
        return Position(11, 10);
    }

    const Building &getHQ() {
        for (auto &b : buildings) {
            if (b.isHQ() && b.isOwned()) {
                return b;
            }
        }
    }

    const Building &getOpponentHQ() {
        for (auto &b : buildings) {
            if (b.isHQ() && !b.isOwned()) {
                return b;
            }
        }
    }
};

int main()
{
    Game g;
    g.init();

    // game loop
    while (true) {
        g.update();
        //g.debug();
        g.buildOutput();
        g.output();
    }
    return 0;
}
