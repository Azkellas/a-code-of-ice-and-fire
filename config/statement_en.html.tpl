<!-- LEAGUES level1 level2 level3 level4 -->
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
            Summary of new rules
        </p>
        <span class="statement-league-alert-content">
            <!-- BEGIN level2 -->
            You are now able to <action>TRAIN</action> level 2 and 3 units. Level 1 units cannot kill level 1 units anymore.<br/>
            See the updated statement for details.
            <!-- END -->
            <!-- BEGIN level3 -->
            You are now able to <action>BUILD MINE</action> to generate more income. They can be built only on mine spots.<br />
            See the updated statement for details.
            <!-- END -->
            <!-- BEGIN level4 -->
            You are now able to <action>BUILD TOWER</action> to protect cells. You now have access to all the rules.<br />
            See the updated statement for details.
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
            Each player controls a faction, starting from an edge of a grid-map.<br/>
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
                <p>The map is a grid of size 12x12, where the top-left corner is the cell <const>(0,0)</const>. The map is randomly generated at the start of each game.</p>
                <p>
                    A map cell can be either
                    <ul>
                        <li> void: it is not a playable cell (like a hole or a wall).</li>
                        <li> neutral: the cell belongs to no player and is colored in grey.</li>
                        <li> captured: the cell belongs to a player and is of the color of the owner. <img src="http://file.azke.fr/tuile_red02.png" style="height:20px;"/> <img src="http://file.azke.fr/tuile_blue02.png" style="height:20px;"/></li>
                        <li> inactive: the cell used to belong to a player but is not connected to this player's territory (see next section). <img src="http://file.azke.fr/tuile_red01.png" style="height:20px;"/> <img src="http://file.azke.fr/tuile_blue01.png" style="height:20px;"/></li>
                    </ul>
                </p>



                <p><strong>Territory ownership</strong></p>
                <p>
                    Throughout the game, each player will capture cells to enlarge their territory. A player territory is composed of all the cells owned by the player that are <strong>active</strong>. A cell is said to be <strong>active</strong> if and only if the cell is connected to the headquarters. That is, there exists a path of owned cells from the headquarters to this cell.
                </p>
                <figure style="margin-left:auto; margin-right:auto; width:350px;">
                    <img src="https://i.imgur.com/LMAw3OG.png" alt="territory-ownership" style="margin-left:auto; margin-right:auto; width:350px;" /><br/>
                    <figcaption>Red's territory is composed of 6 cells. The 3 red-dark are cells they used to control but Blue player interrupted part of this territory. They are thus inactive. By capturing for example the cell marked by an X, Red can make these cells active again.</figcaption>
                </figure>


                <br/>
                <p><strong>Buildings</strong></p>
                <!-- BEGIN level1 level2 -->
                <p>
                    Players cannot build buildings in this league.<br/>
                </p>
                <p>
                    You start the game with one important building. This is the <img src="http://file.azke.fr/forteresse_red.png" style="height:20px;"/> <strong>headquarters</strong>. A player has to protect their own headquarters while trying to destroy opponent's one. Losing their headquarters results in a lost game.<br/>
                    <br/>
                </p>
                <!-- END -->

                <!-- BEGIN level3 level4 -->
                <p>
                    Players can <action>BUILD</action> buildings to have more economic or defensive power. A player can only build on owned active cells that are unoccupied.<br/>
                </p>
                <!-- END -->

                <!-- BEGIN level3 -->
                <p>
                    There is, in this league, only 1 building that can be built:
                <!-- END -->

                <!-- BEGIN level4 -->
                <p>
                    There are 2 differents buildings that can be built:
                <!-- END -->

                <!-- BEGIN level3 level4 -->
                <ul>
                    <li><img src="http://file.azke.fr/mine_neutre.png" style="height:20px;"/><action>MINE</action>: mines produce gold each turn and are the main source of income. A mine costs (20 + 4*<var>nbMines</var>) to build. So the first costs 20, the second one 24, and so on and so forth. A mine's income is always <const>+4</const>. Mines can only be built on mine spots: these are specific locations on the map.</li>
                <!-- END -->
                <!-- BEGIN level4 -->
                    <li><img src="http://file.azke.fr/tower_red.png" style="height:20px;"/>
                        <action>TOWER</action>: towers protect nearby (up, down, left and right, but not diagonals) and <strong>owned</strong> cells (being active or not). Cells protected by a tower can only be reached by enemy units of level <const>3</const>. Likewise, the tower can only be destroyed by a level <const>3</const> unit. A tower costs <const>15</const> gold to build.
                        Here is a figure of a tower protection range:
                        <figure style="margin-left:auto; margin-right:auto; width:350px;">
                            <img src="https://i.imgur.com/WWu1qPB.png" alt="tower-protection" style="margin-left:auto; margin-right:auto; width:350px;" /><br/>
                            <figcaption>
                                The tower owned by Red protects the nearby cells marked by a black cross. Cells on the diagonals are <strong>not</strong> protected. Moreover, the blue cell on the right of the tower is not protected as it is not a red cell.
                            </figcaption>
                        </figure>
                    </li>
                <!-- END -->

                <!-- BEGIN level3 level4 -->
                </ul>
                    Another building is the <img src="http://file.azke.fr/forteresse_red.png" style="height:20px;"/> <strong>headquarters</strong>. A player has to protect their own headquarters while trying to destroy opponent's one. Losing their headquarters results in a lost game.<br/>



                    If a building is on an inactive cell, it stays there. A <action>TOWER</action> will still protect surrounding owned cells. However a <action>MINE</action> that is on an inactive cell will not provide income.
                    <br/>
                </p>
                <!-- END -->

                <br/>
                <p><strong>Income</strong></p>
                <p>
                    At the beginning of each turn, a player gains or loses gold based on their income. A player has <const>+1</const> income for each <strong>active</strong> cell owned and <const>+4</const> income for each mine in their territory.<br/>

                    Players will have to build units to capture the opponent's headquarters. These units cost income (upkeep).
                    <ul>
                        <li>Level <const>1</const> units reduce income by <const>1</const>.</li>
                        <!-- BEGIN level2 level3 level4 -->
                        <li>Level <const>2</const> units reduce income by <const>4</const>.</li>
                        <li>Level <const>3</const> units reduce income by <const>20</const>.</li>
                        <!-- END -->
                    </ul>
                    If a player has negative income and cannot pay their upkeep using their gold capital, all player's units die and the player has <const>0</const> gold for this turn.
                </p>

                <br/>
                <p><strong>Armies</strong></p>
                <p>
                    To destroy opponent's headquarters, a player has to train up an army. Units of an army are used to capture cells and destroy opponent's buildings and units. <br/>

                    There are <const>3</const> levels of units and the higher the level, the stronger the unit is.
                    <!-- BEGIN level1 -->
                    However, in this league, only level 1 units are available.
                    <!-- END -->
                    When a player unit walks on a cell not owned by this player, the cell is <strong>captured</strong>. Also, when a player unit walks on a cell where another enemy unit is, the enemy unit is instantly killed (without harm to the player unit).
                    <ul>
                        <!-- BEGIN level1 -->
                        <li>Level <const>1</const> units can move on empty cells or opponent headquarters. In this league <strong>only</strong>, they can also kill level 1 units.</li>
                        <!-- END -->
                        
                        <!-- BEGIN level2 -->
                        <li>Level <const>1</const> units can only move on empty cells or opponent headquarters.</li>
                        <!-- END -->

                        <!-- BEGIN level3 level4 -->
                        <li>Level <const>1</const> units can only move on empty cells, opponent mines or opponent headquarters.</li>
                        <!-- END -->

                        <!-- BEGIN level2 level3 level4 -->
                        <li>Level <const>2</const> units, in addition, can kill level <const>1</const> units.</li>
                        <li>Level <const>3</const> units, besides, can kill any units or buildings.</li>
                        <!-- END -->
                    </ul>

                    If a unit is on an inactive cell at the beginning of a turn, the unit is instantly killed.

                    <!-- BEGIN level2 level3 level4 -->
                    Here is a table summary for each unit, including their recruitment cost and their impact on income (upkeep):

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
                                <!-- BEGIN level3 level4 -->
                                Mines
                                <br/>
                                <!-- END -->
                                Headquarters
                            </td>
                            <td>
                                <!-- BEGIN level3 level4 -->
                                Mines
                                <br/>
                                <!-- END -->
                                Headquarters
                            </td>
                            <td>
                                <!-- BEGIN level4 -->
                                Towers
                                <br/>
                                <!-- BEGIN level3 -->
                                Mines
                                <br/>
                                <!-- END -->
                                <!-- END -->
                                Headquarters
                            </td>
                        </tr>
                    </table>
                    <!-- END -->



                    <ul>
                        <li>Each unit can be moved once each turn. Units move only <const>1</const> cell towards their target position, using the command <action>MOVE id x y</action>.
                        <ul>
                            <li>The target cell must be free or capturable.</li>
                            <li>If the distance between the unit and the target coordinates x and y is greater than 1, pathfinding is automatically done to get the unit closer.</li>
                        </ul>
                        <li>Players can train new units for their army using the <action>TRAIN level x y</action> action. They can only be trained on cells in the player's territory or its direct neighbourhood (border). Training follows the same rules as moving: no training can be done on a cell occupied by a friendly unit/building, and the unit must be powerful enough to be trained on a cell occupied by an enemy.</li>
                        <li>All actions are processed sequentially and the game state is recomputed after each one: an action not possible at the beginning of the turn might be done later in the same turn.</li>
                        <figure style="margin-left:auto; margin-right:auto; width:500px;">
                            <img src="https://i.imgur.com/6guSobH.png" alt="action-sequence-diagram" style="margin-left:auto; margin-right:auto; width:550px;" /><br/>
                        </figure>

                    </ul>
                    <br/>
                </p>
            </div>

            <!-- Victory conditions -->
            <div class="statement-victory-conditions">
                <div class="icon victory"></div>
                <div class="blk">
                    <div class="title">Victory Conditions</div>
                    <div class="text">
                        <ul style="padding-bottom: 0;">
                            <li>Capture the enemy headquarters.</li>
                            <li>After <const>100</const> turns, you have more military power than your opponent, where the military power is computed by the sum of the cost of all units you have + the amount of gold you have.</li>
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
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- EXPERT RULES -->
    <!-- BEGIN level4 -->
    <div class="statement-section statement-expertrules">
        <h1>
            <span class="icon icon-expertrules">&nbsp;</span>
            <span>Expert Rules</span>
        </h1>

        <div class="statement-expert-rules-content">
            The source code can be found here: <a src="https://github.com/Azkellas/a-code-of-ice-and-fire">https://github.com/Azkellas/a-code-of-ice-and-fire</a>.
            <ul>
                <li>There are between <const>8</const> and <const>20</const> mine spots.</li>
                <li>A mine spot is always present on the right cell of red's headquarters and the left side of blue's headquarters.</li>
                <li>Cells surrounding the headquarters at distance 1 (including diagonally) are never <strong>void</strong> cells.</li>
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
                <p><span class="statement-lineno">Line 1: </span>two ints <var>width</var> and <var>height</var>, the dimensions of the map</p>
                <p><span class="statement-lineno">Line 2: </span>one int <var>numberMineSpots</var>: the number of mine spots on the map.
                    <!-- BEGIN level1 level2 -->
                    Mine spots will be used from Wood1 league onwards.
                    <!-- END -->
                </p>
                <p><span class="statement-lineno">Next <var>numberMineSpots</var> lines:</span> two ints
                    <ul>
                        <li><var>x</var> and <var>y</var>: coordinates of the mine spot.</li>
                    </ul>
            </div>

            <div class="title">Game turn input</div>
            <div class="text">
                <p><span class="statement-lineno">Line 1:</span><var>gold</var>: an int being the amount of gold the player has.</p>
                <p><span class="statement-lineno">Line 2:</span><var>income</var>: an int being the income the player has.</p>
                <p><span class="statement-lineno">Line 3:</span><var>opponentGold</var>: an int being the amount of gold the opponent player has.</p>
                <p><span class="statement-lineno">Line 4:</span><var>opponentIncome</var>: an int being the amount of gold the opponent player has.</p>

                <p><span class="statement-lineno">Next <var>height</var> lines:</span>
                    <ul>
                        <li> <var>width</var> characters, the <var>owner</var> of the cell:
                        <ul>
                            <li><const>#</const>: void</li>
                            <li><const>.</const>: neutral</li>
                            <li><const>O</const>: owned and active cell</li>
                            <li><const>o</const>: owned and inactive</li>
                            <li><const>X</const>: active opponent cell</li>
                            <li><const>x</const>: inactive opponent cell</li>
                        </ul>
                    </ul>
                </p>
                <p><span class="statement-lineno">Next line:</span> <var>buildingCount</var>: the amount of buildings on the map.</p>
                <p><span class="statement-lineno">Next <var>buildingCount</var> lines:</span> four ints
                    <ul>
                        <li><var>owner</var>
                            <ul>
                                <li><const>0</const>: Owned</li>
                                <li><const>1</const>: Enemy</li>
                            </ul>
                        </li>
                        <li><var>buildingType</var>: the type of building
                            <ul>
                                <li><const>0</const>: HQ</li>
                                
                                <!-- BEGIN level3 -->
                                <li><const>1</const>: Mine</li>
                                <!-- END -->
                                
                                <!-- BEGIN level4 -->
                                <li><const>1</const>: Mine</li>
                                <li><const>2</const>: Tower</li>
                                <!-- END -->
                            </ul>
                        </li>
                        <li><var>x</var> and <var>y</var>: building's cell coordinates.
                    </ul>
                </p>
                <p><span class="statement-lineno">Next line:</span><var>unitCount</var>: the amount of units on the map.</p>
                <p><span class="statement-lineno">Next <var>unitCount</var> lines:</span> five ints
                    <ul>
                        <li><var>owner</var>
                            <ul>
                                <li><const>0</const>: Owned</li>
                                <li><const>1</const>: Enemy</li>
                            </ul>
                        </li>
                        <li><var>unitId</var></li>
                        <!-- BEGIN level1 -->
                        <li><var>level</var>: <const>1</const> only in this league.</li>
                        <!-- END -->
                        <!-- BEGIN level2 level3 level4 -->
                        <li><var>level</var>: <const>1</const>, <const>2</const> or <const>3</const>.</li>
                        <!-- END -->
                        <li><var>x</var> and <var>y</var>: unit's coordinates.</li>
                    </ul>
                </p>
            </div>
        </div>

        <!-- Protocol block -->
        <div class="blk">
            <div class="title">Output</div>
            <div class="text">
                <span class="statement-lineno">A single line being a combination of these commands separated by <action>;</action></span>
                <ul>
                    <li><action>MOVE id x y</action></li>
                    <li><action>TRAIN level x y</action></li>
                    <!-- BEGIN level3 -->
                    <li><action>BUILD {building-type} x y</action> where the building type can only be <action>MINE</action> in this league.</li>
                    <!-- END -->
                    <!-- BEGIN level4 -->
                    <li><action>BUILD {building-type} x y</action> where the building type is either <action>MINE</action> or <action>TOWER</action>.</li>
                    <!-- END -->
                    <li><action>WAIT</action></li>
                </ul>
                <!-- BEGIN level1 level2 -->
                <span class="statement-lineno">Example</span>: "MOVE 1 2 3; TRAIN 3 3 3; MOVE 2 3 1"<br>
                <!-- END -->
                <!-- BEGIN level3 level4 -->
                <span class="statement-lineno">Example</span>: "MOVE 1 2 3; TRAIN 3 3 3; BUILD MINE 0 1"<br>
                <!-- END -->
                <br>
            </div>
        </div>
    
        <!-- Protocol block -->
        <div class="blk">
            <div class="title">Constraints</div>
            Allotted response time to output is ≤ <const>50ms</const>.
            Allotted response time to output on the first time is ≤ <const>1000ms</const>.
        </div>
    </div>
    
</div>
