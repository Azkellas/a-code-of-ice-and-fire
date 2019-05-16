<!-- LEAGUES level1 level2 level3 level4 level5 -->
<div id="statement_back" class="statement_back" style="display:none"></div>
<div class="statement-body">

    <!-- BEGIN level2 level3 level4 -->
    <!-- LEAGUE ALERT -->
    <div style="color: #7cc576;
    background-color: rgba(124, 197, 118,.1);
    padding: 20px;
    margin-right: 15px;
    margin-left: 15px;
    margin-bottom: 10px;
    text-align: left;">

        <div style="text-align: center; margin-bottom: 6px">
            <img src="//cdn.codingame.com/smash-the-code/statement/league_wood_04.png"/>
        </div>

        <p style="text-align: center; font-weight: 700; margin-bottom: 6px;">
          <!-- BEGIN level1 -->
          This is a <b>league based</b> challenge.
          <!-- END -->
          <!-- BEGIN level2 -->
          Welcome to the Wood2 league!
          <!-- END -->
          <!-- BEGIN level3 -->
          Welcome to the Wood1 league!
          <!-- END -->
          <!-- BEGIN level4 -->
          Welcome to the Bronze league!
          <!-- END -->
        </p>
        <span class="statement-league-alert-content">
            <!-- BEGIN level1 -->
              Wood leagues should be considered as a tutorial which lets players discover the different rules of the game. <br/>
              In Bronze league, all rules will be unlocked and the real challenge will begin. <br/> <br/>
            <!-- END -->
            <!-- BEGIN level2 -->
            You are now able to train level 2 and 3 units. Level 1 units cannot kill level 1 units anymore.<br/>
            <!-- END -->
            <!-- BEGIN level3 -->
            You are now able to build mines (<action>BUILD MINE</action>) to generate more income. They can be built only on mine spots.<br />
            <!-- END -->
            <!-- BEGIN level4 -->
            You are now able to build towers (<action>BUILD TOWER</action>) to protect cells. You now have access to all the rules.<br />
            <!-- END -->
            <!-- BEGIN level1 level2 level3 level4 -->
              Starter AIs are available in the <a target="_blank" href="https://github.com/Azkellas/a-code-of-ice-and-fire/tree/master/src/test/starterkit">Starter Kit</a>. They can help you get started with coding your own bot.
            <!-- END -->
        </span>
    </div>
    <!-- END -->


    <!-- GOAL -->
    <div class="statement-section statement-goal">
        <h1>
            <span class="icon icon-goal">&nbsp;</span>
            <span>The Goal</span>
        </h1>
        
        <div class="statement-goal-content">
            Build armies to defeat your opponent by destroying their headquarters.<br/>
            <br/>
        </div>
    </div>

    <!-- RULES -->
    <div class="statement-section statement-rules">
        <h1>
            <span class="icon icon-rules">&nbsp;</span>
            <span>Rules</span>
        </h1>
        <div>
            <div class="statement-rules-content">
                <p><strong>The map</strong></p>
                <p>The map is a grid of size <const>12x12</const>, where the top-left corner is the cell <const>(0,0)</const>. The map is randomly generated at the start of each game.
                </p>
                <p>
                    Both players start from opposite edges of the map (<const>(0,0)</const> and <const>(11,11)</const>).</p>
                <p>
                    A map cell can be either:
                </p>
                    <ul style="padding-top: 0;">
                        <li> void (<const>#</const>): it is not a playable cell (like a hole or a wall).</li>
                        <li> neutral (<const>.</const>): the cell is neutral (colored in grey).</li>
                        <li> captured (<const>O</const> or <const>X</const>): the cell belongs to a player and is of the color of the owner. <img src="http://file.azke.fr/tuile_red02.png" style="height:20px;"/> <img src="http://file.azke.fr/tuile_blue02.png" style="height:20px;"/></li>
                        <li> inactive (<const>o</const> or <const>x</const>): the cell used to belong to a player but is not connected to this player's territory anymore (see next section). <img src="http://file.azke.fr/tuile_red01.png" style="height:20px;"/> <img src="http://file.azke.fr/tuile_blue01.png" style="height:20px;"/></li>
                    </ul>


                <br/>
                <p><strong>Territory ownership</strong></p>
                <p>
                    Throughout the game, each player will capture cells to enlarge their territory. A player territory is composed of all the cells owned by the player that are <strong>active</strong>.
                </p>
                <p>
                    A cell is said to be <strong>active</strong> if and only if the cell is connected to the headquarters. That is, there exists a path of owned cells from the headquarters to this cell.
                </p>
                <div style="margin-left:auto; margin-right:auto; width:350px;">
                    <img src="https://i.imgur.com/LMAw3OG.png" alt="territory-ownership" style="margin-left:auto; margin-right:auto; width:350px;" /><br/>
                    <p>The red player's territory is composed of 6 cells. They used to own the 3 red-dark cells but the blue player interrupted part of this territory. These cells are thus inactive. By capturing, for example, the cell marked by an X, the red player can make these cells active again.</p>
                </div>

                <br/>
                <p><strong>Income</strong></p>
                <p>
                    At the beginning of each turn, a player gains or loses gold based on their income. A player has <const>+1</const> income for each <strong>active</strong> cell owned.
                </p>
                <p>
                    Every turn, army units cost some income (upkeep).
                </p>
                <!-- BEGIN level1 -->
                    <ul style="padding-top: 0; padding-bottom:0;">
                        <li>Level <const>1</const> units reduce income by <const>1</const> per unit.</li>
                    </ul>
                <!-- END -->
                <!-- BEGIN level2 -->
                <div style="color: #7cc576;
                    background-color: rgba(124, 197, 118,.1);
                    padding: 2px;">
                    <ul style="padding-top: 0; padding-bottom: 0;">
                      <li>Level <const>1</const> units reduce income by <const>1</const> per unit.</li>
                      <li>Level <const>2</const> units reduce income by <const>4</const> per unit.</li>
                      <li>Level <const>3</const> units reduce income by <const>20</const> per unit.</li>
                    </ul>
                </div>
                <!-- END -->
                <!-- BEGIN level3 level4 level5 -->
                <ul style="padding-top: 0; padding-bottom: 0;">
                  <li>Level <const>1</const> units reduce income by <const>1</const> per unit.</li>
                  <li>Level <const>2</const> units reduce income by <const>4</const> per unit.</li>
                  <li>Level <const>3</const> units reduce income by <const>20</const> per unit.</li>
                </ul>
                <!-- END -->
                <!-- BEGIN level3 -->
                <div style="color: #7cc576;
                    background-color: rgba(124, 197, 118,.1);
                    padding: 2px;">
                    <p>
                        Players has <const>+4</const> income for each mine they control.
                    </p>
                </div>
                <!-- END -->
                <!-- BEGIN level4 level5 -->
                    <p>
                        Players has <const>+4</const> income for each mine they control.
                    </p>
                <!-- END -->
                <p>
                  If a player has negative income and cannot pay their upkeep using their gold, all of the player's units die and the player's gold is reset to <const>0</const>.
                </p>

                <br/>
                <p><strong>Buildings</strong></p>
                <p>
                    Players starts the game with the most important building: the <img src="http://file.azke.fr/forteresse_red.png" style="height:20px;"/> <strong>headquarters (HQ)</strong>. Losing headquarters results in a lost game.<br/>
                </p>
                <!-- BEGIN level1 level2 -->
                <p>
                    In this league, players cannot build buildings.<br/>
                </p>
                <!-- END -->
                <!-- BEGIN level3 -->
                <div style="color: #7cc576;
                    background-color: rgba(124, 197, 118,.1);
                    padding: 2px;">
                    <p>
                        Players can <action>BUILD</action> buildings to improve their economy or military power. A player can only build on owned active cells that are unoccupied.
                    </p>
                    <p>
                        In this league, players can only build one building: the <action>MINE</action>.
                    </p>
                    <ul style="padding-top: 0; padding-bottom: 0;">
                        <li><img src="http://file.azke.fr/mine_neutre.png" style="height:20px;"/><action>MINE</action>: mines produce gold each turn and can only be built on mine spots. Mines cost <const>20 + 4 * playerNbOfMines</const> to build. So the first costs 20, the second one 24... A mine's income is always <const>+4</const>.</li>
                    </ul>
                </div>
                <!-- END -->

                <!-- BEGIN level4 -->
                <p>
                    Players can <action>BUILD</action> buildings to improve their economy or military power. A player can only build on owned active cells that are unoccupied.
                </p>
                <div style="color: #7cc576;
                    background-color: rgba(124, 197, 118,.1);
                    padding: 2px;">
                    <p>
                        Players can build two different building: the <action>MINE</action> and the <action>TOWER</action>.
                    </p>
                </div>
                <ul style="padding-top: 0; padding-bottom: 0;">
                    <li><img src="http://file.azke.fr/mine_neutre.png" style="height:20px;"/><action>MINE</action>: mines produce gold each turn and can only be built on mine spots. Mines cost <const>20 + 4 * playerNbOfMines</const>. So the first costs 20, the second one 24... A mine's income is always <const>+4</const>.</li>
                    <li style="color: #7cc576;
                    background-color: rgba(124, 197, 118,.1);
                    padding: 2px;"><img src="http://file.azke.fr/tower_red.png" style="height:20px;"/>
                        <action>TOWER</action>: towers protect owned cells that are adjacent (up, down, left and right, but not diagonally). Cells protected by a tower can only be reached by enemy units of level <const>3</const>. Likewise, the tower can only be destroyed by a level <const>3</const> unit. A tower costs <const>15</const> gold to build and cannot be built on a mine spot.
                        Here is a figure of a tower protection range:
                        <div style="margin-left:auto; margin-right:auto; width:350px;">
                            <img src="https://i.imgur.com/WWu1qPB.png" alt="tower-protection" style="margin-left:auto; margin-right:auto; width:350px;" /><br/>
                            <p>
                                The tower owned by the red player protects the nearby cells marked by a black cross. Cells are <strong>not</strong> protected diagonally. Moreover, the blue cell on the right of the tower is not protected as it is not a red cell.
                            </p>
                        </div>
                    </li>
                </ul>
                <!-- END -->
                <!-- BEGIN level5 -->
                <p>
                    Players can <action>BUILD</action> buildings to improve their economy or military power. A player can only build on owned active cells that are unoccupied.
                </p>
                <p>
                    Players can build two different building: the <action>MINE</action> and the <action>TOWER</action>.
                </p>
                <ul style="padding-top: 0; padding-bottom: 0;">
                    <li><img src="http://file.azke.fr/mine_neutre.png" style="height:20px;"/><action>MINE</action>: mines produce gold each turn and can only be built on mine spots. Mines cost <const>20 + 4 * playerNbOfMines</const>. So the first costs 20, the second one 24... A mine's income is always <const>+4</const>.</li>
                    <li><img src="http://file.azke.fr/tower_red.png" style="height:20px;"/>
                        <action>TOWER</action>: towers protect owned cells that are adjacent (up, down, left and right, but not diagonally). Cells protected by a tower can only be reached by enemy units of level <const>3</const>. Likewise, the tower can only be destroyed by a level <const>3</const> unit. A tower costs <const>15</const> gold to build and cannot be built on a mine spot.
                        Here is a figure of a tower protection range:
                        <div style="margin-left:auto; margin-right:auto; width:350px;">
                            <img src="https://i.imgur.com/WWu1qPB.png" alt="tower-protection" style="margin-left:auto; margin-right:auto; width:350px;" /><br/>
                            <p>
                                The tower owned by the red player protects the nearby cells marked by a black cross. Cells are <strong>not</strong> protected diagonally. Moreover, the blue cell on the right of the tower is not protected as it is not a red cell.
                            </p>
                        </div>
                    </li>
                </ul>
                <!-- END -->
                <!-- BEGIN level3 -->
                <div style="color: #7cc576;
                    background-color: rgba(124, 197, 118,.1);
                    padding: 2px;">
                    <p>
                        If a building is on an inactive cell, it is not destroyed; it is just inactive.
                    </p>
                </div>
                <!-- END -->
                <!-- BEGIN level4 level5 -->
                <p>
                    If a building is on an inactive cell, it is not destroyed; it is just inactive.
                </p>
                <!-- END -->

                <br/>
                <p><strong>Armies</strong></p>
                <p>
                    Army units are used to capture cells and to destroy opponent's buildings and units.
                </p>
                <p>
                    There are <const>3</const> levels of units. The higher the level, the stronger the unit is.
                    <!-- BEGIN level1 -->
                    <br/>
                    However, in this league, only level 1 units are available. They cost <const>10</const> to train.
                    <!-- END -->
                </p>
                <p>
                    To capture a cell, destroy a unit or a building on a cell, a unit has to move on the cell.
                </p>
                <!-- BEGIN level1 -->
                <p>
                    In this league <strong>only</strong>, level 1 units can destroy other level 1 units. Only the attacker unit survives.
                </p>
                <!-- END -->
                <!-- BEGIN level2 -->
                <div style="color: #7cc576;
                    background-color: rgba(124, 197, 118,.1);
                    padding: 2px;">
                    <p>
                        Army units can only destroy units of inferior level, except level 3 units which can destroy any unit.
                    </p>
                    <p>
                        If the attacking unit cannot destroy the defending unit, the action is invalid; nothing happens. </br>
                        If both units are level 3, only the attacker survives.
                    </p>
                </div>
                <!-- END -->
                <!-- BEGIN level3 level4 level5 -->
                <p>
                    Army units can only destroy units of inferior level, except level 3 units which can destroy any unit.
                </p>
                <p>
                    If the attacking unit cannot destroy the defending unit, the action is invalid; nothing happens. </br>
                    If both units are level 3, only the attacker survives.
                </p>
                <!-- END -->
                <!-- BEGIN level4 -->
                <div style="color: #7cc576;
                    background-color: rgba(124, 197, 118,.1);
                    padding: 2px;">
                    <p>
                        Towers can only be destroyed by level 3 units.
                    </p>
                    <p>
                        Army units of level inferior to 3 cannot move on a cell protected by an opponent tower.
                    </p>
                </div>
                <!-- END -->
                <!-- BEGIN level5 -->
                <p>
                    Towers can only be destroyed by level 3 units.
                </p>
                <p>
                    Army units of level inferior to 3 cannot move on a cell protected by an opponent tower.
                </p>
                <!-- END -->
                <p>
                    If a unit is on an inactive cell at the beginning of a turn, the unit is instantly destroyed.
                </p>
                    <!-- BEGIN level2 level3 level4 level5 -->
                <p>
                    Summary of every unit characteristics:
                </p>
                    <table style="text-align:center; width: 100%; border-spacing: 10px; border-collapse: separate;">
                        <tr>
                            <th>Level</th>
                            <td>1</td>
                            <td>2</td>
                            <td>3</td>
                        </tr>
                        <tr>
                            <th>Recruitment cost</th>
                            <td>10</td>
                            <td>20</td>
                            <td>30</td>
                        </tr>
                        <tr>
                            <th>Upkeep</th>
                            <td>1</td>
                            <td>4</td>
                            <td>20</td>
                        </tr>
                        <tr>
                            <th>Can kill units level</th>
                            <td>-</td>
                            <td>1</td>
                            <td>1, 2, 3</td>
                        </tr>
                        <tr>
                            <th>Can destroy</th>
                            <td>
                                <!-- BEGIN level3 level4 level5 -->
                                Mines
                                <br/>
                                <!-- END -->
                                HQ
                            </td>
                            <td>
                                <!-- BEGIN level3 level4 level 5-->
                                Mines
                                <br/>
                                <!-- END -->
                                HQ
                            </td>
                            <td>
                                <!-- BEGIN level3 level4 level 5-->
                                Mines
                                <br/>
                                <!-- END -->
                                <!-- BEGIN level4 level 5 -->
                                Towers
                                <br/>
                                <!-- END -->
                                HQ
                            </td>
                        </tr>
                    </table>
                    <!-- END -->
                

                
                    <ul style="padding-top: 0; padding-bottom: 0;">
                        <li>Each unit can only move one cell per turn by using the command <action>MOVE id x y</action>.</li>
                        <ul style="padding-top: 0; padding-bottom: 0;">
                            <li style="list-style-type: circle;">
                                The target cell must be free or capturable. Units cannot move on friendly buildings.
                            </li>
                            <li style="list-style-type: circle;">
                                If the distance between the unit and the target coordinates (x,y) is greater than 1, the unit moves towards the target.
                            </li>
                            <li style="list-style-type: circle;">
                                A army unit cannot move the turn it's created.
                            </li>
                        </ul>
                        <li>Players can train new units for their army using the <action>TRAIN level x y</action> action. They can only be trained on cells in the player's territory or its direct neighbourhood (border).
                        </li>
                        <li>
                            Training follows the same rules as moving: no training can be done on a cell occupied by a friendly unit/building, and the unit must be powerful enough to be trained on a cell occupied by an enemy.</li>
                        <li>All actions are processed sequentially. Every invalid actions are ignored.</li>
                        
                    </ul>
                    <div style="margin-left:auto; margin-right:auto; width:500px;">
                            <img src="https://i.imgur.com/6guSobH.png" alt="action-sequence-diagram" style="margin-left:auto; margin-right:auto; width:550px;" />
                    </div>
            </div>

            <!-- Victory conditions -->
            <div class="statement-victory-conditions">
                <div class="icon victory"></div>
                <div class="blk">
                    <div class="title">Victory Conditions</div>
                    <div class="text">
                        <ul style="padding-bottom: 0;">
                            <li>Destroy the enemy headquarters.</li>
                            <li>After <const>100</const> turns, you have more military power than your opponent. The military power is computed by the sum of the cost of all of your units + your amount of gold.</li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- Lose conditions -->
            <div class="statement-lose-conditions">
                <div class="icon lose"></div>
                <div class="blk">
                    <div class="title">Lose Conditions</div>
                    <div class="text">
                        <ul style="padding-bottom: 0;">
                            <li>You fail to provide a valid command in time.</li>
                            <li>You provide a unrecognized command.</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- EXPERT RULES -->
    <!-- BEGIN level4 level5 -->
    <div class="statement-section statement-expertrules">
        <h1>
            <span class="icon icon-expertrules">&nbsp;</span>
            <span>Expert Rules</span>
        </h1>

        <div class="statement-expert-rules-content">
            The source code can be found here: <a src="https://github.com/Azkellas/a-code-of-ice-and-fire">https://github.com/Azkellas/a-code-of-ice-and-fire</a>.
            <ul style="padding-top: 0; padding-bottom: 0;">
                <li>There are between <const>8</const> and <const>20</const> mine spots.</li>
                <li>A mine spot is always present on the cell directly on the right of the red HQ and on the cell directly on the left of the blue HQ.</li>
                <li>Cells surrounding the HQ at distance 1 (including diagonally) are never <strong>void</strong> cells.</li>
            </ul>
        </div>
    </div>
    <!-- END -->

    <!-- PROTOCOL -->
    <div class="statement-section statement-protocol">
        <h1>
            <span class="icon icon-protocol">&nbsp;</span>
            <span>Game Input</span>
        </h1>
        <!-- Protocol block -->
        <div class="blk">
            <div class="title">Initialization input</div>
            <div class="text">
                <p><span class="statement-lineno">Line 1: </span>one integer <var>numberMineSpots</var>: the number of mine spots on the map.
                    <!-- BEGIN level1 level2 -->
                    Mine spots will be used from Wood1 league onwards.
                    <!-- END -->
                </p>
                <span class="statement-lineno">Next <var>numberMineSpots</var> lines:</span> two integers
                    <ul style="padding-top: 0; padding-bottom: 0;">
                        <li><var>x</var> and <var>y</var>: coordinates of the mine spot.</li>
                    </ul>
            </div>

            <div class="title">Game turn input</div>
            <div class="text">
                <p><span class="statement-lineno">Line 1:</span> an integer <var>gold</var>, the player's amount of gold.</p>
                <p><span class="statement-lineno">Line 2:</span> an integer <var>income</var>, the player's income.</p>
                <p><span class="statement-lineno">Line 3:</span> an integer <var>opponentGold</var>, the opponent's amount of gold.</p>
                <p><span class="statement-lineno">Line 4:</span> an integer <var>opponentIncome</var>: the opponent's income.</p>

                <p><span class="statement-lineno">Next <const>12</const> lines:</span> <const>12</const> characters, on for each cell:</p>
                        <ul style="padding-top: 0; padding-bottom: 0;">
                            <li style="list-style-type: circle;"><const>#</const>: void</li>
                            <li style="list-style-type: circle;"><const>.</const>: neutral</li>
                            <li style="list-style-type: circle;"><const>O</const>: owned and active cell</li>
                            <li style="list-style-type: circle;"><const>o</const>: owned and inactive</li>
                            <li style="list-style-type: circle;"><const>X</const>: active opponent cell</li>
                            <li style="list-style-type: circle;"><const>x</const>: inactive opponent cell</li>
                        </ul>
                <p><span class="statement-lineno">Next line:</span> <var>buildingCount</var>: the amount of buildings on the map.</p>
                <p><span class="statement-lineno">Next <var>buildingCount</var> lines:</span> four integers</p>
                    <ul style="padding-top: 0; padding-bottom: 0;">
                        <li><var>owner</var>
                            <ul style="padding-top: 0; padding-bottom: 0;">
                                <li style="list-style-type: circle;"><const>0</const>: Owned</li>
                                <li style="list-style-type: circle;"><const>1</const>: Enemy</li>
                            </ul>
                        </li>
                        <li><var>buildingType</var>: the type of building
                            <ul style="padding-top: 0; padding-bottom: 0;">
                                <li style="list-style-type: circle;"><const>0</const>: HQ</li>
                                
                                <!-- BEGIN level3 -->
                                <li style="list-style-type: circle;"><const>1</const>: Mine</li>
                                <!-- END -->
                                
                                <!-- BEGIN level4 level5 -->
                                <li style="list-style-type: circle;"><const>1</const>: Mine</li>
                                <li style="list-style-type: circle;"><const>2</const>: Tower</li>
                                <!-- END -->
                            </ul>
                        </li>
                        <li><var>x</var> and <var>y</var>: the building's coordinates.</li>
                    </ul>
                
                <p><span class="statement-lineno">Next line:</span><var>unitCount</var>: the amount of units on the map.</p>
                <p><span class="statement-lineno">Next <var>unitCount</var> lines:</span> five integers</p>
                    <ul style="padding-top: 0; padding-bottom: 0;">
                        <li><var>owner</var>
                            <ul style="padding-top: 0; padding-bottom: 0;">
                                <li style="list-style-type: circle;"><const>0</const>: Owned</li>
                                <li style="list-style-type: circle;"><const>1</const>: Enemy</li>
                            </ul>
                        </li>
                        <li><var>unitId</var>: this unit's unique id</li>
                        <!-- BEGIN level1 -->
                        <li><var>level</var>: always <const>1</const> (only in this league).</li>
                        <!-- END -->
                        <!-- BEGIN level2 level3 level4 level5 -->
                        <li><var>level</var>: <const>1</const>, <const>2</const> or <const>3</const>.</li>
                        <!-- END -->
                        <li><var>x</var> and <var>y</var>: the unit's coordinates.</li>
                    </ul>
            </div>
        </div>

        <!-- Protocol block -->
        <div class="blk">
            <div class="title">Output</div>
            <div class="text">
                <span class="statement-lineno">A single line being a combination of these commands separated by <action>;</action></span>
                <ul style="padding-top: 0; padding-bottom: 0;">
                    <li><action>MOVE id x y</action></li>
                    <!-- BEGIN level1 -->
                    <li><action>TRAIN level x y</action> where level can only be <const>1</const></li>
                    <!-- END -->
                    <!-- BEGIN level2 level3 level4 level5 -->
                    <li><action>TRAIN level x y</action> where level is either <const>1</const>, <const>2</const> or <const>3</const></li>
                    <!-- END -->
                    <!-- BEGIN level3 -->
                    <li><action>BUILD building-type x y</action> where the building-type can only be <action>MINE</action>.</li>
                    <!-- END -->
                    <!-- BEGIN level4 level5 -->
                    <li><action>BUILD building-type x y</action> where the building-type is either <action>MINE</action> or <action>TOWER</action>.</li>
                    <!-- END -->
                    <li><action>WAIT</action> to do nothing.</li>
                </ul>
                <!-- BEGIN level1 level2 -->
                <span class="statement-lineno">Example</span>: "MOVE 1 2 3; TRAIN 3 3 3; MOVE 2 3 1"<br>
                <!-- END -->
                <!-- BEGIN level3 level4 level5 -->
                <span class="statement-lineno">Example</span>: "MOVE 1 2 3; TRAIN 3 3 3; BUILD MINE 0 1"<br>
                <!-- END -->
            </div>
        </div>
    
        <!-- Protocol block -->
        <div class="blk">
            <div class="title">Constraints</div>
            Allotted response time to output is ≤ <const>50ms</const>.
            <br />
            Allotted response time to output on the first turn is ≤ <const>1000ms</const>.
        </div>
    </div>
</div>

<!-- BEGIN level1 level2 level3 -->
  <div style="color: #7cc576;
      background-color: rgba(124, 197, 118,.1);
      padding: 20px;
      margin-top: 10px;
      text-align: left;">
    <div style="text-align: center; margin-bottom: 6px">
      <img src="//cdn.codingame.com/smash-the-code/statement/league_wood_04.png" />
    </div>
    <p style="text-align: center; font-weight: 700; margin-bottom: 6px;">
      What is in store in the higher leagues?
    </p>
      The extra rules available in higher leagues are:
      <ul style="margin-top: 0;padding-bottom: 0;" class="statement-next-rules">
        <!-- BEGIN level1 -->
        <li>In Wood 2, you can train army units of level 2 and 3.</li>
        <!-- END -->
        <!-- BEGIN level1 level2 -->
        <li>In Wood 1, you can build mines.</li>
        <!-- END -->
        <!-- BEGIN level1 level2 level3 -->
        <li>In Bronze, you can build towers.</li>
        <!-- END -->
      </ul>
  </div>
  <!-- END -->