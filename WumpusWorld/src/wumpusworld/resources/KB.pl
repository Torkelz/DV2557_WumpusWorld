% X = coordinate x, Y = coordinate Y in a list

%--------------------------------
%Board logic for not checking behind walls
%--------------------------------
inside_bounds([X,Y]) :- X > 0, X < 5, Y > 0, Y < 5.


%--------------------------------
%Logic for adjacent tiles
%--------------------------------
location_toward([X,Y],1,[New_X,Y]) :- New_X is X+1.
location_toward([X,Y],0,[X,New_Y]) :- New_Y is Y+1.
location_toward([X,Y],3,[New_X,Y]) :- New_X is X-1.
location_toward([X,Y],2,[X,New_Y]) :- New_Y is Y-1.


%--------------------------------
%Misc rules
%--------------------------------
not(pit(X)) :- breeze(X).
perception(X) :- breeze(X).
perception(X) :- stench(X).





%--------------------------------
%Handle wumpus logic
%--------------------------------
wumpus(X) :- not(visited(X)), location_toward(X,_,Z), stench(Z), inside_bounds(X), inside_bounds(Z).

add_stench(X) :- assert(stench(X)), check_for_wumpus(X,0), check_for_wumpus(X,1), check_for_wumpus(X,2), check_for_wumpus(X,3).

check_for_wumpus(X,D) :- (not(wumpus_found) -> !), location_toward(X,D,Y), not(visited(Y)), inside_bounds(Y),
 (assume_wumpus(Y) -> (assert(wumpus_found), retractall(assume_wumpus(_)), assert(wumpus_pos(Y)) , !); assert(assume_wumpus(Y))).

%--------------------------------
%Dynamic variables
%--------------------------------
:- dynamic(assume_wumpus/1).
:- dynamic(stench/1).
:- dynamic(wumpus_found/0).
:- dynamic(wumpus_pos/1).


%--------------------------------
%Junk?
%--------------------------------

%adjacent(L1,L2) :- location_toward(L1,_,L2).

%is_pit(X) :- location_toward(X,_,Y), adjacent(X,Y), has_breeze(Z), has_breeze(Y).