import sys

import random
random.seed(0)

HQ = 0
ME = 0

VOID = "#"
NEUTRAL = "."
OWN_ACTIVE_CELL = "O"
OWN_INACTIVE_CELL = "o"
OWNED_CELL = [OWN_ACTIVE_CELL, OWN_INACTIVE_CELL]
OPPONENT_ACTIVE_CELL = "X"
OPPONENT_INACTIVE_CELL = "x"

COST_UNIT = 10
UPKEEP_UNIT = {
  1: 1,
  2: 4,
  3: 20
}
WIDTH = HEIGHT = 12

def clamp_coordinates(x):
    return min(WIDTH-1, max(0, x))


nb_mines = int(input())

for i in range(nb_mines):
    x, y = [int(j) for j in input().split()]

# game loop
while True:

    gold = int(input())
    income = int(input())

    opponent_gold = int(input())
    opponent_income = int(input())

    my_hq_pos = None
    en_hq_pos = None

    game_map = []
    for i in range(HEIGHT):
        line = list(input())
        game_map.append(line)

    building_count = int(input())
    for i in range(building_count):
        owner, buildingType, x, y = [int(j) for j in input().split()]

        if buildingType == HQ and owner == ME:
            my_hq_pos = (x,y)
        if buildingType == HQ and owner != ME:
            en_hq_pos = (x,y)

    actions = ["WAIT"]

    unit_count = int(input())
    rush = True
    for i in range(unit_count):
        owner, unitId, level, x, y = [int(j) for j in input().split()]

        if owner == ME:
            if rush:
                actions.append("MOVE {} {} {}".format(unitId, en_hq_pos[0], en_hq_pos[1]))
                rush = False
            else:
                actions.append("MOVE {} {} {}".format(unitId, clamp_coordinates(x+random.choice([1, -1])), clamp_coordinates(y+random.choice([1, -1]))))

    if gold >= COST_UNIT * 2 and income >= UPKEEP_UNIT[2]:
        if my_hq_pos[1] == 0:
            spawn_point_x = 0
            spawn_point_y = 1
        else:
            spawn_point_x = my_hq_pos[0]
            spawn_point_y = my_hq_pos[1]-1
        actions.append("TRAIN 2 {} {}".format(spawn_point_x, spawn_point_y))
    elif gold >= COST_UNIT:
        if my_hq_pos[0] == 0:
            spawn_point_x = 1
            spawn_point_y = 0
        else:
            spawn_point_x = my_hq_pos[0]-1
            spawn_point_y = my_hq_pos[1]
        actions.append("TRAIN 1 {} {}".format(spawn_point_x, spawn_point_y))


    print(";".join(actions))
