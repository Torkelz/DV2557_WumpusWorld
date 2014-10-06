%bigger(elephant, horse).
%bigger(horse, donkey).
%bigger(donkey, dog).
%bigger(donkey, monkey).
%bigger(monkey, ant).
%bigger(monkey, dog).
%bigger(giant_ant, elephant).
%is_bigger(X, Y) :- bigger(X, Y).
%is_bigger(X, Y) :- bigger(X, Z), is_bigger(Z, Y).
%breeze(1, 4).
has_stench(X,Y) :- stench(X,Y).
has_breeze(X,Y) :- breeze(X,Y).
has_glitter(X,Y) :- glitter(X,Y).
has_wumpus(X,Y) :- wumpus(X,Y).
has_pit(X,Y) :- pit(X,Y).
is_empty(X,Y) :- empty(X,Y).
has_visited(X,Y) :- has_stench(X,Y); has_breeze(X,y); has_glitter(X,Y); has_wumpus(X,Y); has_pit(X,Y); is_empty(X,Y).