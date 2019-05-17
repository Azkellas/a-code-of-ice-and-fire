//----------------------------------------------
// CODE OF ICE AND FIRE - Starter kit
//----------------------------------------------
// Ce code de démarrage cherche une case 
// d'entraînement autour de chaque case actives
//----------------------------------------------

    #include <stdlib.h>
    #include <stdio.h>
    #include <string.h>

// --------- Constantes -----------

    #define G_WIDTH                    12
    #define G_HEIGHT                   12
                                        
    #define NB_PLAYER                  2
    #define NB_MINE                    144
    #define NB_BUILDING                144
    #define NB_UNIT                    144
            
    #define ME                         0
    #define HE                         1
    #define VOID                       -1
            
    #define T_QG                       0
                
    #define M_WAIT                     0
    #define M_MOVE                     1
    #define M_TRAIN                    2
                
    #define TRAIN_COST                 10
    #define LEVEL1_COST                1
    
// -------- Structures ------------

    typedef struct _position {
        char x;
        char y;
        } POSITION;
    
    typedef struct _player {
        int gold;
        int income;
        } PLAYER;
    
    typedef struct _mine {
        char x;
        char y;
        } MINE;
    
    typedef struct _building {
        char x;
        char y;
        int owner;
        int type;
        } BUILDING;
    
    typedef struct _unit {
        int ident;
        int x;
        int y;
        int owner;
        int level;
        } UNIT;
    
    typedef struct _move {
        int move;
        int info;
        int x;
        int y;
        } MOVE;
    
    typedef struct _state {
        struct _player player[NB_PLAYER];
        int opponentGold;
        int opponentIncome;
        char grid[G_HEIGHT][G_WIDTH + 1];
        int nbBuilding;
        struct _building building[NB_BUILDING];
        int nbUnit;
        struct _unit unit[NB_UNIT];
        } STATE;
    
    typedef struct _game {
        int nbMine;
        struct _mine mine[NB_MINE];
        } GAME;
        
//--------- Prototypes -------

    void inputInit();
    void inputLoop();
    STATE* newState();
    void setMove(MOVE*, int, int, int, int);
    void playMove(MOVE*);
    
//--------- Globales ---------

    GAME initGame;                                                              // Constantes du jeu
    char message[100];                                                          // Message personnalisé   

//--------- Programme ---------

int main() {
    
    inputInit(&initGame);                                                       // Lire les infos du jeu

    //------------ Boucle principale ---------
    
    while (1) {
        
        STATE* s = newState();                                                  // Créer un état du jeu
        inputLoop(s);                                                           // Lire les infos du tour
        sprintf(message, "MSG Je tente ce coup");                               // Initialiser le message
        
        MOVE bestMove = {M_WAIT, 0, 0, 0};                                      // Initialiser une action par défaut
        int nbMove = 0;                                                         // Nombre de coups trouvés
        
        // Entraînement des unités
        for(int j = 0; j < G_HEIGHT; j++) {                                     // Pour chaque case
            for(int i = 0; i < G_WIDTH; i++) {
                if(s->player[ME].gold < TRAIN_COST) break;                      // Interrompre si pas assez d'or
                if(s->grid[j][i] != 'O' && s->grid[j][i] != 'o') continue;      // Traiter uniquement les cases actives
                
                // Chercher une case pour l'entraînement
                POSITION pos = {i, j};                                          // Initialiser une case de déplacement
                if(i > 0 && s->grid[j][i - 1] == '.') pos.x--;                  // Chercher une case neutre à droite
                else if(i < G_WIDTH - 1 && s->grid[j][i + 1] == '.') pos.x++;   // Sinon à gauche
                else if(j > 0 && s->grid[j - 1][i] == '.') pos.y--;             // Sinon en haut
                else if(j < G_HEIGHT - 1 && s->grid[j + 1][i] == '.') pos.y++;  // Sinon en bas
                
                // Jouer le coup si il existe
                if(pos.x != i || pos.y != j) {                                  // Si on a trouvé ne case voisine neutre
                    setMove(&bestMove, M_TRAIN, 1, pos.x, pos.y);               // Préparer l'action d'entraînement
                    playMove(&bestMove);                                        // Lancer l'action
                    nbMove++;                                                   // Compter les actions
                    }
                }
            }      
        
        if(!nbMove) playMove(&bestMove);                                        // Attendre si pas de coups trouvé
        printf("%s\n", message);                                                // Terminer la ligne d'actions
        free(s);                                                                // Libérer la mémoire
        }

    return 0;
    }

//--------- Fonctions ---------

// Input au démarrage

    void inputInit(GAME* g) {
        
        scanf("%d", &g->nbMine);
        for (int n = 0; n < g->nbMine; n++) {
            MINE *m = &g->mine[n];
            scanf("%d%d", &m->x, &m->y);
            }
        }

// Input à chaque boucle

    void inputLoop(STATE* s) {
        
        for(int n = 0; n < NB_PLAYER; n++) {
                PLAYER *p = &s->player[n];
                scanf("%d", &p->gold);
                scanf("%d", &p->income);
                fprintf(stderr, "Player:%d gold:%d income:%d\n", n, p->gold, p->income);
                }

        for (int j = 0; j < G_HEIGHT; j++) {
                scanf("%s", &s->grid[j]);
                fprintf(stderr, "%s\n", s->grid[j]);
                }
            
        scanf("%d", &s->nbBuilding);
        for (int n = 0; n < s->nbBuilding; n++) {
                BUILDING *b = &s->building[n];
                scanf("%d%d%d%d", &b->owner, &b->type, &b->x, &b->y);
                fprintf(stderr, "Player:%d Building[%d] pos:(%d %d) type:%d\n", b->owner, n, b->x, b->y, b->type);
                }
            
        scanf("%d", &s->nbUnit);
        for (int n = 0; n < s->nbUnit; n++) {
                UNIT *u = &s->unit[n];
                scanf("%d%d%d%d%d", &u->owner, &u->ident, &u->level, &u->x, &u->y);
                fprintf(stderr, "Player:%d Unit[%d] id:%d pos:(%d %d) level:%d\n", u->owner, n, u->ident, u->x, u->y, u->level);
                }
        }

// Préparer l'action

    void setMove(MOVE* m, int move, int info, int x, int y) {
        m->move = move;
        m->info = info;
        m->x = x;
        m->y = y;
        }
        
// Lancer l'action

    void playMove(MOVE *m) {

        switch(m->move) {

            case(M_WAIT) : 
                printf("WAIT;WAIT;"); 
                break;

            case(M_MOVE) : 
                printf("MOVE %d %d %d;", m->info, m->x, m->y);
                break;

            case(M_TRAIN) :
                printf("TRAIN %d %d %d;", m->info, m->x, m->y);
                break;
            }
        }
        
// Allocation mémoire

    STATE* newState() { return malloc(sizeof(STATE)); }
