(* NAME - ANKIT SABLOK *)
(* E-Mail - ankitsab@buffalo.edu *)

CM.make "$cml/cml.cm";
open CML;

fun mailbox inChannel outChannel lists = (
    if length lists = 0 
	then
		select[wrap(recvEvt inChannel, fn a => (mailbox inChannel outChannel [a]))]
	else 
		( select[ wrap( sendEvt ( outChannel , hd lists ),fn () => ( mailbox inChannel outChannel (tl lists) ) ),
          wrap(recvEvt inChannel, fn value => ( mailbox inChannel outChannel ( lists@[value]) ) ) ] ) )
;

fun receiver channel = (
    sync(wrap(recvEvt channel,fn value => (TextIO.print(Int.toString(value) ^ "\n"))));
    receiver channel);

fun sender channel n =
	if n >= 0 
	then ( sync( wrap( sendEvt(channel , n),fn () => () ) );
		   sender channel( n -1 ) ) 
    else 
	     ()
;

fun generator inChannel outChannel n = 
	if n = 0 then outChannel 
    else( 
	     spawn(fn() => mailbox inChannel outChannel []);
         if n=1 
		 then outChannel 
         else generator outChannel ( channel() ) (n-1)) 
;
 
fun main () =
  let val chStart = channel()
      val chEnd = generator chStart (channel()) 100
      val _ = spawn (fn () => receiver chEnd)
      val _ = spawn (fn () => ignore (sender chStart 50))
      val _ = spawn (fn () => ignore (sender chStart 50))
  in ()
  end
;

RunCML.doit(main , NONE);