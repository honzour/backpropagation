<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation - Examples - Chess</TITLE>
</HEAD>
<BODY>
<?
include '../../google.php'
?>
<H2>Chess 2x2</H2>
<P>
We have a chess board 2x2 and we want to return 1 on any pixel of the white field and 0 on any pixel of the black field. Looks simple but basic backpropagation seems to be useless for this task. It cannot be done by 1 layer network but also 2 layers is not enough. We must place hyperplanes between the fields (1 layer) and it reduces the problem to XOR (2 more layers). So we need 3 layers to compute this. And it is even worse, there is a pretty deep local minimum of the error function when we place hyperplanes in the first layer like in the basic XOR... Simple problem but hard for the backpropagation algorithm. The current version fails here but it is number one on my TODO list.

</P>
<UL>
<LI><A HREF="backpropagation://backpropagation.moxo.cz/2.0/examples/chess/network.csv">Import the problem to be learned</A></LI>
<LI><A HREF="backpropagation://backpropagation.moxo.cz/2.0/examples/chess/network.xml">Import the solution</A></LI>
<LI><A HREF="network.csv">View the problem CSV file</A></LI>
<LI><A HREF="network.xml">View the solution XML file</A></LI>
<LI><BR><IMG SRC="result.png">
<BR>Result visualisation</LI>
<LI><BR><IMG SRC="network.png">
<BR>Network visualisation</LI>
</UL>
<?
include '../../links.php'
?>
</BODY>
</HTML>
