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
not(visited(X)) :- visited(X)-> false; true.
perception(X) :- breeze(X).
perception(X) :- stench(X).

%--------------------------------
%Handle wumpus logic
%--------------------------------
wumpus(X) :- not(visited(X)), location_toward(X,_,Z), stench(Z), inside_bounds(X), inside_bounds(Z).

add_visited(X) :- asserta(visited(X)), retract(assume_wumpus(X)).

%add_stench(X) :- assert(stench(X)), check_for_wumpus(X,0), check_for_wumpus(X,1), check_for_wumpus(X,2), check_for_wumpus(X,3).
add_stench(X) :- assert(stench(X)), check_for_wumpus(X,0), check_for_wumpus(X,1), check_for_wumpus(X,2), check_for_wumpus(X,3), !.

check_for_wumpus(X,D) :- wumpus_found -> !,fail; location_toward(X,D,Y), not(visited(Y)), inside_bounds(Y),
    (assume_wumpus(Y) -> (assert(wumpus_found), abolish(assume_wumpus(_))); asserta(assume_wumpus(Y))).
%(assume_wumpus(Y) -> (assert(wumpus_found), abolish(assume_wumpus(_)), assert(wumpus_pos(Y))); 
%--------------------------------
%Dynamic variables
%--------------------------------
:- dynamic(assume_wumpus/1).
:- dynamic(stench/1).
:- dynamic(wumpus_found/0).
:- dynamic(wumpus_pos/1).
:- dynamic(visited/1).
:- dynamic(breeze/1).


%--------------------------------
%Default values
%--------------------------------
stench(_) :- fail.
breeze(_) :- fail.
visited(_) :- fail.
assume_wumpus(_) :- fail.



%--------------------------------
%Junk?
%--------------------------------

%adjacent(L1,L2) :- location_toward(L1,_,L2).

%is_pit(X) :- location_toward(X,_,Y), adjacent(X,Y), has_breeze(Z), has_breeze(Y).