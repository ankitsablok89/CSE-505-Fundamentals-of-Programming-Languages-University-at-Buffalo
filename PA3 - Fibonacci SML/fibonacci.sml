(* Name - ANKIT SABLOK *)
(* UB email address - ankitsab@buffalo.edu *)

(* This statement creates a new data type in ML *)
datatype 'a inflist = NIL | CONS of 'a * (unit -> 'a inflist);

(* This function is used to retrieve the head of a list in ML *)
fun HD (CONS(a,b)) = a | HD NIL = raise Subscript;

(* This function is used to retrieve the tail of a list in ML *)
fun TL (CONS(a,b)) = b() | TL NIL = raise Subscript;

fun NULL NIL = true | NULL _ = false;

(* This is a function that is used to apply the filter f to every element of the list *)
fun FILTER f l = if NULL l then NIL else if f (HD l) then CONS(HD l, fn () => (FILTER f (TL l))) else FILTER f (TL l);

(* This function takes in a list as a parameter and a number which indicates how many elements to print of that list and prints them out *)
fun TAKE(xs, 0) = [] | TAKE(NIL, n) = raise Subscript | TAKE(CONS(x,xf), n) = x::TAKE(xf(), n-1);

(* This function tests if a number is an even number of not *)
fun even n = if ( n mod 2 = 0 ) then true else false;

(* This function tests if a number is an odd number or not *)
fun odd n = if ( n mod 2 = 1 ) then true else false;

(* This is the curried function called fib that will be used to generate the infinite sequence of fibonacci numbers *)
fun fib a b = CONS(a, fn () => fib b (a + b));

(* This is the variable binding that holds the entire fibonacci sequence *)
val fibs = fib 0 1;

(* This is an infinite list that consists of only even fibonacci numbers made using the FILTER function and even function *)
val evenFibs = FILTER even fibs;

(* This is an infinite list that consists of only odd fibonacci numbers made using the FILTER function and odd function *)
val oddFibs = FILTER odd fibs;

(* This function is used to print a generalized list and uses currying inherently *)
fun printGenList f xs =
      if null xs then ()
      else (ignore(f (hd xs)); printGenList f (tl(xs)));
	  
(* This function uses the printGenList routine to print a list of integers *)
fun printList(xs) =
	if null xs then ()
	else printGenList (fn x => print(Int.toString(x) ^ " ")) xs;
	
(* This function uses printGenList to print out pairs present in a list in a generalized manner *)
fun printPairList(xs) =
	if null xs then ()
	else printGenList (fn x : (int * int) => print("(" ^ Int.toString((#1 x)) ^ ", " ^ Int.toString((#2 x)) ^ ")")) xs;

(* This function is used to zip the elements of the 2 infinite lists *)
fun ZIP (l1 , l2) = CONS( (HD l1 , HD l2), fn () => ZIP (TL l1 ,TL l2));
	
(* This variable will print the first 20 elements of the infinite fibonacci sequence *)
printList(TAKE(fibs,20));

(* This will print the first 10 elements of the evenFibs list *)
printList(TAKE(evenFibs , 10));

(* This will print the first 10 elements of the oddFibs list *)
printList(TAKE(oddFibs , 10));

(* ZIP the first 10 elements of evenFibs and oddFibs *)
printPairList(TAKE(ZIP(evenFibs ,oddFibs) , 10));
