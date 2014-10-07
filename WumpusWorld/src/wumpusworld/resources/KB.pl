% X = coordinate x, Y = coordinate Y
has_stench(X,Y) :- stench(X,Y).
has_breeze(X,Y) :- breeze(X,Y).
has_glitter(X,Y) :- glitter(X,Y).
has_wumpus(X,Y) :- wumpus(X,Y).
has_pit(X,Y) :- pit(X,Y).
is_empty(X,Y) :- empty(X,Y).