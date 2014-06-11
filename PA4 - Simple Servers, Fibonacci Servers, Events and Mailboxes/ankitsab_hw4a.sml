(* NAME - ANKIT SABLOK *)
(* E-Mail - ankitsab@buffalo.edu *)

CM.make "$cml/cml.cm";
open CML;

val chan:int chan = channel();

fun sender n =
       if n < 0
       then raise Subscript
       else
          if n > 100
          then ()
          else (send(chan,n);sender(n+1))
 ;
 
fun receiver ()=
      let
        val n = recv(chan)
      in
        ( TextIO.print(Int.toString(n) ^ "\n");receiver() )
      end
;

fun main () =
       let
          val _ = spawn(fn () => (sender 0))
          val _ = spawn(fn () => receiver())
       in
          ()
       end
;

RunCML.doit(main , NONE);