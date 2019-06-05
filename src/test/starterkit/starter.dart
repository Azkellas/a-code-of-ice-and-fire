import 'dart:io';

enum BuildingType { hq, mine, tower }

enum Team { fire, ice }

const int WIDTH = 12;
const int HEIGHT = 12;

const int ME = 0;
const int OPPONENT = 1;
const int NEUTRAL = -1;

final UNIT_COST = const {
  1: const {'train': 10, 'upkeep': 1},
  2: const {'train': 20, 'upkeep': 4},
  3: const {'train': 30, 'upkeep': 20},
};

const TOWER_COST = 15;
const MINE_COST = 20;

void main() {
  var game = Game();
  game.init();

  // game loop
  while (true) {
    game.update();
    game.solve();
    print(game.output.join(';'));
  }
}

class Game {
  List<String> output = [];

  List<List<Tile>> map = List.generate(WIDTH, (i) => List(HEIGHT));

  List<Position> mineSpots = [];

  List<Unit> myUnits = [];
  List<Unit> opponentUnits = [];

  List<Building> myBuildings = [];
  List<Building> opponentBuildings = [];

  int myGold;
  int myIncome;
  Team myTeam;

  int opponentGold;
  int opponentIncome;
  int turn = 0;

  Position myHq() => myTeam == Team.fire ? Position(0, 0) : Position(11, 11);
  Position opponentHq() =>
      myTeam == Team.fire ? Position(11, 11) : Position(0, 0);

  List<Position> myPositions = [];
  List<Position> opponentPositions = [];
  List<Position> neutralPositions = [];

  void init() {
    for (var y = 0; y < HEIGHT; y++) {
      for (var x = 0; x < WIDTH; x++) {
        map[x][y] = Tile(Position(x, y));
      }
    }

    var numberMineSpots = int.parse(stdin.readLineSync());
    for (var i = 0; i < numberMineSpots; i++) {
      var inputs = stdin.readLineSync().split(' ');
      mineSpots.add(Position(int.parse(inputs[0]), int.parse(inputs[1])));
    }
  }

  void update() {
    myUnits.clear();
    opponentUnits.clear();

    myBuildings.clear();
    opponentBuildings.clear();

    myPositions.clear();
    opponentPositions.clear();
    neutralPositions.clear();

    output.clear();

    // --------------------------------------

    myGold = int.parse(stdin.readLineSync());
    myIncome = int.parse(stdin.readLineSync());
    opponentGold = int.parse(stdin.readLineSync());
    opponentIncome = int.parse(stdin.readLineSync());

    // Read map
    for (var y = 0; y < HEIGHT; y++) {
      var line = stdin.readLineSync();
      for (var x = 0; x < WIDTH; x++) {
        var c = line[x] + "";
        map[x][y].isWall = c == "#";
        map[x][y].active = "OX".contains(c);
        map[x][y].owner = c.toLowerCase() == "o"
            ? ME
            : c.toLowerCase() == "x" ? OPPONENT : NEUTRAL;
        map[x][y].hasMineSpot = mineSpots.fold(
                0, (c, spot) => c + (spot == Position(x, y) ? 1 : 0)) >
            0;

        Position p = Position(x, y);
        if (map[x][y].isOwned()) {
          myPositions.add(p);
        } else if (map[x][y].isOpponent()) {
          opponentPositions.add(p);
        } else if (!map[x][y].isWall) {
          neutralPositions.add(p);
        }
      }
    }

    // Read Buildings
    var buildingCount = int.parse(stdin.readLineSync());
    for (var i = 0; i < buildingCount; i++) {
      var inputs = stdin.readLineSync().split(' ');
      int owner = int.parse(inputs[0]);
      int type = int.parse(inputs[1]);
      Position pos = Position(int.parse(inputs[2]), int.parse(inputs[3]));
      if (owner == ME) {
        myBuildings.add(Building(BuildingType.values[type], pos));
      } else if (owner == OPPONENT) {
        opponentBuildings.add(Building(BuildingType.values[type], pos));
      }
    }

    // Read Units
    var unitCount = int.parse(stdin.readLineSync());
    for (var i = 0; i < unitCount; i++) {
      var inputs = stdin.readLineSync().split(' ');
      int owner = int.parse(inputs[0]);
      int id = int.parse(inputs[1]);
      int level = int.parse(inputs[2]);
      Position pos = Position(int.parse(inputs[3]), int.parse(inputs[4]));
      if (owner == ME) {
        myUnits.add(Unit(id, level, pos));
      } else if (owner == OPPONENT) {
        opponentUnits.add(Unit(id, level, pos));
      }
    }

    // --------------------------------

    // Get Team
    myTeam = myBuildings.singleWhere((b) => b.isHq()).position == Position(0, 0)
        ? Team.fire
        : Team.ice;

    // Usefull for symmetric AI
    if (myTeam == Team.ice) {
      myPositions = myPositions.reversed;
      opponentPositions = opponentPositions.reversed;
      neutralPositions = neutralPositions.reversed;
    }

    // --------------------------------

    debug();
  }

