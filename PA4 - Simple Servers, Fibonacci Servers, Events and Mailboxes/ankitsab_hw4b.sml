(* NAME - ANKIT SABLOK *)
(* E-Mail - ankitsab@buffalo.edu *)

CM.make "$cml/cml.cm";
open CML;

val chan1 : int chan = channel();
val chan2 : int chan = channel();
val chan3 : int chan = channel();

fun sender1 fib1 n =
	if n = 0
	then ()
	else(
		send(chan1 , fib1);
		let 
			val newFib = recv(chan2)
		in
			sender1 newFib (n-1)
		end)
;

fun sender2 fib2 =
	let
		val newFib = recv(chan1)
	in
		send(chan2,fib2);
		send(chan3,newFib);
		sender2(newFib + fib2)
	end
;

fun receiver () =
	let 
		val fibN = recv(chan3)
	in
		TextIO.print(Int.toString(fibN) ^ "\n");
		receiver()
	end
;

fun main () =
	let
		val _ = spawn(fn () => sender1 0 43)
		val _ = spawn(fn () => sender2 1)
		val _ = spawn(fn () => receiver ())
	in
		()
	end
;

RunCML.doit(main,NONE);