import sys
import math
import collections
import random

random.seed(0)
def debug(*args, **kwargs):
    print(*args, **kwargs, file=sys.stderr)

LEVEL1 = 0
LEVEL2 = 1
LEVEL3 = 2

COST = 0
UPKEEP = 1

UNITS = {
    COST: [10, 20, 30],
    UPKEEP: [1, 4, 20]
}

COST_TOWER = 15


HQ = 0
MINE = 1
TOWER = 2

ME = MINE = OWNED = 0
ENEMY = 1

# #: void
# .: neutral
# O: own and active cell
# o: own and inactive
# X: active opponent cell
# x: inactive opponent cell

VOID = "#"
NEUTRAL = "."
OWN_ACTIVE_CELL = "O"
OWN_INACTIVE_CELL = "o"
OWNED_CELL = [OWN_ACTIVE_CELL, OWN_INACTIVE_CELL]
OPPONENT_ACTIVE_CELL = "X"
OPPONENT_INACTIVE_CELL = "x"

WIDTH = HEIGHT = 12

class Unit:
    def __init__(self, _id, owner, level, x, y, has_moved=False):
        self.x = x
        self.y = y
        self.owner = owner
        self.level = level
        self.id = _id

        self.canKillUnits = []

        if level == 2:
            self.canKillUnits = [1]
        elif level == 3:
            self.canKillUnits = [1, 2, 3]

        self.has_moved = has_moved

    def get_possible_neigbhours_cells(self, carte):
        cx = self.x
        cy = self.y

        dirs = []

        if cy-1 >= 0 and VOID not in carte[cy -1][cx]:
            dirs.append((cx, cy-1))
        if cy+1 < HEIGHT and VOID not in carte[cy +1][cx]:
            dirs.append((cx, cy+1))
        if cx+1 < WIDTH and VOID not in carte[cy][cx+1]:
            dirs.append((cx+1, cy))
        if cx-1 >= 0 and VOID not in carte[cy][cx-1]:
            dirs.append((cx-1, cy))

        return dirs


def find_border(carte, hq_pos):
    seen = set([hq_pos])
    queue = collections.deque([hq_pos])

    border = []

    while queue:
        cell = queue.popleft()

        cx = cell[0]
        cy = cell[1]


        if carte[cy][cx] in OWNED_CELL:

            if cy-1 >= 0 and VOID not in carte[cy -1][cx] and (cx, cy-1) not in seen:
                seen.add((cx, cy-1))
                queue.append((cx, cy-1))
            if cy+1 < HEIGHT and VOID not in carte[cy +1][cx] and (cx, cy+1) not in seen:
                seen.add((cx, cy+1))
                queue.append((cx, cy+1))
            if cx+1 < WIDTH and VOID not in carte[cy][cx+1] and (cx+1, cy) not in seen:
                seen.add((cx+1, cy))
                queue.append((cx+1, cy))
            if cx-1 >=0 and VOID not in carte[cy][cx-1] and (cx-1, cy) not in seen:
                seen.add((cx-1, cy))
                queue.append((cx-1, cy))
        else:
            debug("({}, {})".format(cx, cy))
            border.append((cx,cy))
    return border



width = int(input())
height = int(input())

nb_mines = int(input())

for i in range(nb_mines):
    x, y = [int(j) for j in input().split()]

# game loop
while True:
    gold = int(input())
    debug(gold)
    income = int(input())

    opponent_gold = int(input())
    opponent_income = int(input())

    my_hq_pos = None
    en_hq_pos = None

    carte = []
    for i in range(height):
        line = list(input())
        carte.append(line)
        debug(line)
    debug()
    building_count = int(input())
    debug("{} buildings".format(building_count))
    for i in range(building_count):
        owner, buildingType, x, y = [int(j) for j in input().split()]
        debug("owned by {}: {} on ({}, {})".format(owner, buildingType, x, y))

        if buildingType == HQ and owner == ME:
            my_hq_pos = (x,y)
        if buildingType == HQ and owner != ME:
            en_hq_pos = (x,y)




    my_units = []

    actions = ["WAIT"]

    unit_count = int(input())
    debug("{} units".format(unit_count))
    for i in range(unit_count):
        owner, unitId, level, x, y = [int(j) for j in input().split()]
        debug("level {} unit ({}) owned by {} on ({}, {})".format(level, unitId, owner, x, y))
        unit = Unit(unitId, owner, level, x, y)

        if owner == ME:
            my_units.append(unit)

    for unit in my_units:
        neighbours = unit.get_possible_neigbhours_cells(carte)

        voisin = None

        while voisin is None and len(neighbours) > 1:
            voisin = random.choice(neighbours)
            # si c'est une tuile qu'on là on la vire

            if carte[voisin[1]][voisin[0]] in OWNED_CELL:
                neighbours.remove(voisin)
                voisin = None

        if len(neighbours) == 1:
            voisin = neighbours[0]

        actions.append("MOVE {} {} {}".format(unit.id, voisin[0], voisin[1]))
        carte[voisin[1]][voisin[0]] = OWN_ACTIVE_CELL
        #actions.append("MOVE {} {} {}".format(unit.id, en_hq_pos[0], en_hq_pos[1]))

    border = find_border(carte, my_hq_pos)

    while gold >= UNITS[COST][LEVEL1] and len(border) > 0:
        gold -= UNITS[COST][LEVEL1]
        cell = border.pop()
        while carte[cell[1]][cell[0]] in OWNED_CELL:
            cell = border.pop()

        actions.append("TRAIN {} {} {}".format(1, cell[0], cell[1]))
        carte[cell[1]][cell[0]] = OWN_ACTIVE_CELL

        # Update border
        cx = cell[0]
        cy = cell[1]
        if cy-1 >= 0 and VOID not in carte[cy -1][cx]:
            border.append((cx, cy-1))
        if cy+1 < HEIGHT and VOID not in carte[cy +1][cx]:
            border.append((cx, cy+1))
        if cx+1 < WIDTH and VOID not in carte[cy][cx+1]:
            border.append((cx+1, cy))
        if cx-1 >= 0 and VOID not in carte[cy][cx-1]:
            border.append((cx-1, cy))


    # Write an action using print
    # To debug: print("Debug messages...", file=sys.stderr)

    print(";".join(actions))
