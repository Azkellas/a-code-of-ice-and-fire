import sys
def debug(*args, **kwargs):
	print(*args, **kwargs, file=sys.stderr)

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
COST_MINE = 20
WIDTH = HEIGHT = 12

class Unit:
	def __init__(self, _id, owner, x, y):
		self.x = x
		self.y = y
		self.owner = owner
		self.id = _id

def clamp_coordinates(x, y):
	return min(WIDTH-1, max(0, x)), min(HEIGHT-1, max(0, y))


def is_in_range(x, y):
	return x >= 0 and x < WIDTH and y >= 0 and y < HEIGHT

def is_my_border(game_map, x, y):
	return ((is_in_range(x + 1, y) and game_map[y][x + 1] == OWN_ACTIVE_CELL)
		or (is_in_range(x - 1, y) and game_map[y][x - 1] == OWN_ACTIVE_CELL)
		or (is_in_range(x, y + 1) and game_map[y + 1][x] == OWN_ACTIVE_CELL)
		or (is_in_range(x, y - 1) and game_map[y - 1][x] == OWN_ACTIVE_CELL))


mine_spots = []
nb_mines = int(input())
for i in range(nb_mines):
	x, y = [int(j) for j in input().split()]
	mine_spots.append((x,y))

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

	occupied_cells = []

	building_count = int(input())
	for i in range(building_count):
		owner, buildingType, x, y = [int(j) for j in input().split()]

		if buildingType == HQ and owner == ME:
			my_hq_pos = (x,y)
		if buildingType == HQ and owner != ME:
			en_hq_pos = (x,y)
		if owner == ME:
			occupied_cells.append((x,y))

	actions = ["WAIT"]

	my_units = []
	unit_count = int(input())
	for i in range(unit_count):
		owner, unitId, level, x, y = [int(j) for j in input().split()]
		unit = Unit(unitId, owner, x, y)
		occupied_cells.append((x,y))

		if owner == ME:
			my_units.append(unit)

	for unit in my_units:
		if random.randint(0, 1) == 1:
			move_to_x, move_to_y = clamp_coordinates(unit.x, unit.y + random.choice([1, -1]))
		else:
			move_to_x, move_to_y = clamp_coordinates(unit.x + random.choice([1, -1]), unit.y)

		if game_map[move_to_y][move_to_x] != VOID and (move_to_x, move_to_y) not in occupied_cells:
			actions.append("MOVE {} {} {}".format(unit.id, move_to_x, move_to_y))
			occupied_cells.remove((unit.x, unit.y))
			occupied_cells.append((move_to_x, move_to_y))

	if gold >= COST_MINE:
		for x,y in mine_spots:
			if game_map[y][x] == OWN_ACTIVE_CELL and (x,y) not in occupied_cells:
				actions.append(f"BUILD MINE {x} {y}")
				gold -= COST_MINE
				mine_spots.remove((x,y))
				break

	if gold >= COST_UNIT:
		if my_hq_pos[0] == 0:
			spawn_point_x = 1
			spawn_point_y = 0
			reverse = True
		else:
			spawn_point_x = my_hq_pos[0]-1
			spawn_point_y = my_hq_pos[1]
			reverse = False
		if (spawn_point_x, spawn_point_y) not in occupied_cells:
			actions.append("TRAIN 1 {} {}".format(spawn_point_x, spawn_point_y))
			occupied_cells.append((spawn_point_x, spawn_point_y))

		# if reverse_map:
		# 	game_map = [reversed(l) for l in reversed(game_map)]

		for (y, line) in enumerate(game_map) if not reverse else reversed(list(enumerate(game_map))):
			for (x, cell) in enumerate(line) if not reverse else reversed(list(enumerate(line))):
				if cell == OWN_ACTIVE_CELL and (x,y) != my_hq_pos and not (x,y) in occupied_cells and gold >= 2 * COST_UNIT:
					actions.append("TRAIN 1 {} {}".format(x, y))
					gold -= COST_UNIT
					occupied_cells.append((x,y))
				if cell == OPPONENT_ACTIVE_CELL and is_my_border(game_map, x, y):
					if not (x,y) in occupied_cells:
						actions.append("TRAIN 1 {} {}".format(x, y))
						gold -= COST_UNIT
						occupied_cells.append((x,y))
					elif gold >= 2 * COST_UNIT:
						actions.append("TRAIN 2 {} {}".format(x, y))
						gold -= COST_UNIT * 2
						occupied_cells.append((x,y))
			if gold < COST_UNIT: break

	print(";".join(actions))