  void debug() {
    stderr.writeln("Turn: $turn");
    stderr.writeln("My team: $myTeam");
    stderr.writeln("My gold: $myGold (+$myIncome)");
    stderr.writeln("Opponent gold: $opponentGold (+$opponentIncome)");

    stderr.writeln("=== My ===");
    for (var b in myBuildings) stderr.writeln(b);
    for (var u in myUnits) stderr.writeln(u);

    stderr.writeln("=== Opponent ===");
    for (var b in opponentBuildings) stderr.writeln(b);
    for (var u in myUnits) stderr.writeln(u);
  }

  void solve() {
    // Make sur the AI doesn't timeout
    wait();

    moveUnits();

    trainUnits();

    turn++;
  }

  void moveUnits() {
    // Rush center
    Position target = myTeam == Team.fire ? Position(5, 5) : Position(6, 6);

    if (map[target.x][target.y].isOwned()) return;

    for (var unit in myUnits) {
      move(unit.id, target);
    }
  }

  void trainUnits() {
    Position target = myTeam == Team.fire ? Position(1, 0) : Position(10, 11);

    if (myGold >= UNIT_COST[1]['train']) train(1, target);
  }

  void wait() {
    output.add("WAIT");
  }

  void train(int level, Position position) {
    // TODO: Handle upkeep
    myGold -= UNIT_COST[level]['train'];
    output.add("TRAIN $level $position");
  }

  void move(int id, Position position) {
    // TODO: Handle map change
    output.add("MOVE $id $position");
  }

  // TODO: Handle Build command
}

class Unit {
  int id;
  int level;
  Position position;

  Unit(this.id, this.level, this.position);

  String toString() => "Unit => Id: $id Level: $level Position: $position";
}

class Building {
  BuildingType type;
  Position position;

  Building(this.type, this.position);

  bool isHq() => type == BuildingType.hq;
  bool isTower() => type == BuildingType.tower;
  bool isMine() => type == BuildingType.mine;

  String toString() => "Building => Type: $type Position: $position";
}

class Tile {
  Position position;

  bool active;
  bool hasMineSpot;
  bool isWall;
  int owner = NEUTRAL;

  Tile(this.position);

  bool isOwned() => owner == ME;
  bool isOpponent() => owner == OPPONENT;
  bool isNeutral() => owner == NEUTRAL;
}

class Position {
  int x;
  int y;

  Position(this.x, this.y);

  String toString() => '$x $y';

  @override
  int get hashCode {
    int result = 17;
    result = 37 * result + x;
    result = 37 * result + y;
    return result;
  }

  @override
  bool operator ==(dynamic other) {
    if (other is! Position) return false;
    return other.x == x && other.y == y;
  }

  int distance(Position p) => (x - p.x).abs() + (y - p.y).abs();
}
