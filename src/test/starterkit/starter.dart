import 'dart:io';

enum BuildingType { Hq, Mine, Tower }

enum Team { Fire, Ice }

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
  game.Init();

  // game loop
  while (true) {
    game.Update();
    game.Solve();
    print(game.Output.join(';'));
  }
}

class Game {
  List<String> Output = [];

  List<List<Tile>> map = List.generate(WIDTH, (i) => List(HEIGHT));

  List<Position> MineSpots = [];

  List<Unit> MyUnits = [];
  List<Unit> OpponentUnits = [];

  List<Building> MyBuildings = [];
  List<Building> OpponentBuildings = [];

  int MyGold;
  int MyIncome;
  Team MyTeam;

  int OpponentGold;
  int OpponentIncome;
  int Turn = 0;

  Position MyHq() => MyTeam == Team.Fire ? Position(0, 0) : Position(11, 11);
  Position OpponentHq() =>
      MyTeam == Team.Fire ? Position(11, 11) : Position(0, 0);

  List<Position> MyPositions = new List<Position>();
  List<Position> OpponentPositions = new List<Position>();
  List<Position> NeutralPositions = new List<Position>();

  void Init() {
    for (var y = 0; y < HEIGHT; y++) {
      for (var x = 0; x < WIDTH; x++) {
        map[x][y] = Tile(Position(x, y));
      }
    }

    var numberMineSpots = int.parse(stdin.readLineSync());
    for (var i = 0; i < numberMineSpots; i++) {
      var inputs = stdin.readLineSync().split(' ');
      MineSpots.add(Position(int.parse(inputs[0]), int.parse(inputs[1])));
    }
  }

  void Update() {
    MyUnits.clear();
    OpponentUnits.clear();

    MyBuildings.clear();
    OpponentBuildings.clear();

    MyPositions.clear();
    OpponentPositions.clear();
    NeutralPositions.clear();

    Output.clear();

    // --------------------------------------

    MyGold = int.parse(stdin.readLineSync());
    MyIncome = int.parse(stdin.readLineSync());
    OpponentGold = int.parse(stdin.readLineSync());
    OpponentIncome = int.parse(stdin.readLineSync());

    // Read map
    for (var y = 0; y < HEIGHT; y++) {
      var line = stdin.readLineSync();
      for (var x = 0; x < WIDTH; x++) {
        var c = line[x] + "";
        map[x][y].IsWall = c == "#";
        map[x][y].Active = "OX".contains(c);
        map[x][y].Owner = c.toLowerCase() == "o"
            ? ME
            : c.toLowerCase() == "x" ? OPPONENT : NEUTRAL;
        map[x][y].HasMineSpot = MineSpots.fold(
                0, (c, spot) => c + (spot == Position(x, y) ? 1 : 0)) >
            0;

        Position p = Position(x, y);
        if (map[x][y].IsOwned()) {
          MyPositions.add(p);
        } else if (map[x][y].IsOpponent()) {
          OpponentPositions.add(p);
        } else if (!map[x][y].IsWall) {
          NeutralPositions.add(p);
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
        MyBuildings.add(Building(BuildingType.values[type], pos));
      } else if (owner == OPPONENT) {
        OpponentBuildings.add(Building(BuildingType.values[type], pos));
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
        MyUnits.add(Unit(id, level, pos));
      } else if (owner == OPPONENT) {
        OpponentUnits.add(Unit(id, level, pos));
      }
    }

    // --------------------------------

    // Get Team
    MyTeam = MyBuildings.singleWhere((b) => b.IsHq()).position == Position(0, 0)
        ? Team.Fire
        : Team.Ice;

    // Usefull for symmetric AI
    if (MyTeam == Team.Ice) {
      MyPositions = MyPositions.reversed;
      OpponentPositions = OpponentPositions.reversed;
      NeutralPositions = NeutralPositions.reversed;
    }

    // --------------------------------

    Debug();
  }

  void Debug() {
    stderr.writeln("Turn: $Turn");
    stderr.writeln("My team: $MyTeam");
    stderr.writeln("My gold: $MyGold (+$MyIncome)");
    stderr.writeln("Opponent gold: $OpponentGold (+$OpponentIncome)");

    stderr.writeln("=== My ===");
    for (var b in MyBuildings) stderr.writeln(b);
    for (var u in MyUnits) stderr.writeln(u);

    stderr.writeln("=== Opponent ===");
    for (var b in OpponentBuildings) stderr.writeln(b);
    for (var u in MyUnits) stderr.writeln(u);
  }

  void Solve() {
    // Make sur the AI doesn't timeout
    Wait();

    MoveUnits();

    TrainUnits();

    Turn++;
  }

  void MoveUnits() {
    // Rush center
    Position target = MyTeam == Team.Fire ? Position(5, 5) : Position(6, 6);

    if (map[target.X][target.Y].IsOwned()) return;

    for (var unit in MyUnits) {
      Move(unit.Id, target);
    }
  }

  void TrainUnits() {
    Position target = MyTeam == Team.Fire ? Position(1, 0) : Position(10, 11);

    if (MyGold >= UNIT_COST[1]['train']) Train(1, target);
  }

  void Wait() {
    Output.add("WAIT");
  }

  void Train(int level, Position position) {
    // TODO: Handle upkeep
    MyGold -= UNIT_COST[level]['train'];
    Output.add("TRAIN $level $position");
  }

  void Move(int id, Position position) {
    // TODO: Handle map change
    Output.add("MOVE $id $position");
  }

  // TODO: Handle Build command
}

class Unit {
  int Id;
  int Level;
  Position position;

  Unit(this.Id, this.Level, this.position);

  String toString() => "Unit => Id: $Id Level: $Level Position: $position";
}

class Building {
  BuildingType Type;
  Position position;

  Building(this.Type, this.position);

  bool IsHq() => Type == BuildingType.Hq;
  bool IsTower() => Type == BuildingType.Tower;
  bool IsMine() => Type == BuildingType.Mine;

  String toString() => "Building => Type: $Type Position: $position";
}

class Entity {
  int Owner;
  Position position;

  bool IsOwned() => Owner == ME;
  bool IsOpponent() => Owner == OPPONENT;

  int X() => position.X;
  int Y() => position.Y;

  String toString() => 'Owner: $Owner Position: $position';
}

class Tile {
  Position position;

  bool Active;
  bool HasMineSpot;
  bool IsWall;
  int Owner = NEUTRAL;

  Tile(this.position);

  int X() => position.X;
  int Y() => position.Y;

  bool IsOwned() => Owner == ME;
  bool IsOpponent() => Owner == OPPONENT;
  bool IsNeutral() => Owner == NEUTRAL;
}

class Position {
  int X;
  int Y;

  Position(this.X, this.Y);

  String toString() => '$X $Y';

  @override
  int get hashCode {
    int result = 17;
    result = 37 * result + X;
    result = 37 * result + Y;
    return result;
  }

  @override
  bool operator ==(dynamic other) {
    if (other is! Position) return false;
    return other.X == X && other.Y == Y;
  }

  int Dist(Position p) => (X - p.X).abs() + (Y - p.Y).abs();
}
