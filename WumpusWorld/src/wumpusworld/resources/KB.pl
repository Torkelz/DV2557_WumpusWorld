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

adjacent(X,Y) :- location_toward(X,_,Y).

%--------------------------------
%Misc rules
%--------------------------------
%not(breeze(X)) :- visited(X)
%not(pit(X)) :- breeze(X).
%not(visited(X)) :- visited(X)-> false; true.
perception(X) :- breeze(X).
perception(X) :- stench(X).





%--------------------------------
%Handle wumpus logic
%--------------------------------
stench(X) :- wumpus_pos(Z), adjacent(X,Z).




wumpus(X) :- not(visited(X)), location_toward(X,_,Z), stench(Z), inside_bounds(X), inside_bounds(Z).

is_wumpus(X) :- not(visited(X)), adjacent(X,Y), stench(Y).

add_visited(X) :- asserta(visited(X)), retract(assume_wumpus(X)).

%add_stench(X) :- assert(stench(X)), check_for_wumpus(X,0), check_for_wumpus(X,1), check_for_wumpus(X,2), check_for_wumpus(X,3).
add_stench(X) :- asserta(stench(X)), ignore(check_for_wumpus(X,0)), ignore(check_for_wumpus(X,1)), ignore(check_for_wumpus(X,2)), ignore(check_for_wumpus(X,3)), !.

check_for_wumpus(X,D) :- (
						location_toward(X,D,Y), not(visited(Y)), inside_bounds(Y),
    							(
    							assume_wumpus(Y) -> (retractall(assume_wumpus(X_)), asserta(wumpus_pos(Y))); asserta(assume_wumpus(Y))
    							)
    						).
%(assume_wumpus(Y) -> (assert(wumpus_found), abolish(assume_wumpus(_)), assert(wumpus_pos(Y))); 


%--------------------------------
%Handle pit logic
%--------------------------------
breeze(X) :- pit(Z), adjacent(X,Z).

%--------------------------------
%Dynamic variables
%--------------------------------
%:- dynamic(assume_wumpus/1).
%:- dynamic(stench/1).
%:- dynamic(wumpus_found/0).
%:- dynamic(wumpus_pos/1).
%:- dynamic(visited/1).
%:- dynamic(breeze/1).
:- dynamic([
            assume_wumpus/1,
            stench/1,
            wumpus_found/0,
            wumpus_pos/1,
            visited/1,
            breeze/1
        ]).

%--------------------------------
%Default values
%--------------------------------
stench(_) :- fail.
breeze(_) :- fail.
visited(_) :- fail.
assume_wumpus(_) :- fail.

%--------------------------------
%Set functions
%--------------------------------
add_breeze(X) :- (inside_bounds(X), breeze(X)) ; asserta(breeze(X)).
%add_stench(X) :- (inside_bounds(X), stench(X)) ; asserta(stench(X)).

add_glitter(X) :- (inside_bounds(X), glitter(X)) ; asserta(glitter(X)).
add_visited(X) :- (inside_bounds(X), visited(X)) ; (asserta(visited(X)), retract(assume_wumpus(X))).
add_pit(X) :- (inside_bounds(X), pit(X)) ; asserta(pit(X)).

%--------------------------------
%Set not functions
%--------------------------------
%add_not_breeze(X) :- (inside_bounds(X), not(breeze(X))) ; asserta(not(breeze(X))).
%add_not_stench(X) :- (inside_bounds(X), not(stench(X))) ; asserta(not(stench(X))).
%add_not_glitter(X) :- (inside_bounds(X), not(glitter(X))) ; asserta(not(glitter(X))).
%add_not_visited(X) :- (inside_bounds(X), not(visited(X))) ; asserta(not(visited(X))).
%add_not_pit(X) :- (inside_bounds(X), not(pit(X))) ; asserta(not(pit(X))).


%--------------------------------
%Junk?
%--------------------------------

%adjacent(L1,L2) :- location_toward(L1,_,L2).

%is_pit(X) :- location_toward(X,_,Y), adjacent(X,Y), has_breeze(Z), has_breeze(Y).