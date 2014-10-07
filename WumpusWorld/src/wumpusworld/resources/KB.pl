% X = coordinate x, Y = coordinate Y
has_stench(X,Y) :- perception(stench,X,Y).
has_breeze(X,Y) :- perception(breeze,X,Y).
has_glitter(X,Y) :- perception(glitter,X,Y).
has_wumpus(X,Y) :- perception(wumpus,X,Y).
has_pit(X,Y) :- perception(pit,X,Y).