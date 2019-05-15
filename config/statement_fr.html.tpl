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
            Resumé des nouvelles règles
        </p>
        <span class="statement-league-alert-content">
            <!-- BEGIN level2 -->
            Vous pouvez désormais <action>TRAIN</action> des unités de niveau 2 et 3. Les unités niveau 1 ne peuvent plus tuer d'unités niveau 1.<br/>
            Pour en savoir plus, allez voir l'énoncé.
            <!-- END -->
            <!-- BEGIN level3 -->
            Vous pouvez désormais <action>BUILD MINE</action> pour augmenter votre revenu d'or. Les mines ne peuvent être construites que sur des endroits spécifiques ("mine spots").<br />
            Pour en savoir plus, allez voir l'énoncé.
            <!-- END -->
            <!-- BEGIN level4 -->
            Vous pouvez désormais <action>BUILD TOWER</action> pour protéger des cases. Vous avez accès à toutes les règles.<br />
            Pour en savoir plus, allez voir l'énoncé.
            <!-- END -->
        </span>
    </div>
    <!-- END -->


    <!-- GOAL -->
    <div class="statement-section statement-goal">
        <h1>
            <span class="icon icon-goal">&nbsp;</span>
            <span>Objectif</span>
        </h1>
        
        <div class="statement-goal-content">
            Chaque joueur contrôle une faction, commençant dans un coin de la carte (représentée par une grille).<br/>
            Entrainez des armées pour défaire votre adversaire en détruisant son quartier général.<br/>
            <br/>
        </div>
    </div>

    <!-- RULES -->
    <div class="statement-section statement-rules">
        <h1>
            <span class="icon icon-rules">&nbsp;</span>
            <span>Règles du jeu</span>
        </h1>
        <div>
            <div class="statement-rules-content">
                <p><strong>La carte</strong></p>
                <p>La carte est une grille de 12x12 dont le coin haut-gauche est la case <const>(0,0)</const>. La carte est générée aléatoirement au début de chaque partie.</p>
                <p>
                    Une case peut etre
                </p>
                <ul style="padding-top: 0;">
                    <li> néant: ce n'est pas une case jouable (par exemple un trou).</li>
                    <li> neutre: la case n'appartient à aucun joueur et est grise.</li>
                    <li> capturée: la case appartient à un joueur et est de la couleur de son propriétaire.<img src="http://file.azke.fr/tuile_red02.png" style="height:20px;"/> <img src="http://file.azke.fr/tuile_blue02.png" style="height:20px;"/></li>
                    <li> inactive: la case a appartenu à un joueur mais n'est plus connectée à son territoire (voir prochaine partie). <img src="http://file.azke.fr/tuile_red01.png" style="height:20px;"/> <img src="http://file.azke.fr/tuile_blue01.png" style="height:20px;"/></li>
                </ul>


                <p><strong>Possession de territoire</strong></p>
                <p>
                    Tout le long du jeu, chaque joueur/joueuse capturera des cases pour agrandir son territoire. Le territoire d'un joueur est composé de toutes les cases possédées par un joueur qui sont <strong>active</strong>. Une case est dite <strong>active</strong> si et seulement si la case est connectée au quartier général. C'est à dire, il existe un chemin du quartier général jusqu'à cette case.
                </p>
                <div style="margin-left:auto; margin-right:auto; width:350px;">
                    <img src="https://i.imgur.com/LMAw3OG.png" alt="territory-ownership" style="margin-left:auto; margin-right:auto; width:350px;" /><br/>
                    <p>Le territoire du rouge est composé de 6 cases. Les 3 cases rouge foncé sont celles autrefois possédées mais le bleu a coupé une partie de ce territoire. Elles sont donc inactives. En capturant par exemple la case marquée par un X, rouge peut rendre ces cases actives à nouveau.</p>
                </div>


                <br/>
                <p><strong>Bâtiments</strong></p>
                <!-- BEGIN level1 level2 -->
                <p>
                    Les joueurs ne peuvent pas construire de bâtiment dans cette ligue.<br/>
                </p>
                <p>
                    Vous commencez le jeu avec un bâtiment important: le <img src="http://file.azke.fr/forteresse_red.png" style="height:20px;"/> <strong>quartier général</strong>.
                    Un joueur doit protéger son propre quartier général tout en essayant de détruire celui de son adversaire. Perdre son quartier général fait perdre la partie.<br/>
                    <br/>
                </p>
                <!-- END -->

                <!-- BEGIN level3 level4 -->
                <p>
                    Les joueurs peuvent construire des bâtiments avec l'action <action>BUILD</action> pour avoir plus de pouvir économique ou défensif. Il n'est possible de construire que sur une case possédée et active.<br/>
                </p>
                <!-- END -->

                <!-- BEGIN level3 -->
                <p>
                    Dans cette ligue, il n'y a seulement qu'un bâtiment qui peut être construit:
                </p>
                <!-- END -->

                <!-- BEGIN level4 -->
                <p>
                    Il y a deux bâtiments différents qui peuvent être constuits:
                </p>
                <!-- END -->

                <!-- BEGIN level3 level4 -->
                <ul style="padding-top: 0; padding-bottom:0;">
                    <li><img src="http://file.azke.fr/mine_neutre.png" style="height:20px;"/><action>MINE</action>: les mines produisent de l'or chaque tour et sont la source principale d'or. Une mine coûte (20 + 4*<var>nbMines</var> à constuire, soit la première coûte 20, la seconde coûte 24, etc. Chaque mine donne <const>+4</const> de revenu. Les mines ne peuvent être construites uniquement sur des endroits spécifiques ("mine spots"). La case étant un <i>mine spot</i> doit d'abord être controllée avant qu'une <action>MINE</action> puisse être construite dessus.</li>
                <!-- END -->
                <!-- BEGIN level4 -->
                    <li><img src="http://file.azke.fr/tower_red.png" style="height:20px;"/>
                        <action>TOWER</action>: les tours protègent les cases alentours (haut, bas, gauche, droite, mais pas les diagonales). <strong>Uniquement les cases possédées (qu'elles soit actives ou non) sont protégées.</strong> Les cases protégées par une tour ne peuvent seulement être atteintes que par des unités de niveau <const>3</const>. De même, une tour ne peut être détruite que par une unité de niveau <const>3</const>. Une tour coûte 15 d'or à construire et ne peut pas être construite sur un emplacement de mine.
                        Voici une illustration de la portée de protection d'une tour:
                        <div style="margin-left:auto; margin-right:auto; width:350px;">
                            <img src="https://i.imgur.com/WWu1qPB.png" alt="tower-protection" style="margin-left:auto; margin-right:auto; width:350px;" /><br/>
                            <p>
                                La tour possédée par rouge protège les cases alentours marquées par une croix noir. Les cases en diagonal <strong>ne sont pas</strong> protégées. De plus, la case bleue à droite de la tour n'est pas protégée non plus car ce n'est pas une case rouge.
                            </p>
                        </div>
                    </li>
                <!-- END -->

                <!-- BEGIN level3 level4 -->
                </ul>
                <p>
                    Un autre bâtiment est le <img src="http://file.azke.fr/forteresse_red.png" style="height:20px;"/> <strong>quartier général</strong>.
                    Un joueur doit protéger son propre quartier général tout en essayant de détruire celui de son adversaire. Perdre son quartier général fait perdre la partie.
                </p>
                <p>
                    Si un bâtiment est sur une case inactive, il y reste. Une tour (<action>TOWER</action>) continuera de protéger les cases possédées environnantes. Cependant, une <action>MINE</action> sur une case inactive n'augmente pas le revenu.
                </p>
                <!-- END -->

                <br/>
                <p><strong>Revenu</strong></p>
                <p>
                    Au début de chaque tour, les joueurs/joueuses gagnent ou perdent de l'or en fonction de leur revenu. Chaque case <strong>active</strong> possédée donne <const>+1</const> de revenu et chaque mine donne <const>+4</const> de revenu.<br/>

                    Les joueurs doivent construire des unités pour capturer le quartier général adverse. Ces unités impactent négativement le revenu (coût d'entretien).
                </p>
                    <ul style="padding-top: 0; padding-bottom:0;">
                        <li>Les unités niveau <const>1</const> réduisent les revenus de <const>1</const>.</li>
                        <!-- BEGIN level2 level3 level4 -->
                        <li>Les unités niveau <const>2</const>réduisent les revenus de <const>4</const>.</li>
                        <li>Les unités niveau <const>3</const> réduisent les revenus de <const>20</const>.</li>
                        <!-- END -->
                    </ul>
                <p>
                    Si un joueur se retrouve avec un revenu négatif et ne peut pas payer son coût d'entretien avec son capital d'or, toutes ses unités meurent et l'or est mis à <const>0</const> pour ce tour.
                </p>

                <br/>
                <p><strong>Armées</strong></p>
                <p>
                    Pour détruire le quartier général adverse, il est nécessaire de créer une armée. Les unités sont utilisées pour capturer des cases et détruire les bâtiments et unités adverses.<br/>

                    Il y a <const>3</const> niveaux d'unité, et plus ce niveau est haut, plus l'unité est puissante.
                    <!-- BEGIN level1 -->
                    Cependant, dans cette ligue, seule les unités de niveau 1 sont disponibles.
                    <!-- END -->
                    Quand une unité marche sur une case non possédée par le/la propriétaire de cette unité, la case <strong>capturée</strong>. Si une unité ennemie est présente sur cette case, l'unité ennemie est immédiatement tuée et rien n'arrive à l'autre unité.
                </p>
                    <ul style="padding-top: 0; padding-bottom:0;">
                        <!-- BEGIN level1 -->
                        <li>Les unités niveau <const>1</const> peuvent bouger sur des cases vides ou le quartier général adverse. Dans cette ligue <strong>seulement</strong>, elles peuvent aussi tuer les unités de niveau 1.</li>
                        <!-- END -->
                        
                        <!-- BEGIN level2 -->
                        <li>Les unités niveau <const>1</const> peuvent uniquement bouger sur des cases vide ou le quartier général adverse.</li>
                        <!-- END -->

                        <!-- BEGIN level3 level4 -->
                        <li>Les unités niveau <const>1</const> peuvent uniquement bouger sur des cases vide, les mines adverses ou le quartier général adverse.</li>
                        <!-- END -->

                        <!-- BEGIN level2 level3 level4 -->
                        <li>Les unités niveau <const>2</const> peuvent de plus tuer les unités niveau <const>1</const>.</li>
                        <li>Les unités niveau <const>3</const>, en outre, peuvent tuer n'importe quelle unité et détruire n'importe quel bâtiment.</li>
                        <!-- END -->
                    </ul>
                <p>
                    Si une unité est sur une case inactive au début d'un tour, elle est immédiatement tuée.

                    <!-- BEGIN level2 level3 level4 -->
                    Voici un tableau résumant les différentes informatiques de chaque unité.
                </p>
                    <table style="text-align:center; width: 100%; border-spacing: 10px; border-collapse: separate;">
                        <tr>
                            <th>Niveau</th>
                            <td>1</td>
                            <td>2</td>
                            <td>3</td>
                        </tr>
                        <tr>
                            <th>Coût d'entrainement</th>
                            <td>10</td>
                            <td>20</td>
                            <td>30</td>
                        </tr>
                        <tr>
                            <th>Coût d'entretien</th>
                            <td>1</td>
                            <td>4</td>
                            <td>20</td>
                        </tr>
                        <tr>
                            <th>Peut tuer les unités niveau/th>
                            <td>-</td>
                            <td>1</td>
                            <td>1, 2, 3</td>
                        </tr>
                        <tr>
                            <th>Peut détruire</th>
                            <td>
                                <!-- BEGIN level3 level4 -->
                                Mines<br/>
                                <!-- END -->
                                 Quartier général
                            </td>
                            <td>
                                <!-- BEGIN level3 level4 -->
                                Mines<br/>
                                <!-- END -->
                                Quartier général
                            </td>
                            <td>
                                <!-- BEGIN level4 -->
                                Tours<br/>
                                <!-- BEGIN level3 -->
                                Mines<br/>
                                <!-- END -->

                                <!-- END -->
                                Quartier général
                            </td>
                        </tr>
                    </table>
                    <!-- END -->


                    <ul style="padding-top: 0; padding-bottom:0;">
                        <li>Chaque unité ne peut être déplacée qu'une fois par tour. Les unitées bougent de <const>1</const> case, à l'aide de la commande <action>MOVE id x y</action>.
                        <ul style="padding-top: 0; padding-bottom:0;">
                            <li style="list-style-type: circle;">La case cible doit être libre ou capturable.</li>
                            <li style="list-style-type: circle;">Si la distance entre l'unité et les coordonnées cibles x et y est plus grande que 1, une recherche de chemin est faite pour rapprocher l'unité de sa cible.</li>
                        </ul>
                        <li>Les unités sont entrainées avec la commande <action>TRAIN niveau x y</action>. Elles ne peuvent être entrainées que sur des cases du territoire du joueur / de la joueuse ou de son voisinage direct (bordure). La création d'armée suit les mêmes règles que le déplacement: aucune action <action>TRAIN</action> ne peut être fait sur uen case occupée par une unité ou bâtiment alliée, et l'unité doit être assez puissante pour être entrainée sur une case occupée par un adversaire.</li>
                        <li>Toutes les actions sont faites séquentiellement et l'état du jeu est recalculé après chacunes d'elles: une action impossible au début du tour pourrait être faite plus tard dans ce même tour.</li>
                    </ul>
                    <div style="margin-left:auto; margin-right:auto; width:500px;">
                        <img src="https://i.imgur.com/6guSobH.png" alt="action-sequence-diagram" style="margin-left:auto; margin-right:auto; width:550px;" />
                    </div>
            </div>

            <!-- Victory conditions -->
            <div class="statement-victory-conditions">
                <div class="icon victory"></div>
                <div class="blk">
                    <div class="title">Conditions de victoire</div>
                    <div class="text">
                        <ul style="padding-bottom: 0;">
                            <li>Capturer le quartier général adverse.</li>
                            <li>Après <const>100</const> tours, vous avez plus de puissance militaire que votre adversaire. La puissance militaire est la somme du coût de toutes vos unités + la quantité d'or que vous avez.</li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- Lose conditions -->
            <div class="statement-lose-conditions">
                <div class="icon lose"></div>
                <div class="blk">
                    <div class="title">Conditions de défaite</div>
                    <div class="text">
                        <ul style="padding-bottom: 0;">
                            <li>Votre programme ne répond pas dans le temps imparti. </li>
                            <li>Votre programme répond avec une sortie invalide.</li>
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
            <span>Détails des règles</span>
        </h1>

        <div class="statement-expert-rules-content">
            Le code source ce trouve sur ce lien: <a src="https://github.com/Azkellas/a-code-of-ice-and-fire">https://github.com/Azkellas/a-code-of-ice-and-fire</a>.
            <ul style="padding-top: 0; padding-bottom:0;">
                <li>Il y a entre 8 et 20 emplacement de mine.</li>
                <li>Un emplacement de mine est toujours présent sur la case de droite (resp. gauche) du quartier général rouge (resp. bleu).</li>
                <li>Les cases à distance 1 autour du quartier général (incluant la diagonale) ne sont jamais des cases vides.</li>
            </ul>
        </div>
    </div>
    <!-- END -->

    <!-- PROTOCOL -->
    <div class="statement-section statement-protocol">
        <h1>
            <span class="icon icon-protocol">&nbsp;</span>
            <span>Protocole du jeu</span>
        </h1>
        <!-- Protocol block -->
        <div class="blk">
            <div class="title">Entrée d'initialisation</div>
            <div class="text">
                <p><span class="statement-lineno">Ligne 1: </span>un entier <var>numberMineSpots</var>: le nombre d'emplacement de mines sur la carte.
                    <!-- BEGIN level1 level2 -->
                    Les emplacements de mines seront utilisés à partir de la ligue Bois 1.
                    <!-- END -->
                </p>
                <span class="statement-lineno">Prochaines <var>numberMineSpots</var> lignes:</span> deux entiers
                <ul style="padding-top: 0; padding-bottom:0;">
                    <li><var>x</var> et <var>y</var>: coordonnées de l'emplacement de mine.</li>
                </ul>
            </div>

            <div class="title">Entrée pour un tour de jeu</div>
            <div class="text">
                <p><span class="statement-lineno">Ligne 1:</span><var>gold</var>: un entier étant la quantité d'or que vous possédez.</p>
                <p><span class="statement-lineno">Ligne 2:</span><var>income</var>: un entier étant le revenu que vous possédez.</p>
                <p><span class="statement-lineno">Ligne 3:</span><var>opponentGold</var>: un entier étant la quantité d'or que votre adversaire possède.</p>
                <p><span class="statement-lineno">Ligne 4:</span><var>opponentIncome</var>: un entier étant le revenu que votre adversaire possède.</p>

                <p><span class="statement-lineno">Prochaines <const>12</const> lignes:</span> <const>12</const> caractères, un pour chaque case:
                <ul style="padding-top: 0; padding-bottom:0;">
                    <li><const>#</const>: néant</li>
                    <li><const>.</const>: neutre</li>
                    <li><const>O</const>: possédée par vous et active</li>
                    <li><const>o</const>: possédée par vous et inactive</li>
                    <li><const>X</const>: possédée par l'adversaire et active</li>
                    <li><const>x</const>: possédée par l'adversaire et inactive</li>
                </ul>
                <p><span class="statement-lineno">Prochaine ligne:</span> <var>buildingCount</var>: la quantité de bâtiments sur la carte.</p>
                <span class="statement-lineno">Prochaines <var>buildingCount</var> lignes:</span> quatre entiers
                    <ul style="padding-top: 0; padding-bottom:0;">
                        <li><var>owner</var> (propriétaire)
                            <ul style="padding-top: 0; padding-bottom:0;">
                                <li style="list-style-type: circle;"><const>0</const>: vous</li>
                                <li style="list-style-type: circle;"><const>1</const>: adversaire</li>
                            </ul>
                        </li>
                        <li><var>buildingType</var>: le type de bâtiment
                            <ul style="padding-top: 0; padding-bottom:0;">
                                <li style="list-style-type: circle;"><const>0</const>: QG</li>
                                
                                <!-- BEGIN level3 -->
                                <li style="list-style-type: circle;"><const>1</const>: Mine</li>
                                <!-- END -->

                                <!-- BEGIN level4 -->
                                <li style="list-style-type: circle;"><const>1</const>: Mine</li>
                                <li style="list-style-type: circle;"><const>2</const>: Tour</li>
                                <!-- END -->
                            </ul>
                        </li>
                        <li><var>x</var> et <var>y</var>: coordonnées du bâtiment.
                    </ul>
                <p><span class="statement-lineno">Prochaine ligne:</span><var>unitCount</var>: la quantité d'unités sur la carte.</p>
                <span class="statement-lineno">Prochaines <var>unitCount</var> lignes:</span> cinq entiers
                    <ul style="padding-top: 0; padding-bottom:0;">
                        <li><var>owner</var>
                            <ul style="padding-top: 0; padding-bottom:0;">
                                <li style="list-style-type: circle;"><const>0</const>: vous</li>
                                <li style="list-style-type: circle;"><const>1</const>: adversaire</li>
                            </ul>
                        </li>
                        <li><var>unitId</var>: l'identifiant de l'unité.</li>
                        <!-- BEGIN level1 -->
                        <li><var>level</var>: toujours <const>1</const> (seulement dans cette ligue).</li>
                        <!-- END -->
                        <!-- BEGIN level2 level3 level4 -->
                        <li><var>level</var>: <const>1</const>, <const>2</const> ou <const>3</const>.</li>
                        <!-- END -->
                        <li><var>x</var> et <var>y</var>: coordonnées de l'unité.</li>
                    </ul>
            </div>
        </div>

        <!-- Protocol block -->
        <div class="blk">
        <div class="title">Sortie</div>
            <div class="text">
                <span class="statement-lineno">Une ligne étant la combinaison de ces commandes séparées par <action>;</action></span>
                <ul style="padding-top: 0; padding-bottom:0;">
                    <li><action>MOVE id x y</action></li>
                    <li><action>TRAIN level x y</action></li>
                    <!-- BEGIN level3 -->
                    <li><action>BUILD {type-batiment} x y</action> où le type de bâtiment est uniquement <action>MINE</action> dans cette ligue.</li>
                    <!-- END -->
                    <!-- BEGIN level4 -->
                    <li><action>BUILD {type-batiment} x y</action> où le type de bâtiment est soit <action>MINE</action> ou <action>TOWER</action>.</li>
                    <!-- END -->
                    <li><action>WAIT</action></li>
                </ul>
                <!-- BEGIN level1 level2 -->
                <span class="statement-lineno">Exemple</span>: "MOVE 1 2 3; TRAIN 3 3 3; MOVE 2 3 1"<br>
                <!-- END -->
                <!-- BEGIN level3 level4 -->
                <span class="statement-lineno">Exemple</span>: "MOVE 1 2 3; TRAIN 3 3 3; BUILD MINE 0 1"<br>
                <!-- END -->
                <br>
            </div>
        </div>
    
        <!-- Protocol block -->
        <div class="blk">
            <div class="title">Contraintes</div>
            Temps de réponse pour un tour de jeu ≤ <const>50ms</const>.
            <br />
            Temps de réponse pour le premier tour ≤ <const>1000ms</const>.
        </div>
    </div>
</div>
